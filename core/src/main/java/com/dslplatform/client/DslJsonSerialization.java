package com.dslplatform.client;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

import com.dslplatform.json.*;
import com.dslplatform.patterns.*;
import com.dslplatform.storage.S3;
import org.joda.time.DateTime;

public class DslJsonSerialization extends DslJson<ServiceLocator> implements JsonSerialization {

	static class DslFallback implements DslJson.Fallback<ServiceLocator> {

		private DslJsonSerialization dslJson;

		public void bind(DslJsonSerialization dslJson) {
			this.dslJson = dslJson;
		}

		@Override
		public void serialize(Object instance, OutputStream stream) throws IOException {
			Bytes result = serializeJackson(dslJson, instance);
			result.copyTo(stream);
		}

		@Override
		public Object deserialize(ServiceLocator serviceLocator, Type manifest, byte[] body, int size) throws IOException {
			return deserializeJackson(dslJson, manifest, body, size);
		}

		@Override
		public Object deserialize(ServiceLocator serviceLocator, Type manifest, InputStream stream) throws IOException {
			return deserializeJackson(dslJson, manifest, stream);
		}
	}

	public DslJsonSerialization(final ServiceLocator locator) {
		super(locator, Utils.IS_ANDROID, !Utils.IS_ANDROID, true, new DslFallback(), false, Collections.<Configuration>emptyList());

		((DslFallback)fallback).bind(this);

		registerReader(S3.class, StorageConverter.S3Reader);
		registerWriter(S3.class, StorageConverter.S3Writer);
	}

	private Object jackson;

	private static <TResult> TResult deserializeJackson(
			final DslJsonSerialization json,
			final Type manifest,
			final byte[] body,
			final int size) throws IOException {
		if (json.jackson == null) json.jackson = new JacksonJsonSerialization(json.context);
		return ((JacksonJsonSerialization) json.jackson).deserialize(manifest, body, size);
	}

	private static <TResult> TResult deserializeJackson(
			final DslJsonSerialization json,
			final Type manifest,
			final InputStream stream) throws IOException {
		if (json.jackson == null) json.jackson = new JacksonJsonSerialization(json.context);
		return ((JacksonJsonSerialization) json.jackson).deserialize(manifest, stream);
	}

	private static Bytes serializeJackson(
			final DslJsonSerialization json,
			final Object value) throws IOException {
		if (json.jackson == null) json.jackson = new JacksonJsonSerialization(json.context);
		return ((JacksonJsonSerialization) json.jackson).serialize(value);
	}

	public <TResult extends AggregateRoot> List<History<TResult>> deserializeHistoryList(
			final Class<TResult> manifest,
			final byte[] body,
			final int size) throws IOException {
		if (isNull(size, body)) {
			return null;
		}
		if (size == 2 && body[0] == '[' && body[1] == ']') {
			return new ArrayList<History<TResult>>(0);
		}
		final JsonReader json = new JsonReader(body, size, context);
		if (json.getNextToken() != '[') {
			throw new IOException("Expecting '[' as array start. Found: " + (char) json.last());
		}
		if (json.getNextToken() == ']') {
			return new ArrayList<History<TResult>>(0);
		}
		final JsonReader.ReadJsonObject<JsonObject> reader = getObjectReader(manifest);
		if (reader == null) {
			throw new IOException("Unable to find reader for provided history type: " + manifest);
		}
		final ArrayList<History<TResult>> result = new ArrayList<History<TResult>>();
		do {
			if (json.last() != '{') {
				throw new IOException("Expecting '{' at " + json.getCurrentIndex());
			}
			ArrayList<Snapshot<TResult>> snapshots = null;
			json.getNextToken();
			String name = json.readString();
			if (json.getNextToken() != ':') {
				throw new IOException("Expecting ':' at " + json.getCurrentIndex());
			}
			if ("Snapshots".equalsIgnoreCase(name)) {
				snapshots = readSnaphosts(manifest, reader, json, context);
			} else {
				json.skip();
			}
			while (json.getNextToken() == ',') {
				name = json.readString();
				if (json.getNextToken() != ':') {
					throw new IOException("Expecting ':' at" + json.getCurrentIndex());
				}
				if ("Snapshots".equalsIgnoreCase(name)) {
					snapshots = readSnaphosts(manifest, reader, json, context);
				} else {
					json.skip();
				}
			}
			if (json.last() != '}') {
				throw new IOException("Expecting '}' at " + json.getCurrentIndex());
			}
			if (snapshots == null) {
				throw new IOException("Snapshots not provided. It can't be null. Error at " + json.getCurrentIndex());
			}
			result.add(new History<TResult>(snapshots));
			switch (json.getNextToken()) {
				case ']':
					return result;
				case ',':
					json.getNextToken();
					break;
				default:
					throw new IOException("Expecting ']' or ',' at " + json.getCurrentIndex());
			}
		} while (true);
	}

