package com.dslplatform.client.json;

import java.io.IOException;
import java.util.*;

public abstract class ObjectConverter {

	static final JsonReader.ReadObject<Object> ObjectReader = new JsonReader.ReadObject<Object>() {
		@Override
		public Object read(JsonReader reader) throws IOException {
			return deserializeObject(reader);
		}
	};
	static final JsonReader.ReadObject<Collection> CollectionReader = new JsonReader.ReadObject<Collection>() {
		@Override
		public Collection read(JsonReader reader) throws IOException {
			return deserializeList(reader);
		}
	};
	static final JsonReader.ReadObject<Map> MapReader = new JsonReader.ReadObject<Map>() {
		@Override
		public Map read(JsonReader reader) throws IOException {
			return deserializeMap(reader);
		}
	};
	static final JsonWriter.WriteObject<Map> MapWriter = new JsonWriter.WriteObject<Map>() {
		@Override
		public void write(JsonWriter writer, Map value) {
			try {
				serializeMap(value, writer);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	};

	private static final DslJsonSerialization JSON = new DslJsonSerialization(null);

	public static Object deserializeObject(final JsonReader reader) throws IOException {
		switch (reader.last()) {
			case 'n':
				if (!reader.wasNull()) {
					throw new IOException("Expecting 'null' at position " + reader.positionInStream() + ". Found " + (char) reader.last());
				}
				return null;
			case '"':
				return reader.readString();
			case '{':
				return deserializeMap(reader);
			case '[':
				return deserializeList(reader);
			default:
				return NumberConverter.deserializeNumber(reader);
		}
	}

	public static ArrayList<Object> deserializeList(final JsonReader reader) throws IOException {
		if (reader.last() != '[') {
			throw new IOException("Expecting '[' at position " + reader.positionInStream() + ". Found " + (char) reader.last());
		}
		byte nextToken = reader.getNextToken();
		if (nextToken == ']') return new ArrayList<>(0);
		final ArrayList<Object> res = new ArrayList<Object>();
		res.add(deserializeObject(reader));
		while ((nextToken = reader.getNextToken()) == ',') {
			reader.getNextToken();
			res.add(deserializeObject(reader));
		}
		if (nextToken != ']') {
			throw new IOException("Expecting ']' at position " + reader.positionInStream() + ". Found " + (char) nextToken);
		}
		return res;
	}

	public static void serializeMap(final Map<String, Object> value, final JsonWriter sw) throws IOException {
		sw.writeByte(JsonWriter.OBJECT_START);
		final int size = value.size();
		if (size > 0) {
			final Iterator<Map.Entry<String, Object>> iterator = value.entrySet().iterator();
			Map.Entry<String, Object> kv = iterator.next();
			sw.writeString(kv.getKey());
			sw.writeByte(JsonWriter.SEMI);
			JSON.serialize(sw, kv.getValue());
			for (int i = 1; i < size; i++) {
				sw.writeByte(JsonWriter.COMMA);
				kv = iterator.next();
				sw.writeString(kv.getKey());
				sw.writeByte(JsonWriter.SEMI);
				JSON.serialize(sw, kv.getValue());
			}
		}
		sw.writeByte(JsonWriter.OBJECT_END);
	}

	public static Map<String, Object> deserializeMap(final JsonReader reader) throws IOException {
		if (reader.last() != '{') {
			throw new IOException("Expecting '{' at position " + reader.positionInStream() + ". Found " + (char) reader.last());
		}
		byte nextToken = reader.getNextToken();
		if (nextToken == '}') return new HashMap<String, Object>(0);
		final HashMap<String, Object> res = new HashMap<String, Object>();
		String key = StringConverter.deserialize(reader);
		nextToken = reader.getNextToken();
		if (nextToken != ':') {
			throw new IOException("Expecting ':' at position " + reader.positionInStream() + ". Found " + (char) nextToken);
		}
		reader.getNextToken();
		res.put(key, deserializeObject(reader));
		while ((nextToken = reader.getNextToken()) == ',') {
			reader.getNextToken();
			key = StringConverter.deserialize(reader);
			nextToken = reader.getNextToken();
			if (nextToken != ':') {
				throw new IOException("Expecting ':' at position " + reader.positionInStream() + ". Found " + (char) nextToken);
			}
			reader.getNextToken();
			res.put(key, deserializeObject(reader));
		}
		if (nextToken != '}') {
			throw new IOException("Expecting '}' at position " + reader.positionInStream() + ". Found " + (char) nextToken);
		}
		return res;
	}
}
