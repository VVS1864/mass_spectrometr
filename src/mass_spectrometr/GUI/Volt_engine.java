package mass_spectrometr.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mass_spectrometr.Run;

public class Volt_engine extends JPanel{
	private JTextField dac_voltage_textbox;
	private JSlider slider;
	private JRadioButton Cyclic_check;
	
	private JTextField start_textbox;
	private JTextField stop_textbox;
	private JTextField speed_textbox;
	private final JPanel r_p = new JPanel();
	
	public JButton button_update;
	private JButton button_start;
	static final int MIN = 0;
	static final int MAX = 4096;
	static final int INIT = 0;
	
	public Volt_engine() {
		ActionListener start = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (check_values()) {
					if (Run.prog.start_e_scan == 0) {
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
				//cnvs.repaint(); !!!
			}
			
		};
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		r_p.setLayout(new BoxLayout(r_p, BoxLayout.Y_AXIS));
		
		slider = new JSlider(JSlider.HORIZONTAL,
                MIN, MAX, INIT);
		
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(1000);
		slider.setMinorTickSpacing(500);
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		
		JLabel spacer = new JLabel("  ");
		
		JLabel Start = new JLabel(" Start: ");
		start_textbox = new JTextField(Integer.toString(Run.prog.start_V));
		start_textbox.setMaximumSize(new Dimension(50, 30));
		
		JLabel Stop = new JLabel(" Stop: ");
		stop_textbox = new JTextField(Integer.toString(Run.prog.stop_V));
		stop_textbox.setMaximumSize(new Dimension(50, 30));
		
		JLabel Speed = new JLabel(" Speed: ");		
		speed_textbox = new JTextField(Float.toString(Run.prog.step_V));
		speed_textbox.setMaximumSize(new Dimension(50, 30));
		
		JLabel dac_voltage = new JLabel("Voltage: ");
		dac_voltage_textbox = new JTextField(Integer.toString(Run.prog.dac_voltage));
		dac_voltage_textbox.setMaximumSize(new Dimension(50, 30));
		
		
		button_update = new JButton("Update");
		button_update.addActionListener(update);
		
		button_start = new JButton("Start scan");
		button_start.addActionListener(start);
		
		Cyclic_check = new JRadioButton(" Cyclic:");
		
		slider.setMaximumSize(new Dimension(300, 100));
		
		add(Start);
		add(start_textbox);
		add(Stop);
		add(stop_textbox);
		add(Speed);
		add(speed_textbox);
	    
		add(Cyclic_check);
		p.add(dac_voltage);
		p.add(dac_voltage_textbox);
		p.add(button_update);
		p.add(button_start);
		
		r_p.add(p);
		r_p.add(slider);
		
		add(r_p);
		ChangeListener listener = new ChangeListener(){
			public void stateChanged(ChangeEvent event){
		
				JSlider slider = (JSlider)event.getSource();
				int value = slider.getValue();
				dac_voltage_textbox.setText(Integer.toString(value));
				
				if(Run.prog.start_e_scan == 0) Run.prog.dac_voltage = value;
			}
		};
		
		slider.addChangeListener(listener);
	}
	private boolean check_values() {
		String str_start = start_textbox.getText();
		int new_start;
		String str_stop = stop_textbox.getText();
		int new_stop;
		String str_speed = speed_textbox.getText();
		float new_speed;
		String str_dac_voltage = dac_voltage_textbox.getText();
		int new_dac_voltage;
		
		
		try {
			new_start = Integer.parseInt(str_start);
			new_stop = Integer.parseInt(str_stop);
			new_speed = Float.parseFloat(str_speed);
			new_dac_voltage = Integer.parseInt(str_dac_voltage);
		}
		catch(NumberFormatException ex) {
			set_red();
			return false;
		}
		
		// Check values for MAX value
		if (!set_value(new_dac_voltage)||
			(new_stop < new_start)||
			(new_stop > MAX)||
			(new_start + new_speed > MAX)) {
			
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
		
		Run.prog.start_V = new_start;
		Run.prog.stop_V = new_stop;
		Run.prog.step_V = new_speed;
		Run.prog.dac_voltage = new_dac_voltage;
		if (Cyclic_check.isSelected()) Run.prog.cycle_scan = 1;
		else Run.prog.cycle_scan = 0;
		
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
		button_start.setText("Stop scan");
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
		
		slider.setEnabled(false);
		Cyclic_check.setEnabled(false);
		dac_voltage_textbox.setEnabled(false);
		start_textbox.setEnabled(false);
		stop_textbox.setEnabled(false);
		speed_textbox.setEnabled(false);
		button_update.setEnabled(false);
	}
	public void stop_scan() {
		Run.prog.start_e_scan = 0;
		button_start.setText("Start scan");
		
		slider.setEnabled(true);
		Cyclic_check.setEnabled(true);
		dac_voltage_textbox.setEnabled(true);
		start_textbox.setEnabled(true);
		stop_textbox.setEnabled(true);
		speed_textbox.setEnabled(true);
		button_update.setEnabled(true);
	}
	public boolean set_value(int value) {
		if(value <= MAX && value >= MIN) {
			dac_voltage_textbox.setText(Integer.toString(value));
			slider.setValue(value);
			dac_voltage_textbox.setBackground(Color.WHITE);
			return true;
		}
		else {
			dac_voltage_textbox.setBackground(Color.RED);
			return false;
		}
	}
	
	public int get_value() {
		return slider.getValue();
	}
}
