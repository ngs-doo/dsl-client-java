package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

public class BinaryConverter {
	static final boolean isAndroid;

	static {
		isAndroid = System.getProperty("java.runtime.name").toLowerCase().contains("android");
	}

	public static void serialize(final byte[] value, final Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else if (value.length == 0)
			sw.write("\"\"");
		else {
			sw.write('"');
			sw.write(Base64.encodeToChar(value));
			sw.write('"');
		}
	}

	public static byte[] deserialize(final JsonReader reader) throws IOException {
		return reader.readBase64();
	}

	private static JsonReader.ReadObject<byte[]> Base64Reader = new JsonReader.ReadObject<byte[]>() {
		@Override
		public byte[] read(JsonReader reader) throws IOException {
			return deserialize(reader);
		}
	};

	public static ArrayList<byte[]> deserializeCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(Base64Reader);
	}

	public static void deserializeCollection(final JsonReader reader, final Collection<byte[]> res) throws IOException {
		reader.deserializeCollection(Base64Reader, res);
	}

	public static void deserializeNullableCollection(final JsonReader reader, final Collection<byte[]> res) throws IOException {
		reader.deserializeNullableCollection(Base64Reader, res);
	}
}
