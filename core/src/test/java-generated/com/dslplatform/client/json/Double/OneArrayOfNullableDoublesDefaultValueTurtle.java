package com.dslplatform.client.json.Double;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneArrayOfNullableDoublesDefaultValueTurtle {
	private static JsonSerialization jsonSerialization;

	@org.junit.BeforeClass
	public static void initializeJsonSerialization() throws IOException {
		jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
	}

	@org.junit.Test
	public void testDefaultValueEquality() throws IOException {
		final Double[] defaultValue = new Double[0];
		final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
		final Double[] defaultValueJsonDeserialized = jsonSerialization.deserialize(Double[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
		com.dslplatform.ocd.javaasserts.DoubleAsserts.assertOneArrayOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue1Equality() throws IOException {
		final Double[] borderValue1 = new Double[] { null };
		final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
		final Double[] borderValue1JsonDeserialized = jsonSerialization.deserialize(Double[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.DoubleAsserts.assertOneArrayOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue2Equality() throws IOException {
		final Double[] borderValue2 = new Double[] { 0.0 };
		final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
		final Double[] borderValue2JsonDeserialized = jsonSerialization.deserialize(Double[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.DoubleAsserts.assertOneArrayOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue3Equality() throws IOException {
		final Double[] borderValue3 = new Double[] { Double.NaN };
		final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
		final Double[] borderValue3JsonDeserialized = jsonSerialization.deserialize(Double[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.DoubleAsserts.assertOneArrayOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue4Equality() throws IOException {
		final Double[] borderValue4 = new Double[] { 0.0, 1E-307, 9E307, -1.23456789012345E-10, 1.23456789012345E20, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN };
		final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
		final Double[] borderValue4JsonDeserialized = jsonSerialization.deserialize(Double[].class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.DoubleAsserts.assertOneArrayOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue5Equality() throws IOException {
		final Double[] borderValue5 = new Double[] { null, 0.0, 1E-307, 9E307, -1.23456789012345E-10, 1.23456789012345E20, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN };
		final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
		final Double[] borderValue5JsonDeserialized = jsonSerialization.deserialize(Double[].class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.DoubleAsserts.assertOneArrayOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
	}
}
