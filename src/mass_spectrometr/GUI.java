package mass_spectrometr;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class GUI {

	public GUI(){
		JFrame mainFrame = new JFrame("Java AWT Examples");
	    mainFrame.setSize(800,600);
	    mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      }); 
	    Run.cnvs = new Graph_canvas();
	    mainFrame.add(Run.cnvs);
	    mainFrame.setVisible(true); 
	}

}


