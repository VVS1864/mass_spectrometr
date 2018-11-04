package mass_spectrometr.GUI;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;

import javax.swing.JDialog;
import javax.swing.JFrame;

import mass_spectrometr.Run;
import mass_spectrometr.GUI.graphs.Graph_calibration;
import mass_spectrometr.GUI.graphs.Graph_mass;

public class Calibration_frame extends JDialog{
	public Calibration_frame(JFrame jframe, double X_scale, double Y_scale) {
		super(jframe, "Mass calibration");
		//setSize(1000, 500);
		//setMinimumSize(new Dimension(640, 480));
		setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()); 
		Graph_calibration cnvs = new Graph_calibration(Run.prog.data_Bo, Run.prog.data_mass_intensity, "M", "int", Run.prog.analyser_mass, X_scale, Y_scale);
	    add(cnvs, BorderLayout.CENTER);
	    setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
	    setVisible(true);
	}
}
