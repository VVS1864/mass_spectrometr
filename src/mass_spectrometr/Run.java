package mass_spectrometr;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;

public class Run {
	public static ArrayList<Double> mass_data = new ArrayList<Double>();
	
	public static JComboBox<String> pbox;
	public static String[] ports;
	public static Graph_canvas cnvs;

	public static JLabel status_info2;
	public static String status_string;
	public static Color status_color = Color.CYAN;
	public static double X_factor = 5.0;
	public static double Y_factor = 2.0;
	
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
