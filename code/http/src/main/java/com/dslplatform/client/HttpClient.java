package com.dslplatform.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;

import com.dslplatform.client.exceptions.HttpException;
import com.dslplatform.client.exceptions.HttpSecurityException;
import com.dslplatform.client.exceptions.HttpServerErrorException;
import com.dslplatform.client.exceptions.HttpUnexpectedCodeException;
import com.dslplatform.client.json.JsonObject;
import com.dslplatform.client.json.JsonReader;
import com.dslplatform.client.json.JsonWriter;
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
		public final int size;
		public final HttpURLConnection connection;

		public Response(final HttpURLConnection connection, final byte[] buffer) throws IOException {
			this.code = connection.getResponseCode();
			size = connection.getContentLength();
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
						? Utils.inputStreamToByteArray(connection.getInputStream())
						: Utils.inputStreamToByteArray(connection.getErrorStream());
			}
			this.connection = connection;
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
	private final JsonSerialization jsonDeserialization;
	private final ServiceLocator locator;
	private final HttpHeaderProvider headerProvider;
	private final ExecutorService executorService;
	private final String domainPrefix;
	private final int domainPrefixLength;
	private final String[] remoteUrls;
	private final SSLSocketFactory SSL_SOCKET_FACTORY;
	private static final String MIME_TYPE = "application/json";
	private int currentUrl;
	private final BlockingDeque<JsonWriter> jsonWriters;
	private final BlockingDeque<byte[]> resultBuffers;

	public HttpClient(
			final Properties properties,
			final JsonSerialization jsonDeserialization,
			final ServiceLocator locator,
			final Logger logger,
			final HttpHeaderProvider headerProvider,
			final ExecutorService executorService) {
		this.logger = logger;
		this.jsonDeserialization = jsonDeserialization;
		this.locator = locator;
		this.executorService = executorService;
		this.remoteUrls = properties.getProperty("api-url").split(",\\s+");

		final int totalWriters = Runtime.getRuntime().availableProcessors() * 2;
		jsonWriters = new LinkedBlockingDeque<JsonWriter>(totalWriters);
		resultBuffers = new LinkedBlockingDeque<byte[]>(totalWriters);
		for (int i = 0;i < totalWriters; i++) {
			jsonWriters.addFirst(new JsonWriter());
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

		final byte[] bodyContent;
		final int bodySize;
		JsonWriter sw = null;
		final Response response;
		try {
			if (content == null) {
				bodyContent = null;
				bodySize = 0;

				logger.debug("Sending request [{}]: {}", method, service);
			} else {
				if (content instanceof JsonObject) {
					final JsonObject jo = (JsonObject) content;
					sw = jsonWriters.takeFirst();
					jo.serialize(sw, true);
					final JsonWriter.Bytes bytes = sw.toBytes();
					sw.reset();
					bodyContent = bytes.content;
					bodySize = bytes.length;
				} else {
					bodyContent = JsonSerialization.serializeBytes(content);
					bodySize = bodyContent.length;
				}

				logger.debug("Sending request [{}]: {}, content size: {} bytes", method, service, bodySize);
			}
			response = transmit(service, headers, method, bodyContent, bodySize, buffer, 2);
		} finally{
			if(sw!=null) jsonWriters.putFirst(sw);
		}

		if (logger.isDebugEnabled()) {
			final long time = System.currentTimeMillis() - start;
			logger.debug("Received response [{}, {} bytes] in {} ms", response.code, response.body.length, time);

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

	private static final ConcurrentHashMap<Class<?>, JsonReader.ReadJsonObject<JsonObject>> jsonReaders =
			new ConcurrentHashMap<Class<?>, JsonReader.ReadJsonObject<JsonObject>>();

	@SuppressWarnings("unchecked")
	private static JsonReader.ReadJsonObject<JsonObject> getReader(final Class<?> manifest) {
		try {
			JsonReader.ReadJsonObject<JsonObject> reader = jsonReaders.get(manifest);
			if (reader == null) {
				reader = (JsonReader.ReadJsonObject<JsonObject>) manifest.getField("JSON_READER").get(null);
				jsonReaders.putIfAbsent(manifest, reader);
			}
			return reader;
		}catch (final Exception ignore) {
			return null;
		}
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
					if (JsonObject.class.isAssignableFrom(manifest)) {
						final JsonReader.ReadJsonObject<JsonObject> reader = getReader(manifest);
						if (reader != null) {
							final JsonReader json = new JsonReader(response.body, response.size, locator);
							if (json.getNextToken() == '{') {
								return (TResult) reader.deserialize(json, locator);
							}
						}
					}
					return jsonDeserialization.deserialize(manifest, response.body, response.size);
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
					if (JsonObject.class.isAssignableFrom(manifest)) {
						final JsonReader.ReadJsonObject<JsonObject> reader = getReader(manifest);
						if (reader != null) {
							final JsonReader json = new JsonReader(response.body, response.size, locator);
							if (json.getNextToken() == '[') {
								if (json.getNextToken() == ']') {
									return new ArrayList<TResult>();
								}
								return (List<TResult>) json.deserializeCollection(reader);
							}
						}
					}
					final JavaType type = JsonSerialization.buildCollectionType(ArrayList.class, manifest);
					return jsonDeserialization.deserialize(type, response.body, response.size);
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
			final byte[] payload,
			final int size,
			final byte[] buffer,
			final int retriesOnConflictOrConnectionError) throws IOException {
		IOException error = null;
		final int maxLen = remoteUrls.length + currentUrl;
		for (int i = currentUrl; i < maxLen; i++) {
			try {
				final URL url = new URL(remoteUrls[i % remoteUrls.length] + service);
				final HttpClient.Response response = transmit(url, headers, method, payload, size, buffer);
				if (response.code == HttpURLConnection.HTTP_CONFLICT && retriesOnConflictOrConnectionError > 0) {
					return transmit(service, headers, method, payload, size, buffer, retriesOnConflictOrConnectionError - 1);
				}
				if (response.code < HttpURLConnection.HTTP_INTERNAL_ERROR) {
					return response;
				}
				logger.error("At {} [{}] {}", url, response.code, response.bodyToString());
				logger.error("Error connecting to {}. Trying next server if exists...", url);
				error = new IOException(response.bodyToString());
			} catch (final java.net.ConnectException ce) {
				if (retriesOnConflictOrConnectionError > 0) {
					return transmit(service, headers, method, payload, size, buffer, retriesOnConflictOrConnectionError - 1);
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
			final byte[] payload,
			final int size,
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
			if (logger.isTraceEnabled()) logger.trace("Adding payload: {}", new String(payload, 0, size, "UTF-8"));
			conn.setRequestProperty("Content-Length", Integer.toString(size));
			conn.getOutputStream().write(payload, 0, size);
			conn.getOutputStream().close();
		}

		return new Response(conn, buffer);
	}
}
