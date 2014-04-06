package com.dslplatform.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

public class HttpClientTransport implements HttpTransport {
    private final Logger logger;

    private final org.apache.http.client.HttpClient httpClient;
    private final HttpAuthorization httpAuthorization;
    private final String remoteUrl;
    private static final String MIME_TYPE = "application/json";

    public HttpClientTransport(
            final Logger logger,
            final ProjectSettings project,
            final HttpAuthorization httpAuthorization) {
        remoteUrl = project.get("api-url");
        this.logger = logger;
        httpClient = new DefaultHttpClient();
        this.httpAuthorization = httpAuthorization;
    }

    @Override
    public HttpClient.Response transmit(
            final String service,
            final List<Map.Entry<String, String>> headers,
            final String method,
            final byte[] payload) throws IOException {
        final String url = remoteUrl + service;
        if (logger.isDebugEnabled()) {
            logger.debug("{} to URL: [{}]", method, url);
        }

        final HttpRequestBase req;
        if (method.equals("POST")) {
            final HttpPost post = new HttpPost(url);
            if (payload != null) {
                post.setEntity(new ByteArrayEntity(payload));
                if (logger.isTraceEnabled()) {
                    logger.trace("payload: [{}]", IOUtils.toString(post.getEntity().getContent()));
                }
            }
            req = post;
        } else if (method.equals("PUT")) {
            final HttpPut put = new HttpPut(url);
            if (payload != null) {
                put.setEntity(new ByteArrayEntity(payload));
                if (logger.isTraceEnabled()) {
                    logger.trace("payload: [{}]", IOUtils.toString(put.getEntity().getContent()));
                }
            }
            req = put;
        } else if (method.equals("DELETE")) {
            req = new HttpDelete(url);
        } else {
            req = new HttpGet(url);
        }

        req.setHeader("Accept", MIME_TYPE);
        req.setHeader("Content-Type", MIME_TYPE);

        for (final String authHeader : httpAuthorization.getAuthorizationHeaders()) {
            req.setHeader("Authorization", authHeader);
        }

        for (final Map.Entry<String, String> h : headers) {
            req.setHeader(h.getKey(), h.getValue());
        }

        if (logger.isTraceEnabled()) {
            for (final Header h : req.getAllHeaders()) {
                logger.trace("header:{}:{}", h.getName(), h.getValue());
            }
        }

        try {
            final HttpResponse response = httpClient.execute(req);

            final int code = response.getStatusLine().getStatusCode();
            final byte[] body = EntityUtils.toByteArray(response.getEntity());

            return new HttpClient.Response(code, body);
        } catch (final IOException e) {
            logger.error("{} to URL: [{}]", method, url);

            for (final Header h : req.getAllHeaders()) {
                logger.error("header:{}:{}", h.getName(), h.getValue());
            }

            if (req instanceof HttpEntityEnclosingRequest) {
                final HttpEntityEnclosingRequest heer = (HttpEntityEnclosingRequest) req;
                logger.error("payload:{}", EntityUtils.toString(heer.getEntity()));
            }
            throw e;
        } catch (final RuntimeException e) {
            logger.error("A runtime exception has occured while executing request", e);
            req.abort();
            throw e;
        }
    }
}
