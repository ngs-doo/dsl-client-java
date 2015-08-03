package com.dslplatform.client.json.Map;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class NullableListOfNullableMapsDefaultValueTurtle {
    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.List<java.util.Map<String, String>> defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        @SuppressWarnings("unchecked")
        final java.util.List<java.util.Map<String, String>> defaultValueJsonDeserialized =
                (java.util.List<java.util.Map<String, String>>)
                (java.util.List<?>)
                jsonSerialization.deserializeList(java.util.Map.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.MapAsserts.assertNullableListOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.List<java.util.Map<String, String>> borderValue1 = new java.util.ArrayList<java.util.Map<String, String>>(java.util.Arrays.asList((java.util.Map<String, String>) null));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        @SuppressWarnings("unchecked")
        final java.util.List<java.util.Map<String, String>> borderValue1JsonDeserialized =
                (java.util.List<java.util.Map<String, String>>)
                (java.util.List<?>)
                jsonSerialization.deserializeList(java.util.Map.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.MapAsserts.assertNullableListOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.List<java.util.Map<String, String>> borderValue2 = new java.util.ArrayList<java.util.Map<String, String>>(java.util.Arrays.asList(new java.util.HashMap<String, String>(0)));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        @SuppressWarnings("unchecked")
        final java.util.List<java.util.Map<String, String>> borderValue2JsonDeserialized =
                (java.util.List<java.util.Map<String, String>>)
                (java.util.List<?>)
                jsonSerialization.deserializeList(java.util.Map.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.MapAsserts.assertNullableListOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.List<java.util.Map<String, String>> borderValue3 = new java.util.ArrayList<java.util.Map<String, String>>(java.util.Arrays.asList(new java.util.HashMap<String, String>() {{ put("", "empty"); put("a", "1"); put("b", "2"); put("c", "3"); }}));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        @SuppressWarnings("unchecked")
        final java.util.List<java.util.Map<String, String>> borderValue3JsonDeserialized =
                (java.util.List<java.util.Map<String, String>>)
                (java.util.List<?>)
                jsonSerialization.deserializeList(java.util.Map.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.MapAsserts.assertNullableListOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.util.List<java.util.Map<String, String>> borderValue4 = new java.util.ArrayList<java.util.Map<String, String>>(java.util.Arrays.asList(new java.util.HashMap<String, String>(0), new java.util.HashMap<String, String>() {{ put("a", "b"); }}, new java.util.HashMap<String, String>() {{ put("Quote: \", Solidus /", "Backslash: \\, Aphos: ', Brackets: [] () {}"); }}, new java.util.HashMap<String, String>() {{ put("", "empty"); put("a", "1"); put("b", "2"); put("c", "3"); }}));
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        @SuppressWarnings("unchecked")
        final java.util.List<java.util.Map<String, String>> borderValue4JsonDeserialized =
                (java.util.List<java.util.Map<String, String>>)
                (java.util.List<?>)
                jsonSerialization.deserializeList(java.util.Map.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.MapAsserts.assertNullableListOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.util.List<java.util.Map<String, String>> borderValue5 = new java.util.ArrayList<java.util.Map<String, String>>(java.util.Arrays.asList((java.util.Map<String, String>) null, new java.util.HashMap<String, String>(0), new java.util.HashMap<String, String>() {{ put("a", "b"); }}, new java.util.HashMap<String, String>() {{ put("Quote: \", Solidus /", "Backslash: \\, Aphos: ', Brackets: [] () {}"); }}, new java.util.HashMap<String, String>() {{ put("", "empty"); put("a", "1"); put("b", "2"); put("c", "3"); }}));
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        @SuppressWarnings("unchecked")
        final java.util.List<java.util.Map<String, String>> borderValue5JsonDeserialized =
                (java.util.List<java.util.Map<String, String>>)
                (java.util.List<?>)
                jsonSerialization.deserializeList(java.util.Map.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.MapAsserts.assertNullableListOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
