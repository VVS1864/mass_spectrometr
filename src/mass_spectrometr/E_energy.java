package mass_spectrometr;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class E_energy extends JDialog{
	private boolean visible = false;
	
	public E_energy(JFrame jframe) {
		super(jframe, "Electron energy");
		setSize(640, 480);
		
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowevent) {
				switch_visible();
			}	
		});
	}
	
	public boolean is_visible(){
		return visible;
	}
	
	public void switch_visible() {
		visible = (visible == true ? false : true);
		this.setVisible(visible);		
	}
}
