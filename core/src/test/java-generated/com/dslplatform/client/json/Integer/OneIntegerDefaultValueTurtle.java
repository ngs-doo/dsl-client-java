package com.dslplatform.client.json.Integer;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneIntegerDefaultValueTurtle {
    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final int defaultValue = 0;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final int defaultValueJsonDeserialized = jsonSerialization.deserialize(int.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final int borderValue1 = Integer.MIN_VALUE;
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final int borderValue1JsonDeserialized = jsonSerialization.deserialize(int.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final int borderValue2 = Integer.MAX_VALUE;
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final int borderValue2JsonDeserialized = jsonSerialization.deserialize(int.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final int borderValue3 = -1000000000;
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final int borderValue3JsonDeserialized = jsonSerialization.deserialize(int.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final int borderValue4 = 1000000000;
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final int borderValue4JsonDeserialized = jsonSerialization.deserialize(int.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IntegerAsserts.assertOneEquals(borderValue4, borderValue4JsonDeserialized);
    }
}
