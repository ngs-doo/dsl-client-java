package com.dslplatform.client.json.Binary;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneSetOfNullableBinariesDefaultValueTurtle {
    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.Set<byte[]> defaultValue = new java.util.HashSet<byte[]>(0);
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<byte[]> deserializedTmpList = jsonSerialization.deserializeList(byte[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        final java.util.Set<byte[]> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<byte[]>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneSetOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<byte[]> borderValue1 = new java.util.HashSet<byte[]>(java.util.Arrays.asList((byte[]) null));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<byte[]> deserializedTmpList = jsonSerialization.deserializeList(byte[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        final java.util.Set<byte[]> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<byte[]>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneSetOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<byte[]> borderValue2 = new java.util.HashSet<byte[]>(java.util.Arrays.asList(new byte[0]));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<byte[]> deserializedTmpList = jsonSerialization.deserializeList(byte[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        final java.util.Set<byte[]> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<byte[]>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneSetOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<byte[]> borderValue3 = new java.util.HashSet<byte[]>(java.util.Arrays.asList(new byte[] { Byte.MIN_VALUE, 0, Byte.MAX_VALUE }));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<byte[]> deserializedTmpList = jsonSerialization.deserializeList(byte[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        final java.util.Set<byte[]> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<byte[]>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneSetOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.util.Set<byte[]> borderValue4 = new java.util.HashSet<byte[]>(java.util.Arrays.asList(new byte[0], new byte[] { Byte.MIN_VALUE }, new byte[] { Byte.MIN_VALUE, 0 }, new byte[] { Byte.MIN_VALUE, 0, Byte.MAX_VALUE }));
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.util.List<byte[]> deserializedTmpList = jsonSerialization.deserializeList(byte[].class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        final java.util.Set<byte[]> borderValue4JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<byte[]>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneSetOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.util.Set<byte[]> borderValue5 = new java.util.HashSet<byte[]>(java.util.Arrays.asList((byte[]) null, new byte[0], new byte[] { Byte.MIN_VALUE }, new byte[] { Byte.MIN_VALUE, 0 }, new byte[] { Byte.MIN_VALUE, 0, Byte.MAX_VALUE }));
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final java.util.List<byte[]> deserializedTmpList = jsonSerialization.deserializeList(byte[].class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        final java.util.Set<byte[]> borderValue5JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<byte[]>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneSetOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
