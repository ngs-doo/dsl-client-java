package com.dslplatform.ocd.test.javatest.property.turtles.Guid;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class NullableSetOfNullableGuidsDefaultValueTurtle {

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
        final java.util.Set<java.util.UUID> defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.Set<java.util.UUID> defaultValueJsonDeserialized = new java.util.HashSet<java.util.UUID>(jsonSerialization.deserializeList(java.util.UUID.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length));
        com.dslplatform.ocd.javaasserts.GuidAsserts.assertNullableSetOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<java.util.UUID> borderValue1 = new java.util.HashSet<java.util.UUID>(java.util.Arrays.asList((java.util.UUID) null));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.Set<java.util.UUID> borderValue1JsonDeserialized = new java.util.HashSet<java.util.UUID>(jsonSerialization.deserializeList(java.util.UUID.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.GuidAsserts.assertNullableSetOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<java.util.UUID> borderValue2 = new java.util.HashSet<java.util.UUID>(java.util.Arrays.asList(java.util.UUID.randomUUID()));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.Set<java.util.UUID> borderValue2JsonDeserialized = new java.util.HashSet<java.util.UUID>(jsonSerialization.deserializeList(java.util.UUID.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.GuidAsserts.assertNullableSetOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<java.util.UUID> borderValue3 = new java.util.HashSet<java.util.UUID>(java.util.Arrays.asList(new java.util.UUID(0L, 0L)));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.Set<java.util.UUID> borderValue3JsonDeserialized = new java.util.HashSet<java.util.UUID>(jsonSerialization.deserializeList(java.util.UUID.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.GuidAsserts.assertNullableSetOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.util.Set<java.util.UUID> borderValue4 = new java.util.HashSet<java.util.UUID>(java.util.Arrays.asList(java.util.UUID.randomUUID(), java.util.UUID.fromString("1-2-3-4-5"), new java.util.UUID(0L, 0L)));
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.util.Set<java.util.UUID> borderValue4JsonDeserialized = new java.util.HashSet<java.util.UUID>(jsonSerialization.deserializeList(java.util.UUID.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.GuidAsserts.assertNullableSetOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.util.Set<java.util.UUID> borderValue5 = new java.util.HashSet<java.util.UUID>(java.util.Arrays.asList((java.util.UUID) null, java.util.UUID.randomUUID(), java.util.UUID.fromString("1-2-3-4-5"), new java.util.UUID(0L, 0L)));
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final java.util.Set<java.util.UUID> borderValue5JsonDeserialized = new java.util.HashSet<java.util.UUID>(jsonSerialization.deserializeList(java.util.UUID.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length));
        com.dslplatform.ocd.javaasserts.GuidAsserts.assertNullableSetOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
