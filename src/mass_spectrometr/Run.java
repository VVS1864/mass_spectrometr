package mass_spectrometr;


import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JTextField;

import mass_spectrometr.GUI.GUI;
import mass_spectrometr.GUI.panels.Panel_base_interfase;

public class Run {
	public static Run prog;
	public  ArrayList<Integer> data_time = new ArrayList<>();
	public  ArrayList<Integer> data_en_el = new ArrayList<>();
	public  ArrayList<Integer> data_mass_intensity = new ArrayList<>();
	public  ArrayList<Integer> data_en_el_intensity = new ArrayList<>();
	/**
	 * B - approximated
	 */
	public  ArrayList<Double> data_Bo = new ArrayList<>();
	
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
	public  Chart_analyser analyser_mass;
	public  Chart_analyser analyser_en_el;
	public GUI user_interface;
	private Config cfg;
	
	public  boolean draw_graph_mass = false;
	public  boolean draw_graph_en_el = false;
	public  int rendering_rate = 10;
	public  int current_step = 0;
	public  double M0;
	public  double K;
	public  double B0;
	
	public float en_el_K;
	public float en_el_b;
	
	// Parameters for electron energy
	
	public int start_V_cyclic = 0;
	public int stop_V_cyclic = 3800;
	public float step_V_cyclic = 0.02f;
	
	public int start_V = 0;
	public int stop_V = 3800;
	public float step_V = 0.02f;
	public boolean en_el_cycle_scan = false; //0 - linear, 1 - cycle
	public boolean start_e_scan = false; //0 - stop, 1 - start
	public int dac_voltage = 0;
	public float dac_voltage_float = 0; //for use step_V (float)
	/**
	 * en_el value in volts (-2, 17)
	 */
	public  float current_en_el_float;
	
	//public int scan_count = 0;
	/**
	 * size of array for approximation B
	 */
	public  int approx_N = 100;
	
	public boolean en_el_delay = false;
	
	
	
	public Run() {
		prog = this;
		arduino = new Connector(); 
		cfg = new Config();
		read_settings();
		user_interface = new GUI();
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
	    .addKeyEventDispatcher(new KeyEventDispatcher() {
	        @Override
	        public boolean dispatchKeyEvent(KeyEvent e) {
	        	int action = e.getID();
	        	Component focus = user_interface.get_focus_owner();
	        	
	        	//if (focus != m0_textbox && focus != b0_textbox && focus != k_textbox && focus != N_textbox &&
	        	//		action == KeyEvent.KEY_PRESSED) {
	        	if (!(focus instanceof JTextField) && action == KeyEvent.KEY_PRESSED) {	
	        		Panel_base_interfase D;
	        		if (user_interface.e_energy_frame.isAncestorOf(focus)) {
	        			D = user_interface.e_energy_frame.energy_panel;
	        		}
	        		else {
	        			D = user_interface.mass_panel;
	        		}
	        		char c = e.getKeyChar();
	        		int code = e.getKeyCode();
	        		
					if (c == '+' | c == '-'){
						D.zoom(c, 'X');
					}
					else if(code == KeyEvent.VK_LEFT) {
						D.move('L');
					}
					else if(code == KeyEvent.VK_RIGHT) {
						D.move('R');
					}
	        	}
	          
	          return false;
	        }
	  });
	}
	
	private void read_settings() {
		M0 = parse_double("M0", 0);
		B0 = parse_double("B0", 0);
		K = parse_double("K", 0.001);
		
		en_el_b = (float)parse_double("en_el_b", 400);
		en_el_K = (float)parse_double("en_el_K", 200);
		
		//load standard settings for fast scan of energy
		//start_V_cyclic = parse_double("start_V_cyclic", 0);
		//stop_V_cyclic = parse_double("stop_V_cyclic", 0);
		//step_V_cyclic = (float)parse_double("step_V_cyclic", 0);
		
		
	}
	
	private void write_settings() {
		cfg.set_conf_value("M0", Double.toString(M0));
		cfg.set_conf_value("B0", Double.toString(B0));
		cfg.set_conf_value("K", Double.toString(K));
		
		cfg.set_conf_value("en_el_b", Double.toString(en_el_b));
		cfg.set_conf_value("en_el_K", Double.toString(en_el_K));
		
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
		return M0 + K * Math.pow((B0+B), 2);
	}
	
	/**
	 * For calc (0, 3800) from (-2, 17)
	 * @return
	 */
	public int calc_int_en_el(float en_el) {
		return Math.round(en_el*en_el_K + en_el_b);
	}
	
	/**
	 * For calc (-2, 17) from (0, 3800)
	 * @return
	 */
	public float calc_float_en_el(int en_el) {
		return (en_el-en_el_b)/en_el_K;
	}
	
	/**
	 * for calc (-2, 17) step to (0, 3800)
	 * @param step
	 * @return
	 */
	public float calc_step(float step) {
		return step*(float)en_el_K;
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
		//user_interface.mass_panel.volt.set_value(current_en_el_float);
		if (start_e_scan) {
			user_interface.mass_panel.volt.set_value(current_en_el_float);
			//user_interface.mass_panel.volt.set_new_en_el((int)current_en_el);
			//user_interface.e_energy_frame.energy_panel.volt.set_new_en_el((int)current_en_el);
		}
	}
	
	public void en_el_scan_loop() {
		if(en_el_cycle_scan) {
			en_el_scan_fast();
		}
		else {
			en_el_scan_long();
		}
	}
	
	private void en_el_scan_long() {
		if(dac_voltage + step_V < stop_V) {
			dac_voltage_float += step_V;
			dac_voltage = Math.round(dac_voltage_float);
			en_el_delay = false;
		}
		else {
			dac_voltage = start_V;
			dac_voltage_float = start_V;
			en_el_delay = true;
		}
	}
	
	private void en_el_scan_fast() {
		en_el_scan_long();
	}
	
	public void reset() {
		data_time.clear();
		//data_B.clear();
		data_Bo.clear();
		data_en_el.clear();
		data_mass_intensity.clear();
		
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
