package mass_spectrometr;

import java.util.ArrayList;

public class Run {
	public static ArrayList<Double> mass_data = new ArrayList<Double>();
	//public static GUI user_interface;
	public static Graph_canvas cnvs;
	public static double X_factor = 5.0;
	public static double Y_factor = 2.0;
	
	public Run() {
		GUI user_interface = new GUI();
		Connector ard = new Connector(); 
	}
	
	public static void main(String[] args) {
		Run prog = new Run();
	}
}
