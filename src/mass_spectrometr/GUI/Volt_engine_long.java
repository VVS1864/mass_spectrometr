package mass_spectrometr.GUI;

import mass_spectrometr.Run;

public class Volt_engine_long extends Volt_engine{

	@Override
	double get_start_V() {
		return calc_float_from_int(Run.prog.start_V);
	}

	@Override
	double get_stop_V() {
		return calc_float_from_int(Run.prog.stop_V);
	}

	@Override
	double get_step_V() {
		return calc_float_step(Run.prog.step_V);
	}

	@Override
	boolean get_cycle_scan() {
		return false;
	}

	@Override
	void set_start_V(double v) {
		Run.prog.start_V = calc_int_from_float(v);
	}

	@Override
	void set_stop_V(double v) {
		Run.prog.stop_V = calc_int_from_float(v);
		
	}

	@Override
	void set_step_V(double v) {
		Run.prog.step_V = calc_int_step(v);
		
	}
	

	

}
