package com.dslplatform.client;

import java.util.concurrent.Future;

class HttpApplicationProxy implements ApplicationProxy {
    private final static String APPLICATION_URI = "RestApplication.svc/";

    private final HttpClient client;

    public HttpApplicationProxy(final HttpClient client) {
        this.client = client;
    }

    @Override
    public <TResult> Future<TResult> get(
            final Class<TResult> manifest,
            final String command,
            final int[] expectedStatus) {
        return
            client.sendRequest(
                JsonSerialization.buildType(manifest),
                APPLICATION_URI + command,
                "GET",
                null,
                expectedStatus);
    }

    @Override
    public <TArgument, TResult> Future<TResult> post(
            final Class<TResult> manifest,
            final String command,
            final TArgument argument,
            final int[] expectedStatus) {
        return
            client.sendRequest(
                JsonSerialization.buildType(manifest),
                APPLICATION_URI + command,
                "POST",
                argument,
                expectedStatus);
    }
}
