package com.dslplatform.client.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class BoolConverter {

	public static void serializeNullable(final Boolean value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else if (value) {
			sw.writeAscii("true");
		} else {
			sw.writeAscii("false");
		}
	}

	public static void serialize(final boolean value, final JsonWriter sw) {
		if (value) {
			sw.writeAscii("true");
		} else {
			sw.writeAscii("false");
		}
	}

	public static boolean deserialize(final JsonReader reader) throws IOException {
		if (reader.wasTrue()) {
			return true;
		} else if (reader.wasFalse()) {
			return false;
		}
		throw new IOException("Found invalid boolean value at: " + reader.positionInStream());
	}

	private static JsonReader.ReadObject<Boolean> BooleanReader = new JsonReader.ReadObject<Boolean>() {
		@Override
		public Boolean read(JsonReader reader) throws IOException {
			return deserialize(reader);
		}
	};

	public static ArrayList<Boolean> deserializeCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithGet(BooleanReader);
	}

	public static void deserializeCollection(final JsonReader reader, final Collection<Boolean> res) throws IOException {
		reader.deserializeCollectionWithGet(BooleanReader, res);
	}

	public static ArrayList<Boolean> deserializeNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithGet(BooleanReader);
	}

	public static void deserializeNullableCollection(final JsonReader reader, final Collection<Boolean> res) throws IOException {
		reader.deserializeNullableCollectionWithGet(BooleanReader, res);
	}
}
