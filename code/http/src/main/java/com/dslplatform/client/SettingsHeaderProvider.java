package com.dslplatform.client;

import java.util.*;

public class SettingsHeaderProvider implements HttpHeaderProvider {

	private final List<Map.Entry<String, String>> headers = new ArrayList<Map.Entry<String, String>>();

	public SettingsHeaderProvider(ProjectSettings settings) {
		final String auth = settings.get("basic-auth");
		if (auth != null) {
			headers.add(new AbstractMap.SimpleEntry<String, String>("Authorization", "Basic " + auth));
		}
		else {
			final String username = settings.get("username");
			//TODO: remove this legacy
			final String password = settings.get("project-id");
			if (username != null && password != null) {
				final String authToken = Base64.encodeString(username + ':' + password, "UTF-8");
				headers.add(new AbstractMap.SimpleEntry<String, String>("Authorization", "Basic " + authToken));
			}
		}
	}

	@Override
	public List<Map.Entry<String, String>> getHeaders() {
		return headers;
	}
}
