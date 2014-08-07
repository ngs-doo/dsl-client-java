package com.dslplatform.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.util.Base64;

public class ProjectSettingsAuthorization implements HttpAuthorization {
    private final List<String> authorizationHeaders;

    public ProjectSettingsAuthorization(final ProjectSettings projectSettings) throws IOException {
        final String username = projectSettings.get("username");
        final String password = projectSettings.get("project-id");
        if (username != null && password != null) {
            final String authToken = Base64.encodeToString((username + ':' + password).getBytes("UTF-8"), Base64.NO_WRAP);
            authorizationHeaders = Arrays.asList("Basic " + authToken);
        }
        else {
            authorizationHeaders = Collections.emptyList();
        }
    }

    @Override
    public List<String> getAuthorizationHeaders() {
        return authorizationHeaders;
    }
}
