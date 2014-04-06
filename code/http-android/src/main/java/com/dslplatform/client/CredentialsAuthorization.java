package com.dslplatform.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import android.util.Base64;

public class CredentialsAuthorization implements HttpAuthorization {
    private final List<String> authorizationHeaders;

    public CredentialsAuthorization(final String username, final String password) throws IOException {
        final String authToken = Base64.encodeToString((username + ':' + password).getBytes("UTF-8"), Base64.NO_WRAP);
        authorizationHeaders = Arrays.asList("Basic " + authToken);
    }

    @Override
    public List<String> getAuthorizationHeaders() {
        return authorizationHeaders;
    }
}
