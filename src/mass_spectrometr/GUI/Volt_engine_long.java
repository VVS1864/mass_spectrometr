package mass_spectrometr.GUI;

import mass_spectrometr.Run;

public class Volt_engine_long extends Volt_engine{

	@Override
	float get_start_V() {
		return calc_float_from_int(Run.prog.start_V);
	}

	@Override
	float get_stop_V() {
		return calc_float_from_int(Run.prog.stop_V);
	}

	@Override
	float get_step_V() {
		return Run.prog.step_V;
	}

	@Override
	int get_cycle_scan() {
		return 0;
	}

	@Override
	void set_start_V(float v) {
		Run.prog.start_V = calc_int_from_float(v);
	}

	@Override
	void set_stop_V(float v) {
		Run.prog.stop_V = calc_int_from_float(v);
		
	}

	@Override
	void set_step_V(float v) {
		Run.prog.step_V = calc_step(v);
		
	}
	

	

}
