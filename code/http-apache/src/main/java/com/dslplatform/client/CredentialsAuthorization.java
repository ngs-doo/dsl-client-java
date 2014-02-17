package com.dslplatform.client;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CredentialsAuthorization implements HttpAuthorization {
    private final List<String> authorizationHeaders;

    public CredentialsAuthorization(final String username, final String password)
            throws IOException {
        final String authToken =
                Base64.encodeBase64String((username + ':' + password)
                        .getBytes("ISO-8859-1"));
        authorizationHeaders = Arrays.asList("Basic " + authToken);
    }

    @Override
    public List<String> getAuthorizationHeaders() {
        return authorizationHeaders;
    }
}
