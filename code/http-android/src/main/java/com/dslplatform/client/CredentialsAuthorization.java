package com.dslplatform.client;

import android.util.Base64;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CredentialsAuthorization implements HttpAuthorization {
    private final List<String> authorizationHeaders;

    public CredentialsAuthorization(final String username, final String password)
            throws IOException {
        final String authToken =
                Base64.encodeToString(
                        (username + ':' + password).getBytes("ISO-8859-1"),
                        Base64.NO_WRAP);
        authorizationHeaders = Arrays.asList("Basic " + authToken);
    }

    @Override
    public List<String> getAuthorizationHeaders() {
        return authorizationHeaders;
    }
}
