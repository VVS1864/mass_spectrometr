package mass_spectrometr.GUI;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

import mass_spectrometr.GUI.panels.Panel_energy;
import mass_spectrometr.GUI.panels.Panel_mass;

public class E_energy extends JDialog{
	private boolean visible = false;
	public Panel_energy energy_panel; 
	
	public E_energy(JFrame jframe) {
		super(jframe, "Electron energy");
		setSize(640, 480);
		
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowevent) {
				switch_visible();
			}	
		});
		
		energy_panel = new Panel_energy();
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
