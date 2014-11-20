package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;

public class NumberConverter {
	public static void serializeNullable(final Double value, final Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else 
			sw.write(value.toString());
	}

	public static void serialize(final double value, final Writer sw) throws IOException {
		sw.write(Double.toString(value));
	}

	public static Double deserializeDouble(final JsonReader reader) throws IOException {
		return Double.parseDouble(reader.readShortValue());
	}

	private static JsonReader.ReadObject<Double> DoubleReader = new JsonReader.ReadObject<Double>() {
		@Override
		public Double read(JsonReader reader) throws IOException {
			return deserializeDouble(reader);
		}
	};

	public static ArrayList<Double> deserializeDoubleCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(DoubleReader);
	}

	public static void deserializeDoubleCollection(final JsonReader reader, final Collection<Double> res) throws IOException {
		reader.deserializeCollection(DoubleReader, res);
	}

	public static ArrayList<Double> deserializeDoubleNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(DoubleReader);
	}

	public static void deserializeDoubleNullableCollection(final JsonReader reader, final Collection<Double> res) throws IOException {
		reader.deserializeNullableCollection(DoubleReader, res);
	}

	public static void serializeNullable(final Float value, final Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			sw.write(value.toString());
	}

	public static void serialize(final float value, final Writer sw) throws IOException {
		sw.write(Float.toString(value));
	}

	public static Float deserializeFloat(final JsonReader reader) throws IOException {
		return Float.parseFloat(reader.readShortValue());
	}

	private static JsonReader.ReadObject<Float> FloatReader = new JsonReader.ReadObject<Float>() {
		@Override
		public Float read(JsonReader reader) throws IOException {
			return deserializeFloat(reader);
		}
	};

	public static ArrayList<Float> deserializeFloatCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(FloatReader);
	}

	public static void deserializeFloatCollection(final JsonReader reader, Collection<Float> res) throws IOException {
		reader.deserializeCollection(FloatReader, res);
	}

	public static ArrayList<Float> deserializeFloatNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(FloatReader);
	}

	public static void deserializeFloatNullableCollection(final JsonReader reader, final Collection<Float> res) throws IOException {
		reader.deserializeNullableCollection(FloatReader, res);
	}

	public static void serializeNullable(final Integer value, final Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			sw.write(value.toString());
	}

	public static void serialize(final int value, final Writer sw) throws IOException {
		sw.write(Integer.toString(value));
	}

	public static int deserializeInt(final JsonReader reader) throws IOException {
		char[] buf = reader.readNumber();
		int value = 0;
		int len = reader.getCurrentIndex() - reader.getTokenStart() - 1;
		char ch;
		int start = buf[0] == '-' ? 1 : 0;
		for(int i = start; i < len && i < buf.length; i++) {
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
		return reader.deserializeCollection(IntReader);
	}

	public static void deserializeIntCollection(final JsonReader reader, final Collection<Integer> res) throws IOException {
		reader.deserializeCollection(IntReader, res);
	}

	public static ArrayList<Integer> deserializeIntNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(IntReader);
	}

	public static void deserializeIntNullableCollection(final JsonReader reader, final Collection<Integer> res) throws IOException {
		reader.deserializeNullableCollection(IntReader, res);
	}

	public static void serializeNullable(final Long value, final Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			sw.write(value.toString());
	}

	public static void serialize(final long value, final Writer sw) throws IOException {
		sw.write(Long.toString(value));
	}

	public static long deserializeLong(final JsonReader reader) throws IOException {
		char[] buf = reader.readNumber();
		long value = 0;
		int len = reader.getCurrentIndex() - reader.getTokenStart() - 1;
		char ch;
		int start = buf[0] == '-' ? 1 : 0;
		for(int i = 0; i < len && i < buf.length; i++) {
			ch = buf[i];
			if (ch >= '0' && ch <= '9') {
				value = value * 10 + ch - 48;
			} else return Integer.parseInt(new String(buf, 0, len));
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
		return reader.deserializeCollection(LongReader);
	}

	public static void deserializeLongCollection(final JsonReader reader, final Collection<Long> res) throws IOException {
		reader.deserializeCollection(LongReader, res);
	}

	public static ArrayList<Long> deserializeLongNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(LongReader);
	}

	public static void deserializeLongNullableCollection(final JsonReader reader, final Collection<Long> res) throws IOException {
		reader.deserializeNullableCollection(LongReader, res);
	}

	public static void serializeNullable(final BigDecimal value, final Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			sw.write(value.toString());
	}

	public static void serialize(final BigDecimal value, final Writer sw) throws IOException {
		sw.write(value.toString());
	}

	public static BigDecimal deserializeDecimal(final JsonReader reader) throws IOException {
		char[] buf = reader.readNumber();
		return new BigDecimal(buf, 0, reader.getCurrentIndex() - reader.getTokenStart() - 1);
	}

	private static JsonReader.ReadObject<BigDecimal> DecimalReader = new JsonReader.ReadObject<BigDecimal>() {
		@Override
		public BigDecimal read(JsonReader reader) throws IOException {
			return deserializeDecimal(reader);
		}
	};

	public static ArrayList<BigDecimal> deserializeDecimalCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(DecimalReader);
	}

	public static void deserializeDecimalCollection(final JsonReader reader, final Collection<BigDecimal> res) throws IOException {
		reader.deserializeCollection(DecimalReader, res);
	}

	public static ArrayList<BigDecimal> deserializeDecimalNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(DecimalReader);
	}

	public static void deserializeDecimalNullableCollection(final JsonReader reader, final Collection<BigDecimal> res) throws IOException {
		reader.deserializeNullableCollection(DecimalReader, res);
	}
}
