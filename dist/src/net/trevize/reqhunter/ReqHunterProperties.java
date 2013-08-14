package net.trevize.reqhunter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ReqHunterProperties {

	public static final String PROPERTIES_FILEPATH = "./config/ReqHunter.properties";
	public static Properties properties;
	private static final String PROPERTIES_COMMENTS = " This is the properties file of ReqHunter, a requirements management software\n# Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]";

	public static final String PROPERTY_REQUIREMENTS_FILE_PATH = "PROPERTY_REQUIREMENTS_FILE_PATH";
	public static final String PROPERTY_REQUIREMENT_FACTORY_IMPLEMENTATION = "PROPERTY_REQUIREMENT_FACTORY_IMPLEMENTATION";
	public static final String PROPERTY_MAIN_CSS_PATH = "PROPERTY_MAIN_CSS_PATH";
	public static final String PROPERTY_DOCKING_WINDOWS_THEME = "PROPERTY_DOCKING_WINDOWS_THEME";

	private static String requirements_file_path;
	private static String requirement_factory_implementation;
	private static String main_css_path;
	private static String docking_windows_theme;

	public static void loadProperties() {
		properties = new Properties();
		try {
			properties.load(new FileReader(PROPERTIES_FILEPATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		requirements_file_path = properties
				.getProperty(PROPERTY_REQUIREMENTS_FILE_PATH);
		requirement_factory_implementation = properties
				.getProperty(PROPERTY_REQUIREMENT_FACTORY_IMPLEMENTATION);
		main_css_path = properties.getProperty(PROPERTY_MAIN_CSS_PATH);
		docking_windows_theme = properties
				.getProperty(PROPERTY_DOCKING_WINDOWS_THEME);
	}

	public static String getRequirements_file_path() {
		if (properties == null) {
			loadProperties();
		}
		return requirements_file_path;
	}

	public static String getRequirement_factory_implementation() {
		if (properties == null) {
			loadProperties();
		}
		return requirement_factory_implementation;
	}

	public static String getMain_css_path() {
		if (properties == null) {
			loadProperties();
		}
		return main_css_path;
	}

	public static String getDocking_windows_theme() {
		if (properties == null) {
			loadProperties();
		}
		return docking_windows_theme;
	}

}
