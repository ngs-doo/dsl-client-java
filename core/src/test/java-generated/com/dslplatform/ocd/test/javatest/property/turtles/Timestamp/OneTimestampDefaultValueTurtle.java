package com.dslplatform.ocd.test.javatest.property.turtles.Timestamp;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneTimestampDefaultValueTurtle {

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
        final org.joda.time.DateTime defaultValue = org.joda.time.DateTime.now();
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final org.joda.time.DateTime defaultValueJsonDeserialized = jsonSerialization.deserialize(org.joda.time.DateTime.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.TimestampAsserts.assertOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final org.joda.time.DateTime borderValue1 = new org.joda.time.DateTime(0);
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final org.joda.time.DateTime borderValue1JsonDeserialized = jsonSerialization.deserialize(org.joda.time.DateTime.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.TimestampAsserts.assertOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final org.joda.time.DateTime borderValue2 = new org.joda.time.DateTime(1, 1, 1, 0, 0, org.joda.time.DateTimeZone.UTC);
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final org.joda.time.DateTime borderValue2JsonDeserialized = jsonSerialization.deserialize(org.joda.time.DateTime.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.TimestampAsserts.assertOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final org.joda.time.DateTime borderValue3 = new org.joda.time.DateTime(Integer.MAX_VALUE * 1001L);
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final org.joda.time.DateTime borderValue3JsonDeserialized = jsonSerialization.deserialize(org.joda.time.DateTime.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.TimestampAsserts.assertOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}
