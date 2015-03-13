package com.dslplatform.client.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class NumberConverter {

	private final static int[] Digits = new int[100];

	static {
		for (int i = 0; i < 100; i++) {
			Digits[i] = (i < 10 ? 1 << 16 : 0) + (((i / 10) + '0') << 8) + i % 10 + '0';
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
			serialize(value.doubleValue(), sw);
		}
	}

	private static BigDecimal parseNumberGeneric(final char[] buf, final int len, final int position) throws IOException {
		int end = len;
		while (end > 0 && Character.isWhitespace(buf[end - 1])) {
			end--;
		}
		try {
			return new BigDecimal(buf, 0, end);
		} catch (NumberFormatException nfe) {
			throw new IOException("Error parsing number at position: " + (position - len), nfe);
		}
	}

	public static void serialize(final double value, final JsonWriter sw) {
		if (Double.isNaN(value)) {
			sw.writeAscii("\"NaN\"");
		} else if (Double.isInfinite(value)) {
			final long bits = Double.doubleToLongBits(value);
			if((bits & -9223372036854775808L) != 0L) {
				sw.writeAscii("\"-Infinity\"");
			} else {
				sw.writeAscii("\"Infinity\"");
			}
		} else sw.writeAscii(Double.toString(value));
	}

	private static char[] readLongNumber(final JsonReader reader, final char[] buf) throws IOException {
		char ch;
		int i = 0;
		for (; i < buf.length; i++) {
			ch = buf[i];
			if (!(ch >= '0' && ch < '9' || ch == '-' || ch == '+' || ch == '.' || ch == 'e' || ch == 'E')) {
				return buf;
			}
		}
		char[] tmp = Arrays.copyOf(buf, buf.length * 2);
		do {
			do {
				ch = (char) reader.read();
				tmp[i++] = ch;
				if (!(ch >= '0' && ch < '9' || ch == '-' || ch == '+' || ch == '.' || ch == 'e' || ch == 'E')) {
					return Arrays.copyOf(tmp, i - 1);
				}
			} while (i < tmp.length);
			tmp = Arrays.copyOf(tmp, tmp.length * 2);
		} while (true);
	}

	public static double deserializeDouble(final JsonReader reader) throws IOException {
		if (reader.last() == '"') {
			final int position = reader.getCurrentIndex();
			final char[] buf = reader.readSimpleQuote();
			double result = parseDoubleGeneric(buf, reader.getCurrentIndex() - position - 1, position + 1);
			reader.read();
			return result;
		}
		final char[] buf = reader.readNumber();
		final int position = reader.getCurrentIndex();
		final int len = position - reader.getTokenStart() - 1;
		if (len > 18) {
			if (len == buf.length - 1) {
				final char[] tmp = readLongNumber(reader, buf);
				return parseDoubleGeneric(tmp, tmp.length, position);
			} else {
				return parseDoubleGeneric(buf, len, position);
			}
		}
		char ch = buf[0];
		if (ch == '-') {
			return parseNegativeDouble(buf, position, len, 1);
		} else if (ch == '+') {
			return parsePositiveDouble(buf, position, len, 1);
		}
		return parsePositiveDouble(buf, position, len, 0);
	}

	private static double parsePositiveDouble(final char[] buf, final int position, final int len, int i) throws IOException {
		long value = 0;
		char ch = ' ';
		for (; i < buf.length; i++) {
			if (i == len) return value;
			ch = buf[i];
			if (ch == '.') break;
			final int ind = buf[i] - 48;
			if (ind >= 0 && ind < 10) {
				value = (value << 3) + (value << 1) + ind;
			} else {
				return parseDoubleGeneric(buf, len, position);
			}
		}
		if (ch == '.') {
			i++;
			int div = 1;
			for (; i < buf.length; i++) {
				if (i == len) break;
				final int ind = buf[i] - 48;
				if (ind >= 0 && ind < 10) {
					div = (div << 3) + (div << 1);
					value = (value << 3) + (value << 1) + ind;
				} else {
					return parseDoubleGeneric(buf, len, position);
				}
			}
			return value / (double) div;
		}
		return value;
	}

	private static double parseNegativeDouble(final char[] buf, final int position, final int len, int i) throws IOException {
		long value = 0;
		char ch = ' ';
		for (; i < buf.length; i++) {
			if (i == len) return value;
			ch = buf[i];
			if (ch == '.') break;
			final int ind = buf[i] - 48;
			if (ind >= 0 && ind < 10) {
				value = (value << 3) + (value << 1) - ind;
			} else {
				return parseDoubleGeneric(buf, len, position);
			}
		}
		if (ch == '.') {
			i++;
			int div = 1;
			for (; i < buf.length; i++) {
				if (i == len) break;
				final int ind = buf[i] - 48;
				if (ind >= 0 && ind < 10) {
					div = (div << 3) + (div << 1);
					value = (value << 3) + (value << 1) - ind;
				} else {
					return parseDoubleGeneric(buf, len, position);
				}
			}
			return value / (double) div;
		}
		return value;
	}

	private static double parseDoubleGeneric(final char[] buf, final int len, final int position) throws IOException {
		int end = len;
		while (end > 0 && Character.isWhitespace(buf[end - 1])) {
			end--;
		}
		try {
			return Double.parseDouble(new String(buf, 0, end));
		} catch (NumberFormatException nfe) {
			throw new IOException("Error parsing float number at position: " + (position - len), nfe);
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
			serialize(value.floatValue(), sw);
		}
	}

	public static void serialize(final float value, final JsonWriter sw) {
		if (Float.isNaN(value)) {
			sw.writeAscii("\"NaN\"");
		} else if (Float.isInfinite(value)) {
			final int bits = Float.floatToIntBits(value);
			if ((bits & -2147483648) != 0) {
				sw.writeAscii("\"-Infinity\"");
			} else {
				sw.writeAscii("\"Infinity\"");
			}
		} else sw.writeAscii(Float.toString(value)); //TODO: better implementation required
	}

	public static float deserializeFloat(final JsonReader reader) throws IOException {
		if (reader.last() == '"') {
			final int position = reader.getCurrentIndex();
			final char[] buf = reader.readSimpleQuote();
			float result = parseFloatGeneric(buf, reader.getCurrentIndex() - position - 1, position + 1);
			reader.read();
			return result;
		}
		final char[] buf = reader.readNumber();
		final int position = reader.getCurrentIndex();
		final int len = reader.getCurrentIndex() - reader.getTokenStart() - 1;
		if (len > 18) {
			if (len == buf.length - 1) {
				final char[] tmp = readLongNumber(reader, buf);
				return parseFloatGeneric(tmp, tmp.length, position);
			} else {
				return parseFloatGeneric(buf, len, position);
			}
		}
		char ch = buf[0];
		if (ch == '-') {
			return parseNegativeFloat(buf, position, len, 1);
		} else if (ch == '+') {
			return parsePositiveFloat(buf, position, len, 1);
		}
		return parsePositiveFloat(buf, position, len, 0);
	}

	private static float parsePositiveFloat(final char[] buf, final int position, final int len, int i) throws IOException {
		long value = 0;
		char ch = ' ';
		for (; i < buf.length; i++) {
			if (i == len) return value;
			ch = buf[i];
			if (ch == '.') break;
			final int ind = buf[i] - 48;
			if (ind >= 0 && ind < 10) {
				value = (value << 3) + (value << 1) + ind;
			} else {
				return parseFloatGeneric(buf, len, position);
			}
		}
		if (ch == '.') {
			i++;
			int div = 1;
			for (; i < buf.length; i++) {
				if (i == len) break;
				final int ind = buf[i] - 48;
				if (ind >= 0 && ind < 10) {
					div = (div << 3) + (div << 1);
					value = (value << 3) + (value << 1) + ind;
				} else {
					return parseFloatGeneric(buf, len, position);
				}
			}
			return value / (float) div;
		}
		return value;
	}

	private static float parseNegativeFloat(final char[] buf, final int position, final int len, int i) throws IOException {
		long value = 0;
		char ch = ' ';
		for (; i < buf.length; i++) {
			if (i == len) return value;
			ch = buf[i];
			if (ch == '.') break;
			final int ind = buf[i] - 48;
			if (ind >= 0 && ind < 10) {
				value = (value << 3) + (value << 1) - ind;
			} else {
				return parseFloatGeneric(buf, len, position);
			}
		}
		if (ch == '.') {
			i++;
			int div = 1;
			for (; i < buf.length; i++) {
				if (i == len) break;
				final int ind = buf[i] - 48;
				if (ind >= 0 && ind < 10) {
					div = (div << 3) + (div << 1);
					value = (value << 3) + (value << 1) - ind;
				} else {
					return parseFloatGeneric(buf, len, position);
				}
			}
			return value / (float) div;
		}
		return value;
	}

	private static float parseFloatGeneric(final char[] buf, final int len, final int position) throws IOException {
		int end = len;
		while (end > 0 && Character.isWhitespace(buf[end - 1])) {
			end--;
		}
		try {
			return Float.parseFloat(new String(buf, 0, end));
		} catch (NumberFormatException nfe) {
			throw new IOException("Error parsing float number at position: " + (position - len), nfe);
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
			serialize(value.intValue(), sw);
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

			int v = 0;
			while (charPos > 1) {
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
		final int position = reader.getCurrentIndex();
		final int len = position - reader.getTokenStart() - 1;
		final char ch = buf[0];
		if (ch == '-') {
			return parseNegativeInt(buf, position, len, 1);
		} else if (ch == '+') {
			return parsePositiveInt(buf, position, len, 1);
		}
		return parsePositiveInt(buf, position, len, 0);
	}

	private static int parsePositiveInt(final char[] buf, final int position, final int len, int i) throws IOException {
		int value = 0;
		for (; i < buf.length; i++) {
			if (i == len) break;
			final int ind = buf[i] - 48;
			if (ind >= 0 && ind < 10) {
				value = (value << 3) + (value << 1) + ind;
			} else {
				BigDecimal v = parseNumberGeneric(buf, len, position);
				if (v.scale() <= 0) return v.intValue();
				throw new IOException("Error parsing int number at position: " + (position - len) + ". Found decimal value: " + v);
			}
		}
		return value;
	}

	private static int parseNegativeInt(final char[] buf, final int position, final int len, int i) throws IOException {
		int value = 0;
		for (; i < buf.length; i++) {
			if (i == len) break;
			final int ind = buf[i] - 48;
			if (ind >= 0 && ind < 10) {
				value = (value << 3) + (value << 1) - ind;
			} else {
				BigDecimal v = parseNumberGeneric(buf, len, position);
				if (v.scale() <= 0) return v.intValue();
				throw new IOException("Error parsing int number at position: " + (position - len) + ". Found decimal value: " + v);
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
			serialize(value.longValue(), sw);
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

			int v = 0;
			while (charPos > 1) {
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
		final int position = reader.getCurrentIndex();
		final int len = position - reader.getTokenStart() - 1;
		final char ch = buf[0];
		if (ch == '-') {
			return parseNegativeLong(buf, position, len, 1);
		} else if (ch == '+') {
			return parsePositiveLong(buf, position, len, 1);
		}
		return parsePositiveLong(buf, position, len, 0);
	}

	private static long parsePositiveLong(final char[] buf, final int position, final int len, int i) throws IOException {
		long value = 0;
		for (; i < buf.length; i++) {
			if (i == len) break;
			final int ind = buf[i] - 48;
			if (ind >= 0 && ind < 10) {
				value = (value << 3) + (value << 1) + ind;
			} else {
				BigDecimal v = parseNumberGeneric(buf, len, position);
				if (v.scale() <= 0) return v.longValue();
				throw new IOException("Error parsing long number at position: " + (position - len) + ". Found decimal value: " + v);
			}
		}
		return value;
	}

	private static long parseNegativeLong(final char[] buf, final int position, final int len, int i) throws IOException {
		long value = 0;
		for (; i < buf.length; i++) {
			if (i == len) break;
			final int ind = buf[i] - 48;
			if (ind >= 0 && ind < 10) {
				value = (value << 3) + (value << 1) - ind;
			} else {
				BigDecimal v = parseNumberGeneric(buf, len, position);
				if (v.scale() <= 0) return v.longValue();
				throw new IOException("Error parsing long number at position: " + (position - len) + ". Found decimal value: " + v);
			}
		}
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
		final int position = reader.getCurrentIndex();
		final int len = position - reader.getTokenStart() - 1;
		if (len > 18) {
			if (len == buf.length - 1) {
				final char[] tmp = readLongNumber(reader, buf);
				return parseNumberGeneric(tmp, tmp.length, position);
			} else {
				return parseNumberGeneric(buf, len, position);
			}
		}
		final char ch = buf[0];
		if (ch == '-') {
			return parseNegativeDecimal(buf, position, len, 1);
		} else if (ch == '+') {
			return parsePositiveDecimal(buf, position, len, 1);
		}
		return parsePositiveDecimal(buf, position, len, 0);
	}

	private static BigDecimal parsePositiveDecimal(final char[] buf, final int position, final int len, int i) throws IOException {
		long value = 0;
		char ch = ' ';
		for (; i < buf.length; i++) {
			if (i == len) return BigDecimal.valueOf(value);
			ch = buf[i];
			if (ch == '.' || ch == 'e' || ch == 'E') break;
			final int ind = ch - 48;
			if (ind >= 0 && ind < 10) {
				value = (value << 3) + (value << 1) + ind;
			} else {
				return parseNumberGeneric(buf, len, position);
			}
		}
		if (ch == '.') {
			i++;
			int dp = i;
			for (; i < buf.length; i++) {
				if (i == len) return BigDecimal.valueOf(value, len - dp);
				ch = buf[i];
				if (ch == 'e' || ch == 'E') break;
				final int ind = ch - 48;
				if (ind >= 0 && ind < 10) {
					value = (value << 3) + (value << 1) + ind;
				} else {
					return parseNumberGeneric(buf, len, position);
				}
			}
			if (ch == 'e' || ch == 'E') {
				final int ep = i;
				i++;
				ch = buf[i];
				final int exp;
				if (ch == '-') {
					exp = parseNegativeInt(buf, position, len, i + 1);
				} else if (ch == '+') {
					exp = parsePositiveInt(buf, position, len, i + 1);
				} else {
					exp = parsePositiveInt(buf, position, len, i);
				}
				return BigDecimal.valueOf(value, ep - dp - exp);
			}
			return BigDecimal.valueOf(value, len - dp);
		} else if (ch == 'e' || ch == 'E') {
			i++;
			ch = buf[i];
			final int exp;
			if (ch == '-') {
				exp = parseNegativeInt(buf, position, len, i + 1);
			} else if (ch == '+') {
				exp = parsePositiveInt(buf, position, len, i + 1);
			} else {
				exp = parsePositiveInt(buf, position, len, i);
			}
			return BigDecimal.valueOf(value, -exp);
		}
		return BigDecimal.valueOf(value);
	}

	private static BigDecimal parseNegativeDecimal(final char[] buf, final int position, final int len, int i) throws IOException {
		long value = 0;
		char ch = ' ';
		for (; i < buf.length; i++) {
			if (i == len) return BigDecimal.valueOf(value);
			ch = buf[i];
			if (ch == '.' || ch == 'e' || ch == 'E') break;
			final int ind = ch - 48;
			if (ind >= 0 && ind < 10) {
				value = (value << 3) + (value << 1) - ind;
			} else {
				return parseNumberGeneric(buf, len, position);
			}
		}
		if (ch == '.') {
			i++;
			int dp = i;
			for (; i < buf.length; i++) {
				if (i == len) return BigDecimal.valueOf(value, len - dp);
				ch = buf[i];
				if (ch == 'e' || ch == 'E') break;
				final int ind = ch - 48;
				if (ind >= 0 && ind < 10) {
					value = (value << 3) + (value << 1) - ind;
				} else {
					return parseNumberGeneric(buf, len, position);
				}
			}
			if (ch == 'e' || ch == 'E') {
				final int ep = i;
				i++;
				ch = buf[i];
				final int exp;
				if (ch == '-') {
					exp = parseNegativeInt(buf, position, len, i + 1);
				} else if (ch == '+') {
					exp = parsePositiveInt(buf, position, len, i + 1);
				} else {
					exp = parsePositiveInt(buf, position, len, i);
				}
				return BigDecimal.valueOf(value, ep - dp - exp);
			}
			return BigDecimal.valueOf(value, len - dp);
		} else if (ch == 'e' || ch == 'E') {
			i++;
			ch = buf[i];
			final int exp;
			if (ch == '-') {
				exp = parseNegativeInt(buf, position, len, i + 1);
			} else if (ch == '+') {
				exp = parsePositiveInt(buf, position, len, i + 1);
			} else {
				exp = parsePositiveInt(buf, position, len, i);
			}
			return BigDecimal.valueOf(value, -exp);
		}
		return BigDecimal.valueOf(value);
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
