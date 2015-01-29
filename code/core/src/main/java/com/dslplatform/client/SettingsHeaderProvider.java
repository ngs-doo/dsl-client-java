package com.dslplatform.client;

import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SettingsHeaderProvider implements HttpHeaderProvider {

	private final List<Map.Entry<String, String>> headers = new ArrayList<Map.Entry<String, String>>();

	public SettingsHeaderProvider(final Properties properties) {
		final String basicAuth = properties.getProperty("basic-auth");
		final String hashAuth = properties.getProperty("hash-auth");
		final String authorization = properties.getProperty("authorization");
		if (basicAuth != null) {
			headers.add(new AbstractMap.SimpleEntry<String, String>("Authorization", "Basic " + basicAuth));
		} else if (hashAuth != null) {
			headers.add(new AbstractMap.SimpleEntry<String, String>("Authorization", "Hash " + hashAuth));
		} else if (authorization != null) {
			headers.add(new AbstractMap.SimpleEntry<String, String>("Authorization", authorization));
		} else {
			final String username = properties.getProperty("username");
			//TODO: remove this legacy
			final String password = properties.getProperty("project-id");
			if (username != null && password != null) {
				final String authToken = Utils.base64Encode((username + ':' + password).getBytes(Charset.forName("UTF-8")));
				headers.add(new AbstractMap.SimpleEntry<String, String>("Authorization", "Basic " + authToken));
			}
		}
	}

	@Override
	public List<Map.Entry<String, String>> getHeaders() {
		return headers;
	}
}
