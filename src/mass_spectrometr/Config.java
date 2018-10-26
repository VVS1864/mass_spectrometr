package mass_spectrometr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.Properties;

public class Config {
	String config_path;
	private Properties config = new Properties();

	public Config() {		
		load_config("config.txt");
	}
	/**
	 * store config to file in directory of .jar file
	 */
	public void store_config() {
		File jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
		File conf = new File(jarDir, "config.txt");
		try {
			FileOutputStream stream = new FileOutputStream(conf);
			config.store(stream, null);
			System.out.println("Settings are written to config file " + conf.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Error store config! Config file '" + config_path + "' not found.");
		}
	}
	public void set_conf_value(String value_name, String new_value) {
		if(!config.containsKey(value_name)) {
			System.err.println("Set value error: config.txt has not setting '" + value_name +"'");
			System.err.println("Set new setting field: '" + value_name +"'");
		}
		config.setProperty(value_name, new_value);
	}
	
	public String get_conf_value(String value_name) {
		if(config.containsKey(value_name)) {
			return config.getProperty(value_name);
		}
		else {
			System.err.println("Get value error: config.txt has not setting '" + value_name +"'");
			return null;
		}
	}
	
	private void load_config(String config_name){
		//First try load from config in jar directory
		boolean read_from_fir = false;
		File jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
		File conf = null;
		
		try {
			conf = new File(jarDir, "config.txt");
			InputStream stream = new FileInputStream(conf);
			config.load(stream);
			read_from_fir = true;
			System.out.println("Config loaded from directory " + jarDir.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Error load config! Config file in " + jarDir.getAbsolutePath() + " not found.");
		}
		
		//If config not exist - load default config from jar
		if (read_from_fir == false){
			try {
				InputStreamReader stream = new InputStreamReader(getClass().getResourceAsStream("config.txt"));
				config.load(stream);
				System.out.println("Config loaded from jar by default");
			} catch (IOException e) {
				System.err.println("Error reading default config file!");
			}
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
