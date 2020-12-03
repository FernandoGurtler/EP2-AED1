package main.java.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {
    private static PropertyUtils propertyUtils;
    Properties properties;

    public static synchronized PropertyUtils getInstance() {
        if (propertyUtils == null) {
            propertyUtils = new PropertyUtils();
        }
        return propertyUtils;
    }

    private PropertyUtils() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try {
            properties.load(ClassLoader.getSystemClassLoader().getResource("main/resources/application.properties").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProperties(String key, String value) {
        properties.setProperty(key, value);
    }

    public String getProperty(String propertyName) {
        if (System.getProperty("os.name").toLowerCase().contains("win"))
            return properties.getProperty(propertyName).replaceFirst("C:/", "");
        return properties.getProperty(propertyName);
    }

    public String filePath(String nameKey) {
        return getClass().getClassLoader().getResource(nameKey).getPath();
    }

    public Properties getProperties() {
        return properties;
    }
}
