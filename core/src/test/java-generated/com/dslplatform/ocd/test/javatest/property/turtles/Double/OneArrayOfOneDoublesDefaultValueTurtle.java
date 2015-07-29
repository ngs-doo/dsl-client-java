package com.dslplatform.ocd.test.javatest.property.turtles.Double;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneArrayOfOneDoublesDefaultValueTurtle {

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
        final double[] defaultValue = new double[0];
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final double[] defaultValueJsonDeserialized = jsonSerialization.deserialize(double[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertOneArrayOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final double[] borderValue1 = new double[] { 0.0 };
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final double[] borderValue1JsonDeserialized = jsonSerialization.deserialize(double[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertOneArrayOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final double[] borderValue2 = new double[] { Double.NaN };
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final double[] borderValue2JsonDeserialized = jsonSerialization.deserialize(double[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertOneArrayOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final double[] borderValue3 = new double[] { 0.0, 1E-307, 9E307, -1.23456789012345E-10, 1.23456789012345E20, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN };
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final double[] borderValue3JsonDeserialized = jsonSerialization.deserialize(double[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertOneArrayOfOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}
