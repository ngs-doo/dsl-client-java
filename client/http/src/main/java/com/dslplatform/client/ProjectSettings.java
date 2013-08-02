package com.dslplatform.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Project.ini key->value pairs  
 */
public class ProjectSettings {
    private final Properties properties;

    /**
     * Stream to project.ini file
     * 
     * @param iniStream    project.ini stream
     * @throws IOException in case of error reading stream
     */
    public ProjectSettings(final InputStream iniStream) throws IOException {
        properties = new Properties();
        properties.load(iniStream);
    }

    /**
     * get value for provided property in project.ini
     * property = value
     * 
     * @param property key
     * @return         found value
     */
    public String get(final String property) {
        return properties.getProperty(property);
    }
}
