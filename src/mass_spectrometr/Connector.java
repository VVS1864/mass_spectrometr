package mass_spectrometr;

import jssc.SerialPortList;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Connector {

	private Arduino_state_controller state_controller = new Arduino_state_controller();
	private SerialPort serialPort;
	private String port;
	/**
	 * Size of array of B-raw
	 */
	private int count = 0;

	private ArrayList<Long> time_part;
	private ArrayList<Double> B_part;
	private ArrayList<Double> B_approximated;

	public ArrayList<Integer> en_el_part;
	public ArrayList<Integer> intensity_part;

	/**
	 * Important variable of data transfer "n" - no transfer, "t" - normal transfer,
	 * "w" - wrong data, need reconnect
	 */
	private String data_transfer_state = "n";
	private long last_data_time;

	public Connector() {
		Run.prog.ports = SerialPortList.getPortNames();
	}

	private boolean Connect(String port) {
		clear_parts();
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
			data_transfer_state = "t";
			last_data_time = System.currentTimeMillis();
			state_controller.start();

		} catch (SerialPortException ex) {
			System.err.println(ex);
			return false;
		}
		return true;
	}
	
	public boolean Connect() {
		port = (String) Run.prog.user_interface.portbox.getSelectedItem();
		System.out.println(Arrays.toString(SerialPortList.getPortNames()));
		
		return Connect(port);
	}
		

	public void reconnect() {
		close();
		String[] ports = SerialPortList.getPortNames();
		if (ports.length > 0) {
			for (String port : ports) {
				Connect(port);
			}
		}
	}

	public boolean is_running() {
		long time = System.currentTimeMillis();

		if (time - last_data_time > 2000)
			return false;
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

		private void byte_to_int(byte b[]) {
			Run.prog.current_time = ((b[0] & 0xff) << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff));
			Run.prog.current_B = ((b[4] & 0xff) << 8) | (b[5] & 0xff);
			Run.prog.current_en_el = ((b[6] & 0xff) << 8) | (b[7] & 0xff);
			Run.prog.current_intensity = ((b[8] & 0xff) << 8) | (b[9] & 0xff);
			byte[] tr = new byte[1];
			tr[0] = b[10];
			data_transfer_state = new String(tr);
			// System.out.println(Arrays.toString(b));
		}

		private void int_to_byte(byte buf[]) {
			buf[0] = (byte) (((int) Run.prog.dac_voltage >> 8) & 0xFF);
			buf[1] = (byte) ((int) Run.prog.dac_voltage & 0xFF);
			buf[2] = data_transfer_state.getBytes()[0];
		}

		/**
		 * read bytes from Arduino and put in data arrays, every N times approximate B
		 * and repaint.
		 */
		public void serialEvent(SerialPortEvent event) {
			if (event.isRXCHAR() && event.getEventValue() > 0) {
				last_data_time = System.currentTimeMillis();
				try {
					// Write to arduino
					byte write_buf[] = new byte[3];
					int_to_byte(write_buf);
					serialPort.writeBytes(write_buf);

					// Read from arduino
					byte buf[] = serialPort.readBytes(11);
					byte_to_int(buf);

					if (data_transfer_state.equals("t")) {
						// Collect data
						collect_mass_data();
						collect_en_el_data();

						if (Run.prog.start_e_scan)
							Run.prog.en_el_scan_loop();
					} else {
						System.err.println("reconnect");
						reconnect();
					}

					Run.prog.user_interface.repaint_cnvs();
					Run.prog.print_current_mass_intensity();

				} catch (SerialPortException ex) {
					System.err.println(ex);
				}
			}

		}

		private void collect_mass_data() {
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

							if (fixed_mass_index < Run.prog.fixed_data_mass_intensity.length && fixed_mass_index > 0) {
								Run.prog.fixed_data_mass_intensity[fixed_mass_index] = intensity;
							}
						}
						clear_parts();
					}

				} else if (Run.prog.approx_N == 0) {
					int fixed_mass_index = (int) Math.round(Run.prog.current_B);
					if (fixed_mass_index < Run.prog.fixed_data_mass_intensity.length && fixed_mass_index > 0) {

						Run.prog.fixed_data_mass_intensity[fixed_mass_index] = Run.prog.current_intensity;
					}

				}
			}
		}

		private void collect_en_el_data() {
			if (Run.prog.draw_graph_en_el && !Run.prog.first_scan) {

				if (Run.prog.current_en_el <= Run.prog.fixed_data_en_el_intensity.length) {

					Run.prog.fixed_data_en_el_intensity[Run.prog.current_en_el][0] += Run.prog.current_intensity;
					Run.prog.fixed_data_en_el_intensity[Run.prog.current_en_el][1]++;
				}
			}
		}

	}
}
