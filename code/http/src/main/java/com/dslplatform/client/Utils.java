package com.dslplatform.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

class Utils {
	public static <T> ArrayList<T> toArrayList(final Iterable<T> iterable) {
		final ArrayList<T> copy = new ArrayList<T>();
		for (final T t : iterable) {
			copy.add(t);
		}
		return copy;
	}

	@SuppressWarnings("unchecked")
	public static List<Map.Entry<String, String>> acceptAs(final String mimeType) {
		return Arrays.asList((Map.Entry<String, String>) new AbstractMap.SimpleEntry<String, String>(
				"Accept",
				mimeType));
	}

	private static void appendOrder(
			final StringBuilder sb,
			final Iterable<Map.Entry<String, Boolean>> order,
			final boolean isFirst) {
		if (order != null && order.iterator().hasNext()) {
			sb.append(isFirst ? "?order=" : "&order=");
			for (final Map.Entry<String, Boolean> el : order) {
				// null Boolean object will default to true
				if (el.getValue() == Boolean.FALSE) {
					sb.append('-');
				}
				sb.append(el.getKey()).append(',');
			}
			sb.setLength(sb.length() - 1);
		}
	}

	public static String appendLimitOffsetOrder(
			final String url,
			final Integer limit,
			final Integer offset,
			final Iterable<Map.Entry<String, Boolean>> order,
			final Boolean isFirst) {
		final StringBuilder sB = new StringBuilder(url);
		final boolean limitnull = limit == null;
		final boolean offsetnull = offset == null;
		final boolean orderFirst = limitnull && offsetnull && isFirst;

		if (!limitnull) {
			sB.append(isFirst ? "?limit=" : "&limit=").append(limit);
		}
		if (!offsetnull) {
			sB.append(limitnull && isFirst ? "?offset=" : "&offset=").append(offset);
		}

		appendOrder(sB, order, orderFirst);
		return sB.toString();
	}

	public static String buildOlapArguments(
			final Iterable<String> dimensions,
			final Iterable<String> facts,
			final Iterable<Map.Entry<String, Boolean>> order,
			final String specificationName) {

		final StringBuilder query = new StringBuilder();

		if (order != null && order.iterator().hasNext() && !contains(dimensions, order) && !contains(facts, order))
			throw new IllegalArgumentException("Order must be an element of dimensions or facts!");

		appendUrlParam(query, "dimensions", dimensions);
		appendUrlParam(query, "facts", facts);

		if (query.length() == 0) throw new IllegalArgumentException("At least one dimension or fact is required");

		appendUrlParam(query, "specification", specificationName);
		appendOrder(query, order, false);

		return query.toString();
	}

	public static String buildOlapArguments(
			final Iterable<String> dimensions,
			final Iterable<String> facts,
			final Iterable<Map.Entry<String, Boolean>> order) {
		final StringBuilder query = new StringBuilder();

		if (order != null && order.iterator().hasNext() && !contains(dimensions, order) && !contains(facts, order))
			throw new IllegalArgumentException("Order must be an element of dimensions or facts!");

		appendUrlParam(query, "dimensions", dimensions);
		appendUrlParam(query, "facts", facts);

		if (query.length() == 0) throw new IllegalArgumentException("At least one dimension or fact is required");

		appendOrder(query, order, false);

		return query.toString();
	}

	private static Boolean contains(final Iterable<String> source, final Iterable<Map.Entry<String, Boolean>> orders) {
		if (source == null) return false;
		for (final Map.Entry<String, Boolean> ord : orders) {
			if (!contains(source, ord.getKey())) return false;
		}
		return true;
	}

	private static void appendUrlParam(final StringBuilder sb, final String param, final Iterable<String> args) {
		if (args == null) return;

		final Iterator<String> aI = args.iterator();
		if (!aI.hasNext()) return;

		sb.append(sb.length() == 0 ? "?" : "&").append(param).append('=').append(aI.next());
		while (aI.hasNext()) {
			sb.append(',').append(aI.next());
		}
	}

	private static void appendUrlParam(final StringBuilder sB, final String param, final String arg) {
		if (arg == null) return;
		sB.append(sB.length() == 0 ? "?" : "&").append(param).append('=').append(arg);
	}

	private static Boolean contains(final Iterable<String> source, final String orders) {
		for (final String src : source) {
			if (orders.equals(src)) return true;
		}
		return false;
	}

	static byte[] inputStreamToByteArray(final InputStream stream) throws IOException {
		if (stream == null) return null;
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final byte[] buffer = new byte[1024];
		int len;
		while ((len = stream.read(buffer, 0, 1024)) != -1) {
			byteArrayOutputStream.write(buffer, 0, len);
		}

		return byteArrayOutputStream.toByteArray();
	}

	static final boolean isAndroid;

	static {
		isAndroid = System.getProperty("java.runtime.name").toLowerCase().contains("android");
	}

	static class AndroidEncoding {
		private static String toBase64(final byte[] body) {
			return android.util.Base64.encodeToString(body, android.util.Base64.NO_WRAP);
		}
	}

	static class JavaEncoding {
		private static String toBase64(final byte[] body) {
			return javax.xml.bind.DatatypeConverter.printBase64Binary(body);
		}
	}

	static String base64Encode(final byte[] body) {
		if (isAndroid) {
			return AndroidEncoding.toBase64(body);
		} else {
			return JavaEncoding.toBase64(body);
		}
	}
}
