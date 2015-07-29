package com.dslplatform.client.json.Point;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneArrayOfNullablePointsDefaultValueTurtle {
    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.awt.Point[] defaultValue = new java.awt.Point[0];
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.awt.Point[] defaultValueJsonDeserialized = jsonSerialization.deserialize(java.awt.Point[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.PointAsserts.assertOneArrayOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.awt.Point[] borderValue1 = new java.awt.Point[] { null };
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.awt.Point[] borderValue1JsonDeserialized = jsonSerialization.deserialize(java.awt.Point[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.PointAsserts.assertOneArrayOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.awt.Point[] borderValue2 = new java.awt.Point[] { new java.awt.Point() };
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.awt.Point[] borderValue2JsonDeserialized = jsonSerialization.deserialize(java.awt.Point[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.PointAsserts.assertOneArrayOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.awt.Point[] borderValue3 = new java.awt.Point[] { new java.awt.Point(0, 1000000000) };
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.awt.Point[] borderValue3JsonDeserialized = jsonSerialization.deserialize(java.awt.Point[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.PointAsserts.assertOneArrayOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.awt.Point[] borderValue4 = new java.awt.Point[] { new java.awt.Point(), new java.awt.Point(Integer.MIN_VALUE, Integer.MIN_VALUE), new java.awt.Point(Integer.MAX_VALUE, Integer.MAX_VALUE), new java.awt.Point(0, -1000000000), new java.awt.Point(0, 1000000000) };
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.awt.Point[] borderValue4JsonDeserialized = jsonSerialization.deserialize(java.awt.Point[].class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.PointAsserts.assertOneArrayOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.awt.Point[] borderValue5 = new java.awt.Point[] { null, new java.awt.Point(), new java.awt.Point(Integer.MIN_VALUE, Integer.MIN_VALUE), new java.awt.Point(Integer.MAX_VALUE, Integer.MAX_VALUE), new java.awt.Point(0, -1000000000), new java.awt.Point(0, 1000000000) };
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final java.awt.Point[] borderValue5JsonDeserialized = jsonSerialization.deserialize(java.awt.Point[].class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.PointAsserts.assertOneArrayOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
