package io.github.s3s3l.yggdrasil.game.core.config;

import java.util.Properties;

public class Props extends Properties {

    public Props addProps(String key, String value) {
        this.put(key, value);
        return this;
    }
    
    public float getFloat(String key, float defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
                // Log warning if necessary
            }
        }
        return defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // Log warning if necessary
            }
        }
        return defaultValue;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }

    public String getString(String key, String defaultValue) {
        return getProperty(key, defaultValue);
    }

}
