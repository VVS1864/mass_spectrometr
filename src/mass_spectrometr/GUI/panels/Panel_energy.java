package mass_spectrometr.GUI.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import mass_spectrometr.Run;
import mass_spectrometr.GUI.graphs.Graph_energy;
import mass_spectrometr.GUI.graphs.Graph_mass;

public class Panel_energy extends Panel_base{
	
	protected JTextField start_textbox;
	protected JTextField stop_textbox;
	protected JTextField speed_textbox;
	
	
	public Panel_energy() {
		ActionListener update = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str_start = start_textbox.getText();
				double new_start;
				String str_stop = stop_textbox.getText();
				double new_stop;
				String str_speed = speed_textbox.getText();
				double new_speed;
				
				
				try {
					new_start = Double.parseDouble(str_start);
					new_stop = Double.parseDouble(str_stop);
					new_speed = Double.parseDouble(str_speed);
					
				}
				catch(NumberFormatException ex) {
					start_textbox.setBackground(Color.RED);
					stop_textbox.setBackground(Color.RED);
					speed_textbox.setBackground(Color.RED);
					return;
				}
				start_textbox.setBackground(Color.WHITE);
				stop_textbox.setBackground(Color.WHITE);
				speed_textbox.setBackground(Color.WHITE);
				Run.prog.start_V = new_start;
				Run.prog.stop_V = new_stop;
				Run.prog.speed_V = new_speed;
				
				cnvs.repaint();
			}
			
		};
		
		JLabel Voltage = new JLabel(" Voltage: ");
		
		JLabel Start = new JLabel(" Start: ");
		start_textbox = new JTextField(Double.toString(Run.prog.start_V));
		start_textbox.setMaximumSize(new Dimension(50, 40));
		
		JLabel Stop = new JLabel(" Stop: ");
		stop_textbox = new JTextField(Double.toString(Run.prog.stop_V));
		stop_textbox.setMaximumSize(new Dimension(50, 40));
		
		JLabel Speed = new JLabel(" Speed: ");		
		speed_textbox = new JTextField(Double.toString(Run.prog.speed_V));
		speed_textbox.setMaximumSize(new Dimension(50, 40));
	
		
		JButton button_update = new JButton("Update");
		button_update.addActionListener(update);
		JLabel current_energy = new JLabel(" Energy: ");
		label_X = new JLabel();
		JLabel spacer = new JLabel("  ");
		JLabel current_intensity = new JLabel(" Intensity: ");
		label_Y = new JLabel();	

		top_panel_2.add(Voltage);
		top_panel_2.add(Start);
		top_panel_2.add(start_textbox);
		top_panel_2.add(Stop);
	    top_panel_2.add(stop_textbox);
	    top_panel_2.add(Speed);
	    top_panel_2.add(speed_textbox);
	    top_panel_2.add(button_update);
	    top_panel_2.add(current_energy);
	    top_panel_2.add(label_X);
	    top_panel_2.add(spacer);
	    top_panel_2.add(current_intensity);
	    top_panel_2.add(label_Y);
	    top_panel_2.setAlignmentX(Component.LEFT_ALIGNMENT);
	    
		
		cnvs = new Graph_energy(Run.prog.data_Bo, Run.prog.data_intensity, "En", "int");
		add(cnvs, BorderLayout.CENTER);
	}
}
