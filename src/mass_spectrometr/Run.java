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

	public static double manual_X_factor = 4.0;
	public static double manual_Y_factor = 4.0;
	public static double scale_rate = 1.3;
	
	public static boolean autoscaleY = true;
	
	public static int x0 = 10;

	public static Connector arduino;
	
	public static boolean transferring_data = false;
	public static int rendering_rate = 10;
	public static int current_step = 0;
	
	public Run() {
		arduino = new Connector(); 
		GUI user_interface = new GUI();
	}
	
	public static void main(String[] args) {
		Run prog = new Run();
	}
}