	@SuppressWarnings("unchecked")
	private static <TResult extends AggregateRoot> ArrayList<Snapshot<TResult>> readSnaphosts(
			final Class<TResult> manifest,
			final JsonReader.ReadJsonObject<JsonObject> reader,
			final JsonReader json,
			final ServiceLocator locator) throws IOException {
		if (json.getNextToken() != '[') {
			throw new IOException("Expecting '[' at " + json.getCurrentIndex());
		}
		if (json.getNextToken() == ']') {
			return new ArrayList<Snapshot<TResult>>(0);
		}
		final ArrayList<Snapshot<TResult>> result = new ArrayList<Snapshot<TResult>>();
		do {
			if (json.last() != '{') {
				throw new IOException("Expecting '{' at " + json.getCurrentIndex());
			}
			DateTime at = Utils.MIN_DATE_TIME;
			String action = null;
			TResult value = null;
			String name;
			json.getNextToken();
			name = json.readString();
			if (json.getNextToken() != ':') {
				throw new IOException("Expecting ':' at " + json.getCurrentIndex());
			}
			json.getNextToken();
			if ("at".equalsIgnoreCase(name)) {
				at = JodaTimeConverter.deserializeDateTime(json);
			} else if ("action".equalsIgnoreCase(name)) {
				action = StringConverter.deserialize(json);
			} else if ("value".equalsIgnoreCase(name)) {
				if (json.last() != '{') {
					throw new IOException("Expecting '{' at " + json.getCurrentIndex());
				}
				json.getNextToken();
				value = (TResult) reader.deserialize(json);
			} else {
				json.skip();
			}
			while (json.getNextToken() == ',') {
				json.getNextToken();
				name = json.readString();
				if (json.getNextToken() != ':') {
					throw new IOException("Expecting ':' at " + json.getCurrentIndex());
				}
				json.getNextToken();
				if ("at".equalsIgnoreCase(name)) {
					at = JodaTimeConverter.deserializeDateTime(json);
				} else if ("action".equalsIgnoreCase(name)) {
					action = StringConverter.deserialize(json);
				} else if ("value".equalsIgnoreCase(name)) {
					if (json.last() != '{') {
						throw new IOException("Expecting '{' at " + json.getCurrentIndex());
					}
					json.getNextToken();
					value = (TResult) reader.deserialize(json);
				} else {
					json.skip();
				}
			}
			if (json.last() != '}') {
				throw new IOException("Expecting '}' at " + json.getCurrentIndex());
			}
			if (action == null) {
				throw new IOException("Action not provided. It can't be null. Error at " + json.getCurrentIndex());
			}
			if (value == null) {
				throw new IOException("Value not provided. It can't be null. Error at " + json.getCurrentIndex());
			}
			result.add(new Snapshot<TResult>(at, action, value));
			switch (json.getNextToken()) {
				case ']':
					return result;
				case ',':
					json.getNextToken();
					break;
				default:
					throw new IOException("Expecting ']' or ',' at " + json.getCurrentIndex());
			}
		} while (true);
	}

	private static final Bytes NULL = new Bytes(new byte[]{'n', 'u', 'l', 'l'}, 4);

	public final Bytes serialize(final Object value) throws IOException {
		if (value == null) return NULL;
		final JsonWriter jw = new JsonWriter();
		final Class<?> manifest = value.getClass();
		if (!serialize(jw, manifest, value)) {
			if (Utils.HAS_JACKSON) {
				return serializeJackson(this, value);
			}
			throw new IOException("Unable to serialize provided object. Failed to find serializer for: " + manifest);
		}
		return new Bytes(jw.getByteBuffer(), jw.size());
	}

	@Override
	public final void serialize(final Writer writer, final Object value) throws IOException {
		if (writer instanceof JsonWriter) {
			serialize((JsonWriter) writer, value);
		} else {
			final JsonWriter jw = new JsonWriter();
			serialize(jw, value);
			writer.write(jw.toString());
		}
	}
}
