package com.dslplatform.ocd.test.javatest.property.turtles.Date;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneArrayOfNullableDatesDefaultValueTurtle {

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
        final org.joda.time.LocalDate[] defaultValue = new org.joda.time.LocalDate[0];
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final org.joda.time.LocalDate[] defaultValueJsonDeserialized = jsonSerialization.deserialize(org.joda.time.LocalDate[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DateAsserts.assertOneArrayOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final org.joda.time.LocalDate[] borderValue1 = new org.joda.time.LocalDate[] { null };
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final org.joda.time.LocalDate[] borderValue1JsonDeserialized = jsonSerialization.deserialize(org.joda.time.LocalDate[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DateAsserts.assertOneArrayOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final org.joda.time.LocalDate[] borderValue2 = new org.joda.time.LocalDate[] { org.joda.time.LocalDate.now() };
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final org.joda.time.LocalDate[] borderValue2JsonDeserialized = jsonSerialization.deserialize(org.joda.time.LocalDate[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DateAsserts.assertOneArrayOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final org.joda.time.LocalDate[] borderValue3 = new org.joda.time.LocalDate[] { new org.joda.time.LocalDate(9999, 12, 31) };
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final org.joda.time.LocalDate[] borderValue3JsonDeserialized = jsonSerialization.deserialize(org.joda.time.LocalDate[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DateAsserts.assertOneArrayOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final org.joda.time.LocalDate[] borderValue4 = new org.joda.time.LocalDate[] { org.joda.time.LocalDate.now(), new org.joda.time.LocalDate(1, 2, 3), new org.joda.time.LocalDate(1, 1, 1), new org.joda.time.LocalDate(0), new org.joda.time.LocalDate(Integer.MAX_VALUE * 1001L), new org.joda.time.LocalDate(9999, 12, 31) };
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final org.joda.time.LocalDate[] borderValue4JsonDeserialized = jsonSerialization.deserialize(org.joda.time.LocalDate[].class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DateAsserts.assertOneArrayOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final org.joda.time.LocalDate[] borderValue5 = new org.joda.time.LocalDate[] { null, org.joda.time.LocalDate.now(), new org.joda.time.LocalDate(1, 2, 3), new org.joda.time.LocalDate(1, 1, 1), new org.joda.time.LocalDate(0), new org.joda.time.LocalDate(Integer.MAX_VALUE * 1001L), new org.joda.time.LocalDate(9999, 12, 31) };
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final org.joda.time.LocalDate[] borderValue5JsonDeserialized = jsonSerialization.deserialize(org.joda.time.LocalDate[].class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.DateAsserts.assertOneArrayOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
