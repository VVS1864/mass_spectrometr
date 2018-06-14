package mass_spectrometr;

import jssc.SerialPortList;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

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
	private int local_N_approx;
	
	private ArrayList<Integer> time_part;
	private ArrayList<Double> B_part;
	private ArrayList<Double> B_approximated;
	
	public  ArrayList<Integer> en_el_part;
	public  ArrayList<Integer> intensity_part;
	//private static boolean f = true;
	
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
			if (serialPort!=null && serialPort.isOpened()) serialPort.closePort();
		} catch (SerialPortException ex) {
			System.out.println(ex);
		}		
	}
	
	public void clear_parts() {
		local_N_approx = Run.prog.approx_N;
		time_part = new ArrayList<Integer>(local_N_approx);
		B_part = new ArrayList<Double>(local_N_approx);
		B_approximated = new ArrayList<Double>(local_N_approx);
		en_el_part = new ArrayList<Integer>(local_N_approx);
		intensity_part = new ArrayList<Integer>(local_N_approx);
		
		count = 0;
	}
	
	private class EventListener implements SerialPortEventListener {
		
		public void byte_to_int(byte b[]) {
			Run.prog.current_time = (
					(b[0] & 0xff) << 24 | 
					(b[1] & 0xff) << 16 | 
					(b[2] & 0xff) << 8 | 
					(b[3] & 0xff));

			Run.prog.current_B = ((b[4] & 0xff) << 8) | (b[5] & 0xff);
			Run.prog.current_en_el = ((b[6] & 0xff) << 8) | (b[7] & 0xff);
			Run.prog.current_intensity = ((b[8] & 0xff) << 8) | (b[9] & 0xff);
		}
		
		/**
		 * read bytes from Arduino and put in data arrays, every N times approximate B and repaint.
		 */
		public void serialEvent(SerialPortEvent event) {
			if (event.isRXCHAR() && event.getEventValue() > 0) {
				try {
					byte buf[] = serialPort.readBytes(10);

					byte_to_int(buf);
					
					if (Run.prog.draw_graph) {
						time_part.add(Run.prog.current_time);
						B_part.add(Run.prog.current_B);
						en_el_part.add(Run.prog.current_en_el);
						intensity_part.add(Run.prog.current_intensity);
						count++;

						// Calculate approximation and repaint
						if (count == local_N_approx) {
							Approximator A = new Approximator();
							B_approximated = A.Approximate(B_part, time_part, local_N_approx);
							Run.prog.data_Bo.addAll(B_approximated);
							Run.prog.data_time.addAll(time_part);
							Run.prog.data_en_el.addAll(en_el_part);
							Run.prog.data_intensity.addAll(intensity_part);
							clear_parts();
						}
					}
					Run.prog.user_interface.repaint_cnvs();
					Run.prog.print_current_mass_intensity();
					
				} catch (SerialPortException ex) {
					System.err.println(ex);
				}
			}
		}
		
	}
}
