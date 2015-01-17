package com.dslplatform.client.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

public class NumberConverter {

	private final static char[] Digits = new char[100];

	static {
		for (int i = 0; i < 100; i++) {
			Digits[i] = (char)((((i / 10) + '0') << 8) + i % 10 + '0');
		}
	}

	static void write4(final int value, final byte[] buf, final int pos) {
		if (value > 9999) {
			throw new IllegalArgumentException("Only 4 digits numbers are supported. Provided: " + value);
		}
		final int q = value / 100;
		final int v1 = Digits[q];
		final int v2 = Digits[value - ((q << 6) + (q << 5) + (q << 2))];
		buf[pos] = (byte) (v1 >> 8);
		buf[pos + 1] = (byte) v1;
		buf[pos + 2] = (byte) (v2 >> 8);
		buf[pos + 3] = (byte) v2;
	}

	static void write2(final int value, final byte[] buf, final int pos) {
		final int v = Digits[value];
		buf[pos] = (byte) (v >> 8);
		buf[pos + 1] = (byte) v;
	}

	static int read2(final char[] buf, final int pos) {
		return (buf[pos] - 48) * 10 + buf[pos + 1] - 48;
	}

	static int read(final char[] buf, final int start, int end) {
		int value = 0;
		for (int i = start; i < end; i++) {
			value = value * 10 + buf[i] - 48;
		}
		return value;
	}

	static int read4(final char[] buf, final int pos) {
		return (buf[pos] - 48) * 1000 + (buf[pos + 1] - 48) * 100 + (buf[pos + 2] - 48) * 10 + buf[pos + 3] - 48;
	}

