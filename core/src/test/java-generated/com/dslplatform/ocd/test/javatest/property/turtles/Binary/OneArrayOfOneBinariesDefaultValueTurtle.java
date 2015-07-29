package com.dslplatform.ocd.test.javatest.property.turtles.Binary;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneArrayOfOneBinariesDefaultValueTurtle {

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
        final byte[][] defaultValue = new byte[0][];
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final byte[][] defaultValueJsonDeserialized = jsonSerialization.deserialize(byte[][].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneArrayOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final byte[][] borderValue1 = new byte[][] { new byte[0] };
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final byte[][] borderValue1JsonDeserialized = jsonSerialization.deserialize(byte[][].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneArrayOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final byte[][] borderValue2 = new byte[][] { new byte[] { Byte.MIN_VALUE, 0, Byte.MAX_VALUE } };
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final byte[][] borderValue2JsonDeserialized = jsonSerialization.deserialize(byte[][].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneArrayOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final byte[][] borderValue3 = new byte[][] { new byte[0], new byte[] { Byte.MIN_VALUE }, new byte[] { Byte.MIN_VALUE, 0 }, new byte[] { Byte.MIN_VALUE, 0, Byte.MAX_VALUE } };
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final byte[][] borderValue3JsonDeserialized = jsonSerialization.deserialize(byte[][].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BinaryAsserts.assertOneArrayOfOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}
