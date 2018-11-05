package mass_spectrometr.GUI.graphs.mass_setter_panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import mass_spectrometr.GUI.Calibration_frame;

public class Mass_setter_panel extends JPanel implements ActionListener{
	private JButton button;
	private JTextField mass_textbox;
	private Calibration_frame parent;
	private float mass;
	private boolean used = false;
	
	public Mass_setter_panel(String text, Calibration_frame parent) {
		TitledBorder title = new TitledBorder(text);
		setBorder(title);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.parent = parent;
				
		button = new JButton(text);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.addActionListener(this);
		
		mass_textbox = new JTextField();
		
		JPanel button_panel = new JPanel(new CardLayout());
		button_panel.setPreferredSize(new Dimension(50, 25));
		
		button_panel.add(button);
		
		add(button_panel);
		add(mass_textbox);
	}
	
	private void set_text(String text) {
		button.setText(text);
	}
	public boolean is_used() {
		return used;
	}
	public void set_mass(float mass) {
		this.mass = mass;
		set_text(Float.toString(mass));
		used = true;
	}
	
	public float get_mass() {
		return mass;
	}
	
	public float get_real_mass() {
		float real_mass = 0;
		try {
			real_mass = Float.parseFloat(mass_textbox.getText());
			mass_textbox.setBackground(Color.WHITE);
		}
		catch(NumberFormatException ex) {
			parent.format_err = true;
			mass_textbox.setBackground(Color.RED);
		}
		return real_mass;
		
	}
	
	public void set_enable(boolean enable) {
		button.setEnabled(enable);
		mass_textbox.setEnabled(enable);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		parent.move_active = true;
		parent.current_set = this;
		parent.set_enable(false);
	}
}
