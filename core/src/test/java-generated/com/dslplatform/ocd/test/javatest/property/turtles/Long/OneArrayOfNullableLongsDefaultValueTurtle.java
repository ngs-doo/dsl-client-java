package com.dslplatform.ocd.test.javatest.property.turtles.Long;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneArrayOfNullableLongsDefaultValueTurtle {

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
        final Long[] defaultValue = new Long[0];
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final Long[] defaultValueJsonDeserialized = jsonSerialization.deserialize(Long[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneArrayOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final Long[] borderValue1 = new Long[] { null };
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final Long[] borderValue1JsonDeserialized = jsonSerialization.deserialize(Long[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneArrayOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final Long[] borderValue2 = new Long[] { 0L };
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final Long[] borderValue2JsonDeserialized = jsonSerialization.deserialize(Long[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneArrayOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final Long[] borderValue3 = new Long[] { Long.MAX_VALUE };
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final Long[] borderValue3JsonDeserialized = jsonSerialization.deserialize(Long[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneArrayOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final Long[] borderValue4 = new Long[] { 0L, 1L, 1000000000000000000L, -1000000000000000000L, Long.MIN_VALUE, Long.MAX_VALUE };
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final Long[] borderValue4JsonDeserialized = jsonSerialization.deserialize(Long[].class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneArrayOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final Long[] borderValue5 = new Long[] { null, 0L, 1L, 1000000000000000000L, -1000000000000000000L, Long.MIN_VALUE, Long.MAX_VALUE };
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final Long[] borderValue5JsonDeserialized = jsonSerialization.deserialize(Long[].class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneArrayOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
