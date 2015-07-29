package com.dslplatform.ocd.test.javatest.property.turtles.DecimalWithScaleOf9;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class NullableSetOfOneDecimalsWithScaleOf9DefaultValueTurtle {

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
        final java.util.Set<java.math.BigDecimal> defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.Set<java.math.BigDecimal> defaultValueJsonDeserialized = new java.util.HashSet<java.math.BigDecimal>(jsonSerialization.deserializeList(java.math.BigDecimal.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length));
        com.dslplatform.ocd.javaasserts.DecimalWithScaleOf9Asserts.assertNullableSetOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<java.math.BigDecimal> borderValue1 = new java.util.HashSet<java.math.BigDecimal>(java.util.Arrays.asList(java.math.BigDecimal.ZERO.setScale(9)));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.Set<java.math.BigDecimal> borderValue1JsonDeserialized = new java.util.HashSet<java.math.BigDecimal>(jsonSerialization.deserializeList(java.math.BigDecimal.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.DecimalWithScaleOf9Asserts.assertNullableSetOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<java.math.BigDecimal> borderValue2 = new java.util.HashSet<java.math.BigDecimal>(java.util.Arrays.asList(new java.math.BigDecimal("1E19")));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.Set<java.math.BigDecimal> borderValue2JsonDeserialized = new java.util.HashSet<java.math.BigDecimal>(jsonSerialization.deserializeList(java.math.BigDecimal.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.DecimalWithScaleOf9Asserts.assertNullableSetOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<java.math.BigDecimal> borderValue3 = new java.util.HashSet<java.math.BigDecimal>(java.util.Arrays.asList(java.math.BigDecimal.ZERO.setScale(9), java.math.BigDecimal.ONE, new java.math.BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679").setScale(9, java.math.BigDecimal.ROUND_HALF_UP), new java.math.BigDecimal("-1E-9"), new java.math.BigDecimal("1E19")));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.Set<java.math.BigDecimal> borderValue3JsonDeserialized = new java.util.HashSet<java.math.BigDecimal>(jsonSerialization.deserializeList(java.math.BigDecimal.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.DecimalWithScaleOf9Asserts.assertNullableSetOfOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}
