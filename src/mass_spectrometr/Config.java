package mass_spectrometr;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
	String config_path;
	private Properties config = new Properties();

	public Config() {		
		load_config("config.txt");
	}
	public void store_config() {
		try {
			FileOutputStream fos = new FileOutputStream(config_path);
			config.store(fos, null);

		} catch (IOException e) {
			System.err.println("Error! Config file '" + config_path + "' not found.");
		}
	}
	public void set_conf_value(String value_name, String new_value) {
		if(config.containsKey(value_name)) {
			config.setProperty(value_name, new_value);
		}
	}
	
	public String get_conf_value(String value_name) {
		if(config.containsKey(value_name)) {
			return config.getProperty(value_name);
		}
		else {
			System.err.println("config.txt has not setting '" + value_name +"'");
			return null;
		}
	}
	
	private void load_config(String config_name){
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		config_path = rootPath + "/mass_spectrometr/config.txt";
		
		try {
			FileInputStream fis = new FileInputStream(config_path);
			config.load(fis);

		} catch (IOException e) {
			System.err.println("Error! Config file '" + config_path + "' not found.");
		}
	}

	public static void main(String[] args) {
		Config r = new Config();
		System.out.println(r.get_conf_value("M0"));
		System.out.println(r.get_conf_value("B0"));
		System.out.println(r.get_conf_value("K"));
		r.set_conf_value("M0", "456");
		System.out.println(r.get_conf_value("M0"));
		r.store_config();
		r = new Config();
		System.out.println(r.get_conf_value("M0"));
	}
}
