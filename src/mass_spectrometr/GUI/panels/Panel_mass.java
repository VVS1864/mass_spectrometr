package mass_spectrometr.GUI.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import mass_spectrometr.Run;
import mass_spectrometr.GUI.Volt_engine;
import mass_spectrometr.GUI.graphs.Graph_mass;

public class Panel_mass extends Panel_base{
	
	protected JTextField m0_textbox;
	protected JTextField k_textbox;
	protected JTextField b0_textbox;
	protected JTextField N_textbox;
	public Volt_engine volt;
	public Panel_mass() {
		
		ActionListener update_K = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str_m0 = m0_textbox.getText();
				double new_M0;
				String str_k = k_textbox.getText();
				double new_K;
				String str_b0 = b0_textbox.getText();
				double new_B0;
				String str_N = N_textbox.getText();
				int new_N;
				
				try {
					new_M0 = Double.parseDouble(str_m0);
					new_K = Double.parseDouble(str_k);
					new_B0 = Double.parseDouble(str_b0);
					new_N = Integer.parseInt(str_N);
				}
				catch(NumberFormatException ex) {
					k_textbox.setBackground(Color.RED);
					m0_textbox.setBackground(Color.RED);
					b0_textbox.setBackground(Color.RED);
					N_textbox.setBackground(Color.RED);
					return;
				}
				k_textbox.setBackground(Color.WHITE);
				m0_textbox.setBackground(Color.WHITE);
				b0_textbox.setBackground(Color.WHITE);
				N_textbox.setBackground(Color.WHITE);
				Run.prog.M0 = new_M0;
				Run.prog.K = new_K;
				Run.prog.B0 = new_B0;
				Run.prog.approx_N = new_N;
				
				cnvs.repaint();
			}
			
		};
		
		JLabel coefficients = new JLabel(" Coefficients: ");
		JLabel M0 = new JLabel(" M0: ");
		m0_textbox = new JTextField(Double.toString(Run.prog.M0));
		m0_textbox.setMaximumSize(new Dimension(50, 40));
		JLabel K = new JLabel(" K: ");
		k_textbox = new JTextField(Double.toString(Run.prog.K));
		k_textbox.setMaximumSize(new Dimension(50, 40));
		JLabel B0 = new JLabel(" B0: ");
		b0_textbox = new JTextField(Double.toString(Run.prog.B0));
		b0_textbox.setMaximumSize(new Dimension(50, 40));
		JLabel N = new JLabel(" N: ");
		N_textbox = new JTextField(Integer.toString(Run.prog.approx_N));
		N_textbox.setMaximumSize(new Dimension(50, 40));
		JButton button_K = new JButton("Update");
		button_K.addActionListener(update_K);
		JLabel current_mass = new JLabel(" Mass: ");
		label_X = new JLabel();
		JLabel spacer = new JLabel("  ");
		JLabel current_intensity = new JLabel(" Intensity: ");
		label_Y = new JLabel();	

		top_panel_2.add(coefficients);
		top_panel_2.add(M0);
		top_panel_2.add(m0_textbox);
		top_panel_2.add(K);
	    top_panel_2.add(k_textbox);
	    top_panel_2.add(B0);
	    top_panel_2.add(b0_textbox);
	    top_panel_2.add(N);
	    top_panel_2.add(N_textbox);
	    top_panel_2.add(button_K);
	    top_panel_2.add(current_mass);
	    top_panel_2.add(label_X);
	    top_panel_2.add(spacer);
	    top_panel_2.add(current_intensity);
	    top_panel_2.add(label_Y);
	    top_panel_2.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    cnvs = new Graph_mass(Run.prog.data_Bo, Run.prog.data_mass_intensity, "M", "int", Run.prog.analyser_mass);
	    add(cnvs, BorderLayout.CENTER);
	    
	    volt = new Volt_engine();
	    add(volt, BorderLayout.SOUTH);
	}
}
