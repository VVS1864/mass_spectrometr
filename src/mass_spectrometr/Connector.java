package mass_spectrometr;

import jssc.SerialPortList;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Connector {

	private static SerialPort serialPort;
	public static double a1;
	public static double a2;
	public static double a3;
	public static double a4;
	private static boolean f = true;
	
	
	public Connector() {
		Run.ports = SerialPortList.getPortNames();
	}
	
	public boolean Connect() {
		serialPort = new SerialPort((String) Run.pbox.getSelectedItem());
		try {
			serialPort.openPort();
			serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			
			serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
			serialPort.addEventListener(new EventListener());
		
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
			serialPort.closePort();
		} catch (SerialPortException ex) {
			System.out.println(ex);
		}		
	}
	
	private static class EventListener implements SerialPortEventListener {

		public void serialEvent(SerialPortEvent event) {
			if (event.isRXCHAR() && event.getEventValue() > 0){
				try {
					String buffer = serialPort.readString();
					//System.out.println(buffer);
					//Float 
					//String regex = "<(\\d+[.]\\d+), (\\d+[.]\\d+), (\\d+[.]\\d+), (\\d+[.]\\d+)>";
					//int
					String regex = "<(\\d+[.]\\d+), (\\d+), (\\d+), (\\d+)>";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(buffer);
					
					if (matcher.find()) {
						a1 = Double.parseDouble(matcher.group(1));
						a2 = Double.parseDouble(matcher.group(2));
						a3 = Double.parseDouble(matcher.group(3));
						a4 = Double.parseDouble(matcher.group(4));
						if (a1<0) {
							serialPort.closePort();
							return;
						}
						//if first 
						if(f) {
							Run.mass_data.add(a1);
							Run.mass_data.add(0.0);
							f = false;
						}
						Run.mass_data.add(a1);
						Run.mass_data.add(a2);
						Run.cnvs.repaint();
					}
					
				} catch (SerialPortException ex) {
					System.out.println(ex);
				}
			}
		}
	}
}
