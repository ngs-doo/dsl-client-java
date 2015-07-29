package com.dslplatform.ocd.test.javatest.property.turtles.Binary;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class NullableBinaryDefaultValueTurtle {

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
        final byte[] defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final byte[] defaultValueJsonDeserialized = jsonSerialization.deserialize(byte[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final byte[] borderValue1 = new byte[0];
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final byte[] borderValue1JsonDeserialized = jsonSerialization.deserialize(byte[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final byte[] borderValue2 = new byte[] { Byte.MIN_VALUE };
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final byte[] borderValue2JsonDeserialized = jsonSerialization.deserialize(byte[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final byte[] borderValue3 = new byte[] { Byte.MIN_VALUE, 0 };
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final byte[] borderValue3JsonDeserialized = jsonSerialization.deserialize(byte[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final byte[] borderValue4 = new byte[] { Byte.MIN_VALUE, 0, Byte.MAX_VALUE };
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final byte[] borderValue4JsonDeserialized = jsonSerialization.deserialize(byte[].class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }
}
