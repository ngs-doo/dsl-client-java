package com.dslplatform.client.json.Long;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneSetOfNullableLongsDefaultValueTurtle {
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
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneSetOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<Long> borderValue1 = new java.util.HashSet<Long>(java.util.Arrays.asList((Long) null));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<Long> deserializedTmpList = jsonSerialization.deserializeList(Long.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        final java.util.Set<Long> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Long>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneSetOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<Long> borderValue2 = new java.util.HashSet<Long>(java.util.Arrays.asList(0L));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<Long> deserializedTmpList = jsonSerialization.deserializeList(Long.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        final java.util.Set<Long> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Long>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneSetOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<Long> borderValue3 = new java.util.HashSet<Long>(java.util.Arrays.asList(Long.MAX_VALUE));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<Long> deserializedTmpList = jsonSerialization.deserializeList(Long.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        final java.util.Set<Long> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Long>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneSetOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.util.Set<Long> borderValue4 = new java.util.HashSet<Long>(java.util.Arrays.asList(0L, 1L, 1000000000000000000L, -1000000000000000000L, Long.MIN_VALUE, Long.MAX_VALUE));
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.util.List<Long> deserializedTmpList = jsonSerialization.deserializeList(Long.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        final java.util.Set<Long> borderValue4JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Long>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneSetOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.util.Set<Long> borderValue5 = new java.util.HashSet<Long>(java.util.Arrays.asList((Long) null, 0L, 1L, 1000000000000000000L, -1000000000000000000L, Long.MIN_VALUE, Long.MAX_VALUE));
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final java.util.List<Long> deserializedTmpList = jsonSerialization.deserializeList(Long.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        final java.util.Set<Long> borderValue5JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Long>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneSetOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
