package mass_spectrometr.GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
import mass_spectrometr.GUI.graphs.mass_setter_panel.Mass_setter_panel;

public class Calibration_frame extends JDialog implements MouseMotionListener, MouseListener, ActionListener{
	private double X_scale;
	private double Y_scale;
	private double x0;
	public Graph_calibration cnvs;
	private Mass_setter_panel mass_1_panel;
	private Mass_setter_panel mass_2_panel;
	private Mass_setter_panel mass_3_panel;

	public Mass_setter_panel current_set;
	public boolean move_active = false;
	public boolean format_err = false;
	private JButton button_apply;

	public Calibration_frame(JFrame jframe, double X_scale, double Y_scale, int x0) {
		super(jframe, "Mass calibration");		
		// setSize(1000, 500);
		setMinimumSize(new Dimension(640, 480));
		setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
		setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
		this.X_scale = X_scale;
		this.Y_scale = Y_scale;
		this.x0 = x0;
		cnvs = new Graph_calibration("M", "int", Run.prog.analyser_mass,
				X_scale, Y_scale, x0);

		cnvs.addMouseMotionListener(this);
		cnvs.addMouseListener(this);

		JPanel fields_panel = new JPanel();
		fields_panel.setLayout(new BoxLayout(fields_panel, BoxLayout.X_AXIS));

		mass_1_panel = new Mass_setter_panel("Mass 1", this);
		mass_2_panel = new Mass_setter_panel("Mass 2", this);
		mass_3_panel = new Mass_setter_panel("Mass 3", this);
		JPanel apply_panel = new JPanel();

		button_apply = new JButton("Apply");
		button_apply.setAlignmentX(Component.CENTER_ALIGNMENT);
		button_apply.addActionListener(this);
		
		apply_panel.add(button_apply);

		fields_panel.add(mass_1_panel);
		fields_panel.add(mass_2_panel);
		fields_panel.add(mass_3_panel);
		fields_panel.add(apply_panel);

		add(fields_panel, BorderLayout.SOUTH);
		add(cnvs, BorderLayout.CENTER);
		setVisible(true);

	}
	
	public void set_enable(boolean enable) {
		button_apply.setEnabled(enable);
		if(current_set != mass_1_panel) {
			mass_1_panel.set_enable(enable);
		}
		if(current_set != mass_2_panel) {
			mass_2_panel.set_enable(enable);
		}
		if(current_set != mass_3_panel) {
			mass_3_panel.set_enable(enable);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (move_active) {
			int x = e.getX();
			double real_x = (x - cnvs.x0) / X_scale;
			
			
			int B = (int)Math.round(Math.sqrt((real_x - Run.prog.M0)/Run.prog.K) - Run.prog.B0);
			//System.out.println("B " + B);
			int B_exact = B;
			double max_value = 0;
			for(int i = 0; i<Run.prog.fixed_data_mass_intensity.length; i++) {
				if (Math.abs(B-i) < cnvs.snap && Run.prog.fixed_data_mass_intensity[i]>max_value) {
					
					max_value = Run.prog.fixed_data_mass_intensity[i];
					B_exact = i;
					
				}
			}
			cnvs.cursor_x = Run.prog.calc_mass(B_exact);
			cnvs.cursor_y = max_value;
			cnvs.draw_cursor = true;
			current_set.set_mass(Run.prog.calc_mass(B_exact));
			cnvs.repaint();
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		move_active = false;
		current_set = null;
		set_enable(true);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//Apply masses and real masses for calc coefficients
		if(mass_1_panel.is_used() && mass_2_panel.is_used() && mass_3_panel.is_used()) {
			format_err = false;
			double mass_1 = mass_1_panel.get_mass();
			double mass_2 = mass_2_panel.get_mass();
			double mass_3 = mass_3_panel.get_mass();
			
			double mass_1_real = mass_1_panel.get_real_mass();
			double mass_2_real = mass_2_panel.get_real_mass();
			double mass_3_real = mass_3_panel.get_real_mass();
			if (format_err) return;
			
			Run.prog.calc_coefficients(mass_1, mass_2, mass_3, mass_1_real, mass_2_real, mass_3_real);
			dispose();
		}
	}

}
