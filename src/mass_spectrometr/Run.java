package mass_spectrometr;


import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;

public class Run {
	public static ArrayList<Double> mass_data = new ArrayList<Double>();
	
	public static JComboBox<String> pbox;
	public static String[] ports;
	public static Graph_canvas cnvs;

	public static JLabel status_info2;

	public static double X_factor = 4.0;
	public static double Y_factor = 1.0;
	public static boolean autoscale = true;
	
	public static int x0 = 100;
	
	public static Connector arduino;
	
	public static boolean transferring_data = false;
	
	public Run() {
		arduino = new Connector(); 
		GUI user_interface = new GUI();
	}
	
	public static void main(String[] args) {
		Run prog = new Run();
	}
}
