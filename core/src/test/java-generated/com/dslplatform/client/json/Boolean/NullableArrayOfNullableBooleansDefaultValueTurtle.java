package com.dslplatform.client.json.Boolean;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class NullableArrayOfNullableBooleansDefaultValueTurtle {
    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final Boolean[] defaultValue = null;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final Boolean[] defaultValueJsonDeserialized = jsonSerialization.deserialize(Boolean[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertNullableArrayOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final Boolean[] borderValue1 = new Boolean[] { null };
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final Boolean[] borderValue1JsonDeserialized = jsonSerialization.deserialize(Boolean[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertNullableArrayOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final Boolean[] borderValue2 = new Boolean[] { false };
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final Boolean[] borderValue2JsonDeserialized = jsonSerialization.deserialize(Boolean[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertNullableArrayOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final Boolean[] borderValue3 = new Boolean[] { true };
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final Boolean[] borderValue3JsonDeserialized = jsonSerialization.deserialize(Boolean[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertNullableArrayOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final Boolean[] borderValue4 = new Boolean[] { false, true };
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final Boolean[] borderValue4JsonDeserialized = jsonSerialization.deserialize(Boolean[].class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertNullableArrayOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final Boolean[] borderValue5 = new Boolean[] { null, false, true };
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final Boolean[] borderValue5JsonDeserialized = jsonSerialization.deserialize(Boolean[].class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertNullableArrayOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
