package com.dslplatform.client.json.Float;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneArrayOfOneFloatsDefaultValueTurtle {
	private static JsonSerialization jsonSerialization;

	@org.junit.BeforeClass
	public static void initializeJsonSerialization() throws IOException {
		jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
	}

	@org.junit.Test
	public void testDefaultValueEquality() throws IOException {
		final float[] defaultValue = new float[0];
		final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
		final float[] defaultValueJsonDeserialized = jsonSerialization.deserialize(float[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
		com.dslplatform.ocd.javaasserts.FloatAsserts.assertOneArrayOfOneEquals(defaultValue, defaultValueJsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue1Equality() throws IOException {
		final float[] borderValue1 = new float[] { 0.0f };
		final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
		final float[] borderValue1JsonDeserialized = jsonSerialization.deserialize(float[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.FloatAsserts.assertOneArrayOfOneEquals(borderValue1, borderValue1JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue2Equality() throws IOException {
		final float[] borderValue2 = new float[] { Float.POSITIVE_INFINITY };
		final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
		final float[] borderValue2JsonDeserialized = jsonSerialization.deserialize(float[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.FloatAsserts.assertOneArrayOfOneEquals(borderValue2, borderValue2JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue3Equality() throws IOException {
		final float[] borderValue3 = new float[] { 0.0f, -1.2345E-10f, 1.2345E20f, -1E-5f, Float.NaN, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY };
		final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
		final float[] borderValue3JsonDeserialized = jsonSerialization.deserialize(float[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.FloatAsserts.assertOneArrayOfOneEquals(borderValue3, borderValue3JsonDeserialized);
	}
}
