package com.dslplatform.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;

/**
 * Project.ini key->value pairs
 */
public class ProjectSettings {
    private final Properties properties;

    private final Logger logger;

    /**
     * Stream to project.ini file
     *
     * @param iniStream    project.ini stream
     * @throws IOException in case of error reading stream
     */
    public ProjectSettings(
            final Logger logger,
            final InputStream iniStream) throws IOException {
        this.logger = logger;
        properties = new Properties();
        properties.load(iniStream);
        if (logger.isDebugEnabled()) {
            for (final Map.Entry<Object, Object> prop : properties
                    .entrySet()) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Setting [" + prop.getKey() + "] = "
                            + prop.getValue());
                } else {
                    logger.debug("Setting [" + prop.getKey() + "]");
                }
            }
        }
    }

    /**
     * get value for provided property in project.ini
     * property = value
     *
     * @param property key
     * @return         found value
     */
    public String get(final String property) {
        logger.trace("Getting property [" + property + "]");
        return properties.getProperty(property);
    }
}
