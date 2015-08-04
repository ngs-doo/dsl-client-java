package com.dslplatform.client.json.Float;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class NullableFloatDefaultValueTurtle {
	private static JsonSerialization jsonSerialization;

	@org.junit.BeforeClass
	public static void initializeJsonSerialization() throws IOException {
		jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
	}

	@org.junit.Test
	public void testDefaultValueEquality() throws IOException {
		final Float defaultValue = null;
		final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
		final Float defaultValueJsonDeserialized = jsonSerialization.deserialize(Float.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
		com.dslplatform.ocd.javaasserts.FloatAsserts.assertNullableEquals(defaultValue, defaultValueJsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue1Equality() throws IOException {
		final Float borderValue1 = 0.0f;
		final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
		final Float borderValue1JsonDeserialized = jsonSerialization.deserialize(Float.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.FloatAsserts.assertNullableEquals(borderValue1, borderValue1JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue2Equality() throws IOException {
		final Float borderValue2 = -1.2345E-10f;
		final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
		final Float borderValue2JsonDeserialized = jsonSerialization.deserialize(Float.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.FloatAsserts.assertNullableEquals(borderValue2, borderValue2JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue3Equality() throws IOException {
		final Float borderValue3 = 1.2345E20f;
		final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
		final Float borderValue3JsonDeserialized = jsonSerialization.deserialize(Float.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.FloatAsserts.assertNullableEquals(borderValue3, borderValue3JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue4Equality() throws IOException {
		final Float borderValue4 = -1E-5f;
		final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
		final Float borderValue4JsonDeserialized = jsonSerialization.deserialize(Float.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.FloatAsserts.assertNullableEquals(borderValue4, borderValue4JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue5Equality() throws IOException {
		final Float borderValue5 = Float.NaN;
		final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
		final Float borderValue5JsonDeserialized = jsonSerialization.deserialize(Float.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.FloatAsserts.assertNullableEquals(borderValue5, borderValue5JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue6Equality() throws IOException {
		final Float borderValue6 = Float.NEGATIVE_INFINITY;
		final Bytes borderValue6JsonSerialized = jsonSerialization.serialize(borderValue6);
		final Float borderValue6JsonDeserialized = jsonSerialization.deserialize(Float.class, borderValue6JsonSerialized.content, borderValue6JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.FloatAsserts.assertNullableEquals(borderValue6, borderValue6JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue7Equality() throws IOException {
		final Float borderValue7 = Float.POSITIVE_INFINITY;
		final Bytes borderValue7JsonSerialized = jsonSerialization.serialize(borderValue7);
		final Float borderValue7JsonDeserialized = jsonSerialization.deserialize(Float.class, borderValue7JsonSerialized.content, borderValue7JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.FloatAsserts.assertNullableEquals(borderValue7, borderValue7JsonDeserialized);
	}
}
