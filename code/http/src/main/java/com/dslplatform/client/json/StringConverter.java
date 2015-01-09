package com.dslplatform.client.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class StringConverter {

	public static void serializeShortNullable(final String value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			sw.writeString(value);
		}
	}

	public static void serializeShort(final String value, final JsonWriter sw) {
		sw.writeString(value);
	}

	public static void serializeNullable(final String value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			sw.writeString(value);
		}
	}

	public static void serialize(final String value, final JsonWriter sw) {
		sw.writeString(value);
	}

	public static String deserialize(final JsonReader reader) throws IOException {
		return reader.readString();
	}

	public static String deserializeNullable(final JsonReader reader) throws IOException {
		if (reader.last() == 'n') {
			if (!reader.wasNull()) throw new IOException("Expecting 'null' at position " + reader.positionInStream() + ". Found " + (char)reader.last());
			return null;
		}
		return reader.readString();
	}

	private static JsonReader.ReadObject<String> Reader = new JsonReader.ReadObject<String>() {
		@Override
		public String read(JsonReader reader) throws IOException {
			return deserialize(reader);
		}
	};

	public static ArrayList<String> deserializeCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithGet(Reader);
	}

	public static void deserializeCollection(final JsonReader reader, final Collection<String> res) throws IOException {
		reader.deserializeCollectionWithGet(Reader, res);
	}

	public static ArrayList<String> deserializeNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithGet(Reader);
	}

	public static void deserializeNullableCollection(final JsonReader reader, final Collection<String> res) throws IOException {
		reader.deserializeNullableCollectionWithGet(Reader, res);
	}
}
