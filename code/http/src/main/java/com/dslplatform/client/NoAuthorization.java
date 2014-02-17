package com.dslplatform.client;

import java.util.Arrays;
import java.util.List;

public class NoAuthorization implements HttpAuthorization {
    @Override
    public List<String> getAuthorizationHeaders() {
        return Arrays.asList();
    }
}
