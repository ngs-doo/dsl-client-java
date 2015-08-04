package com.dslplatform.client.json.Integer;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneSetOfNullableIntegersDefaultValueTurtle {
	private static JsonSerialization jsonSerialization;

	@org.junit.BeforeClass
	public static void initializeJsonSerialization() throws IOException {
		jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
	}

	@org.junit.Test
	public void testDefaultValueEquality() throws IOException {
		final java.util.Set<Integer> defaultValue = new java.util.HashSet<Integer>(0);
		final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
		final java.util.List<Integer> deserializedTmpList = jsonSerialization.deserializeList(Integer.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
		final java.util.Set<Integer> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Integer>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneSetOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue1Equality() throws IOException {
		final java.util.Set<Integer> borderValue1 = new java.util.HashSet<Integer>(java.util.Arrays.asList((Integer) null));
		final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
		final java.util.List<Integer> deserializedTmpList = jsonSerialization.deserializeList(Integer.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
		final java.util.Set<Integer> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Integer>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneSetOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue2Equality() throws IOException {
		final java.util.Set<Integer> borderValue2 = new java.util.HashSet<Integer>(java.util.Arrays.asList(0));
		final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
		final java.util.List<Integer> deserializedTmpList = jsonSerialization.deserializeList(Integer.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
		final java.util.Set<Integer> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Integer>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneSetOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue3Equality() throws IOException {
		final java.util.Set<Integer> borderValue3 = new java.util.HashSet<Integer>(java.util.Arrays.asList(1000000000));
		final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
		final java.util.List<Integer> deserializedTmpList = jsonSerialization.deserializeList(Integer.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
		final java.util.Set<Integer> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Integer>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneSetOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue4Equality() throws IOException {
		final java.util.Set<Integer> borderValue4 = new java.util.HashSet<Integer>(java.util.Arrays.asList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, -1000000000, 1000000000));
		final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
		final java.util.List<Integer> deserializedTmpList = jsonSerialization.deserializeList(Integer.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
		final java.util.Set<Integer> borderValue4JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Integer>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneSetOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue5Equality() throws IOException {
		final java.util.Set<Integer> borderValue5 = new java.util.HashSet<Integer>(java.util.Arrays.asList((Integer) null, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, -1000000000, 1000000000));
		final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
		final java.util.List<Integer> deserializedTmpList = jsonSerialization.deserializeList(Integer.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
		final java.util.Set<Integer> borderValue5JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Integer>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneSetOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
	}
}
