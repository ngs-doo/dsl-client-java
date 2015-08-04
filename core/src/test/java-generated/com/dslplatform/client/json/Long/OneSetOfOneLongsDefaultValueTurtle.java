package com.dslplatform.client.json.Long;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneSetOfOneLongsDefaultValueTurtle {
	private static JsonSerialization jsonSerialization;

	@org.junit.BeforeClass
	public static void initializeJsonSerialization() throws IOException {
		jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
	}

	@org.junit.Test
	public void testDefaultValueEquality() throws IOException {
		final java.util.Set<Long> defaultValue = new java.util.HashSet<Long>(0);
		final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
		final java.util.List<Long> deserializedTmpList = jsonSerialization.deserializeList(Long.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
		final java.util.Set<Long> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Long>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.LongAsserts.assertOneSetOfOneEquals(defaultValue, defaultValueJsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue1Equality() throws IOException {
		final java.util.Set<Long> borderValue1 = new java.util.HashSet<Long>(java.util.Arrays.asList(0L));
		final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
		final java.util.List<Long> deserializedTmpList = jsonSerialization.deserializeList(Long.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
		final java.util.Set<Long> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Long>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.LongAsserts.assertOneSetOfOneEquals(borderValue1, borderValue1JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue2Equality() throws IOException {
		final java.util.Set<Long> borderValue2 = new java.util.HashSet<Long>(java.util.Arrays.asList(Long.MAX_VALUE));
		final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
		final java.util.List<Long> deserializedTmpList = jsonSerialization.deserializeList(Long.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
		final java.util.Set<Long> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Long>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.LongAsserts.assertOneSetOfOneEquals(borderValue2, borderValue2JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue3Equality() throws IOException {
		final java.util.Set<Long> borderValue3 = new java.util.HashSet<Long>(java.util.Arrays.asList(0L, 1L, 1000000000000000000L, -1000000000000000000L, Long.MIN_VALUE, Long.MAX_VALUE));
		final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
		final java.util.List<Long> deserializedTmpList = jsonSerialization.deserializeList(Long.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
		final java.util.Set<Long> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Long>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.LongAsserts.assertOneSetOfOneEquals(borderValue3, borderValue3JsonDeserialized);
	}
}
