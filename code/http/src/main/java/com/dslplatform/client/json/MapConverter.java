package com.dslplatform.client.json;

import org.joda.time.LocalDate;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

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
			Map.Entry<String, String> kv;
			for (int i = 0; i < size; i++) {
				kv = iterator.next();
				StringConverter.serialize(kv.getKey(), sw);
				sw.write(':');
				StringConverter.serializeNullable(kv.getValue(), sw);
				sw.write(',');
			}
			kv = iterator.next();
			StringConverter.serialize(kv.getKey(), sw);
			sw.write(':');
			StringConverter.serializeNullable(kv.getValue(), sw);
		}
		sw.write('}');
	}

	public static Map<String, String> deserialize(final JsonReader reader) throws IOException {
		return null;
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

	public static ArrayList<Map<String, String>> deserializeNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(MapReader);
	}
}
