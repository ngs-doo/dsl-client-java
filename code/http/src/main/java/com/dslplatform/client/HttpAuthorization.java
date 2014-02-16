package com.dslplatform.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public interface HttpAuthorization {
    public java.util.List<String> getAuthorizationHeaders();

    public static class NoAuthorization implements HttpAuthorization {
        @Override
        public List<String> getAuthorizationHeaders() {
            return Arrays.asList();
        }
    }

    public static class CredentialsAuthorization implements HttpAuthorization {
        private final List<String> authorizationHeaders;

        public CredentialsAuthorization(
                final String username,
                final String password) throws IOException {
            final String authToken = Base64
                    .encodeBase64String((username + ':' + password)
                            .getBytes("ISO-8859-1"));
            authorizationHeaders = Arrays.asList("Basic " + authToken);
        }

        @Override
        public List<String> getAuthorizationHeaders() {
            return authorizationHeaders;
        }
    }

    public static class ProjectAuthorization extends CredentialsAuthorization {
        public ProjectAuthorization(
                final ProjectSettings projectSettings) throws IOException {
            super(projectSettings.get("username"), projectSettings
                    .get("project-id"));
        }
    }
}
