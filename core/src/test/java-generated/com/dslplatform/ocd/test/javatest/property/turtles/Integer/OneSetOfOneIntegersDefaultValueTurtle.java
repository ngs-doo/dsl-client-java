package com.dslplatform.ocd.test.javatest.property.turtles.Integer;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneSetOfOneIntegersDefaultValueTurtle {

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
        final java.util.Set<Integer> defaultValue = new java.util.HashSet<Integer>(0);
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.Set<Integer> defaultValueJsonDeserialized = new java.util.HashSet<Integer>(jsonSerialization.deserializeList(Integer.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length));
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneSetOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<Integer> borderValue1 = new java.util.HashSet<Integer>(java.util.Arrays.asList(0));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.Set<Integer> borderValue1JsonDeserialized = new java.util.HashSet<Integer>(jsonSerialization.deserializeList(Integer.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneSetOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<Integer> borderValue2 = new java.util.HashSet<Integer>(java.util.Arrays.asList(1000000000));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.Set<Integer> borderValue2JsonDeserialized = new java.util.HashSet<Integer>(jsonSerialization.deserializeList(Integer.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneSetOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<Integer> borderValue3 = new java.util.HashSet<Integer>(java.util.Arrays.asList(0, Integer.MIN_VALUE, Integer.MAX_VALUE, -1000000000, 1000000000));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.Set<Integer> borderValue3JsonDeserialized = new java.util.HashSet<Integer>(jsonSerialization.deserializeList(Integer.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneSetOfOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}
