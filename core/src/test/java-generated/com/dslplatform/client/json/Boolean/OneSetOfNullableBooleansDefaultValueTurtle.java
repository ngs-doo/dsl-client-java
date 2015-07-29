package com.dslplatform.client.json.Boolean;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneSetOfNullableBooleansDefaultValueTurtle {
    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.Set<Boolean> defaultValue = new java.util.HashSet<Boolean>(0);
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<Boolean> deserializedTmpList = jsonSerialization.deserializeList(Boolean.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        final java.util.Set<Boolean> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Boolean>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertOneSetOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<Boolean> borderValue1 = new java.util.HashSet<Boolean>(java.util.Arrays.asList((Boolean) null));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<Boolean> deserializedTmpList = jsonSerialization.deserializeList(Boolean.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        final java.util.Set<Boolean> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Boolean>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertOneSetOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<Boolean> borderValue2 = new java.util.HashSet<Boolean>(java.util.Arrays.asList(false));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<Boolean> deserializedTmpList = jsonSerialization.deserializeList(Boolean.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        final java.util.Set<Boolean> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Boolean>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertOneSetOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<Boolean> borderValue3 = new java.util.HashSet<Boolean>(java.util.Arrays.asList(true));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<Boolean> deserializedTmpList = jsonSerialization.deserializeList(Boolean.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        final java.util.Set<Boolean> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Boolean>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertOneSetOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.util.Set<Boolean> borderValue4 = new java.util.HashSet<Boolean>(java.util.Arrays.asList(false, true));
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.util.List<Boolean> deserializedTmpList = jsonSerialization.deserializeList(Boolean.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        final java.util.Set<Boolean> borderValue4JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Boolean>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertOneSetOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.util.Set<Boolean> borderValue5 = new java.util.HashSet<Boolean>(java.util.Arrays.asList((Boolean) null, false, true));
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final java.util.List<Boolean> deserializedTmpList = jsonSerialization.deserializeList(Boolean.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        final java.util.Set<Boolean> borderValue5JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Boolean>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertOneSetOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
