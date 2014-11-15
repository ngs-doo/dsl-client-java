package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class BinaryConverter {
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

	public static void serialize(final byte[] value, final Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else if (value.length == 0)
			sw.write("\"\"");
		else
		{
			if (isAndroid) {
				sw.write(AndroidEncoding.toBase64(value));
			} else {
				sw.write(JavaEncoding.toBase64(value));
			}
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

	public static ArrayList<byte[]> deserializeNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(Base64Reader);
	}
}
