package com.dslplatform.ocd.test.javatest.property.turtles.Long;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneArrayOfOneLongsDefaultValueTurtle {

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
        final long[] defaultValue = new long[0];
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final long[] defaultValueJsonDeserialized = jsonSerialization.deserialize(long[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneArrayOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final long[] borderValue1 = new long[] { 0L };
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final long[] borderValue1JsonDeserialized = jsonSerialization.deserialize(long[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneArrayOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final long[] borderValue2 = new long[] { Long.MAX_VALUE };
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final long[] borderValue2JsonDeserialized = jsonSerialization.deserialize(long[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneArrayOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final long[] borderValue3 = new long[] { 0L, 1L, 1000000000000000000L, -1000000000000000000L, Long.MIN_VALUE, Long.MAX_VALUE };
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final long[] borderValue3JsonDeserialized = jsonSerialization.deserialize(long[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LongAsserts.assertOneArrayOfOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}
