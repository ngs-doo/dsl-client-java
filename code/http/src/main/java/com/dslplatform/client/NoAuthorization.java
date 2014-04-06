package com.dslplatform.client;

import java.util.Collections;
import java.util.List;

public class NoAuthorization implements HttpAuthorization {
    @Override
    public List<String> getAuthorizationHeaders() {
        return Collections.emptyList();
    }
}
