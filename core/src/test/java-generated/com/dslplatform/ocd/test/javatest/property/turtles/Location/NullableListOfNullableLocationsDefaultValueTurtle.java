package com.dslplatform.ocd.test.javatest.property.turtles.Location;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class NullableListOfNullableLocationsDefaultValueTurtle {

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
        final java.util.List<java.awt.geom.Point2D> defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<java.awt.geom.Point2D> defaultValueJsonDeserialized = jsonSerialization.deserializeList(java.awt.geom.Point2D.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LocationAsserts.assertNullableListOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.List<java.awt.geom.Point2D> borderValue1 = new java.util.ArrayList<java.awt.geom.Point2D>(java.util.Arrays.asList((java.awt.geom.Point2D) null));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<java.awt.geom.Point2D> borderValue1JsonDeserialized = jsonSerialization.deserializeList(java.awt.geom.Point2D.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LocationAsserts.assertNullableListOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.List<java.awt.geom.Point2D> borderValue2 = new java.util.ArrayList<java.awt.geom.Point2D>(java.util.Arrays.asList(new java.awt.geom.Point2D.Float()));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<java.awt.geom.Point2D> borderValue2JsonDeserialized = jsonSerialization.deserializeList(java.awt.geom.Point2D.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LocationAsserts.assertNullableListOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.List<java.awt.geom.Point2D> borderValue3 = new java.util.ArrayList<java.awt.geom.Point2D>(java.util.Arrays.asList(new java.awt.geom.Point2D.Double(-1.000000000000001, 1.000000000000001)));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<java.awt.geom.Point2D> borderValue3JsonDeserialized = jsonSerialization.deserializeList(java.awt.geom.Point2D.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LocationAsserts.assertNullableListOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.util.List<java.awt.geom.Point2D> borderValue4 = new java.util.ArrayList<java.awt.geom.Point2D>(java.util.Arrays.asList(new java.awt.geom.Point2D.Float(), new java.awt.Point(Integer.MIN_VALUE, Integer.MAX_VALUE), new java.awt.Point(-1000000000, 1000000000), new java.awt.geom.Point2D.Float(Float.MIN_VALUE, Float.MAX_VALUE), new java.awt.geom.Point2D.Float(-1.0000001f, 1.0000001f), new java.awt.geom.Point2D.Double(Double.MIN_VALUE, Double.MAX_VALUE), new java.awt.geom.Point2D.Double(-1.000000000000001, 1.000000000000001)));
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.util.List<java.awt.geom.Point2D> borderValue4JsonDeserialized = jsonSerialization.deserializeList(java.awt.geom.Point2D.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LocationAsserts.assertNullableListOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.util.List<java.awt.geom.Point2D> borderValue5 = new java.util.ArrayList<java.awt.geom.Point2D>(java.util.Arrays.asList((java.awt.geom.Point2D) null, new java.awt.geom.Point2D.Float(), new java.awt.Point(Integer.MIN_VALUE, Integer.MAX_VALUE), new java.awt.Point(-1000000000, 1000000000), new java.awt.geom.Point2D.Float(Float.MIN_VALUE, Float.MAX_VALUE), new java.awt.geom.Point2D.Float(-1.0000001f, 1.0000001f), new java.awt.geom.Point2D.Double(Double.MIN_VALUE, Double.MAX_VALUE), new java.awt.geom.Point2D.Double(-1.000000000000001, 1.000000000000001)));
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final java.util.List<java.awt.geom.Point2D> borderValue5JsonDeserialized = jsonSerialization.deserializeList(java.awt.geom.Point2D.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.LocationAsserts.assertNullableListOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
