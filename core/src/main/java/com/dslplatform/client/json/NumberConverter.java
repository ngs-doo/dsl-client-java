package com.dslplatform.client.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

public class NumberConverter {

	private final static int[] Digits = new int[100];
	private final static int[] Digit = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

	static {
		for (int i = 0; i < 100; i++) {
			Digits[i] = (i / 10 == 0 ? 1 << 16 : 0) + (((i / 10) + '0') << 8) + i % 10 + '0';
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
		final int v1 = buf[pos] - 48;
		return (v1 << 3) + (v1 << 1) + buf[pos + 1] - 48;
	}

	static int read4(final char[] buf, final int pos) {
		final int v2 = buf[pos + 1] - 48;
		final int v3 = buf[pos + 2] - 48;
		return (buf[pos] - 48) * 1000
				+ (v2 << 6) + (v2 << 5) + (v2 << 2)
				+ (v3 << 3) + (v3 << 1)
				+ buf[pos + 3] - 48;
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

	public static double deserializeDouble(final JsonReader reader) throws IOException {
		final char[] buf = reader.readNumber();
		final int len = reader.getCurrentIndex() - reader.getTokenStart() - 1;
		long value = 0;
		char ch = buf[0];
		try {
			if (ch == '-') {
				int i = 1;
				for (; i < buf.length; i++) {
					ch = buf[i];
					if (i == len || ch == '.') break;
					value = (value << 3) + (value << 1) - Digit[ch - 48];
				}
				if (ch == '.') {
					i++;
					int div = 1;
					for (; i < buf.length; i++) {
						if (i == len) break;
						div = (div << 3) + (div << 1);
						value = (value << 3) + (value << 1) - Digit[buf[i] - 48];
					}
					return value / (double)div;
				} else {
					return value;
				}
			} else {
				int i = 0;
				for (; i < buf.length; i++) {
					ch = buf[i];
					if (i == len || ch == '.') break;
					value = (value << 3) + (value << 1) + Digit[ch - 48];
				}
				if (ch == '.') {
					i++;
					int div = 1;
					for (; i < buf.length; i++) {
						if (i == len) break;
						div = (div << 3) + (div << 1);
						value = (value << 3) + (value << 1) + Digit[buf[i] - 48];
					}
					return value / (double)div;
				} else {
					return value;
				}
			}
		} catch (ArrayIndexOutOfBoundsException ignore) {
			int end = len;
			while (end > 0 && Character.isWhitespace(buf[end - 1])) {
				end --;
			}
			try {
				return Double.parseDouble(new String(buf, 0, end));
			} catch (NumberFormatException nfe) {
				throw new IOException("Error parsing float number at position: " + (reader.getCurrentIndex() - len), nfe);
			}
		}
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

	public static float deserializeFloat(final JsonReader reader) throws IOException {
		final char[] buf = reader.readNumber();
		final int len = reader.getCurrentIndex() - reader.getTokenStart() - 1;
		long value = 0;
		char ch = buf[0];
		try {
			if (ch == '-') {
				int i = 1;
				for (; i < buf.length; i++) {
					ch = buf[i];
					if (i == len || ch == '.') break;
					value = (value << 3) + (value << 1) - Digit[ch - 48];
				}
				if (ch == '.') {
					i++;
					int div = 1;
					for (; i < buf.length; i++) {
						if (i == len) break;
						div = (div << 3) + (div << 1);
						value = (value << 3) + (value << 1) - Digit[buf[i] - 48];
					}
					return value / (float)div;
				} else {
					return value;
				}
			} else {
				int i = 0;
				for (; i < buf.length; i++) {
					ch = buf[i];
					if (i == len || ch == '.') break;
					value = (value << 3) + (value << 1) + Digit[ch - 48];
				}
				if (ch == '.') {
					i++;
					int div = 1;
					for (; i < buf.length; i++) {
						if (i == len) break;
						div = (div << 3) + (div << 1);
						value = (value << 3) + (value << 1) + Digit[buf[i] - 48];
					}
					return value / (float)div;
				} else {
					return value;
				}
			}
		} catch (ArrayIndexOutOfBoundsException ignore) {
			int end = len;
			while (end > 0 && Character.isWhitespace(buf[end - 1])) {
				end --;
			}
			try {
				return Float.parseFloat(new String(buf, 0, end));
			} catch (NumberFormatException nfe) {
				throw new IOException("Error parsing float number at position: " + (reader.getCurrentIndex() - len), nfe);
			}
		}
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

			int v;
			for (; ; ) {
				q = i / 100;
				r = i - ((q << 6) + (q << 5) + (q << 2));
				i = q;
				v = Digits[r];
				buf[charPos--] = (byte) v;
				buf[charPos--] = (byte) (v >> 8);
				if (i == 0) break;
			}

			sw.writeBuffer(charPos + 1 + (v >> 16), 11);
		}
	}

	public static int deserializeInt(final JsonReader reader) throws IOException {
		final char[] buf = reader.readNumber();
		final int len = reader.getCurrentIndex() - reader.getTokenStart() - 1;
		int value = 0;
		try {
			if (buf[0] == '-') {
				for (int i = 1; i < buf.length; i++) {
					if (i == len) break;
					value = (value << 3) + (value << 1) - Digit[buf[i] - 48];
				}
			} else {
				for (int i = 0; i < buf.length; i++) {
					if (i == len) break;
					value = (value << 3) + (value << 1) + Digit[buf[i] - 48];
				}
			}
		} catch (ArrayIndexOutOfBoundsException ignore) {
			int end = len;
			while (end > 0 && Character.isWhitespace(buf[end - 1])) {
				end --;
			}
			try {
				return Integer.parseInt(new String(buf, 0, end));
			} catch (NumberFormatException nfe) {
				throw new IOException("Error parsing int number at position: " + (reader.getCurrentIndex() - len), nfe);
			}
		}
		return value;
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

			int v;
			for (; ; ) {
				q = i / 100;
				r = (int) (i - ((q << 6) + (q << 5) + (q << 2)));
				i = q;
				v = Digits[r];
				buf[charPos--] = (byte) v;
				buf[charPos--] = (byte) (v >> 8);
				if (i == 0) break;
			}

			sw.writeBuffer(charPos + 1 + (v >> 16), 21);
		}
	}

	public static long deserializeLong(final JsonReader reader) throws IOException {
		final char[] buf = reader.readNumber();
		final int len = reader.getCurrentIndex() - reader.getTokenStart() - 1;
		long value = 0;
		try {
			if (buf[0] == '-') {
				for (int i = 1; i < buf.length; i++) {
					if (i == len) break;
					value = (value << 3) + (value << 1) - Digit[buf[i] - 48];
				}
			} else {
				for (int i = 0; i < buf.length; i++) {
					if (i == len) break;
					value = (value << 3) + (value << 1) + Digit[buf[i] - 48];
				}
			}
		} catch (ArrayIndexOutOfBoundsException ignore) {
			int end = len;
			while (end > 0 && Character.isWhitespace(buf[end - 1])) {
				end --;
			}
			try {
				return Long.parseLong(new String(buf, 0, end));
			} catch (NumberFormatException nfe) {
				throw new IOException("Error parsing long number at position: " + (reader.getCurrentIndex() - len), nfe);
			}
		}
		//TODO: leading zero...
		return value;
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
		final int len = reader.getCurrentIndex() - reader.getTokenStart() - 1;
		long value = 0;
		char ch = buf[0];
		try {
			if (ch == '-') {
				int i = 1;
				for (; i < buf.length; i++) {
					ch = buf[i];
					if (i == len || ch == '.') break;
					value = (value << 3) + (value << 1) - Digit[ch - 48];
				}
				if (ch == '.') {
					i++;
					int dp = i;
					for (; i < buf.length; i++) {
						if (i == len) break;
						value = (value << 3) + (value << 1) - Digit[buf[i] - 48];
					}
					return BigDecimal.valueOf(value, len - dp);
				} else {
					return BigDecimal.valueOf(value);
				}
			} else {
				int i = 0;
				for (; i < buf.length; i++) {
					ch = buf[i];
					if (i == len || ch == '.') break;
					value = (value << 3) + (value << 1) + Digit[ch - 48];
				}
				if (ch == '.') {
					i++;
					int dp = i;
					for (; i < buf.length; i++) {
						if (i == len) break;
						value = (value << 3) + (value << 1) + Digit[buf[i] - 48];
					}
					return BigDecimal.valueOf(value, len - dp);
				} else {
					return BigDecimal.valueOf(value);
				}
			}
		} catch (ArrayIndexOutOfBoundsException ignore) {
			int end = len;
			while (end > 0 && Character.isWhitespace(buf[end - 1])) {
				end --;
			}
			try {
				return new BigDecimal(buf, 0, end);
			} catch (NumberFormatException nfe) {
				throw new IOException("Error parsing decimal number at position: " + (reader.getCurrentIndex() - len), nfe);
			}
		}
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
