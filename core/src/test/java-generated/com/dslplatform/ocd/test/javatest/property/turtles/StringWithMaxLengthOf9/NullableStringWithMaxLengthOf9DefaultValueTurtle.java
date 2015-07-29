package com.dslplatform.ocd.test.javatest.property.turtles.StringWithMaxLengthOf9;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class NullableStringWithMaxLengthOf9DefaultValueTurtle {

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
        final String defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final String defaultValueJsonDeserialized = jsonSerialization.deserialize(String.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final String borderValue1 = "";
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final String borderValue1JsonDeserialized = jsonSerialization.deserialize(String.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final String borderValue2 = "\"";
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final String borderValue2JsonDeserialized = jsonSerialization.deserialize(String.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final String borderValue3 = "'/\\[](){}";
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final String borderValue3JsonDeserialized = jsonSerialization.deserialize(String.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final String borderValue4 = "\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t";
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final String borderValue4JsonDeserialized = jsonSerialization.deserialize(String.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final String borderValue5 = "xxxxxxxxx";
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final String borderValue5JsonDeserialized = jsonSerialization.deserialize(String.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.StringWithMaxLengthOf9Asserts.assertNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
