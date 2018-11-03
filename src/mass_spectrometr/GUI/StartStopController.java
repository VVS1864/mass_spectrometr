package mass_spectrometr.GUI;

import mass_spectrometr.Run;

public class StartStopController {
	public static Volt_engine volt_fast = Run.prog.user_interface.mass_panel.volt;
	public static Volt_engine volt_long = Run.prog.user_interface.e_energy_frame.energy_panel.volt;
	
	public static void set_enable_disable(boolean enable) {
		set_enabled(volt_fast, enable);
		set_enabled(volt_long, enable);
	}
	public static void set_sliders(int value) {
		volt_fast.slider.setValue(value);
		volt_long.slider.setValue(value);
		//Run.prog.user_interface.repaint_cnvs();
	}
	
	public static void set_enabled(Volt_engine volt, boolean enable) {
		volt.slider.setEnabled(enable);
		volt.dac_voltage_textbox.setEnabled(enable);
		volt.start_textbox.setEnabled(enable);
		volt.stop_textbox.setEnabled(enable);
		volt.speed_textbox.setEnabled(enable);
		volt.button_update.setEnabled(enable);
		if(enable) volt.button_start.setText("Start scan");
		else volt.button_start.setText("Stop scan");
		
	}
}
