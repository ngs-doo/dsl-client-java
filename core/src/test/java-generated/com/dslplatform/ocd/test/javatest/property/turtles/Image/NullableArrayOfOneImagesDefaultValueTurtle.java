package com.dslplatform.ocd.test.javatest.property.turtles.Image;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class NullableArrayOfOneImagesDefaultValueTurtle {

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
        final java.awt.image.BufferedImage[] defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.awt.image.BufferedImage[] defaultValueJsonDeserialized = jsonSerialization.deserialize(java.awt.image.BufferedImage[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.ImageAsserts.assertNullableArrayOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.awt.image.BufferedImage[] borderValue1 = new java.awt.image.BufferedImage[] { new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_4BYTE_ABGR) };
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.awt.image.BufferedImage[] borderValue1JsonDeserialized = jsonSerialization.deserialize(java.awt.image.BufferedImage[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.ImageAsserts.assertNullableArrayOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.awt.image.BufferedImage[] borderValue2 = new java.awt.image.BufferedImage[] { new java.awt.image.BufferedImage(6, 6, java.awt.image.BufferedImage.TYPE_INT_RGB) };
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.awt.image.BufferedImage[] borderValue2JsonDeserialized = jsonSerialization.deserialize(java.awt.image.BufferedImage[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.ImageAsserts.assertNullableArrayOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.awt.image.BufferedImage[] borderValue3 = new java.awt.image.BufferedImage[] { new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_4BYTE_ABGR), new java.awt.image.BufferedImage(2, 2, java.awt.image.BufferedImage.TYPE_3BYTE_BGR), new java.awt.image.BufferedImage(3, 3, java.awt.image.BufferedImage.TYPE_BYTE_BINARY), new java.awt.image.BufferedImage(4, 4, java.awt.image.BufferedImage.TYPE_BYTE_GRAY), new java.awt.image.BufferedImage(5, 5, java.awt.image.BufferedImage.TYPE_BYTE_INDEXED), new java.awt.image.BufferedImage(6, 6, java.awt.image.BufferedImage.TYPE_INT_RGB) };
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.awt.image.BufferedImage[] borderValue3JsonDeserialized = jsonSerialization.deserialize(java.awt.image.BufferedImage[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.ImageAsserts.assertNullableArrayOfOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}
