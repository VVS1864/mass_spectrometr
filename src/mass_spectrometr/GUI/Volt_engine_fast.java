package mass_spectrometr.GUI;

import mass_spectrometr.Run;

public class Volt_engine_fast extends Volt_engine{

	@Override
	int get_start_V() {
		return Run.prog.start_V_cyclic;
	}

	@Override
	int get_stop_V() {
		return Run.prog.stop_V_cyclic;
	}

	@Override
	float get_step_V() {
		return Run.prog.step_V_cyclic;
	}

	@Override
	int get_cycle_scan() {
		return 1;
	}

	@Override
	void set_start_V(int v) {
		Run.prog.start_V_cyclic = v;
	}

	@Override
	void set_stop_V(int v) {
		Run.prog.stop_V_cyclic = v;
		
	}

	@Override
	void set_step_V(float v) {
		Run.prog.step_V_cyclic = v;
		
	}


}