	public static void serializeNullable(final Double value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			sw.writeAscii(Double.toString(value));
		}
	}

	public static void serialize(final double value, final JsonWriter sw) {
		//TODO: better implementation required
		sw.writeAscii(Double.toString(value));
	}

	public static Double deserializeDouble(final JsonReader reader) throws IOException {
		//TODO: better implementation required
		return Double.parseDouble(reader.readShortValue());
	}

	private static JsonReader.ReadObject<Double> DoubleReader = new JsonReader.ReadObject<Double>() {
		@Override
		public Double read(JsonReader reader) throws IOException {
			return deserializeDouble(reader);
		}
	};

	public static ArrayList<Double> deserializeDoubleCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithMove(DoubleReader);
	}

	public static void deserializeDoubleCollection(final JsonReader reader, final Collection<Double> res) throws IOException {
		reader.deserializeCollectionWithMove(DoubleReader, res);
	}

	public static ArrayList<Double> deserializeDoubleNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithMove(DoubleReader);
	}

	public static void deserializeDoubleNullableCollection(final JsonReader reader, final Collection<Double> res) throws IOException {
		reader.deserializeNullableCollectionWithMove(DoubleReader, res);
	}

	public static void serializeNullable(final Float value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			sw.writeAscii(Float.toString(value));
		}
	}

	public static void serialize(final float value, final JsonWriter sw) {
		//TODO: better implementation required
		sw.writeAscii(Float.toString(value));
	}

	public static Float deserializeFloat(final JsonReader reader) throws IOException {
		//TODO: better implementation required
		return Float.parseFloat(reader.readShortValue());
	}

	private static JsonReader.ReadObject<Float> FloatReader = new JsonReader.ReadObject<Float>() {
		@Override
		public Float read(JsonReader reader) throws IOException {
			return deserializeFloat(reader);
		}
	};

	public static ArrayList<Float> deserializeFloatCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithMove(FloatReader);
	}

	public static void deserializeFloatCollection(final JsonReader reader, Collection<Float> res) throws IOException {
		reader.deserializeCollectionWithMove(FloatReader, res);
	}

	public static ArrayList<Float> deserializeFloatNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithMove(FloatReader);
	}

	public static void deserializeFloatNullableCollection(final JsonReader reader, final Collection<Float> res) throws IOException {
		reader.deserializeNullableCollectionWithMove(FloatReader, res);
	}

	public static void serializeNullable(final Integer value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			serialize(value, sw);
		}
	}

	private static final byte MINUS = '-';

	public static void serialize(final int value, final JsonWriter sw) {
		if (value == Integer.MIN_VALUE) {
			sw.writeAscii("-2147483648");
		} else {
			final byte[] buf = sw.tmp;
			int q, r;
			int charPos = 10;
			int i;
			if (value < 0) {
				i = -value;
				sw.writeByte(MINUS);
			} else {
				i = value;
			}

			do {
				q = i / 100;
				r = i - ((q << 6) + (q << 5) + (q << 2));
				i = q;
				final int v = Digits[r];
				buf[charPos--] = (byte)v;
				buf[charPos--] = (byte)(v >> 8);
			} while (i != 0);

			final int start = buf[charPos + 1] == '0' ? charPos + 2 : charPos + 1;
			sw.writeBuffer(start, 11);
		}
	}

	public static int deserializeInt(final JsonReader reader) throws IOException {
		final char[] buf = reader.readNumber();
		int value = 0;
		final int len = reader.getCurrentIndex() - reader.getTokenStart() - 1;
		char ch;
		final int start = buf[0] == '-' ? 1 : 0;
		for (int i = start; i < len && i < buf.length; i++) {
			ch = buf[i];
			if (ch >= '0' && ch <= '9') {
				value = value * 10 + ch - 48;
			} else return Integer.parseInt(new String(buf, 0, len));
		}
		//TODO: leading zero...
		return start == 0 ? value : -value;
	}

	private static JsonReader.ReadObject<Integer> IntReader = new JsonReader.ReadObject<Integer>() {
		@Override
		public Integer read(JsonReader reader) throws IOException {
			return deserializeInt(reader);
		}
	};

	public static ArrayList<Integer> deserializeIntCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithMove(IntReader);
	}

	public static void deserializeIntCollection(final JsonReader reader, final Collection<Integer> res) throws IOException {
		reader.deserializeCollectionWithMove(IntReader, res);
	}

	public static ArrayList<Integer> deserializeIntNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithMove(IntReader);
	}

	public static void deserializeIntNullableCollection(final JsonReader reader, final Collection<Integer> res) throws IOException {
		reader.deserializeNullableCollectionWithMove(IntReader, res);
	}

	public static void serializeNullable(final Long value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			serialize(value, sw);
		}
	}

	public static void serialize(final long value, final JsonWriter sw) {
		if (value == Long.MIN_VALUE) {
			sw.writeAscii("-9223372036854775808");
		} else {
			final byte[] buf = sw.tmp;
			long q;
			int r;
			int charPos = 20;
			long i;
			if (value < 0) {
				i = -value;
				sw.writeByte(MINUS);
			} else {
				i = value;
			}

			do {
				q = i / 100;
				r = (int) (i - ((q << 6) + (q << 5) + (q << 2)));
				i = q;
				final int v = Digits[r];
				buf[charPos--] = (byte)v;
				buf[charPos--] = (byte)(v >> 8);
			} while (i != 0);

			final int start = buf[charPos + 1] == '0' ? charPos + 2 : charPos + 1;
			sw.writeBuffer(start, 21);
		}
	}

	public static long deserializeLong(final JsonReader reader) throws IOException {
		final char[] buf = reader.readNumber();
		long value = 0;
		final int len = reader.getCurrentIndex() - reader.getTokenStart() - 1;
		char ch;
		final int start = buf[0] == '-' ? 1 : 0;
		for (int i = 0; i < len && i < buf.length; i++) {
			ch = buf[i];
			if (ch >= '0' && ch <= '9') {
				value = value * 10 + ch - 48;
			} else return Long.parseLong(new String(buf, 0, len));
		}
		//TODO: leading zero...
		return start == 0 ? value : -value;
	}

	private static JsonReader.ReadObject<Long> LongReader = new JsonReader.ReadObject<Long>() {
		@Override
		public Long read(JsonReader reader) throws IOException {
			return deserializeLong(reader);
		}
	};

	public static ArrayList<Long> deserializeLongCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithMove(LongReader);
	}

	public static void deserializeLongCollection(final JsonReader reader, final Collection<Long> res) throws IOException {
		reader.deserializeCollectionWithMove(LongReader, res);
	}

	public static ArrayList<Long> deserializeLongNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithMove(LongReader);
	}

	public static void deserializeLongNullableCollection(final JsonReader reader, final Collection<Long> res) throws IOException {
		reader.deserializeNullableCollectionWithMove(LongReader, res);
	}

	public static void serializeNullable(final BigDecimal value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			sw.writeAscii(value.toString());
		}
	}

	public static void serialize(final BigDecimal value, final JsonWriter sw) {
		sw.writeAscii(value.toString());
	}

	public static BigDecimal deserializeDecimal(final JsonReader reader) throws IOException {
		final char[] buf = reader.readNumber();
		return new BigDecimal(buf, 0, reader.getCurrentIndex() - reader.getTokenStart() - 1);
	}

	private static JsonReader.ReadObject<BigDecimal> DecimalReader = new JsonReader.ReadObject<BigDecimal>() {
		@Override
		public BigDecimal read(JsonReader reader) throws IOException {
			return deserializeDecimal(reader);
		}
	};

	public static ArrayList<BigDecimal> deserializeDecimalCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithMove(DecimalReader);
	}

	public static void deserializeDecimalCollection(final JsonReader reader, final Collection<BigDecimal> res) throws IOException {
		reader.deserializeCollectionWithMove(DecimalReader, res);
	}

	public static ArrayList<BigDecimal> deserializeDecimalNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithMove(DecimalReader);
	}

	public static void deserializeDecimalNullableCollection(final JsonReader reader, final Collection<BigDecimal> res) throws IOException {
		reader.deserializeNullableCollectionWithMove(DecimalReader, res);
	}
}
