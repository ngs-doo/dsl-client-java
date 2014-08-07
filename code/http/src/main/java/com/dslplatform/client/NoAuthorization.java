package com.dslplatform.client;

import java.util.Collections;
import java.util.List;

public enum NoAuthorization implements HttpAuthorization {
    INSTANCE;

    @Override
    public List<String> getAuthorizationHeaders() {
        return Collections.emptyList();
    }
}
