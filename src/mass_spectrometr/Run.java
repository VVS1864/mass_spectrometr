package mass_spectrometr;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JSlider;
import javax.swing.JTextField;

import mass_spectrometr.GUI.GUI;
import mass_spectrometr.GUI.Volt_engine;
import mass_spectrometr.GUI.panels.Panel_base_interfase;

public class Run {
	public static Run prog;
	
	public String save_directory = "";
	public static enum graph_type {MASS_GRAPH, ENERGY_GRAPH};
	//public ArrayList<Integer> data_time = new ArrayList<>();
	//public ArrayList<Integer> data_en_el = new ArrayList<>();
	//public ArrayList<Integer> data_mass_intensity = new ArrayList<>();
	//public ArrayList<Integer> data_en_el_intensity = new ArrayList<>();
	/**
	 * Array like:
	 * { {Sum_value_1, number_of_addition_1}, {Sum_value_2, number_of_addition_2},}
	 */
	public int[][] fixed_data_en_el_intensity = new int[4000][2];
	public int[] fixed_data_mass_intensity = new int[65600];
	/**
	 * B - approximated
	 */
	//public ArrayList<Double> data_Bo = new ArrayList<>();

	public long current_time;
	public double current_B;
	public int current_en_el;
	public int current_intensity;

	public String[] ports;

	// public double manual_X_factor = 4.0;
	// public double manual_Y_factor = 4.0;
	// public double scale_rate = 1.3;

	// public boolean autoscaleY = true;

	// public int x0 = 100;

	public Connector arduino;
	public Chart_analyser analyser_mass;
	public Chart_analyser analyser_en_el;
	public GUI user_interface;
	private Config cfg;

	public boolean draw_graph_mass = false;
	public boolean draw_graph_en_el = false;
	public int rendering_rate = 10;
	public int current_step = 0;
	public double M0;
	public double K;
	public double B0;

	public double en_el_K;
	public double en_el_b;

	// Parameters for electron energy

	public int start_V_cyclic = 0;
	public int stop_V_cyclic = 3800;
	public double step_V_cyclic = 0.02;

	public int start_V = 0;
	public int stop_V = 3800;
	public double step_V = 0.02;
	public boolean en_el_cycle_scan = false; 
	public boolean start_e_scan = false; 
	public boolean first_scan = false;
	public int dac_voltage = 0;
	/**
	 * for use step_V (float)
	 */
	public double dac_voltage_float = 0;
	/**
	 * en_el value in volts (-2, 17)
	 */
	public double current_en_el_float;

	/**
	 * size of array for approximation B
	 */
	public int approx_N = 50;
	
	/**
	 * 1 = forward, -1 = backward
	 */
	private int fast_scan_direction = 1; 
	public boolean en_el_delay = false;

