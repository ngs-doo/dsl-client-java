package com.dslplatform.ocd.test.javatest.property.turtles.Map;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class NullableSetOfOneMapsDefaultValueTurtle {

    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        final ServiceLocator locator = Bootstrap.init(new java.io.ByteArrayInputStream(
                "username=unused\nproject-id=unused\napi-url=unused\npackage-name=unused".getBytes("UTF-8")));
        jsonSerialization = locator.resolve(JsonSerialization.class);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.Set<java.util.Map<String, String>> defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.Set<java.util.Map<String, String>> defaultValueJsonDeserialized = new java.util.HashSet<java.util.Map<String, String>>((java.util.List<java.util.Map<String, String>>) (java.util.List<?>) jsonSerialization.deserialize(java.util.Map.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length));
        com.dslplatform.ocd.javaasserts.MapAsserts.assertNullableSetOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<java.util.Map<String, String>> borderValue1 = new java.util.HashSet<java.util.Map<String, String>>(java.util.Arrays.asList(new java.util.HashMap<String, String>(0)));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.Set<java.util.Map<String, String>> borderValue1JsonDeserialized = new java.util.HashSet<java.util.Map<String, String>>((java.util.List<java.util.Map<String, String>>) (java.util.List<?>) jsonSerialization.deserialize(java.util.Map.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.MapAsserts.assertNullableSetOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<java.util.Map<String, String>> borderValue2 = new java.util.HashSet<java.util.Map<String, String>>(java.util.Arrays.asList(new java.util.HashMap<String, String>() {{ put("", "empty"); put("a", "1"); put("b", "2"); put("c", "3"); }}));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.Set<java.util.Map<String, String>> borderValue2JsonDeserialized = new java.util.HashSet<java.util.Map<String, String>>((java.util.List<java.util.Map<String, String>>) (java.util.List<?>) jsonSerialization.deserialize(java.util.Map.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.MapAsserts.assertNullableSetOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<java.util.Map<String, String>> borderValue3 = new java.util.HashSet<java.util.Map<String, String>>(java.util.Arrays.asList(new java.util.HashMap<String, String>(0), new java.util.HashMap<String, String>() {{ put("a", "b"); }}, new java.util.HashMap<String, String>() {{ put("Quote: \", Solidus /", "Backslash: \\, Aphos: ', Brackets: [] () {}"); }}, new java.util.HashMap<String, String>() {{ put("", "empty"); put("a", "1"); put("b", "2"); put("c", "3"); }}));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.Set<java.util.Map<String, String>> borderValue3JsonDeserialized = new java.util.HashSet<java.util.Map<String, String>>((java.util.List<java.util.Map<String, String>>) (java.util.List<?>) jsonSerialization.deserialize(java.util.Map.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.MapAsserts.assertNullableSetOfOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}
