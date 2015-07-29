package com.dslplatform.client.json.Integer;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class NullableSetOfOneIntegersDefaultValueTurtle {
    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.Set<Integer> defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<Integer> deserializedTmpList = jsonSerialization.deserializeList(Integer.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        final java.util.Set<Integer> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Integer>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertNullableSetOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<Integer> borderValue1 = new java.util.HashSet<Integer>(java.util.Arrays.asList(0));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<Integer> deserializedTmpList = jsonSerialization.deserializeList(Integer.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        final java.util.Set<Integer> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Integer>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertNullableSetOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<Integer> borderValue2 = new java.util.HashSet<Integer>(java.util.Arrays.asList(1000000000));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<Integer> deserializedTmpList = jsonSerialization.deserializeList(Integer.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        final java.util.Set<Integer> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Integer>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertNullableSetOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<Integer> borderValue3 = new java.util.HashSet<Integer>(java.util.Arrays.asList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, -1000000000, 1000000000));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<Integer> deserializedTmpList = jsonSerialization.deserializeList(Integer.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        final java.util.Set<Integer> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Integer>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertNullableSetOfOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}