	public Run() {
		prog = this;
		arduino = new Connector();
		cfg = new Config();
		read_settings();
		if (save_directory == "") save_directory = cfg.config_path;
		user_interface = new GUI();

		set_zero_energy();
		set_zero_mass();
		/*
		set_demo_mass();
		System.out.println(calc_mass(200));
		System.out.println(calc_mass(500));
		System.out.println(calc_mass(800));
		*/
		//calc_coefficients(2.5, 40, 160, 2.5, 40, 160);
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				int action = e.getID();
				Component focus = user_interface.get_focus_owner();

				// if (focus != m0_textbox && focus != b0_textbox && focus != k_textbox && focus
				// != N_textbox &&
				// action == KeyEvent.KEY_PRESSED) {
				if (!(focus instanceof JTextField) && !(focus instanceof JSlider) && action == KeyEvent.KEY_PRESSED) {
					Panel_base_interfase D;
					if (user_interface.e_energy_frame.isAncestorOf(focus)) {
						D = user_interface.e_energy_frame.energy_panel;
					} else {
						D = user_interface.mass_panel;
					}
					char c = e.getKeyChar();
					int code = e.getKeyCode();

					if (c == '+' | c == '-') {
						D.zoom(c, 'X');
					} else if (code == KeyEvent.VK_LEFT) {
						D.move('L');
					} else if (code == KeyEvent.VK_RIGHT) {
						D.move('R');
					}
				}

				return false;
			}
		});
	}

	private void read_settings() {
		M0 = Double.parseDouble(parse_conf("M0", "0"));
		B0 = Double.parseDouble(parse_conf("B0", "0"));
		K = Double.parseDouble(parse_conf("K", "0.001"));

		en_el_b = Double.parseDouble(parse_conf("en_el_b", "400.0"));
		en_el_K = Double.parseDouble(parse_conf("en_el_K", "200.0"));
		
		save_directory = parse_conf("save_path", "");
		// load standard settings for fast scan of energy
		// start_V_cyclic = parse_double("start_V_cyclic", 0);
		// stop_V_cyclic = parse_double("stop_V_cyclic", 0);
		// step_V_cyclic = (float)parse_double("step_V_cyclic", 0);

	}

	private void write_settings() {
		cfg.set_conf_value("M0", Double.toString(M0));
		cfg.set_conf_value("B0", Double.toString(B0));
		cfg.set_conf_value("K", Double.toString(K));

		cfg.set_conf_value("en_el_b", Double.toString(en_el_b));
		cfg.set_conf_value("en_el_K", Double.toString(en_el_K));
		
		cfg.set_conf_value("save_path", save_directory);
		
		cfg.store_config();
	}

	private String parse_conf(String value_name, String default_value) {
		String val = cfg.get_conf_value(value_name);
		String ret;
		if (val != null) {
			ret = val;
		} else {
			ret = default_value;
		}
		return ret;
	}

	private void set_new_coefficients(double new_K, double new_M0, double new_B0) {
		K = new_K;
		M0 = new_M0;
		B0 = new_B0;
		user_interface.mass_panel.set_new_coef_values(K, M0, B0);
	}

	public void calc_coefficients(double M1, double M2, double M3, double M1_real, double M2_real, double M3_real) {
		//System.out.println(M1 + " " + M2 + " " + M3);
		//System.out.println(M1_real + " " + M2_real + " " + M3_real);
		double B1 = Math.sqrt((M1 - M0)/K) - B0;
		double B2 = Math.sqrt((M2 - M0)/K) - B0;
		double B3 = Math.sqrt((M3 - M0)/K) - B0;
		//System.out.println("M " + M1 + " " + M2 + " " + M3);
		M1 = M1_real;
		M2 = M2_real;
		M3 = M3_real;
		/*
		System.out.println("M_real " + M1 + " " + M2 + " " + M3);
		System.out.println("B " + B1 + " " + B2 + " " + B3);
		System.out.println("M1_new " + calc_mass(B1) + " M2_new " + calc_mass(B2) + " M3_new " + calc_mass(B3));
		*/
		// my version
		
		double a = M3 - M1;
		double b = M2 - M1;
		
		double new_B0 = (a * (Math.pow(B1, 2.0) - Math.pow(B2, 2.0)) + b * (Math.pow(B3, 2.0) - (Math.pow(B1, 2.0)))) /
				(2.0 * ( a*(B2-B1) + b*(B1-B3) ) );
		
		double new_K = b / (Math.pow(B2, 2.0) + 2.0*new_B0*(B2-B1) - Math.pow(B1, 2.0));
		
		double new_M0 = M1 - new_K*Math.pow(B1, 2.0) - 2.0*new_K*B1*new_B0 - new_K*Math.pow(new_B0, 2.0);
		
		/*first
		double new_B0 = ((M3-M1)*(B1-B2)+(M1-M2)*(B3-B1)) / 
				(2* (  ((M2-M1)*(Math.pow(B3, 2)-Math.pow(B1, 2))) + ((M1-M3)*(Math.pow(B2, 2)-Math.pow(B1, 2)))  )  );
		*/
		//System.out.println("B0 den"	+ (2* (  ((M2-M1)*(Math.pow(B3, 2)-Math.pow(B1, 2))) + ((M1-M3)*(Math.pow(B2, 2)-Math.pow(B1, 2)))  )));	
		/*first
		double new_K = (M2-M1) / (Math.pow(B2, 2) - Math.pow(B1, 2) + 2*new_B0*(B2-B1));
		*/
		//System.out.println("K den" + (Math.pow(B2, 2) - Math.pow(B1, 2) + 2*new_B0*(B2-B1)));
		/*first
		double new_M0 = M1 - new_K*Math.pow(B1, 2) - 2*new_K*B1*new_B0 - new_K*Math.pow(new_B0, 2);
		*/
		//new Alexey version
		/*
		double new_B0 = (  (M2-M1) * (Math.pow(B3, 2)-Math.pow(B1, 2)) + (M1-M3) * (Math.pow(B2, 2)-Math.pow(B1, 2)) ) / 
				(2*(  M3*(B2-B1) - M1*(B2-B3) - M2*(B3-B1)  ) );
		double new_K = (M1-M2) / (Math.pow(B2, 2) + 2*new_B0*(B1-B2) - Math.pow(B1, 2));
		double new_M0 = M1 - new_K*Math.pow(B1, 2) + 2*new_K*B1*new_B0 - new_K*Math.pow(new_B0, 2);
		*/
		/*
		System.out.println("K" + new_K);
		System.out.println("B0" + new_B0);
		System.out.println("M0" + new_M0);
		*/
		set_new_coefficients(new_K, new_M0, new_B0);
	}

	public double calc_mass(double B) {
		return M0 + K * Math.pow((B0 + B), 2);
	}

	/**
	 * For calc (0, 3800) from (-2, 17)
	 * 
	 * @return
	 */
	public int calc_int_en_el(double en_el) {
		return (int)Math.round(en_el * en_el_K + en_el_b);
	}

	/**
	 * For calc (-2, 17) from (0, 3800)
	 * 
	 * @return
	 */
	public double calc_float_en_el(int en_el) {
		return (en_el - en_el_b) / en_el_K;
	}

	/**
	 * for calc (-2, 17) step to (0, 3800)
	 * 
	 * @param step
	 * @return
	 */
	public double calc_step(double step) {
		return step * en_el_K;
	}

	public void print_current_mass_intensity() {
		DecimalFormat formatter = new DecimalFormat("#0.00");
		double mass = calc_mass(current_B);
		current_en_el_float = calc_float_en_el(current_en_el);

		user_interface.mass_panel.label_X.setText(formatter.format(mass));
		user_interface.e_energy_frame.energy_panel.mass_indication.setText(formatter.format(mass));
		user_interface.mass_panel.label_Y.setText(formatter.format(current_intensity));
		user_interface.e_energy_frame.energy_panel.label_X.setText(formatter.format(current_en_el_float));
		user_interface.e_energy_frame.energy_panel.label_Y.setText(formatter.format(current_intensity));
		// user_interface.mass_panel.volt.set_value(current_en_el_float);
		if (start_e_scan) {
			user_interface.mass_panel.volt.set_value(current_en_el_float);
			// user_interface.mass_panel.volt.set_new_en_el((int)current_en_el);
			// user_interface.e_energy_frame.energy_panel.volt.set_new_en_el((int)current_en_el);
		}
	}

	public void en_el_scan_loop() {
		if (first_scan && current_en_el == start_V) {
			first_scan = false;
			dac_voltage = start_V;
			dac_voltage_float = start_V;
			return;
		}
		if (en_el_cycle_scan) {
			en_el_scan_fast();
		} else {
			en_el_scan_long();
		}
	}

	private void en_el_scan_long() {
		if (dac_voltage_float + step_V < stop_V) {
			dac_voltage_float += step_V;
			dac_voltage = (int)Math.round(dac_voltage_float);
			en_el_delay = false;
		} else {
			dac_voltage = start_V;
			dac_voltage_float = start_V;
			en_el_delay = true;
		}
	}

	private void en_el_scan_fast() {
		double step;
		int new_direction;
		if (dac_voltage + step_V >= stop_V && fast_scan_direction == 1) {
			new_direction = -1;
			step = stop_V - dac_voltage;
		}
		else if (dac_voltage - step_V <= start_V && fast_scan_direction == -1){
			new_direction = 1;
			step = dac_voltage - start_V;
		}
		else {
			new_direction = fast_scan_direction;
			step = step_V;
		}
		
		dac_voltage_float += step*fast_scan_direction;
		dac_voltage = (int)Math.round(dac_voltage_float);
		fast_scan_direction = new_direction;		
	}

	private void set_zero_energy() {
		// set zeros in en_el intensity array
		for (int i = 0; i < fixed_data_en_el_intensity.length; i++) {
			fixed_data_en_el_intensity[i][0] = 0;
			fixed_data_en_el_intensity[i][1] = 1;
		}
	}
	
	private void set_zero_mass() {
		// set zeros in mass intensity array
		for (int i = 0; i < fixed_data_mass_intensity.length; i++) {
			fixed_data_mass_intensity[i] = 0;
		}
	}

	public void reset_mass() {
		//set_zero_energy();
		set_zero_mass();
		arduino.clear_parts();
	}
	public void reset_energy() {
		
		
		set_zero_energy();
	
		//arduino.clear_parts();
	}
	
	private void set_demo_mass(){
		for (int i = 0; i < fixed_data_mass_intensity.length; i++) {
			if (i == 200 || i == 500 || i == 800){
				fixed_data_mass_intensity[i] = 500;
			}
		}
	}

	public void close() {
		arduino.close();
		write_settings();
	}

	public static void main(String[] args) {
		Run prog = new Run();
	}
}
