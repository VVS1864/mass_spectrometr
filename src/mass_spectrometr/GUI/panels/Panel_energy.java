package mass_spectrometr.GUI.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mass_spectrometr.Run;
import mass_spectrometr.GUI.Volt_engine;
import mass_spectrometr.GUI.Volt_engine_long;
import mass_spectrometr.GUI.graphs.Graph_energy;


public class Panel_energy extends Panel_base{
	
	//protected JTextField start_textbox;
	//protected JTextField stop_textbox;
	//protected JTextField speed_textbox;
	
	protected final JPanel mass_indication_panel = new JPanel();
	protected JLabel mass_label;
	public JLabel mass_indication;
	public Volt_engine volt;
	private final JPanel bot_panel = new JPanel();
	
	public Panel_energy() {
		bot_panel.setLayout(new BoxLayout(bot_panel, BoxLayout.Y_AXIS));
		mass_indication_panel.setLayout(new BoxLayout(mass_indication_panel, BoxLayout.X_AXIS));
		
		//JLabel Voltage = new JLabel(" Voltage: ");
		/*
		JLabel Start = new JLabel(" Start: ");
		start_textbox = new JTextField(Integer.toString(Run.prog.start_V));
		start_textbox.setMaximumSize(new Dimension(50, 40));
		
		JLabel Stop = new JLabel(" Stop: ");
		stop_textbox = new JTextField(Integer.toString(Run.prog.stop_V));
		stop_textbox.setMaximumSize(new Dimension(50, 40));
		
		JLabel Speed = new JLabel(" Speed: ");		
		speed_textbox = new JTextField(Integer.toString(Run.prog.step_V));
		speed_textbox.setMaximumSize(new Dimension(50, 40));
		*/
		//JLabel Cyclic = new JLabel(" Cyclic: ");		
		//JCheckBox Cyclic_check = new JCheckBox();
	
		//volt = new Volt_engine();
		
		//JButton button_update = new JButton("Update");
		//button_update.addActionListener(update);
		
		JLabel current_energy = new JLabel(" Energy: ");
		label_X = new JLabel();
		JLabel spacer = new JLabel("  ");
		JLabel current_intensity = new JLabel(" Intensity: ");
		label_Y = new JLabel();	
		mass_label = new JLabel(" Current mass: ");
		mass_indication = new JLabel("");

		//top_panel_2.add(Voltage);
		/*
		top_panel_2.add(Start);
		top_panel_2.add(start_textbox);
		top_panel_2.add(Stop);
	    top_panel_2.add(stop_textbox);
	    top_panel_2.add(Speed);
	    top_panel_2.add(speed_textbox);
	    */
	    //top_panel_2.add(Cyclic);
	    //top_panel_2.add(Cyclic_check);
	    
	    //top_panel_2.add(button_update);
	    //top_panel_2.add(volt);
	    top_panel_2.add(current_energy);
	    top_panel_2.add(label_X);
	    top_panel_2.add(spacer);
	    top_panel_2.add(current_intensity);
	    top_panel_2.add(label_Y);
	    top_panel_2.add(mass_label);
	    top_panel_2.add(mass_indication);
		
	    top_panel_2.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		cnvs = new Graph_energy(Run.prog.data_en_el, Run.prog.data_en_el_intensity, "En", "int", Run.prog.analyser_en_el);
		add(cnvs, BorderLayout.CENTER);
		
		volt = new Volt_engine_long();
	    
		//mass_label = new JLabel("Current mass: ");
		//mass_indication = new JLabel("");
		//mass_indication_panel.add(mass_label);
		//mass_indication_panel.add(mass_indication);
		
		//bot_panel.add(volt);
		//bot_panel.add(mass_indication_panel);
		//bot_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(volt, BorderLayout.SOUTH);

	}
}
