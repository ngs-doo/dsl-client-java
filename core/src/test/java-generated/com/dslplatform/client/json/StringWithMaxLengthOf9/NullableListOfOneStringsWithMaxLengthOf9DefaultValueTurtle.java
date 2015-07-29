package com.dslplatform.client.json.StringWithMaxLengthOf9;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class NullableListOfOneStringsWithMaxLengthOf9DefaultValueTurtle {
    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.List<String> defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<String> defaultValueJsonDeserialized = jsonSerialization.deserializeList(String.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertNullableListOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.List<String> borderValue1 = new java.util.ArrayList<String>(java.util.Arrays.asList(""));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<String> borderValue1JsonDeserialized = jsonSerialization.deserializeList(String.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertNullableListOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.List<String> borderValue2 = new java.util.ArrayList<String>(java.util.Arrays.asList("xxxxxxxxx"));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<String> borderValue2JsonDeserialized = jsonSerialization.deserializeList(String.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertNullableListOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.List<String> borderValue3 = new java.util.ArrayList<String>(java.util.Arrays.asList("", "\"", "'/\\[](){}", "\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t", "xxxxxxxxx"));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<String> borderValue3JsonDeserialized = jsonSerialization.deserializeList(String.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertNullableListOfOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}