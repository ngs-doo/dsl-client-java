package com.dslplatform.client.json.Boolean;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneBooleanDefaultValueTurtle {
	private static JsonSerialization jsonSerialization;

	@org.junit.BeforeClass
	public static void initializeJsonSerialization() throws IOException {
		jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
	}

	@org.junit.Test
	public void testDefaultValueEquality() throws IOException {
		final boolean defaultValue = false;
		final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
		final boolean defaultValueJsonDeserialized = jsonSerialization.deserialize(boolean.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
		com.dslplatform.ocd.javaasserts.BooleanAsserts.assertOneEquals(defaultValue, defaultValueJsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue1Equality() throws IOException {
		final boolean borderValue1 = true;
		final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
		final boolean borderValue1JsonDeserialized = jsonSerialization.deserialize(boolean.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.BooleanAsserts.assertOneEquals(borderValue1, borderValue1JsonDeserialized);
	}
}
