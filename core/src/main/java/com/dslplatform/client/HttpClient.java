package com.dslplatform.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.dslplatform.patterns.Bytes;
import org.slf4j.Logger;

import com.dslplatform.client.exceptions.HttpException;
import com.dslplatform.client.exceptions.HttpSecurityException;
import com.dslplatform.client.exceptions.HttpServerErrorException;
import com.dslplatform.client.exceptions.HttpUnexpectedCodeException;

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
		public final int size;
		public final HttpURLConnection connection;

		public Response(final HttpURLConnection connection, final byte[] buffer) throws IOException {
			this.code = connection.getResponseCode();
			int size = connection.getContentLength();
			if (code < 300 && size >= 0) {
				byte[] bytes = buffer;
				if (bytes.length < size) {
					bytes = Arrays.copyOf(bytes, size);
				}
				int pos = 0;
				final InputStream stream = connection.getInputStream();
				while (pos < size) {
					pos += stream.read(bytes, pos, size - pos);
				}
				body = bytes;
			} else {
				body = code < 400
						? inputStreamToByteArray(connection.getInputStream(), buffer)
						: inputStreamToByteArray(connection.getErrorStream(), buffer);
				if (body != null) {
					size = body.length;
				}
			}
			this.size = size;
			this.connection = connection;
		}

		private static byte[] inputStreamToByteArray(final InputStream stream, final byte[] buffer) throws IOException {
			if (stream == null) return null;
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			int len;
			while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
				os.write(buffer, 0, len);
			}

			return os.toByteArray();
		}


		public String bodyToString() {
			try {
				return body == null ? "" : new String(body, 0, size, "UTF-8");
			} catch (final UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private final Logger logger;
	private final JsonSerialization json;
	private final HttpHeaderProvider headerProvider;
	private final ExecutorService executorService;
	private final String domainPrefix;
	private final int domainPrefixLength;
	private final String[] remoteUrls;
	private final SSLSocketFactory SSL_SOCKET_FACTORY;
	private static final String MIME_TYPE = "application/json";
	private int currentUrl;
	private final BlockingDeque<byte[]> resultBuffers;

	public HttpClient(
			final Properties properties,
			final JsonSerialization json,
			final Logger logger,
			final HttpHeaderProvider headerProvider,
			final ExecutorService executorService) {
		this.logger = logger;
		this.json = json;
		this.executorService = executorService;
		this.remoteUrls = properties.getProperty("api-url").split(",\\s+");

		final int queueSize = Runtime.getRuntime().availableProcessors() * 2;
		resultBuffers = new LinkedBlockingDeque<byte[]>(queueSize);
		for (int i = 0; i < queueSize; i++) {
			resultBuffers.addFirst(new byte[1024]);
		}

		SSL_SOCKET_FACTORY = createSSLSocketFactory(properties);

		domainPrefix = properties.getProperty("package-name");
		if (domainPrefix == null) {
			throw new IllegalArgumentException("package-name is missing from provided configuration. It is used to specify root namespace");
		}
		domainPrefixLength = domainPrefix.length() + 1;

		this.headerProvider = headerProvider;
	}

	private SSLSocketFactory createSSLSocketFactory(
			final Properties properties) {
		final String trustStore = properties.getProperty("trustStore");
		final String trustStorePassword = properties.getProperty("trustStorePassword");
		if (trustStore != null && trustStorePassword != null)
			return createSSLSocketFactory(trustStore, trustStorePassword);
		else {
			final String trustStoreEnv = System.getenv("trustStore");
			final String trustStorePasswordEnv = System.getenv("trustStorePassword");
			if (trustStoreEnv != null && trustStorePasswordEnv != null)
				return createSSLSocketFactory(trustStoreEnv, trustStorePasswordEnv);
			else
				return null;
		}
	}

	private SSLSocketFactory createSSLSocketFactory(
			final String trustStore,
			final String trustStorePassword) {

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
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

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

	private <TArgument> Response doRawRequest(
			final String service,
			final List<Map.Entry<String, String>> headers,
			final String method,
			final TArgument content,
			final int[] expected,
			final byte[] buffer,
			final long start) throws IOException, InterruptedException {

		final Response response;
		final Bytes payload;
		if (content == null) {
			payload = null;
			logger.debug("Sending request [{}]: {}", method, service);
		} else {
			payload = json.serialize(content);

			logger.debug("Sending request [{}]: {}, content size: {} bytes", method, service, payload.length);
		}
		response = transmit(service, headers, method, payload, buffer, 2);

		if (logger.isDebugEnabled()) {
			final long time = System.currentTimeMillis() - start;
			logger.debug("Received response [{}, {} bytes] in {} ms", response.code, response.size, time);

			if (logger.isTraceEnabled()) {
				logger.trace("Received response body: {}", response.bodyToString());
			}
		}

		if (response.code == HttpURLConnection.HTTP_FORBIDDEN || response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
			throw new HttpSecurityException(response.bodyToString(), response.code, response.connection.getHeaderFields());
		} else if (response.code >= HttpURLConnection.HTTP_INTERNAL_ERROR) {
			throw new HttpServerErrorException(response.bodyToString(), response.code, response.connection.getHeaderFields());
		} else if (expected != null && !contains(expected, response.code)) {
			throw new HttpUnexpectedCodeException(response.bodyToString(), expected, response.code, response.connection.getHeaderFields());
		} else if (expected == null && response.code >= HttpURLConnection.HTTP_BAD_REQUEST) {
			throw new HttpException(response.bodyToString(), response.code, response.connection.getHeaderFields());
		}

		return response;
	}

	private static final List<Map.Entry<String, String>> emptyHeaders = new java.util.ArrayList<Map.Entry<String, String>>(0);

	public <TArgument> Future<byte[]> sendRawRequest(
			final String service,
			final String method,
			final TArgument content,
			final List<Map.Entry<String, String>> headers,
			final int[] expected) {

		final long start = System.currentTimeMillis();

		return executorService.submit(new Callable<byte[]>() {
			@Override
			public byte[] call() throws IOException, InterruptedException {
				byte[] buffer = resultBuffers.takeFirst();
				try {
					final Response response = doRawRequest(service, headers, method, content, expected, buffer, start);
					if (response.code < 300) {
						final byte[] result = Arrays.copyOf(response.body, response.size);
						buffer = response.body;
						return result;
					} else {
						return response.body;
					}
				} finally {
					resultBuffers.putFirst(buffer);
				}
			}
		});
	}

	public <TArgument, TResult> Future<TResult> sendRequest(
			final Class<TResult> manifest,
			final String service,
			final String method,
			final TArgument content,
			final int[] expected) {

		final long start = System.currentTimeMillis();

		return executorService.submit(new Callable<TResult>() {
			@SuppressWarnings("unchecked")
			@Override
			public TResult call() throws IOException, InterruptedException {
				byte[] buffer = resultBuffers.takeFirst();
				try {
					final Response response = doRawRequest(service, emptyHeaders, method, content, expected, buffer, start);
					buffer = response.body;
					return json.deserialize(manifest, response.body, response.size);
				} finally {
					resultBuffers.putFirst(buffer);
				}
			}
		});
	}

	public <TArgument, TResult> Future<List<TResult>> sendCollectionRequest(
			final Class<TResult> manifest,
			final String service,
			final String method,
			final TArgument content,
			final int[] expected) {

		final long start = System.currentTimeMillis();

		return executorService.submit(new Callable<List<TResult>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<TResult> call() throws IOException, InterruptedException {
				byte[] buffer = resultBuffers.takeFirst();
				try {
					final Response response = doRawRequest(service, emptyHeaders, method, content, expected, buffer, start);
					buffer = response.body;
					return json.deserializeList(manifest, response.body, response.size);
				} finally {
					resultBuffers.putFirst(buffer);
				}
			}
		});
	}

	private Response transmit(
			final String service,
			final List<Map.Entry<String, String>> headers,
			final String method,
			final Bytes payload,
			final byte[] buffer,
			final int retriesOnConflictOrConnectionError) throws IOException {
		IOException error = null;
		final int maxLen = remoteUrls.length + currentUrl;
		for (int i = currentUrl; i < maxLen; i++) {
			try {
				final URL url = new URL(remoteUrls[i % remoteUrls.length] + service);
				final HttpClient.Response response = transmit(url, headers, method, payload, buffer);
				if (response.code == HttpURLConnection.HTTP_CONFLICT && retriesOnConflictOrConnectionError > 0) {
					return transmit(service, headers, method, payload, buffer, retriesOnConflictOrConnectionError - 1);
				}
				if (response.code < HttpURLConnection.HTTP_INTERNAL_ERROR) {
					return response;
				}
				logger.error("At {} [{}] {}", url, response.code, response.bodyToString());
				logger.error("Error connecting to {}. Trying next server if exists...", url);
				error = new IOException(response.bodyToString());
			} catch (final java.net.ConnectException ce) {
				if (retriesOnConflictOrConnectionError > 0) {
					return transmit(service, headers, method, payload, buffer, retriesOnConflictOrConnectionError - 1);
				}
				logger.error("At {} {}. Trying next server if exists...", service, ce.getMessage());
				error = ce;
			} catch (final IOException ex) {
				logger.error("IOException {} to {}. Trying next server if exists...", ex.getMessage(), service);
				error = ex;
			}
			currentUrl++;
			currentUrl %= remoteUrls.length;
		}
		throw error;
	}

	private Response transmit(
			final URL url,
			final List<Map.Entry<String, String>> headers,
			final String method,
			final Bytes payload,
			final byte[] buffer) throws IOException {

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
		for (final Map.Entry<String, String> h : headerProvider.getHeaders()) {
			conn.setRequestProperty(h.getKey(), h.getValue());
		}

		if (logger.isTraceEnabled()) {
			for (final Map.Entry<String, List<String>> header : conn.getRequestProperties().entrySet()) {
				logger.trace("{}: {}", header.getKey(), header.getValue());
			}
		}

		if (payload != null) {
			conn.setDoOutput(true);
			if (logger.isTraceEnabled()) logger.trace("Adding payload: {}", payload.toUtf8());
			conn.setRequestProperty("Content-Length", Integer.toString(payload.length));
			payload.copyTo(conn.getOutputStream());
			conn.getOutputStream().close();
		}

		return new Response(conn, buffer);
	}
}
