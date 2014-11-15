package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;

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

	public static ArrayList<Double> deserializeDoubleNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(DoubleReader);
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

	public static ArrayList<Float> deserializeFloatNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(FloatReader);
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

	public static Integer deserializeInt(final JsonReader reader) throws IOException {
		return Integer.parseInt(reader.readShortValue());
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

	public static ArrayList<Integer> deserializeIntNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(IntReader);
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

	public static Long deserializeLong(final JsonReader reader) throws IOException {
		return Long.parseLong(reader.readShortValue());
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

	public static ArrayList<Long> deserializeLongNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(LongReader);
	}

	public static void serializeNullable(final BigDecimal value, final Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			sw.write(value.toPlainString());
	}

	public static void serialize(final BigDecimal value, final Writer sw) throws IOException {
		sw.write(value.toPlainString());
	}

	public static BigDecimal deserializeDecimal(final JsonReader reader) throws IOException {
		return new BigDecimal(reader.readShortValue());
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

	public static ArrayList<BigDecimal> deserializeDecimalNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(DecimalReader);
	}
}
