package mass_spectrometr;

import jssc.SerialPortList;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Connector {

	private static SerialPort serialPort;
	public static int[] a;
	public static double a1;
	public static double a2;
	public static double a3;
	public static double a4;
	private static boolean f = true;
	private static int m = 151;
	
	public Connector() {
		Run.ports = SerialPortList.getPortNames();
	}
	
	public boolean Connect() {
		serialPort = new SerialPort((String) Run.pbox.getSelectedItem());
		try {
			serialPort.openPort();
			serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			
			serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
			serialPort.addEventListener(new EventListener());
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (SerialPortException ex) {
			System.out.println(ex);
			return false;
		}	
		return true;
	}
	public void Start() {
		try {
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!Run.transferring_data) {
				serialPort.writeBytes("1".getBytes());
			}
			else {
				serialPort.writeBytes("0".getBytes());
				
			}
		} catch (SerialPortException ex) {
			System.out.println(ex);
		}		
		
	}
	
	public void close() {
		try {
			if (serialPort!=null && serialPort.isOpened()) serialPort.closePort();
		} catch (SerialPortException ex) {
			System.out.println(ex);
		}		
	}
	
	private static class EventListener implements SerialPortEventListener {
		public int[] byte_to_int(byte b[]) {
			int value[] = new int[2];
			value[0] = ((b[0] & 0xff) << 8) | (b[1] & 0xff);
			value[1] = ((b[2] & 0xff) << 8) | (b[3] & 0xff);
			return value;
		}
		public void serialEvent(SerialPortEvent event) {			
			if (event.isRXCHAR() && event.getEventValue() > 0){
				try {
					byte buf[] = serialPort.readBytes(4);

					a = byte_to_int(buf);
					a1 = a[0];
					a1 /= 10.0;
					a2 = a[1];

					// if first
					if (f) {
						Run.mass_data.add(a1);
						Run.mass_data.add(0.0);
						f = false;
					}

					if (Run.mass_data.get(Run.mass_data.size() - 2) - (a1 + 1) > 0.000001) {
						System.out.println("!!!");
					}
					Run.mass_data.add(a1);
					Run.mass_data.add(a2);
					/*
					  if (Run.current_step == Run.rendering_rate) {
					  
					  Run.current_step = 0; } Run.current_step ++;
					 */
					Run.cnvs.repaint();
					
				} catch (SerialPortException ex) {
					System.out.println(ex);
				}
			}
		}
	}
}
