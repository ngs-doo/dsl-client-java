package com.dslplatform.client.json.Binary;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneListOfNullableBinariesDefaultValueTurtle {
	private static JsonSerialization jsonSerialization;

	@org.junit.BeforeClass
	public static void initializeJsonSerialization() throws IOException {
		jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
	}

	@org.junit.Test
	public void testDefaultValueEquality() throws IOException {
		final java.util.List<byte[]> defaultValue = new java.util.ArrayList<byte[]>(0);
		final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
		final java.util.List<byte[]> defaultValueJsonDeserialized = jsonSerialization.deserializeList(byte[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
		com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneListOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue1Equality() throws IOException {
		final java.util.List<byte[]> borderValue1 = new java.util.ArrayList<byte[]>(java.util.Arrays.asList((byte[]) null));
		final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
		final java.util.List<byte[]> borderValue1JsonDeserialized = jsonSerialization.deserializeList(byte[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneListOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue2Equality() throws IOException {
		final java.util.List<byte[]> borderValue2 = new java.util.ArrayList<byte[]>(java.util.Arrays.asList(new byte[0]));
		final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
		final java.util.List<byte[]> borderValue2JsonDeserialized = jsonSerialization.deserializeList(byte[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneListOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue3Equality() throws IOException {
		final java.util.List<byte[]> borderValue3 = new java.util.ArrayList<byte[]>(java.util.Arrays.asList(new byte[] { Byte.MIN_VALUE, 0, Byte.MAX_VALUE }));
		final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
		final java.util.List<byte[]> borderValue3JsonDeserialized = jsonSerialization.deserializeList(byte[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneListOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue4Equality() throws IOException {
		final java.util.List<byte[]> borderValue4 = new java.util.ArrayList<byte[]>(java.util.Arrays.asList(new byte[0], new byte[] { Byte.MIN_VALUE }, new byte[] { Byte.MIN_VALUE, 0 }, new byte[] { Byte.MIN_VALUE, 0, Byte.MAX_VALUE }));
		final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
		final java.util.List<byte[]> borderValue4JsonDeserialized = jsonSerialization.deserializeList(byte[].class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneListOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue5Equality() throws IOException {
		final java.util.List<byte[]> borderValue5 = new java.util.ArrayList<byte[]>(java.util.Arrays.asList((byte[]) null, new byte[0], new byte[] { Byte.MIN_VALUE }, new byte[] { Byte.MIN_VALUE, 0 }, new byte[] { Byte.MIN_VALUE, 0, Byte.MAX_VALUE }));
		final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
		final java.util.List<byte[]> borderValue5JsonDeserialized = jsonSerialization.deserializeList(byte[].class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneListOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
	}
}
