package com.dslplatform.ocd.test.javatest.property.turtles.Float;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneFloatDefaultValueTurtle {

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
        final float defaultValue = 0.0f;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final float defaultValueJsonDeserialized = jsonSerialization.deserialize(float.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.FloatAsserts.assertOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final float borderValue1 = -1.2345E-10f;
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final float borderValue1JsonDeserialized = jsonSerialization.deserialize(float.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.FloatAsserts.assertOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final float borderValue2 = 1.2345E20f;
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final float borderValue2JsonDeserialized = jsonSerialization.deserialize(float.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.FloatAsserts.assertOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final float borderValue3 = -1E-5f;
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final float borderValue3JsonDeserialized = jsonSerialization.deserialize(float.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.FloatAsserts.assertOneEquals(borderValue3, borderValue3JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final float borderValue4 = Float.NaN;
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final float borderValue4JsonDeserialized = jsonSerialization.deserialize(float.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.FloatAsserts.assertOneEquals(borderValue4, borderValue4JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final float borderValue5 = Float.NEGATIVE_INFINITY;
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final float borderValue5JsonDeserialized = jsonSerialization.deserialize(float.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.FloatAsserts.assertOneEquals(borderValue5, borderValue5JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue6Equality() throws IOException {
        final float borderValue6 = Float.POSITIVE_INFINITY;
        final Bytes borderValue6JsonSerialized = jsonSerialization.serialize(borderValue6);
        final float borderValue6JsonDeserialized = jsonSerialization.deserialize(float.class, borderValue6JsonSerialized.content, borderValue6JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.FloatAsserts.assertOneEquals(borderValue6, borderValue6JsonDeserialized);
    }
}
