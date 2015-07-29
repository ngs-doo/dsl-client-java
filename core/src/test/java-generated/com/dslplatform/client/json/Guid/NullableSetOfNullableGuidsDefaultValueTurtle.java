package com.dslplatform.client.json.Guid;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class NullableSetOfNullableGuidsDefaultValueTurtle {
    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.Set<java.util.UUID> defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<java.util.UUID> deserializedTmpList = jsonSerialization.deserializeList(java.util.UUID.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        final java.util.Set<java.util.UUID> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.util.UUID>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.GuidAsserts.assertNullableSetOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<java.util.UUID> borderValue1 = new java.util.HashSet<java.util.UUID>(java.util.Arrays.asList((java.util.UUID) null));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<java.util.UUID> deserializedTmpList = jsonSerialization.deserializeList(java.util.UUID.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        final java.util.Set<java.util.UUID> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.util.UUID>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.GuidAsserts.assertNullableSetOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<java.util.UUID> borderValue2 = new java.util.HashSet<java.util.UUID>(java.util.Arrays.asList(java.util.UUID.randomUUID()));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<java.util.UUID> deserializedTmpList = jsonSerialization.deserializeList(java.util.UUID.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        final java.util.Set<java.util.UUID> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.util.UUID>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.GuidAsserts.assertNullableSetOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<java.util.UUID> borderValue3 = new java.util.HashSet<java.util.UUID>(java.util.Arrays.asList(new java.util.UUID(0L, 0L)));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<java.util.UUID> deserializedTmpList = jsonSerialization.deserializeList(java.util.UUID.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        final java.util.Set<java.util.UUID> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.util.UUID>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.GuidAsserts.assertNullableSetOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.util.Set<java.util.UUID> borderValue4 = new java.util.HashSet<java.util.UUID>(java.util.Arrays.asList(java.util.UUID.randomUUID(), java.util.UUID.fromString("1-2-3-4-5"), new java.util.UUID(0L, 0L)));
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.util.List<java.util.UUID> deserializedTmpList = jsonSerialization.deserializeList(java.util.UUID.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        final java.util.Set<java.util.UUID> borderValue4JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.util.UUID>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.GuidAsserts.assertNullableSetOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.util.Set<java.util.UUID> borderValue5 = new java.util.HashSet<java.util.UUID>(java.util.Arrays.asList((java.util.UUID) null, java.util.UUID.randomUUID(), java.util.UUID.fromString("1-2-3-4-5"), new java.util.UUID(0L, 0L)));
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final java.util.List<java.util.UUID> deserializedTmpList = jsonSerialization.deserializeList(java.util.UUID.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        final java.util.Set<java.util.UUID> borderValue5JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.util.UUID>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.GuidAsserts.assertNullableSetOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
