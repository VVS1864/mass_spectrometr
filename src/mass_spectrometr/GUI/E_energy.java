package mass_spectrometr.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mass_spectrometr.Run;
import mass_spectrometr.GUI.panels.Panel_energy;

public class E_energy extends JDialog{
	private boolean visible = false;
	public Panel_energy energy_panel; 
	public JButton button_start;
	
	public E_energy(JFrame jframe) {
		super(jframe, "Electron energy");
		setSize(1000, 500);
		setMinimumSize(new Dimension(640, 480));
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowevent) {
				switch_visible();
			}	
		});
		
		//ActionListener start/stop draw graph 		
	    ActionListener start_action_listener = new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent e) {
	    		if(!Run.prog.draw_graph_en_el) {
	    			button_start.setText("Stop");
  					Run.prog.draw_graph_en_el = true;
	    		}
	    		else {
	    			button_start.setText("Draw graph");
  					Run.prog.draw_graph_en_el = false;
	    		}
			}
	    };
		
		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BoxLayout(top_panel, BoxLayout.X_AXIS));
		
		button_start = new JButton("Draw graph");
		button_start.addActionListener(start_action_listener);
				
		top_panel.add(button_start);
		
		energy_panel = new Panel_energy();
		add(top_panel, BorderLayout.NORTH);
		add(energy_panel,  BorderLayout.CENTER);
	}
	
	public boolean is_visible(){
		return visible;
	}
	
	public void switch_visible() {
		visible = (visible == true ? false : true);
		this.setVisible(visible);		
	}
}
