package mass_spectrometr;


import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;

public class Run {
	public static ArrayList<Integer> time_data = new ArrayList<Integer>();
	public static ArrayList<Double> mass_data = new ArrayList<Double>();
	public static ArrayList<Integer> en_el_data = new ArrayList<Integer>();
	public static ArrayList<Integer> intensity_data = new ArrayList<Integer>();
	
	public static int current_time = 0;
	public static double current_mass = 0;
	public static int current_en_el = 0;
	public static int current_intensity = 0;
	
	
	public static JComboBox<String> pbox;
	public static String[] ports;
	public static Graph_canvas cnvs;

	public static JLabel status_info2;

	public static double manual_X_factor = 4.0;
	public static double manual_Y_factor = 4.0;
	public static double scale_rate = 1.3;
	
	public static boolean autoscaleY = true;
	
	public static int x0 = 100;

	public static Connector arduino;
	public static Chart_analyser analyser;
	
	public static boolean draw_graph = false;
	public static int rendering_rate = 10;
	public static int current_step = 0;
	public static double K = 1;
	
	
	public Run() {
		arduino = new Connector(); 
		GUI user_interface = new GUI();
	}
	
	public static void reset() {
		time_data.clear();
		mass_data.clear();
		en_el_data.clear();
		intensity_data.clear();
	}
	
	public static void main(String[] args) {
		Run prog = new Run();
	}
}
