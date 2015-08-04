package com.dslplatform.client.json.Boolean;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class NullableArrayOfOneBooleansDefaultValueTurtle {
	private static JsonSerialization jsonSerialization;

	@org.junit.BeforeClass
	public static void initializeJsonSerialization() throws IOException {
		jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
	}

	@org.junit.Test
	public void testDefaultValueEquality() throws IOException {
		final boolean[] defaultValue = null;
		final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
		final boolean[] defaultValueJsonDeserialized = jsonSerialization.deserialize(boolean[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
		com.dslplatform.ocd.javaasserts.BooleanAsserts.assertNullableArrayOfOneEquals(defaultValue, defaultValueJsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue1Equality() throws IOException {
		final boolean[] borderValue1 = new boolean[] { false };
		final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
		final boolean[] borderValue1JsonDeserialized = jsonSerialization.deserialize(boolean[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.BooleanAsserts.assertNullableArrayOfOneEquals(borderValue1, borderValue1JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue2Equality() throws IOException {
		final boolean[] borderValue2 = new boolean[] { true };
		final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
		final boolean[] borderValue2JsonDeserialized = jsonSerialization.deserialize(boolean[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.BooleanAsserts.assertNullableArrayOfOneEquals(borderValue2, borderValue2JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue3Equality() throws IOException {
		final boolean[] borderValue3 = new boolean[] { false, true };
		final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
		final boolean[] borderValue3JsonDeserialized = jsonSerialization.deserialize(boolean[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.BooleanAsserts.assertNullableArrayOfOneEquals(borderValue3, borderValue3JsonDeserialized);
	}
}
