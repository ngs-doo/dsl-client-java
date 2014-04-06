package com.dslplatform.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;

import com.dslplatform.patterns.ServiceLocator;
import com.fasterxml.jackson.databind.JavaType;

class HttpClient {
    static String encode(final String param) {
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    static class Response {
        public final int code;
        public final byte[] body;

        public Response(final int code, final byte[] body) {
            this.code = code;
            this.body = body;
        }

        public String bodyToString() {
            try {
                return body == null ? "" : new String(body, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

// -----------------------------------------------------------------------------

    private final Logger logger;
    private final JsonSerialization jsonDeserialization;
    private final ExecutorService executorService;
    private final HttpTransport httpTransport;
    private final String domainPrefix;
    private final int domainPrefixLength;

    public HttpClient(
            final ProjectSettings project,
            final ServiceLocator locator,
            final JsonSerialization jsonDeserialization,
            final Logger logger,
            final ExecutorService executorService,
            final HttpTransport httpTransport) {
        this.logger = logger;
        this.jsonDeserialization = jsonDeserialization;
        this.executorService = executorService;
        this.httpTransport = httpTransport;

        domainPrefix = project.get("package-name");
        domainPrefixLength = domainPrefix.length() + 1;
    }

//-----------------------------------------------------------------------------

    public String getDslName(final Class<?> clazz) {
        final String domainObjectName = clazz.getName();
        if (domainObjectName.startsWith(domainPrefix)) return domainObjectName.substring(domainPrefixLength);
        throw new RuntimeException(domainObjectName + " is not defined for package " + domainPrefix);
    }

    private static boolean contains(final int[] array, final int v) {
        for (final int e : array) {
            if (e == v) return true;
        }
        return false;
    }

    //-----------------------------------------------------------------------------

    private <TArgument> Response doRawRequest(
            final String service,
            final List<Map.Entry<String, String>> headers,
            final String method,
            final TArgument content,
            final int[] expected,
            final long start) throws IOException {

        final byte[] body;
        if (content == null) {
            body = null;

            if (logger.isDebugEnabled()) {
                logger.debug("Sending request [{}]: {}", method, service);
            }
        } else {
            final String jsonBody = JsonSerialization.serialize(content);
            body = jsonBody.getBytes("UTF-8");

            if (logger.isDebugEnabled()) {
                logger.debug("Sending request [{}]: {}, content size: {} bytes", method, service, jsonBody.length());
            }
        }

        final Response response = httpTransport.transmit(service, headers, method, body);

        if (logger.isDebugEnabled()) {
            final long time = System.currentTimeMillis() - start;
            logger.debug("Received response [{}, {} bytes] in {} ms", response.code, response.body.length, time);

            if (logger.isTraceEnabled()) {
                logger.trace("Received response body: {}", response.bodyToString());
            }
        }

        if (expected != null && !contains(expected, response.code))
            throw new IOException("Unexpected return code: " + response.code + ", response: " + response.bodyToString());

        if (expected == null && response.code >= 300) throw new IOException(response.bodyToString());

        return response;
    }

    private static final List<Map.Entry<String, String>> emptyHeaders =
            new java.util.ArrayList<Map.Entry<String, String>>(0);

    public <TArgument> Future<byte[]> sendRawRequest(
            final String service,
            final String method,
            final TArgument content,
            final List<Map.Entry<String, String>> headers,
            final int[] expected) {

        final long start = System.currentTimeMillis();

        return executorService.submit(new Callable<byte[]>() {
            @Override
            public byte[] call() throws IOException {
                return doRawRequest(service, headers, method, content, expected, start).body;
            }
        });
    }

    public <TArgument, TResult> Future<TResult> sendRequest(
            final JavaType type,
            final String service,
            final String method,
            final TArgument content,
            final int[] expected) {

        final long start = System.currentTimeMillis();

        return executorService.submit(new Callable<TResult>() {
            @SuppressWarnings("unchecked")
            @Override
            public TResult call() throws IOException {
                final Response response = doRawRequest(service, emptyHeaders, method, content, expected, start);
                return type != null
                        ? (TResult) jsonDeserialization.deserialize(type, response.bodyToString())
                        : null;
            }
        });
    }
}
