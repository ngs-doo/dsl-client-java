package com.dslplatform.client.json.Decimal;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class NullableSetOfNullableDecimalsDefaultValueTurtle {
	private static JsonSerialization jsonSerialization;

	@org.junit.BeforeClass
	public static void initializeJsonSerialization() throws IOException {
		jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
	}

	@org.junit.Test
	public void testDefaultValueEquality() throws IOException {
		final java.util.Set<java.math.BigDecimal> defaultValue = null;
		final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
		final java.util.List<java.math.BigDecimal> deserializedTmpList = jsonSerialization.deserializeList(java.math.BigDecimal.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
		final java.util.Set<java.math.BigDecimal> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.math.BigDecimal>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.DecimalAsserts.assertNullableSetOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue1Equality() throws IOException {
		final java.util.Set<java.math.BigDecimal> borderValue1 = new java.util.HashSet<java.math.BigDecimal>(java.util.Arrays.asList((java.math.BigDecimal) null));
		final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
		final java.util.List<java.math.BigDecimal> deserializedTmpList = jsonSerialization.deserializeList(java.math.BigDecimal.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
		final java.util.Set<java.math.BigDecimal> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.math.BigDecimal>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.DecimalAsserts.assertNullableSetOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue2Equality() throws IOException {
		final java.util.Set<java.math.BigDecimal> borderValue2 = new java.util.HashSet<java.math.BigDecimal>(java.util.Arrays.asList(java.math.BigDecimal.ZERO));
		final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
		final java.util.List<java.math.BigDecimal> deserializedTmpList = jsonSerialization.deserializeList(java.math.BigDecimal.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
		final java.util.Set<java.math.BigDecimal> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.math.BigDecimal>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.DecimalAsserts.assertNullableSetOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue3Equality() throws IOException {
		final java.util.Set<java.math.BigDecimal> borderValue3 = new java.util.HashSet<java.math.BigDecimal>(java.util.Arrays.asList(new java.math.BigDecimal("1E28")));
		final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
		final java.util.List<java.math.BigDecimal> deserializedTmpList = jsonSerialization.deserializeList(java.math.BigDecimal.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
		final java.util.Set<java.math.BigDecimal> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.math.BigDecimal>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.DecimalAsserts.assertNullableSetOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue4Equality() throws IOException {
		final java.util.Set<java.math.BigDecimal> borderValue4 = new java.util.HashSet<java.math.BigDecimal>(java.util.Arrays.asList(java.math.BigDecimal.ZERO, java.math.BigDecimal.ONE, new java.math.BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679").setScale(28, java.math.BigDecimal.ROUND_HALF_UP), new java.math.BigDecimal("-1E-28"), new java.math.BigDecimal("1E28")));
		final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
		final java.util.List<java.math.BigDecimal> deserializedTmpList = jsonSerialization.deserializeList(java.math.BigDecimal.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
		final java.util.Set<java.math.BigDecimal> borderValue4JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.math.BigDecimal>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.DecimalAsserts.assertNullableSetOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue5Equality() throws IOException {
		final java.util.Set<java.math.BigDecimal> borderValue5 = new java.util.HashSet<java.math.BigDecimal>(java.util.Arrays.asList((java.math.BigDecimal) null, java.math.BigDecimal.ZERO, java.math.BigDecimal.ONE, new java.math.BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679").setScale(28, java.math.BigDecimal.ROUND_HALF_UP), new java.math.BigDecimal("-1E-28"), new java.math.BigDecimal("1E28")));
		final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
		final java.util.List<java.math.BigDecimal> deserializedTmpList = jsonSerialization.deserializeList(java.math.BigDecimal.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
		final java.util.Set<java.math.BigDecimal> borderValue5JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.math.BigDecimal>(deserializedTmpList);
		com.dslplatform.ocd.javaasserts.DecimalAsserts.assertNullableSetOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
	}
}
