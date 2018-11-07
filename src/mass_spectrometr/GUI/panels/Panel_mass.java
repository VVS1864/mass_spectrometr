package mass_spectrometr.GUI.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import mass_spectrometr.Run;
import mass_spectrometr.GUI.Volt_engine;
import mass_spectrometr.GUI.Volt_engine_fast;
import mass_spectrometr.GUI.Volt_engine_long;
import mass_spectrometr.GUI.graphs.Graph_mass;

public class Panel_mass extends Panel_base{
	
	protected JTextField m0_textbox;
	protected JTextField k_textbox;
	protected JTextField b0_textbox;
	protected JTextField N_textbox;
	
	protected JTextField en_el_K_textbox;
	protected JTextField en_el_b_textbox;
	
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
				
				String str_en_el_K = en_el_K_textbox.getText();
				float new_en_el_K;
				String str_en_el_b = en_el_b_textbox.getText();
				float new_en_el_b;
				
				try {
					new_M0 = Double.parseDouble(str_m0);
					new_K = Double.parseDouble(str_k);
					new_B0 = Double.parseDouble(str_b0);
					new_N = Integer.parseInt(str_N);
					
					new_en_el_K = Float.parseFloat(str_en_el_K);
					new_en_el_b = Float.parseFloat(str_en_el_b);
				}
				catch(NumberFormatException ex) {
					k_textbox.setBackground(Color.RED);
					m0_textbox.setBackground(Color.RED);
					b0_textbox.setBackground(Color.RED);
					N_textbox.setBackground(Color.RED);
					
					en_el_K_textbox.setBackground(Color.RED);
					en_el_b_textbox.setBackground(Color.RED);
					return;
				}
				k_textbox.setBackground(Color.WHITE);
				m0_textbox.setBackground(Color.WHITE);
				b0_textbox.setBackground(Color.WHITE);
				N_textbox.setBackground(Color.WHITE);
				
				en_el_K_textbox.setBackground(Color.WHITE);
				en_el_b_textbox.setBackground(Color.WHITE);
				
				Run.prog.M0 = new_M0;
				Run.prog.K = new_K;
				Run.prog.B0 = new_B0;
				Run.prog.approx_N = new_N;
				
				Run.prog.en_el_K = new_en_el_K;
				Run.prog.en_el_b = new_en_el_b;
				
				cnvs.repaint();
			}
			
		};
		
		JLabel coefficients = new JLabel(" Coefficients: ");
		JLabel M0 = new JLabel(" M0: ");
		m0_textbox = new JTextField();
		m0_textbox.setPreferredSize(new Dimension(80, 25));
		//m0_textbox.setMaximumSize(new Dimension(50, 40));
		JLabel K = new JLabel(" K: ");
		k_textbox = new JTextField();
		k_textbox.setPreferredSize(new Dimension(80, 25));
		//k_textbox.setMaximumSize(new Dimension(50, 40));
		JLabel B0 = new JLabel(" B0: ");
		b0_textbox = new JTextField();
		b0_textbox.setPreferredSize(new Dimension(80, 25));
		//b0_textbox.setMaximumSize(new Dimension(50, 40));
		
		set_new_coef_values(Run.prog.K, Run.prog.M0, Run.prog.B0);
		
		JLabel N = new JLabel(" N: ");
		N_textbox = new JTextField(Integer.toString(Run.prog.approx_N));
		//N_textbox.setMaximumSize(new Dimension(50, 40));
		N_textbox.setPreferredSize(new Dimension(80, 25));
		JLabel Energy_K = new JLabel(" En K: ");
		en_el_K_textbox = new JTextField(Double.toString(Run.prog.en_el_K));
		en_el_K_textbox.setPreferredSize(new Dimension(80, 25));
		//en_el_K_textbox.setMaximumSize(new Dimension(50, 40));
		JLabel Energy_B = new JLabel(" En B: ");
		en_el_b_textbox = new JTextField(Double.toString(Run.prog.en_el_b));
		en_el_b_textbox.setPreferredSize(new Dimension(80, 25));
		//en_el_b_textbox.setMaximumSize(new Dimension(50, 40));
		
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
	    
	    top_panel_2.add(Energy_K);
	    top_panel_2.add(en_el_K_textbox);
	    top_panel_2.add(Energy_B);
	    top_panel_2.add(en_el_b_textbox);
	    
	    top_panel_2.add(button_K);
	    top_panel_2.add(current_mass);
	    top_panel_2.add(label_X);
	    top_panel_2.add(spacer);
	    top_panel_2.add(current_intensity);
	    top_panel_2.add(label_Y);
	    top_panel_2.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
	    cnvs = new Graph_mass("M", "int", Run.prog.analyser_mass);
	    cnvs.addMouseListener(this);
	    add(cnvs, BorderLayout.CENTER);
	    
	    volt = new Volt_engine_fast();
	    add(volt, BorderLayout.SOUTH);
	}
	
	public void set_new_coef_values(double new_K, double new_M0, double new_B0) {
		//DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		//otherSymbols.setDecimalSeparator('.');
		//DecimalFormat formatter = new DecimalFormat("#0.000000", otherSymbols);
		
		m0_textbox.setText(Double.toString(new_M0));
		b0_textbox.setText(Double.toString(new_B0));
		k_textbox.setText(Double.toString(new_K));
	}
}
