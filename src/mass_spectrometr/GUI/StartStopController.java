package mass_spectrometr.GUI;

import mass_spectrometr.Run;

public class StartStopController {
	private static Volt_engine volt_fast = Run.prog.user_interface.mass_panel.volt;
	private static Volt_engine volt_long = Run.prog.user_interface.e_energy_frame.energy_panel.volt;
	private static GUI user_interface = Run.prog.user_interface;
	
	public static void set_enable_disable(boolean enable) {
		set_enabled(volt_fast, enable);
		set_enabled(volt_long, enable);
	}
	public static void set_sliders(int value) {
		volt_fast.slider.setValue(value);
		volt_long.slider.setValue(value);
	}
	
	public static void set_enabled(Volt_engine volt, boolean enable) {
		volt.slider.setEnabled(enable);
		volt.dac_voltage_textbox.setEnabled(enable);
		volt.start_textbox.setEnabled(enable);
		volt.stop_textbox.setEnabled(enable);
		volt.speed_textbox.setEnabled(enable);
		volt.button_update.setEnabled(enable);
		
	}
	
	public static void set_enabled_fast_scan(boolean enable) {
		volt_fast.button_start.setEnabled(enable);
	}
	
	public static void set_enabled_long_scan(boolean enable) {
		volt_long.button_start.setEnabled(enable);
	}
	
	public static void set_enabled_calibration(boolean enable) {
		user_interface.button_calibration.setEnabled(enable);
	}
}
