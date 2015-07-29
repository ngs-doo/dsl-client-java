package com.dslplatform.client.json.Image;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class NullableImageDefaultValueTurtle {
    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.awt.image.BufferedImage defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.awt.image.BufferedImage defaultValueJsonDeserialized = jsonSerialization.deserialize(java.awt.image.BufferedImage.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.ImageAsserts.assertNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.awt.image.BufferedImage borderValue1 = new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_4BYTE_ABGR);
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.awt.image.BufferedImage borderValue1JsonDeserialized = jsonSerialization.deserialize(java.awt.image.BufferedImage.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.ImageAsserts.assertNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.awt.image.BufferedImage borderValue2 = new java.awt.image.BufferedImage(2, 2, java.awt.image.BufferedImage.TYPE_3BYTE_BGR);
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.awt.image.BufferedImage borderValue2JsonDeserialized = jsonSerialization.deserialize(java.awt.image.BufferedImage.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.ImageAsserts.assertNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.awt.image.BufferedImage borderValue3 = new java.awt.image.BufferedImage(3, 3, java.awt.image.BufferedImage.TYPE_BYTE_BINARY);
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.awt.image.BufferedImage borderValue3JsonDeserialized = jsonSerialization.deserialize(java.awt.image.BufferedImage.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.ImageAsserts.assertNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.awt.image.BufferedImage borderValue4 = new java.awt.image.BufferedImage(4, 4, java.awt.image.BufferedImage.TYPE_BYTE_GRAY);
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.awt.image.BufferedImage borderValue4JsonDeserialized = jsonSerialization.deserialize(java.awt.image.BufferedImage.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.ImageAsserts.assertNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.awt.image.BufferedImage borderValue5 = new java.awt.image.BufferedImage(5, 5, java.awt.image.BufferedImage.TYPE_BYTE_INDEXED);
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final java.awt.image.BufferedImage borderValue5JsonDeserialized = jsonSerialization.deserialize(java.awt.image.BufferedImage.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.ImageAsserts.assertNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue6Equality() throws IOException {
        final java.awt.image.BufferedImage borderValue6 = new java.awt.image.BufferedImage(6, 6, java.awt.image.BufferedImage.TYPE_INT_RGB);
        final Bytes borderValue6JsonSerialized = jsonSerialization.serialize(borderValue6);
        final java.awt.image.BufferedImage borderValue6JsonDeserialized = jsonSerialization.deserialize(java.awt.image.BufferedImage.class, borderValue6JsonSerialized.content, borderValue6JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.ImageAsserts.assertNullableEquals(borderValue6, borderValue6JsonDeserialized);
    }
}
