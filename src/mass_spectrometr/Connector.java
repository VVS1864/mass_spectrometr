package mass_spectrometr;

import jssc.SerialPortList;

import java.util.ArrayList;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Connector {

	private SerialPort serialPort;
	/**
	 * Size of array of B-raw
	 */
	private int count = 0;

	// private int local_N_approx;

	private ArrayList<Long> time_part;
	private ArrayList<Double> B_part;
	private ArrayList<Double> B_approximated;

	public ArrayList<Integer> en_el_part;
	public ArrayList<Integer> intensity_part;
	
	private long old_time = 0;

	public Connector() {
		Run.prog.ports = SerialPortList.getPortNames();
		clear_parts();
	}

	public boolean Connect() {
		String port = (String) Run.prog.user_interface.portbox.getSelectedItem();
		serialPort = new SerialPort(port);
		try {
			serialPort.openPort();
			serialPort.setParams(SerialPort.BAUDRATE_38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
			serialPort.addEventListener(new EventListener());

			try {
				Thread.sleep(1600);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			serialPort.writeBytes("1".getBytes());

		} catch (SerialPortException ex) {
			System.err.println(ex);
			return false;
		}
		return true;
	}

	public void close() {
		try {
			if (serialPort != null && serialPort.isOpened())
				serialPort.closePort();
		} catch (SerialPortException ex) {
			System.out.println(ex);
		}
	}

	public void clear_parts() {
		int local_N_approx = Run.prog.approx_N;
		time_part = new ArrayList<Long>(local_N_approx);
		B_part = new ArrayList<Double>(local_N_approx);
		B_approximated = new ArrayList<Double>(local_N_approx);
		en_el_part = new ArrayList<Integer>(local_N_approx);
		intensity_part = new ArrayList<Integer>(local_N_approx);

		count = 0;
	}

	private class EventListener implements SerialPortEventListener {

		public void byte_to_int(byte b[]) {
			Run.prog.current_time = ((b[0] & 0xff) << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff));
			Run.prog.current_B = ((b[4] & 0xff) << 8) | (b[5] & 0xff);
			Run.prog.current_en_el = ((b[6] & 0xff) << 8) | (b[7] & 0xff);
			Run.prog.current_intensity = ((b[8] & 0xff) << 8) | (b[9] & 0xff);
		}

		public void int_to_byte(byte buf[]) {
			/*
			 * //unsigned long time buf[0] = (byte)((Run.prog.current_time >> 24) & 0xFF);
			 * buf[1] = (byte)((Run.prog.current_time >> 16) & 0xFF); buf[2] =
			 * (byte)((Run.prog.current_time >> 8) & 0xFF); buf[3] =
			 * (byte)(Run.prog.current_time & 0xFF);
			 */
			buf[0] = (byte) (((int) Run.prog.dac_voltage >> 8) & 0xFF);
			buf[1] = (byte) ((int) Run.prog.dac_voltage & 0xFF);
			/*
			 * buf[2] = (byte)((Run.prog.start_e_scan >> 8) & 0xFF); buf[3] =
			 * (byte)(Run.prog.start_e_scan & 0xFF); buf[4] = (byte)((Run.prog.start_V >> 8)
			 * & 0xFF); buf[5] = (byte)(Run.prog.start_V & 0xFF); buf[6] =
			 * (byte)((Run.prog.stop_V >> 8) & 0xFF); buf[7] = (byte)(Run.prog.stop_V &
			 * 0xFF); buf[8] = (byte)((step_V >> 8) & 0xFF); buf[9] = (byte)(step_V & 0xFF);
			 */
			// buf[10] = (byte)((Run.prog.cycle_scan >> 8) & 0xFF);
			// buf[11] = (byte)(Run.prog.cycle_scan & 0xFF);

		}

		/**
		 * read bytes from Arduino and put in data arrays, every N times approximate B
		 * and repaint.
		 */
		public void serialEvent(SerialPortEvent event) {
			if (event.isRXCHAR() && event.getEventValue() > 0) {
				try {
					// Write to arduino
					byte write_buf[] = new byte[2];
					int_to_byte(write_buf);
					serialPort.writeBytes(write_buf);
					
					
					
					// Read from arduino
					byte buf[] = serialPort.readBytes(10);
					byte_to_int(buf);
					// Run.prog.current_B++;

					// Collect data
					if (Run.prog.draw_graph_mass) {
						if (Run.prog.approx_N > 0) {
							
							time_part.add(Run.prog.current_time);
							B_part.add(Run.prog.current_B);
							intensity_part.add(Run.prog.current_intensity);
							
							count++;

							// Calculate approximation B and repaint
							if (count == Run.prog.approx_N) {
								

								Approximator A = new Approximator(B_part, time_part, Run.prog.approx_N);
								B_approximated = A.get_approx();
								for (int i = 0; i < B_approximated.size(); i++) {
									Double B = B_approximated.get(i);
									double b = B.doubleValue();

									Integer Intensity = intensity_part.get(i);
									int intensity = Intensity.intValue();
									
									int fixed_mass_index = (int) Math.round(b);
									/*
									if (old_ind != fixed_mass_index+1) {
										System.out.println("=============");
										System.out.println(fixed_mass_index);
										System.out.println(old_ind);
										System.out.println(b);
										
									}
									*/
									
									if (fixed_mass_index < Run.prog.fixed_data_mass_intensity.length
											&& fixed_mass_index > 0) {
										Run.prog.fixed_data_mass_intensity[fixed_mass_index] = intensity;
									}
								}
								clear_parts();
							}

						}
						else if(Run.prog.approx_N == 0) {
							int fixed_mass_index = (int) Math.round(Run.prog.current_B);
							if (fixed_mass_index < Run.prog.fixed_data_mass_intensity.length
									&& fixed_mass_index > 0) {
								
								Run.prog.fixed_data_mass_intensity[fixed_mass_index] = Run.prog.current_intensity;
							}
							
						}

					}
					if (Run.prog.draw_graph_en_el) {
						
						if (Run.prog.current_en_el <= Run.prog.fixed_data_en_el_intensity.length) {
							Run.prog.fixed_data_en_el_intensity[Run.prog.current_en_el][0] += Run.prog.current_intensity;
							Run.prog.fixed_data_en_el_intensity[Run.prog.current_en_el][1]++;
							//System.out.println(Run.prog.current_B + " " + Run.prog.fixed_data_en_el_intensity[Run.prog.current_en_el][0] + " " +
							//		Run.prog.fixed_data_en_el_intensity[Run.prog.current_en_el][1]);
						}
					}

					if (Run.prog.start_e_scan)
						Run.prog.en_el_scan_loop();

					Run.prog.user_interface.repaint_cnvs();
					Run.prog.print_current_mass_intensity();
					
					if (Run.prog.en_el_delay) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				} catch (SerialPortException ex) {
					System.err.println(ex);
				}
			}
		}

	}
}
