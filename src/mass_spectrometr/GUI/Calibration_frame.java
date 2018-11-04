package mass_spectrometr.GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


import mass_spectrometr.Run;
import mass_spectrometr.GUI.graphs.Graph_calibration;
import mass_spectrometr.GUI.graphs.Graph_canvas;


public class Calibration_frame extends JDialog implements MouseMotionListener {
	private double X_scale;
	private double Y_scale;
	private Graph_canvas cnvs;
	
	private JButton button_1;
	private JButton button_2;
	private JButton button_3;
	private JButton button_apply;
	
	public Calibration_frame(JFrame jframe, double X_scale, double Y_scale) {
		super(jframe, "Mass calibration");
		// setSize(1000, 500);
		// setMinimumSize(new Dimension(640, 480));
		setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.X_scale = X_scale;
		this.Y_scale = Y_scale;
		
		cnvs = new Graph_calibration(Run.prog.data_Bo, Run.prog.data_mass_intensity, "M", "int",
				Run.prog.analyser_mass, X_scale, Y_scale);

		cnvs.addMouseMotionListener(this);

		JPanel fields_panel = new JPanel();
		fields_panel.setLayout(new BoxLayout(fields_panel, BoxLayout.X_AXIS));

		JPanel mass_1_panel = new JPanel();
		mass_1_panel.setLayout(new BoxLayout(mass_1_panel, BoxLayout.Y_AXIS));
		JPanel mass_2_panel = new JPanel();
		mass_2_panel.setLayout(new BoxLayout(mass_2_panel, BoxLayout.Y_AXIS));
		JPanel mass_3_panel = new JPanel();
		mass_3_panel.setLayout(new BoxLayout(mass_3_panel, BoxLayout.Y_AXIS));
		JPanel apply_panel = new JPanel();

		JPanel button_1_panel = new JPanel(new CardLayout());
		button_1 = new JButton("Mass 1");
		button_1_panel.setPreferredSize(new Dimension(50, 30));
		
		button_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		button_2 = new JButton("Mass 2");
		button_2.setAlignmentX(Component.CENTER_ALIGNMENT);
		button_3 = new JButton("Mass 3");
		button_3.setAlignmentX(Component.CENTER_ALIGNMENT);
		button_apply = new JButton("Apply");
		button_apply.setAlignmentX(Component.CENTER_ALIGNMENT);

		JTextField mass_1_textbox = new JTextField();
		JTextField mass_2_textbox = new JTextField();
		JTextField mass_3_textbox = new JTextField();

		TitledBorder title = new TitledBorder("Mass 1");
		mass_1_panel.setBorder(title);
		title = new TitledBorder("Mass 2");
		mass_2_panel.setBorder(title);
		title = new TitledBorder("Mass 3");
		mass_3_panel.setBorder(title);

		button_1_panel.add(button_1);
		mass_1_panel.add(button_1_panel);
		mass_1_panel.add(mass_1_textbox);
		mass_2_panel.add(button_2);
		mass_2_panel.add(mass_2_textbox);
		mass_3_panel.add(button_3);
		mass_3_panel.add(mass_3_textbox);
		apply_panel.add(button_apply);

		fields_panel.add(mass_1_panel);
		fields_panel.add(mass_2_panel);
		fields_panel.add(mass_3_panel);
		fields_panel.add(apply_panel);

		add(fields_panel, BorderLayout.SOUTH);
		add(cnvs, BorderLayout.CENTER);
		setVisible(true);

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int w = cnvs.getWidth();
		double real_x = (x - cnvs.x0) / X_scale;
		button_1.setText(Double.toString(real_x));

	}

}
