package com.dslplatform.ocd.test.javatest.property.turtles.Double;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class NullableDoubleDefaultValueTurtle {

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
        final Double defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final Double defaultValueJsonDeserialized = jsonSerialization.deserialize(Double.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final Double borderValue1 = 0.0;
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final Double borderValue1JsonDeserialized = jsonSerialization.deserialize(Double.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final Double borderValue2 = 1E-307;
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final Double borderValue2JsonDeserialized = jsonSerialization.deserialize(Double.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final Double borderValue3 = 9E307;
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final Double borderValue3JsonDeserialized = jsonSerialization.deserialize(Double.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final Double borderValue4 = -1.23456789012345E-10;
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final Double borderValue4JsonDeserialized = jsonSerialization.deserialize(Double.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final Double borderValue5 = 1.23456789012345E20;
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final Double borderValue5JsonDeserialized = jsonSerialization.deserialize(Double.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue6Equality() throws IOException {
        final Double borderValue6 = Double.NEGATIVE_INFINITY;
        final Bytes borderValue6JsonSerialized = jsonSerialization.serialize(borderValue6);
        final Double borderValue6JsonDeserialized = jsonSerialization.deserialize(Double.class, borderValue6JsonSerialized.content, borderValue6JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableEquals(borderValue6, borderValue6JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue7Equality() throws IOException {
        final Double borderValue7 = Double.POSITIVE_INFINITY;
        final Bytes borderValue7JsonSerialized = jsonSerialization.serialize(borderValue7);
        final Double borderValue7JsonDeserialized = jsonSerialization.deserialize(Double.class, borderValue7JsonSerialized.content, borderValue7JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableEquals(borderValue7, borderValue7JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue8Equality() throws IOException {
        final Double borderValue8 = Double.NaN;
        final Bytes borderValue8JsonSerialized = jsonSerialization.serialize(borderValue8);
        final Double borderValue8JsonDeserialized = jsonSerialization.deserialize(Double.class, borderValue8JsonSerialized.content, borderValue8JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DoubleAsserts.assertNullableEquals(borderValue8, borderValue8JsonDeserialized);
    }
}
