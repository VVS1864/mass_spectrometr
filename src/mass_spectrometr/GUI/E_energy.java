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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mass_spectrometr.Run;
import mass_spectrometr.Save_graph;
import mass_spectrometr.GUI.panels.Panel_energy;
import mass_spectrometr.Run.graph_type;

public class E_energy extends JDialog {
	private boolean visible = false;
	public Panel_energy energy_panel;
	private JButton button_start;
	private JButton button_reset;
	private JButton button_save;
	private E_energy t;

	public E_energy(JFrame jframe) {
		super();
		t = this;
		setTitle("Electron Energy");
		setSize(1000, 500);
		setMinimumSize(new Dimension(640, 480));
		setLocationRelativeTo(jframe);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowevent) {
				switch_visible();
			}
		});

		ActionListener reset_action_listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(t, "Reset Energy graph?", "Mass Energy",
						JOptionPane.YES_NO_OPTION);

				if (n == 0)
					Run.prog.reset_energy();
			}
		};

		// ActionListener start/stop draw graph
		ActionListener start_action_listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Run.prog.start_e_scan) {
					
					if (!energy_panel.volt.start_scan()) return;
					
					button_start.setText("Stop scan");
					Run.prog.set_scan_delay();
					Run.prog.draw_graph_en_el = true;
				} else if (Run.prog.start_e_scan && !Run.prog.en_el_cycle_scan) {
					//boolean not_delay = energy_panel.volt.stop_scan();
					//if (not_delay) {
					button_start.setText("Start scan");
					energy_panel.volt.stop_scan();
					Run.prog.draw_graph_en_el = false;
					//}
				}
			}
		};
		
		ActionListener save_action_listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Save_graph a = new Save_graph(graph_type.ENERGY_GRAPH);
			}
		};

		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BoxLayout(top_panel, BoxLayout.X_AXIS));

		button_start = new JButton("Start scan");
		button_start.addActionListener(start_action_listener);
		top_panel.add(button_start);

		button_reset = new JButton("Reset");
		button_reset.addActionListener(reset_action_listener);
		top_panel.add(button_reset);
		
		button_save = new JButton("Save graph");
		button_save.addActionListener(save_action_listener);
		top_panel.add(button_save);

		energy_panel = new Panel_energy();
		add(top_panel, BorderLayout.NORTH);
		add(energy_panel, BorderLayout.CENTER);
		energy_panel.volt.button_start = button_start;
	}

	public boolean is_visible() {
		return visible;
	}

	public void switch_visible() {
		visible = (visible == true ? false : true);
		this.setVisible(visible);
	}
}
