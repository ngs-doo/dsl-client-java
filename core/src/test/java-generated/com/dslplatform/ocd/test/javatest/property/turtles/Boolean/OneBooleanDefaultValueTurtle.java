package com.dslplatform.ocd.test.javatest.property.turtles.Boolean;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneBooleanDefaultValueTurtle {

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
        final boolean defaultValue = false;
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final boolean defaultValueJsonDeserialized = jsonSerialization.deserialize(boolean.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final boolean borderValue1 = true;
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final boolean borderValue1JsonDeserialized = jsonSerialization.deserialize(boolean.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.BooleanAsserts.assertOneEquals(borderValue1, borderValue1JsonDeserialized);
    }
}
