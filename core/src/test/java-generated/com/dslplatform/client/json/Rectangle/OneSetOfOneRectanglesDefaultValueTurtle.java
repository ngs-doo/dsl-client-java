package com.dslplatform.client.json.Rectangle;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneSetOfOneRectanglesDefaultValueTurtle {
	private static JsonSerialization jsonSerialization;

	@org.junit.BeforeClass
	public static void initializeJsonSerialization() throws IOException {
		jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
	}

	@org.junit.Test
	public void testDefaultValueEquality() throws IOException {
		final java.util.Set<java.awt.geom.Rectangle2D> defaultValue = new java.util.HashSet<java.awt.geom.Rectangle2D>(0);
		final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
		final java.util.List<java.awt.geom.Rectangle2D> deserializedTmpList = jsonSerialization.deserializeList(java.awt.geom.Rectangle2D.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
		final java.util.Set<java.awt.geom.Rectangle2D> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.awt.geom.Rectangle2D>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.RectangleAsserts.assertOneSetOfOneEquals(defaultValue, defaultValueJsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue1Equality() throws IOException {
		final java.util.Set<java.awt.geom.Rectangle2D> borderValue1 = new java.util.HashSet<java.awt.geom.Rectangle2D>(java.util.Arrays.asList(new java.awt.geom.Rectangle2D.Float()));
		final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
		final java.util.List<java.awt.geom.Rectangle2D> deserializedTmpList = jsonSerialization.deserializeList(java.awt.geom.Rectangle2D.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
		final java.util.Set<java.awt.geom.Rectangle2D> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.awt.geom.Rectangle2D>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.RectangleAsserts.assertOneSetOfOneEquals(borderValue1, borderValue1JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue2Equality() throws IOException {
		final java.util.Set<java.awt.geom.Rectangle2D> borderValue2 = new java.util.HashSet<java.awt.geom.Rectangle2D>(java.util.Arrays.asList(new java.awt.geom.Rectangle2D.Double(-1.000000000000001, -1.000000000000001, 1.000000000000001, 1.000000000000001)));
		final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
		final java.util.List<java.awt.geom.Rectangle2D> deserializedTmpList = jsonSerialization.deserializeList(java.awt.geom.Rectangle2D.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
		final java.util.Set<java.awt.geom.Rectangle2D> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.awt.geom.Rectangle2D>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.RectangleAsserts.assertOneSetOfOneEquals(borderValue2, borderValue2JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue3Equality() throws IOException {
		final java.util.Set<java.awt.geom.Rectangle2D> borderValue3 = new java.util.HashSet<java.awt.geom.Rectangle2D>(java.util.Arrays.asList(new java.awt.geom.Rectangle2D.Float(), new java.awt.Rectangle(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), new java.awt.Rectangle(-1000000000, -1000000000, 1000000000, 1000000000), new java.awt.geom.Rectangle2D.Float(Float.MIN_VALUE, Float.MIN_VALUE, Float.MAX_VALUE, Float.MAX_VALUE), new java.awt.geom.Rectangle2D.Float(-1.0000001f, -1.0000001f, 1.0000001f, 1.0000001f), new java.awt.geom.Rectangle2D.Double(Double.MIN_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MAX_VALUE), new java.awt.geom.Rectangle2D.Double(-1.000000000000001, -1.000000000000001, 1.000000000000001, 1.000000000000001)));
		final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
		final java.util.List<java.awt.geom.Rectangle2D> deserializedTmpList = jsonSerialization.deserializeList(java.awt.geom.Rectangle2D.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
		final java.util.Set<java.awt.geom.Rectangle2D> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.awt.geom.Rectangle2D>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.RectangleAsserts.assertOneSetOfOneEquals(borderValue3, borderValue3JsonDeserialized);
	}
}
