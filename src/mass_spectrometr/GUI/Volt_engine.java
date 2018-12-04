package mass_spectrometr.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.border.TitledBorder;

import mass_spectrometr.Run;

public abstract class Volt_engine extends JPanel{
	protected JTextField dac_voltage_textbox;
	protected JSlider slider;
	protected JPanel update_panel;
	protected ChangeListener c_listener;
	
	protected JTextField start_textbox;
	protected JTextField stop_textbox;
	protected JTextField speed_textbox;
	private final JPanel r_p = new JPanel();
	
	public JButton button_update;
	protected JButton button_start;
	
	private int MIN;
	private int MAX;
	private int INIT;
	//public static final int MIN = -200;
	//public static final int MAX = 1700;
	//public static int INIT;
		
	public Volt_engine() {		
		ActionListener update = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				check_values();
			}
			
		};
		c_listener = new ChangeListener(){
			public void stateChanged(ChangeEvent event){
				JSlider slider = (JSlider)event.getSource();
				double f_value = get_slider_value();
				dac_voltage_textbox.setText(Double.toString(f_value));
				StartStopController.set_sliders(slider.getValue());
				if(Run.prog.start_e_scan == false) set_dac_voltage();
				
			}
		};
		create_slider();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		r_p.setLayout(new BoxLayout(r_p, BoxLayout.Y_AXIS));
		
		update_panel = new JPanel();
		update_panel.setLayout(new BoxLayout(update_panel, BoxLayout.X_AXIS));
		
		JLabel spacer = new JLabel("  ");
		
		JLabel Start = new JLabel(" Start: ");
		start_textbox = new JTextField(Double.toString(get_start_V()));
		start_textbox.setMaximumSize(new Dimension(50, 30));
		
		
		JLabel Stop = new JLabel(" Stop: ");
		stop_textbox = new JTextField(Double.toString(get_stop_V()));
		stop_textbox.setMaximumSize(new Dimension(50, 30));
		
		JLabel Speed = new JLabel(" Speed: ");		
		speed_textbox = new JTextField(Double.toString(get_step_V()));
		speed_textbox.setMaximumSize(new Dimension(50, 30));
		
		JLabel dac_voltage = new JLabel("Voltage: ");
		dac_voltage_textbox = new JTextField(Double.toString(calc_float_from_int(Run.prog.dac_voltage)));
		dac_voltage_textbox.setMaximumSize(new Dimension(50, 30));
		
		
		button_update = new JButton("Update");
		button_update.addActionListener(update);
				
		//Cyclic_check = new JRadioButton(" Cyclic:");
		
		//slider.setMaximumSize(new Dimension(300, 100));
		
		add(Start);
		add(start_textbox);
		add(Stop);
		add(stop_textbox);
		add(Speed);
		add(speed_textbox);
	    
		//add(Cyclic_check);
		update_panel.add(dac_voltage);
		update_panel.add(dac_voltage_textbox);
		update_panel.add(button_update);
		
		
		r_p.add(update_panel);
		r_p.add(slider);
		
		add(r_p);
		
		String t;
		if(get_cycle_scan()) t = "Fast scan";
		else t = "Long scan";
		TitledBorder title = new TitledBorder(t);
		setBorder(title);
		
		
		
		
		
	}
	private boolean check_values() {
		String str_start = start_textbox.getText();
		double new_start;
		String str_stop = stop_textbox.getText();
		double new_stop;
		String str_speed = speed_textbox.getText();
		double new_speed;
		String str_dac_voltage = dac_voltage_textbox.getText();
		double new_dac_voltage;
		
		
		try {
			new_start = Double.parseDouble(str_start);
			new_stop = Double.parseDouble(str_stop);
			new_speed = Double.parseDouble(str_speed);
			new_dac_voltage = Double.parseDouble(str_dac_voltage);
		}
		catch(NumberFormatException ex) {
			set_red();
			return false;
		}
		int i_new_stop = (int)Math.round(new_stop * 100);
		int i_new_start = (int)Math.round(new_start * 100);
		double f_new_speed = calc_int_step(new_speed);
		int i_new_dac_voltage = (int)Math.round(new_dac_voltage*100);
		// Check values for MAX value
		
		if (!(i_new_dac_voltage <= MAX && i_new_dac_voltage >= MIN)||
			(i_new_stop < i_new_start)||
			(i_new_stop > MAX)||
			(i_new_start + f_new_speed > MAX)||
			i_new_start < MIN) {

			set_red();
			return false;
		}
				
				
		/*		 
		if (new_stop < new_start) return false;
		if (new_stop > MAX) return false;
		if (new_start + new_speed > MAX) return false;
		*/
		
		start_textbox.setBackground(Color.WHITE);
		stop_textbox.setBackground(Color.WHITE);
		speed_textbox.setBackground(Color.WHITE);
		dac_voltage_textbox.setBackground(Color.WHITE);
		
		set_start_V(new_start);
		set_stop_V(new_stop);
		set_step_V(new_speed);
		set_dac_voltage();
		StartStopController.set_sliders(i_new_dac_voltage);
		//Run.prog.dac_voltage = new_dac_voltage;
		
		//if (Cyclic_check.isSelected()) Run.prog.cycle_scan = 1;
		//else Run.prog.cycle_scan = 0;
		
		return true;
	}
	private void set_red() {
		start_textbox.setBackground(Color.RED);
		stop_textbox.setBackground(Color.RED);
		speed_textbox.setBackground(Color.RED);
		dac_voltage_textbox.setBackground(Color.RED);
	}
	
	public boolean start_scan() {
		if (check_values()) {
			start_scan_event();
			return true;
		}
		else {
			return false;
		}
	}
	
	private void start_scan_event() {
		Run.prog.start_e_scan = true;
		Run.prog.first_scan = true;
		if (Run.prog.en_el_cycle_scan == true) {
			button_start.setText("Stop fast scan");
			Run.prog.start_V = Run.prog.start_V_cyclic;
			Run.prog.stop_V = Run.prog.stop_V_cyclic;
			Run.prog.step_V = Run.prog.step_V_cyclic;
			StartStopController.set_enabled_long_scan(false);
		}
		else {
			StartStopController.set_enabled_fast_scan(false);
		}
		
		Run.prog.dac_voltage_float = Run.prog.start_V;
		Run.prog.dac_voltage = Run.prog.start_V;
				
		StartStopController.set_enable_disable(false);
	}
	public boolean stop_scan() {
		//if(Run.prog.en_el_delay) return false;
		Run.prog.en_el_delay = false;
		Run.prog.delay_count = 0;
		Run.prog.start_e_scan = false;
		Run.prog.draw_graph_en_el = false;
		if (Run.prog.en_el_cycle_scan == true) {
			Run.prog.en_el_cycle_scan = false;
			button_start.setText("Start fast scan");
			StartStopController.set_enabled_long_scan(true);
		}
		else {
			StartStopController.set_enabled_fast_scan(true);
		}
		StartStopController.set_enable_disable(true);
		return true;
	}
	/*
	public void set_new_en_el(int en_el) {
		Run.prog.current_en_el_float = calc_float_from_int(en_el);
		set_value(Run.prog.current_en_el_float);
		
	}
	*/
	/*
	public boolean set_value(float value) {
		int i_value = Math.round(value*100);
		if(i_value <= MAX && i_value >= MIN) {
			//dac_voltage_textbox.setText(Float.toString(value));
			//slider.setValue(value);
			StartStopController.set_sliders(i_value);
			dac_voltage_textbox.setBackground(Color.WHITE);
			return true;
		}
		else {
			dac_voltage_textbox.setBackground(Color.RED);
			return false;
		}
	}
	*/
	public void set_value(double value) {
		int i_value = (int)Math.round(value*100);
		StartStopController.set_sliders(i_value);
	}
	
	public double get_slider_value() {
		return slider.getValue()/100.0f;
	}
	private void set_slider_value(double v) {
		slider.setValue((int)Math.round(v*100));
	}
	
	abstract double get_start_V();
	abstract double get_stop_V();
	abstract double get_step_V();
	abstract boolean get_cycle_scan();
	
	abstract void set_start_V(double v);
	abstract void set_stop_V(double v);
	abstract void set_step_V(double v);
	
	private void set_dac_voltage() {
		Run.prog.dac_voltage = calc_int_from_float(get_slider_value());
	}
	/**
	 * For calc (0, 3800) from (-2, 17)
	 * @return
	 */
	protected int calc_int_from_float(double f_value){
		return Run.prog.calc_int_en_el(f_value);
	}
	/**
	 * For calc (-2, 17) from (0, 3800)
	 * @return
	 */
	protected double calc_float_from_int(int i_value){	
		return Run.prog.calc_float_en_el(i_value);
	}
	
	/**
	 * for calc (-2, 17) step to (0, 3800)
	 * @param step
	 * @return
	 */
	protected double calc_int_step(double step) {
		return Run.prog.calc_int_step(step);
	}
	
	/**
	 * for calc (0, 3800)-step to (-2, 17)
	 * @param step
	 * @return
	 */
	protected double calc_float_step(double step) {
		return Run.prog.calc_float_step(step);
	}
	
	private Hashtable<Integer, JLabel> make_label_table(int MIN, int MAX) {
		Hashtable<Integer, JLabel> label_table = new Hashtable<>();
		int[] labels_ints = new int[(MAX - MIN)/100+1];
		labels_ints[0] = MIN/100;
		
		int j = MIN/100+1;
		for (int i = 1; i<labels_ints.length; i++) {
			labels_ints[i] = j;
			j++;
		}
		
		for(int i:labels_ints) {
			label_table.put(new Integer(i*100), new JLabel(Integer.toString(i)));
		}
		return label_table;
	}
	
	public void create_slider() {
		try{
			r_p.remove(slider);
		}
		catch(NullPointerException ex) {
		}
		MIN = (int)Math.round(calc_float_from_int(0)*100);
		MAX = (int)Math.round(calc_float_from_int(3800)*100);
		INIT = (int)Math.round(calc_float_from_int(Run.prog.dac_voltage)*100);
		
		Hashtable<Integer, JLabel> label_table = make_label_table(MIN, MAX);
		
		slider = new JSlider(JSlider.HORIZONTAL, MIN, MAX, INIT);
		
		slider.setLabelTable(label_table);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(100);
		slider.setMinorTickSpacing(50);
		slider.addChangeListener(c_listener);
		r_p.add(slider);
		revalidate();
		repaint();
		
	}
	
	public int get_MIN() {
		return MIN/100;
	}
	
	
}
