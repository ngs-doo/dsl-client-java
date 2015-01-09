package com.dslplatform.client.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class UUIDConverter {

	public static final UUID MIN_UUID = new java.util.UUID(0L, 0L);

	private static final char[] LookupFirst;
	private static final char[] LookupSecond;
	private static final byte[] Values;

	static {
		LookupFirst = new char[256];
		LookupSecond = new char[256];
		Values = new byte['f' + 1 - '0'];
		for (int i = 0; i < 256; i++) {
			int hi = (i >> 4) & 15;
			int lo = i & 15;
			LookupFirst[i] = (char) (hi < 10 ? '0' + hi : 'a' + hi - 10);
			LookupSecond[i] = (char) (lo < 10 ? '0' + lo : 'a' + lo - 10);
		}
		for (char c = '0'; c <= '9'; c++) {
			Values[c - '0'] = (byte) (c - '0');
		}
		for (char c = 'a'; c <= 'f'; c++) {
			Values[c - '0'] = (byte) (c - 'a' + 10);
		}
		for (char c = 'A'; c <= 'F'; c++) {
			Values[c - '0'] = (byte) (c - 'A' + 10);
		}
	}

	public static void serializeNullable(final UUID value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			serialize(value, sw);
		}
	}

	public static void serialize(final UUID value, final JsonWriter sw) {
		final long hi = value.getMostSignificantBits();
		final long lo = value.getLeastSignificantBits();
		final int hi1 = (int) (hi >> 32);
		final int hi2 = (int) hi;
		final int lo1 = (int) (lo >> 32);
		final int lo2 = (int) lo;
		final char[] buf = sw.tmp;
		buf[0] = '"';
		int v = (hi1 >> 24) & 255;
		buf[1] = LookupFirst[v];
		buf[2] = LookupSecond[v];
		v = (hi1 >> 16) & 255;
		buf[3] = LookupFirst[v];
		buf[4] = LookupSecond[v];
		v = (hi1 >> 8) & 255;
		buf[5] = LookupFirst[v];
		buf[6] = LookupSecond[v];
		v = hi1 & 255;
		buf[7] = LookupFirst[v];
		buf[8] = LookupSecond[v];
		buf[9] = '-';
		v = (hi2 >> 24) & 255;
		buf[10] = LookupFirst[v];
		buf[11] = LookupSecond[v];
		v = (hi2 >> 16) & 255;
		buf[12] = LookupFirst[v];
		buf[13] = LookupSecond[v];
		buf[14] = '-';
		v = (hi2 >> 8) & 255;
		buf[15] = LookupFirst[v];
		buf[16] = LookupSecond[v];
		v = hi2 & 255;
		buf[17] = LookupFirst[v];
		buf[18] = LookupSecond[v];
		buf[19] = '-';
		v = (lo1 >> 24) & 255;
		buf[20] = LookupFirst[v];
		buf[21] = LookupSecond[v];
		v = (lo1 >> 16) & 255;
		buf[22] = LookupFirst[v];
		buf[23] = LookupSecond[v];
		buf[24] = '-';
		v = (lo1 >> 8) & 255;
		buf[25] = LookupFirst[v];
		buf[26] = LookupSecond[v];
		v = lo1 & 255;
		buf[27] = LookupFirst[v];
		buf[28] = LookupSecond[v];
		v = (lo2 >> 24) & 255;
		buf[29] = LookupFirst[v];
		buf[30] = LookupSecond[v];
		v = (lo2 >> 16) & 255;
		buf[31] = LookupFirst[v];
		buf[32] = LookupSecond[v];
		v = (lo2 >> 8) & 255;
		buf[33] = LookupFirst[v];
		buf[34] = LookupSecond[v];
		v = lo2 & 255;
		buf[35] = LookupFirst[v];
		buf[36] = LookupSecond[v];
		buf[37] = '"';
		sw.writeBuffer(38);
	}

	public static UUID deserialize(final JsonReader reader) throws IOException {
		final char[] buf = reader.readSimpleQuote();
		final int len = reader.getCurrentIndex() - reader.getTokenStart();
		if (len == 37 && buf[8] == '-' && buf[13] == '-' && buf[18] == '-' && buf[23] == '-') {
			try {
				long hi = 0;
				for (int i = 0; i < 8; i++)
					hi = (hi << 4) + Values[buf[i] - '0'];
				for (int i = 9; i < 13; i++)
					hi = (hi << 4) + Values[buf[i] - '0'];
				for (int i = 14; i < 18; i++)
					hi = (hi << 4) + Values[buf[i] - '0'];
				long lo = 0;
				for (int i = 19; i < 23; i++)
					lo = (lo << 4) + Values[buf[i] - '0'];
				for (int i = 24; i < 36; i++)
					lo = (lo << 4) + Values[buf[i] - '0'];
				return new UUID(hi, lo);
			} catch (IndexOutOfBoundsException ex) {
				return UUID.fromString(new String(buf, 0, 36));
			}
		} else if (len == 33) {
			try {
				long hi = 0;
				for (int i = 0; i < 16; i++)
					hi = (hi << 4) + Values[buf[i] - '0'];
				long lo = 0;
				for (int i = 16; i < 32; i++)
					lo = (lo << 4) + Values[buf[i] - '0'];
				return new UUID(hi, lo);
			} catch (IndexOutOfBoundsException ex) {
				return UUID.fromString(new String(buf, 0, 32));
			}
		} else {
			return UUID.fromString(new String(buf, 0, len - 1));
		}
	}

	private static JsonReader.ReadObject<UUID> Reader = new JsonReader.ReadObject<UUID>() {
		@Override
		public UUID read(JsonReader reader) throws IOException {
			return deserialize(reader);
		}
	};

	public static ArrayList<UUID> deserializeCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithGet(Reader);
	}

	public static void deserializeCollection(final JsonReader reader, final Collection<UUID> res) throws IOException {
		reader.deserializeCollectionWithGet(Reader, res);
	}

	public static ArrayList<UUID> deserializeNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithGet(Reader);
	}

	public static void deserializeNullableCollection(final JsonReader reader, final Collection<UUID> res) throws IOException {
		reader.deserializeNullableCollectionWithGet(Reader, res);
	}
}
