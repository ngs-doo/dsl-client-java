package com.dslplatform.ocd.test.javatest.property.turtles.Integer;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneIntegerDefaultValueTurtle {

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
        final int defaultValue = 0;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final int defaultValueJsonDeserialized = jsonSerialization.deserialize(int.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final int borderValue1 = Integer.MIN_VALUE;
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final int borderValue1JsonDeserialized = jsonSerialization.deserialize(int.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final int borderValue2 = Integer.MAX_VALUE;
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final int borderValue2JsonDeserialized = jsonSerialization.deserialize(int.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final int borderValue3 = -1000000000;
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final int borderValue3JsonDeserialized = jsonSerialization.deserialize(int.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneEquals(borderValue3, borderValue3JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final int borderValue4 = 1000000000;
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final int borderValue4JsonDeserialized = jsonSerialization.deserialize(int.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneEquals(borderValue4, borderValue4JsonDeserialized);
    }
}
