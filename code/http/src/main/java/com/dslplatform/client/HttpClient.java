package com.dslplatform.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.net.ssl.*;

import org.slf4j.Logger;

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
    private final String domainPrefix;
    private final int domainPrefixLength;
    private final List<String> authorizationHeaders;
    private final String[] remoteUrls;
    private final SSLSocketFactory SSL_SOCKET_FACTORY;
    private static final String MIME_TYPE = "application/json";

    public HttpClient(
            final ProjectSettings project,
            final JsonSerialization jsonDeserialization,
            final Logger logger,
            final ExecutorService executorService) {
        this.logger = logger;
        this.jsonDeserialization = jsonDeserialization;
        this.executorService = executorService;
        this.remoteUrls = project.get("api-url").split(",\\s+");

        SSL_SOCKET_FACTORY = createSSLSocketFactory(project);

        domainPrefix = project.get("package-name");
        domainPrefixLength = domainPrefix.length() + 1;

        final String username = project.get("username");
        final String password = project.get("project-id");
        if (username != null && password != null) {
            final String authToken = Base64.encodeString(username + ':' + password, "UTF-8");
            authorizationHeaders = Arrays.asList("Basic " + authToken);
        } else {
            authorizationHeaders = Collections.emptyList();
        }
    }

    // -----------------------------------------------------------------------------

    private SSLSocketFactory createSSLSocketFactory(
            final ProjectSettings project) {
        final String trustStore = project.get("trustStore");
        final String trustStorePassword = project.get("trustStorePassword");
        if (trustStore != null && trustStorePassword != null)
            return createSSLSocketFactory(trustStore, trustStorePassword);
        else {
            final String trustStoreEnv = System.getenv("trustStore");
            final String trustStorePasswordEnv = System.getenv("trustStorePassword");
            if (trustStoreEnv != null && trustStorePasswordEnv != null)
                return createSSLSocketFactory(trustStoreEnv, trustStorePasswordEnv);
            else
                return createSSLSocketFactory("common-cas.jks", "common-cas");
        }
    }

    private SSLSocketFactory createSSLSocketFactory(
            String trustStore,
            String trustStorePassword) {

        final String storeType = KeyStore.getDefaultType();

        try {
            if (storeType.equals("jks")) {
                final SSLContext sslContext = SSLContext.getInstance("TLS");
                final KeyStore keystore = KeyStore.getInstance(storeType);
                keystore.load(HttpClient.class.getResourceAsStream(trustStore), trustStorePassword.toCharArray());
                final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(keystore);
                final TrustManager[] tms = tmf.getTrustManagers();
                sslContext.init(null, tms, null);
                return sslContext.getSocketFactory();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

        final Response response = transmit(service, headers, method, body);

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

    private Response transmit(
            final String service,
            final List<Map.Entry<String, String>> headers,
            final String method,
            final byte[] payload
    ) throws IOException {
        final StringBuilder errorResponseBuilder = new StringBuilder();
        for (String remoteUrl : remoteUrls) {
            URL url = new URL(remoteUrl + service);
            try {
                final HttpClient.Response response = transmit(url, headers, method, payload);
                if (response.code < 500) return response;
                logger.error("At {} [{}] {}", remoteUrl, response.code, response.bodyToString());
                errorResponseBuilder.append("Error connecting to ").append(remoteUrl).append(response.bodyToString()).append("\n");
            } catch (java.net.ConnectException ce) {
                logger.error("At {} {}", remoteUrl, ce.getMessage());
                errorResponseBuilder.append("Error connecting to ").append(remoteUrl).append(ce.getMessage()).append("\n");
            }
        }
        throw new RuntimeException(errorResponseBuilder.toString());
    }

    private Response transmit(
            final URL url,
            final List<Map.Entry<String, String>> headers,
            final String method,
            final byte[] payload) throws IOException {

        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        if (conn instanceof HttpsURLConnection && SSL_SOCKET_FACTORY != null)
            ((HttpsURLConnection) conn).setSSLSocketFactory(SSL_SOCKET_FACTORY);

        conn.setRequestMethod(method);

        conn.setRequestProperty("Accept", MIME_TYPE);
        conn.setRequestProperty("Content-Type", MIME_TYPE);

        for (final Map.Entry<String, String> h : headers) {
            conn.setRequestProperty(h.getKey(), h.getValue());
        }

        logger.debug("{} {}", method, url.toString());
        for (final String authHeader : authorizationHeaders) {
            conn.setRequestProperty("Authorization", authHeader);
            logger.trace("Authorization: {}", authHeader);
        }

        if (logger.isTraceEnabled()) {
            for (final Map.Entry<String, List<String>> header : conn.getRequestProperties().entrySet()) {
                final StringBuilder stringBuilder = new StringBuilder();
                final Iterator<String> iterator = header.getValue().iterator();
                stringBuilder.append(iterator.next());
                while (iterator.hasNext()) {
                    String headerValue = iterator.next();
                    stringBuilder.append(", ").append(headerValue);
                }
                logger.trace("{}: {}", header.getKey(), header.getValue());
            }
        }

        if (payload != null) {
            conn.setDoOutput(true);
            if (logger.isTraceEnabled()) logger.trace("Adding payload: {}", new String(payload, "UTF-8"));
            conn.setRequestProperty("Content-Length", Integer.toString(payload.length));
            conn.getOutputStream().write(payload);
            conn.getOutputStream().close();
        }

        try {
            final int responseCode = conn.getResponseCode();
            final byte[] responseBody =
                    (responseCode < 400) ?
                            Utils.inputStreamToByteArray(conn.getInputStream()) :
                            Utils.inputStreamToByteArray(conn.getErrorStream());
            return new Response(responseCode, responseBody);
        } catch (IOException e) {
            logger.error(e.getMessage());
            final byte[] bytes = Utils.inputStreamToByteArray(conn.getErrorStream());
            if (bytes != null && logger.isDebugEnabled()) logger.debug(new String(bytes, "UTF-8"));
            throw e;
        }
    }

}
