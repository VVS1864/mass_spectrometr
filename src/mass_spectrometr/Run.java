package mass_spectrometr;


import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;

public class Run {
	public static Run prog;
	public  ArrayList<Integer> time_data = new ArrayList<Integer>();
	public  ArrayList<Double> mass_data = new ArrayList<Double>();
	public  ArrayList<Integer> en_el_data = new ArrayList<Integer>();
	public  ArrayList<Integer> intensity_data = new ArrayList<Integer>();
	
	public  int current_time = 0;
	public  double current_mass = 0;
	public  int current_en_el = 0;
	public  int current_intensity = 0;
	
	
	public  JComboBox<String> portbox;
	public  String[] ports;
	public  Graph_canvas cnvs;

	public  JLabel status_info2;
	public  JLabel label_mass;
	public  JLabel label_intensity;

	public  double manual_X_factor = 4.0;
	public  double manual_Y_factor = 4.0;
	public  double scale_rate = 1.3;
	
	public  boolean autoscaleY = true;
	
	public  int x0 = 100;

	public  Connector arduino;
	public  Chart_analyser analyser;
	
	public  boolean draw_graph = false;
	public  int rendering_rate = 10;
	public  int current_step = 0;
	public  double M0;
	public  double K;
	public  double B0;
	
	private Config cfg;
	
	public Run() {
		prog = this;
		arduino = new Connector(); 
		cfg = new Config();
		read_settings();
		GUI user_interface = new GUI();
	}
	
	private void read_settings() {
		M0 = parse_double("M0", 0);
		B0 = parse_double("B0", 0);
		K = parse_double("K", 0.001);
	}
	
	private void write_settings() {
		cfg.set_conf_value("M0", Double.toString(M0));
		cfg.set_conf_value("B0", Double.toString(B0));
		cfg.set_conf_value("K", Double.toString(K));
		cfg.store_config();
		System.out.println("Settings are written to config!");
	}
	
	private double parse_double(String value_name, double default_value) {
		String val = cfg.get_conf_value(value_name);
		double ret;
		if (val != null) {
			ret = Double.parseDouble(val);
		}
		else {
			ret = default_value;
		}
		return ret;
	}
	
	public void reset() {
		time_data.clear();
		mass_data.clear();
		en_el_data.clear();
		intensity_data.clear();
	}
	
	public void close() {
		 arduino.close();
		 write_settings();
	}
	
	public static void main(String[] args) {
		Run prog = new Run();
	}
}
