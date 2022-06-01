package Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static final String CONFIGURATION_FILE = "/application.properties";
    private static final String SYSTEM_KEY = "config.file";
    private static final Properties properties;

    static {
        properties = new Properties();
        try (InputStream inputStream = Configuration.class.getResourceAsStream(System.getProperty(SYSTEM_KEY, CONFIGURATION_FILE))) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file" + System.getProperty(SYSTEM_KEY, CONFIGURATION_FILE));
        }
    }
    public static String getConfigurationValue(String key){
        return ((System.getProperty(key)
                == null) ? properties.getProperty(key)
                : System.getProperty(key));
    }
}
