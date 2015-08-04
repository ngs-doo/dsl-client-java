package com.dslplatform.client.json.Rectangle;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class NullableRectangleDefaultValueTurtle {
	private static JsonSerialization jsonSerialization;

	@org.junit.BeforeClass
	public static void initializeJsonSerialization() throws IOException {
		jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
	}

	@org.junit.Test
	public void testDefaultValueEquality() throws IOException {
		final java.awt.geom.Rectangle2D defaultValue = null;
		final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
		final java.awt.geom.Rectangle2D defaultValueJsonDeserialized = jsonSerialization.deserialize(java.awt.geom.Rectangle2D.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
		com.dslplatform.ocd.javaasserts.RectangleAsserts.assertNullableEquals(defaultValue, defaultValueJsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue1Equality() throws IOException {
		final java.awt.geom.Rectangle2D borderValue1 = new java.awt.geom.Rectangle2D.Float();
		final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
		final java.awt.geom.Rectangle2D borderValue1JsonDeserialized = jsonSerialization.deserialize(java.awt.geom.Rectangle2D.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.RectangleAsserts.assertNullableEquals(borderValue1, borderValue1JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue2Equality() throws IOException {
		final java.awt.geom.Rectangle2D borderValue2 = new java.awt.Rectangle(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
		final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
		final java.awt.geom.Rectangle2D borderValue2JsonDeserialized = jsonSerialization.deserialize(java.awt.geom.Rectangle2D.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.RectangleAsserts.assertNullableEquals(borderValue2, borderValue2JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue3Equality() throws IOException {
		final java.awt.geom.Rectangle2D borderValue3 = new java.awt.Rectangle(-1000000000, -1000000000, 1000000000, 1000000000);
		final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
		final java.awt.geom.Rectangle2D borderValue3JsonDeserialized = jsonSerialization.deserialize(java.awt.geom.Rectangle2D.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.RectangleAsserts.assertNullableEquals(borderValue3, borderValue3JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue4Equality() throws IOException {
		final java.awt.geom.Rectangle2D borderValue4 = new java.awt.geom.Rectangle2D.Float(Float.MIN_VALUE, Float.MIN_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
		final java.awt.geom.Rectangle2D borderValue4JsonDeserialized = jsonSerialization.deserialize(java.awt.geom.Rectangle2D.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.RectangleAsserts.assertNullableEquals(borderValue4, borderValue4JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue5Equality() throws IOException {
		final java.awt.geom.Rectangle2D borderValue5 = new java.awt.geom.Rectangle2D.Float(-1.0000001f, -1.0000001f, 1.0000001f, 1.0000001f);
		final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
		final java.awt.geom.Rectangle2D borderValue5JsonDeserialized = jsonSerialization.deserialize(java.awt.geom.Rectangle2D.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.RectangleAsserts.assertNullableEquals(borderValue5, borderValue5JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue6Equality() throws IOException {
		final java.awt.geom.Rectangle2D borderValue6 = new java.awt.geom.Rectangle2D.Double(Double.MIN_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
		final Bytes borderValue6JsonSerialized = jsonSerialization.serialize(borderValue6);
		final java.awt.geom.Rectangle2D borderValue6JsonDeserialized = jsonSerialization.deserialize(java.awt.geom.Rectangle2D.class, borderValue6JsonSerialized.content, borderValue6JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.RectangleAsserts.assertNullableEquals(borderValue6, borderValue6JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue7Equality() throws IOException {
		final java.awt.geom.Rectangle2D borderValue7 = new java.awt.geom.Rectangle2D.Double(-1.000000000000001, -1.000000000000001, 1.000000000000001, 1.000000000000001);
		final Bytes borderValue7JsonSerialized = jsonSerialization.serialize(borderValue7);
		final java.awt.geom.Rectangle2D borderValue7JsonDeserialized = jsonSerialization.deserialize(java.awt.geom.Rectangle2D.class, borderValue7JsonSerialized.content, borderValue7JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.RectangleAsserts.assertNullableEquals(borderValue7, borderValue7JsonDeserialized);
	}
}
