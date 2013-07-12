package com.dslplatform.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//TODO this should not be public. it's public only for S3
public class ProjectSettings {
    private final Properties properties;

    public ProjectSettings(final InputStream iniStream) throws IOException {
        properties = new Properties();
        properties.load(iniStream);
    }

    public String get(final String property) {
        return properties.getProperty(property);
    }
}
