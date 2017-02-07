package libs.helpers;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {
    private PropertiesConfiguration configs;

    public Config(String settingsFile) {
        try {
            this.configs = new PropertiesConfiguration(settingsFile);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return this.configs.getString(key, null);
    }

    public String get(String key, String defaultValue) {
        return this.configs.getString(key, defaultValue);
    }
}
