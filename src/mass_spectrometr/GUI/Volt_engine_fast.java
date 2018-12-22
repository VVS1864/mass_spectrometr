package mass_spectrometr.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import mass_spectrometr.Run;

public class Volt_engine_fast extends Volt_engine {

	public Volt_engine_fast() {
		ActionListener start = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Run.prog.start_e_scan) {
					Run.prog.en_el_cycle_scan = true;
					start_scan();
				}
				else if (Run.prog.start_e_scan && Run.prog.en_el_cycle_scan) {
					stop_scan();
				}
			}
		};
		button_start = new JButton("Start fast scan");
		button_start.addActionListener(start);
		update_panel.add(button_start);
	}

	@Override
	double get_start_V() {
		return Run.prog.d_start_V_cyclic;
	}

	@Override
	double get_stop_V() {
		return Run.prog.d_stop_V_cyclic;
	}

	@Override
	double get_step_V() {
		return Run.prog.d_step_V_cyclic;
	}

	@Override
	boolean get_cycle_scan() {
		return true;
	}

	@Override
	void set_start_V(double v) {
		Run.prog.start_V_cyclic = calc_int_from_float(v);
		Run.prog.d_start_V_cyclic = v;
	}

	@Override
	void set_stop_V(double v) {
		Run.prog.stop_V_cyclic = calc_int_from_float(v);
		Run.prog.d_stop_V_cyclic = v;

	}

	@Override
	void set_step_V(double v) {
		Run.prog.step_V_cyclic = calc_int_step(v);
		Run.prog.d_step_V_cyclic = v;

	}

}
