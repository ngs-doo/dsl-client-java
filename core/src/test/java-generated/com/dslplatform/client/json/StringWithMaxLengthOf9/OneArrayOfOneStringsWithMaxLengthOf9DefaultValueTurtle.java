package com.dslplatform.client.json.StringWithMaxLengthOf9;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneArrayOfOneStringsWithMaxLengthOf9DefaultValueTurtle {
	private static JsonSerialization jsonSerialization;

	@org.junit.BeforeClass
	public static void initializeJsonSerialization() throws IOException {
		jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
	}

	@org.junit.Test
	public void testDefaultValueEquality() throws IOException {
		final String[] defaultValue = new String[0];
		final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
		final String[] defaultValueJsonDeserialized = jsonSerialization.deserialize(String[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
		com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertOneArrayOfOneEquals(defaultValue, defaultValueJsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue1Equality() throws IOException {
		final String[] borderValue1 = new String[] { "" };
		final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
		final String[] borderValue1JsonDeserialized = jsonSerialization.deserialize(String[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertOneArrayOfOneEquals(borderValue1, borderValue1JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue2Equality() throws IOException {
		final String[] borderValue2 = new String[] { "xxxxxxxxx" };
		final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
		final String[] borderValue2JsonDeserialized = jsonSerialization.deserialize(String[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertOneArrayOfOneEquals(borderValue2, borderValue2JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue3Equality() throws IOException {
		final String[] borderValue3 = new String[] { "", "\"", "'/\\[](){}", "\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t", "xxxxxxxxx" };
		final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
		final String[] borderValue3JsonDeserialized = jsonSerialization.deserialize(String[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertOneArrayOfOneEquals(borderValue3, borderValue3JsonDeserialized);
	}
}
