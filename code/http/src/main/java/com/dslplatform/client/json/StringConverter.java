package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

public class StringConverter {
	public static void serializeNullable(final String value, final Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(final String value, final Writer sw) throws IOException {
		sw.write('"');
		char c;
		int i = 0;
		for (; i < value.length(); i++) {
			c = value.charAt(i);
			if (c < 32 || c == '"' || c == '\\')
				break;
		}
		sw.write(value, 0, i);
		for (; i < value.length(); i++) {
			c = value.charAt(i);
			switch (c) {
				case 0: sw.write("\\u0000"); break;
				case 1: sw.write("\\u0001"); break;
				case 2: sw.write("\\u0002"); break;
				case 3: sw.write("\\u0003"); break;
				case 4: sw.write("\\u0004"); break;
				case 5: sw.write("\\u0005"); break;
				case 6: sw.write("\\u0006"); break;
				case 7: sw.write("\\u0007"); break;
				case 8: sw.write("\\b"); break;
				case 9: sw.write("\\t"); break;
				case 10: sw.write("\\n"); break;
				case 11: sw.write("\\u000B"); break;
				case 12: sw.write("\\f"); break;
				case 13: sw.write("\\r"); break;
				case 14: sw.write("\\u000E"); break;
				case 15: sw.write("\\u000F"); break;
				case 16: sw.write("\\u0010"); break;
				case 17: sw.write("\\u0011"); break;
				case 18: sw.write("\\u0012"); break;
				case 19: sw.write("\\u0013"); break;
				case 20: sw.write("\\u0014"); break;
				case 21: sw.write("\\u0015"); break;
				case 22: sw.write("\\u0016"); break;
				case 23: sw.write("\\u0017"); break;
				case 24: sw.write("\\u0018"); break;
				case 25: sw.write("\\u0019"); break;
				case 26: sw.write("\\u001A"); break;
				case 27: sw.write("\\u001B"); break;
				case 28: sw.write("\\u001C"); break;
				case 29: sw.write("\\u001D"); break;
				case 30: sw.write("\\u001E"); break;
				case 31: sw.write("\\u001F"); break;
				case '\\': sw.write("\\\\"); break;
				case '"': sw.write("\\\""); break;
				default: sw.write(c); break;
			}
		}
		sw.write('"');
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
		return reader.deserializeCollection(Reader);
	}

	public static void deserializeCollection(final JsonReader reader, final Collection<String> res) throws IOException {
		reader.deserializeCollection(Reader, res);
	}

	public static ArrayList<String> deserializeNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(Reader);
	}

	public static void deserializeNullableCollection(final JsonReader reader, final Collection<String> res) throws IOException {
		reader.deserializeNullableCollection(Reader, res);
	}
}
