package com.dslplatform.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface HttpTransport {
    public HttpClient.Response transmit(
            final String service,
            final List<Map.Entry<String, String>> headers,
            final String method,
            final byte[] payload) throws IOException;
}
