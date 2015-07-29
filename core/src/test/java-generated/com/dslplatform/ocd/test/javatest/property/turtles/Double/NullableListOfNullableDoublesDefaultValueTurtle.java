package com.dslplatform.ocd.test.javatest.property.turtles.Double;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class NullableListOfNullableDoublesDefaultValueTurtle {

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
        final java.util.List<Double> defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<Double> defaultValueJsonDeserialized = jsonSerialization.deserializeList(Double.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableListOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.List<Double> borderValue1 = new java.util.ArrayList<Double>(java.util.Arrays.asList((Double) null));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<Double> borderValue1JsonDeserialized = jsonSerialization.deserializeList(Double.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableListOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.List<Double> borderValue2 = new java.util.ArrayList<Double>(java.util.Arrays.asList(0.0));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<Double> borderValue2JsonDeserialized = jsonSerialization.deserializeList(Double.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableListOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.List<Double> borderValue3 = new java.util.ArrayList<Double>(java.util.Arrays.asList(Double.NaN));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<Double> borderValue3JsonDeserialized = jsonSerialization.deserializeList(Double.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableListOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.util.List<Double> borderValue4 = new java.util.ArrayList<Double>(java.util.Arrays.asList(0.0, 1E-307, 9E307, -1.23456789012345E-10, 1.23456789012345E20, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN));
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.util.List<Double> borderValue4JsonDeserialized = jsonSerialization.deserializeList(Double.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableListOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.util.List<Double> borderValue5 = new java.util.ArrayList<Double>(java.util.Arrays.asList((Double) null, 0.0, 1E-307, 9E307, -1.23456789012345E-10, 1.23456789012345E20, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN));
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final java.util.List<Double> borderValue5JsonDeserialized = jsonSerialization.deserializeList(Double.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableListOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
