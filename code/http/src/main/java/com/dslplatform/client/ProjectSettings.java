package com.dslplatform.client;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;

/**
 * dsl-project.properties key-&gt;value pairs
 */
public class ProjectSettings {
	private final Properties properties;

	private final Logger logger;

	/**
	 * Useless wrapper around project Properties (usually defined in to dsl-project.properties file)
	 *
	 * @param properties map containing project settings
	 * @throws IOException in case of error reading stream
	 */
	public ProjectSettings(final Logger logger, final Properties properties) throws IOException {
		this.logger = logger;
		this.properties = properties;
		if (logger.isDebugEnabled()) {
			for (final Map.Entry<Object, Object> prop : properties.entrySet()) {
				if (logger.isTraceEnabled()) {
					logger.trace("Setting [" + prop.getKey() + "] = " + prop.getValue());
				} else {
					logger.debug("Setting [" + prop.getKey() + "]");
				}
			}
		}
	}

	/**
	 * get value for provided property in dsl-project.properties
	 * property = value
	 *
	 * @param property key
	 * @return		 found value
	 */
	public String get(final String property) {
		logger.trace("Getting property [" + property + "]");
		return properties.getProperty(property);
	}
}