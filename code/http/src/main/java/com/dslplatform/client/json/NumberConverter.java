package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;

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

	public static void serializeNullable(final Float value, final Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			sw.write(value.toString());
	}

	public static void serialize(final float value, final Writer sw) throws IOException {
		sw.write(Float.toString(value));
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

	public static void serializeNullable(final Long value, final Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			sw.write(value.toString());
	}

	public static void serialize(final long value, final Writer sw) throws IOException {
		sw.write(Long.toString(value));
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
}
