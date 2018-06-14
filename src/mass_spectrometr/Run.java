package mass_spectrometr;


import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;

public class Run {
	public static Run prog;
	public  ArrayList<Integer> data_time = new ArrayList<Integer>();
	public  ArrayList<Integer> data_en_el = new ArrayList<Integer>();
	public  ArrayList<Integer> data_intensity = new ArrayList<Integer>();
	/**
	 * B - approximated
	 */
	public  ArrayList<Double> data_Bo = new ArrayList<Double>();
	
	public  int current_time;
	public  double current_B;
	public  int current_en_el;
	public  int current_intensity;
	
	public  String[] ports;
	

	//public  double manual_X_factor = 4.0;
	//public  double manual_Y_factor = 4.0;
	//public  double scale_rate = 1.3;
	
	//public  boolean autoscaleY = true;
	
	//public  int x0 = 100;

	public  Connector arduino;
	public  Chart_analyser analyser;
	public GUI user_interface;
	private Config cfg;
	
	public  boolean draw_graph = false;
	public  int rendering_rate = 10;
	public  int current_step = 0;
	public  double M0;
	public  double K;
	public  double B0;
	/**
	 * size of array for approximation B
	 */
	public  int approx_N = 100;
	
	
	
	public Run() {
		prog = this;
		arduino = new Connector(); 
		cfg = new Config();
		read_settings();
		user_interface = new GUI();
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
	
	public double calc_mass(double B) {
		return M0 + K * (B0+B)*(B0+B);
	}
	
	public void print_current_mass_intensity() {
		DecimalFormat formatter = new DecimalFormat("#0.00");
		double mass = calc_mass(current_B);
		Run.prog.user_interface.cnvs_panel.label_mass.setText(formatter.format(mass));
		Run.prog.user_interface.cnvs_panel.label_intensity.setText(formatter.format(current_intensity));
	}
	public void reset() {
		data_time.clear();
		//data_B.clear();
		data_Bo.clear();
		data_en_el.clear();
		data_intensity.clear();
		
		arduino.clear_parts();
	}
	
	public void close() {
		 arduino.close();
		 write_settings();
	}
	
	public static void main(String[] args) {
		Run prog = new Run();
	}
}
