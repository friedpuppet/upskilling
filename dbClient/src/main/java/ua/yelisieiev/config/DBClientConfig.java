package ua.yelisieiev.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Properties;

public class DBClientConfig {
    private final HashMap<ConfigParameterName, String> parameters = new HashMap<>();

    public String getParam(ConfigParameterName param) {
        return parameters.get(param);
    }

    public void load() {
        try {
            loadFromApplicationProperties();
            loadFromEnv();
            loadFromSystemProperties();
        } catch (FilePropertiesException e) {
            System.out.println("Couldn't load from application.properties - will try to get settings from other sources");
            e.printStackTrace();
        }
        checkParameters();
    }

    public void checkParameters() {
        if (parameters.size() < ConfigParameterName.values().length) {
            throw new RuntimeException("Not all of the settings were provided");
        }
    }


    void loadFromEnv() {
        for (ConfigParameterName paramName : ConfigParameterName.values()) {
            if (System.getenv().containsKey(paramName.toString())) {
                parameters.put(paramName, System.getenv(paramName.toString()));
            }
        }
    }


    void loadFromSystemProperties() {
        for (ConfigParameterName paramName : ConfigParameterName.values()) {
            if (System.getProperties().containsKey(paramName.toString())) {
                parameters.put(paramName, System.getProperty(paramName.toString()));
            }
        }
    }


    void loadFromApplicationProperties() throws FilePropertiesException {
        Properties properties = new Properties(ConfigParameterName.values().length);
        try (FileReader reader = new FileReader("application.properties")) {
            properties.load(reader);
            for (ConfigParameterName paramName : ConfigParameterName.values()) {
                if (properties.containsKey(paramName.toString())) {
                    parameters.put(paramName, properties.getProperty(paramName.toString()));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No properties file");
        } catch (Exception e) {
            throw new FilePropertiesException(e);
        }
    }

    public void setParam(ConfigParameterName name, String value) {
        parameters.put(name, value);
    }

    static class FilePropertiesException extends RuntimeException {
        public FilePropertiesException(Throwable cause) {
            super(cause);
        }
    }
}
