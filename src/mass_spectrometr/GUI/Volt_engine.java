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
	//private JRadioButton Cyclic_check;
	
	protected JTextField start_textbox;
	protected JTextField stop_textbox;
	protected JTextField speed_textbox;
	private final JPanel r_p = new JPanel();
	
	public JButton button_update;
	protected JButton button_start;
	static final int MIN = -200;
	static final int MAX = 1700;
	static int INIT;
		
	public Volt_engine() {
		ActionListener start = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (check_values()) {
					if (Run.prog.start_e_scan == 0) {
						Run.prog.cycle_scan = get_cycle_scan();
						start_scan();
					}
					else {
						stop_scan();
					}
				}
			}
		};
		
		ActionListener update = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				check_values();
				//cnvs.repaint(); 
			}
			
		};
		INIT = Math.round(calc_float_from_int(Run.prog.dac_voltage)*100);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		r_p.setLayout(new BoxLayout(r_p, BoxLayout.Y_AXIS));
		
		Hashtable<Integer, JLabel> label_table = new Hashtable<>();
		int[] labels_ints = new int[] {-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
		for(int i:labels_ints) {
			label_table.put(new Integer(i*100), new JLabel(Integer.toString(i)));
		}
		
		slider = new JSlider(JSlider.HORIZONTAL, MIN, MAX, INIT);
		
		slider.setLabelTable(label_table);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(100);
		slider.setMinorTickSpacing(50);
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		
		JLabel spacer = new JLabel("  ");
		
		JLabel Start = new JLabel(" Start: ");
		start_textbox = new JTextField(Float.toString(get_start_V()));
		start_textbox.setMaximumSize(new Dimension(50, 30));
		
		
		JLabel Stop = new JLabel(" Stop: ");
		stop_textbox = new JTextField(Float.toString(get_stop_V()));
		stop_textbox.setMaximumSize(new Dimension(50, 30));
		
		JLabel Speed = new JLabel(" Speed: ");		
		speed_textbox = new JTextField(Float.toString(get_step_V()));
		speed_textbox.setMaximumSize(new Dimension(50, 30));
		
		JLabel dac_voltage = new JLabel("Voltage: ");
		dac_voltage_textbox = new JTextField(Float.toString(calc_float_from_int(Run.prog.dac_voltage)));
		dac_voltage_textbox.setMaximumSize(new Dimension(50, 30));
		
		
		button_update = new JButton("Update");
		button_update.addActionListener(update);
		
		button_start = new JButton("Start scan");
		button_start.addActionListener(start);
		
		//Cyclic_check = new JRadioButton(" Cyclic:");
		
		//slider.setMaximumSize(new Dimension(300, 100));
		
		add(Start);
		add(start_textbox);
		add(Stop);
		add(stop_textbox);
		add(Speed);
		add(speed_textbox);
	    
		//add(Cyclic_check);
		p.add(dac_voltage);
		p.add(dac_voltage_textbox);
		p.add(button_update);
		p.add(button_start);
		
		r_p.add(p);
		r_p.add(slider);
		
		add(r_p);
		
		String t;
		if(get_cycle_scan() == 1) t = "Fast cyclic scan";
		else t = "Long linear scan";
		TitledBorder title = new TitledBorder(t);
		setBorder(title);
		
		ChangeListener listener = new ChangeListener(){
			public void stateChanged(ChangeEvent event){
				JSlider slider = (JSlider)event.getSource();
				float f_value = get_slider_value();
				dac_voltage_textbox.setText(Float.toString(f_value));
				StartStopController.set_sliders(slider.getValue());
				if(Run.prog.start_e_scan == 0) set_dac_voltage();
				
			}
		};
		
		slider.addChangeListener(listener);
		
	}
	private boolean check_values() {
		String str_start = start_textbox.getText();
		float new_start;
		String str_stop = stop_textbox.getText();
		float new_stop;
		String str_speed = speed_textbox.getText();
		float new_speed;
		String str_dac_voltage = dac_voltage_textbox.getText();
		float new_dac_voltage;
		
		
		try {
			new_start = Float.parseFloat(str_start);
			new_stop = Float.parseFloat(str_stop);
			new_speed = Float.parseFloat(str_speed);
			new_dac_voltage = Float.parseFloat(str_dac_voltage);
		}
		catch(NumberFormatException ex) {
			set_red();
			return false;
		}
		int i_new_stop = Math.round(new_stop * 100);
		int i_new_start = Math.round(new_start * 100);
		float f_new_speed = calc_step(new_speed);
		int i_new_dac_voltage = Math.round(new_dac_voltage*100);
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
	
	public void start_scan() {
		Run.prog.start_e_scan = 1;
		
		if (Run.prog.cycle_scan == 1) {
			Run.prog.start_V = Run.prog.start_V_cyclic;
			Run.prog.stop_V = Run.prog.stop_V_cyclic;
			Run.prog.step_V = Run.prog.step_V_cyclic;
		}
		Run.prog.dac_voltage_float = Run.prog.start_V;
		Run.prog.dac_voltage = Run.prog.start_V;
		//else {
			//Run.prog.scan_count = Math.round((Run.prog.stop_V - Run.prog.start_V)/Run.prog.step_V);
		//}
		
		StartStopController.set_enable_disable(false);
		/*
		button_start.setText("Stop scan");
		slider.setEnabled(false);
		dac_voltage_textbox.setEnabled(false);
		start_textbox.setEnabled(false);
		stop_textbox.setEnabled(false);
		speed_textbox.setEnabled(false);
		button_update.setEnabled(false);
		*/
	}
	public void stop_scan() {
		Run.prog.start_e_scan = 0;
		
		StartStopController.set_enable_disable(true);
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
	public void set_value(float value) {
		int i_value = Math.round(value*100);
		StartStopController.set_sliders(i_value);
	}
	
	public float get_slider_value() {
		return slider.getValue()/100.0f;
	}
	private void set_slider_value(float v) {
		slider.setValue(Math.round(v*100));
	}
	
	abstract float get_start_V();
	abstract float get_stop_V();
	abstract float get_step_V();
	abstract int get_cycle_scan();
	
	abstract void set_start_V(float v);
	abstract void set_stop_V(float v);
	abstract void set_step_V(float v);
	
	private void set_dac_voltage() {
		Run.prog.dac_voltage = calc_int_from_float(get_slider_value());
	}
	/**
	 * For calc (0, 3800) from (-2, 17)
	 * @return
	 */
	protected int calc_int_from_float(float f_value){
		return Run.prog.calc_int_en_el(f_value);
	}
	/**
	 * For calc (-2, 17) from (0, 3800)
	 * @return
	 */
	protected float calc_float_from_int(int i_value){	
		return Run.prog.calc_float_en_el(i_value);
	}
	
	/**
	 * for calc (-2, 17) step to (0, 3800)
	 * @param step
	 * @return
	 */
	protected float calc_step(float step) {
		return Run.prog.calc_step(step);
	}
	
	
}
