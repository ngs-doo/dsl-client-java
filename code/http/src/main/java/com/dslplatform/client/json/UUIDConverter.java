package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.UUID;

public class UUIDConverter {
	public static void serializeNullable(final UUID value, final Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(final UUID value, final Writer sw) throws IOException {
		sw.write('"');
		sw.write(value.toString());
		sw.write('"');
	}

	public static UUID deserialize(final JsonReader reader) throws IOException {
		return UUID.fromString(reader.readSimpleString());
	}

	private static JsonReader.ReadObject<UUID> Reader = new JsonReader.ReadObject<UUID>() {
		@Override
		public UUID read(JsonReader reader) throws IOException {
			return deserialize(reader);
		}
	};

	public static ArrayList<UUID> deserializeCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(Reader);
	}

	public static ArrayList<UUID> deserializeNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(Reader);
	}
}
