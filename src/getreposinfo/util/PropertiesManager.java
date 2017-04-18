package getreposinfo.util;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesManager {

	private static Properties properties;
	private static String path = "general.test.config";

	public static String getProperty(String propertie) {
		try {
			if (properties == null) {
				properties = new Properties();
				FileInputStream file = new FileInputStream(path);
				properties.load(file);
			}
		} catch (Exception e) {
			System.out.println("error to get properties file");
		}

		return properties.getProperty(propertie);
	}

	public static void setNewPath(String newpath) {
		path = newpath;
		properties = null;
	}
}
