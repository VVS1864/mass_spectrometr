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
	/*
	public static int[] a;
	public static int a1;
	public static int a2;
	public static int a3;
	public static int a4;*/
	private static boolean f = true;
	
	public Connector() {
		Run.prog.ports = SerialPortList.getPortNames();
	}
	
	public boolean Connect() {
		serialPort = new SerialPort((String) Run.prog.portbox.getSelectedItem());
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
			System.out.println(ex);
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
	
	private static class EventListener implements SerialPortEventListener {
		public void byte_to_int(byte b[]) {
			
			Run.prog.current_time = (
					(b[0] & 0xff) << 24 | 
					(b[1] & 0xff) << 16 | 
					(b[2] & 0xff) << 8 | 
					(b[3] & 0xff));
			
			Run.prog.current_mass = ((b[4] & 0xff) << 8) | (b[5] & 0xff);
			Run.prog.current_en_el = ((b[6] & 0xff) << 8) | (b[7] & 0xff);
			Run.prog.current_intensity = ((b[8] & 0xff) << 8) | (b[9] & 0xff);
			
		}
		public void serialEvent(SerialPortEvent event) {			
			if (event.isRXCHAR() && event.getEventValue() > 0){
				try {
					byte buf[] = serialPort.readBytes(10);

					byte_to_int(buf);
					/*
					if (Run.prog.current_time==0 && Run.prog.current_mass==0 && Run.prog.current_en_el==0 & Run.prog.current_intensity==0) {
						if(Run.prog.flow_mass) {
							System.out.println(111);
							Run.prog.time_data.clear(); 
							Run.prog.mass_data.clear();
							Run.prog.en_el_data.clear();
							Run.prog.intensity_data.clear();
							Run.prog.flow_mass = false;
						}
						else Run.prog.flow_mass = true;
						return;
					}
					*/
					//double B = ((double)Run.prog.current_mass)/100.0;
					//Run.prog.current_mass = B;
					
					if (Run.prog.draw_graph && f) {
						Run.prog.time_data.add(0);
						Run.prog.mass_data.add(Run.prog.current_mass);
						Run.prog.en_el_data.add(0);
						Run.prog.intensity_data.add(0);
						f = false;
					}
					/*
					if (Run.prog.mass_data.get(Run.prog.mass_data.size() - 1) - (a2 + 1) > 0.000001) {
						System.out.println("!!!");
					}*/
					if (Run.prog.draw_graph) {
						Run.prog.time_data.add(Run.prog.current_time);
						Run.prog.mass_data.add(Run.prog.current_mass);

						Run.prog.en_el_data.add(Run.prog.current_en_el);

						Run.prog.intensity_data.add(Run.prog.current_intensity);
					}
					/*
					  if (Run.prog.current_step == Run.prog.rendering_rate) {
					  
					  Run.prog.current_step = 0; } Run.prog.current_step ++;
					 */
					
					Run.prog.cnvs.repaint();
					
				} catch (SerialPortException ex) {
					System.out.println(ex);
				}
			}
		}
	}
}
