package com.dslplatform.client.json.Binary;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class NullableSetOfOneBinariesDefaultValueTurtle {
    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.Set<byte[]> defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<byte[]> deserializedTmpList = jsonSerialization.deserializeList(byte[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        final java.util.Set<byte[]> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<byte[]>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertNullableSetOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<byte[]> borderValue1 = new java.util.HashSet<byte[]>(java.util.Arrays.asList(new byte[0]));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<byte[]> deserializedTmpList = jsonSerialization.deserializeList(byte[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        final java.util.Set<byte[]> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<byte[]>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertNullableSetOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<byte[]> borderValue2 = new java.util.HashSet<byte[]>(java.util.Arrays.asList(new byte[] { Byte.MIN_VALUE, 0, Byte.MAX_VALUE }));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<byte[]> deserializedTmpList = jsonSerialization.deserializeList(byte[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        final java.util.Set<byte[]> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<byte[]>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertNullableSetOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<byte[]> borderValue3 = new java.util.HashSet<byte[]>(java.util.Arrays.asList(new byte[0], new byte[] { Byte.MIN_VALUE }, new byte[] { Byte.MIN_VALUE, 0 }, new byte[] { Byte.MIN_VALUE, 0, Byte.MAX_VALUE }));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<byte[]> deserializedTmpList = jsonSerialization.deserializeList(byte[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        final java.util.Set<byte[]> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<byte[]>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertNullableSetOfOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}
