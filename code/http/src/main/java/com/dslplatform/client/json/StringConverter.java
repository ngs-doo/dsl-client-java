package com.dslplatform.client.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class StringConverter {

	public static void serializeNullable(final String value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			serialize(value, sw);
		}
	}

	public static void serialize(final String value, final JsonWriter sw) {
		sw.writeByte(JsonWriter.QUOTE);
		char c;
		int i = 0, s = 0;
		for (; i < value.length(); i++) {
			c = value.charAt(i);
			if (c < 32 || c == '"' || c == '\\')
			{
				sw.writeString(value, s, i);
				switch (c) {
					case 0: sw.writeAscii("\\u0000"); break;
					case 1: sw.writeAscii("\\u0001"); break;
					case 2: sw.writeAscii("\\u0002"); break;
					case 3: sw.writeAscii("\\u0003"); break;
					case 4: sw.writeAscii("\\u0004"); break;
					case 5: sw.writeAscii("\\u0005"); break;
					case 6: sw.writeAscii("\\u0006"); break;
					case 7: sw.writeAscii("\\u0007"); break;
					case 8: sw.writeAscii("\\b"); break;
					case 9: sw.writeAscii("\\t"); break;
					case 10: sw.writeAscii("\\n"); break;
					case 11: sw.writeAscii("\\u000B"); break;
					case 12: sw.writeAscii("\\f"); break;
					case 13: sw.writeAscii("\\r"); break;
					case 14: sw.writeAscii("\\u000E"); break;
					case 15: sw.writeAscii("\\u000F"); break;
					case 16: sw.writeAscii("\\u0010"); break;
					case 17: sw.writeAscii("\\u0011"); break;
					case 18: sw.writeAscii("\\u0012"); break;
					case 19: sw.writeAscii("\\u0013"); break;
					case 20: sw.writeAscii("\\u0014"); break;
					case 21: sw.writeAscii("\\u0015"); break;
					case 22: sw.writeAscii("\\u0016"); break;
					case 23: sw.writeAscii("\\u0017"); break;
					case 24: sw.writeAscii("\\u0018"); break;
					case 25: sw.writeAscii("\\u0019"); break;
					case 26: sw.writeAscii("\\u001A"); break;
					case 27: sw.writeAscii("\\u001B"); break;
					case 28: sw.writeAscii("\\u001C"); break;
					case 29: sw.writeAscii("\\u001D"); break;
					case 30: sw.writeAscii("\\u001E"); break;
					case 31: sw.writeAscii("\\u001F"); break;
					case '\\': sw.writeAscii("\\\\"); break;
					default: sw.writeAscii("\\\""); break;
				}
				s = i;
			}
		}
		sw.writeString(value, s, value.length());
		sw.writeByte(JsonWriter.QUOTE);
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
