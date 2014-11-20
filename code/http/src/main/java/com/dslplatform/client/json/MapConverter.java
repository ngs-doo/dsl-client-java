package com.dslplatform.client.json;

import org.joda.time.LocalDate;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class MapConverter {
	public static void serializeNullable(final Map<String, String> value, final Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(final Map<String, String> value, final Writer sw) throws IOException {
		sw.write('{');
		final int size = value.size();
		if (size > 0) {
			final Iterator<Map.Entry<String, String>> iterator = value.entrySet().iterator();
			Map.Entry<String, String> kv = iterator.next();
			StringConverter.serialize(kv.getKey(), sw);
			sw.write(':');
			StringConverter.serializeNullable(kv.getValue(), sw);
			for (int i = 1; i < size; i++) {
				sw.write(',');
				kv = iterator.next();
				StringConverter.serialize(kv.getKey(), sw);
				sw.write(':');
				StringConverter.serializeNullable(kv.getValue(), sw);
			}
		}
		sw.write('}');
	}

	public static Map<String, String> deserialize(final JsonReader reader) throws IOException {
		if (reader.last() != '{') throw new IOException("Expecting '{' at position " + reader.positionInStream() + ". Found " + (char)reader.last());
		HashMap<String, String> res = new HashMap<String, String>();
		byte nextToken = reader.getNextToken();
		if (nextToken == '}') return res;
		String key = StringConverter.deserialize(reader);
		nextToken = reader.getNextToken();
		if (nextToken != ':') throw new IOException("Expecting ':' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
		reader.getNextToken();
		String value = StringConverter.deserializeNullable(reader);
		res.put(key, value);
		while ((nextToken = reader.getNextToken()) == ',') {
			reader.getNextToken();
			key = StringConverter.deserialize(reader);
			nextToken = reader.getNextToken();
			if (nextToken != ':') throw new IOException("Expecting ':' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
			reader.getNextToken();
			value = StringConverter.deserializeNullable(reader);
			res.put(key, value);
		}
		if (nextToken != '}') throw new IOException("Expecting '}' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
		return res;
	}

	private static JsonReader.ReadObject<Map<String, String>> MapReader = new JsonReader.ReadObject<Map<String, String>>() {
		@Override
		public Map<String, String> read(JsonReader reader) throws IOException {
			return deserialize(reader);
		}
	};

	public static ArrayList<Map<String, String>> deserializeCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(MapReader);
	}

	public static void deserializeCollection(final JsonReader reader, Collection<Map<String, String>> res) throws IOException {
		reader.deserializeCollection(MapReader, res);
	}

	public static ArrayList<Map<String, String>> deserializeNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(MapReader);
	}

	public static void deserializeNullableCollection(final JsonReader reader, Collection<Map<String, String>> res) throws IOException {
		reader.deserializeNullableCollection(MapReader, res);
	}
}
