package org.example.config;

import java.util.Properties;

public class AppConfig {
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}