package com.dslplatform.ocd.test.javatest.property.turtles.Url;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneUrlDefaultValueTurtle {

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
        final java.net.URI defaultValue = com.dslplatform.ocd.test.TypeFactory.buildURI("http://127.0.0.1/");
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.net.URI defaultValueJsonDeserialized = jsonSerialization.deserialize(java.net.URI.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.UrlAsserts.assertOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.net.URI borderValue1 = com.dslplatform.ocd.test.TypeFactory.buildURI("http://www.xyz.com/");
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.net.URI borderValue1JsonDeserialized = jsonSerialization.deserialize(java.net.URI.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.UrlAsserts.assertOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.net.URI borderValue2 = com.dslplatform.ocd.test.TypeFactory.buildURI("https://www.abc.com/");
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.net.URI borderValue2JsonDeserialized = jsonSerialization.deserialize(java.net.URI.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.UrlAsserts.assertOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.net.URI borderValue3 = com.dslplatform.ocd.test.TypeFactory.buildURI("ftp://www.pqr.com/");
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.net.URI borderValue3JsonDeserialized = jsonSerialization.deserialize(java.net.URI.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.UrlAsserts.assertOneEquals(borderValue3, borderValue3JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.net.URI borderValue4 = com.dslplatform.ocd.test.TypeFactory.buildURI("https://localhost:8080/");
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.net.URI borderValue4JsonDeserialized = jsonSerialization.deserialize(java.net.URI.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.UrlAsserts.assertOneEquals(borderValue4, borderValue4JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.net.URI borderValue5 = com.dslplatform.ocd.test.TypeFactory.buildURI("mailto:snail@mail.hu");
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final java.net.URI borderValue5JsonDeserialized = jsonSerialization.deserialize(java.net.URI.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.UrlAsserts.assertOneEquals(borderValue5, borderValue5JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue6Equality() throws IOException {
        final java.net.URI borderValue6 = com.dslplatform.ocd.test.TypeFactory.buildURI("file:///~/opt/somefile.md");
        final Bytes borderValue6JsonSerialized = jsonSerialization.serialize(borderValue6);
        final java.net.URI borderValue6JsonDeserialized = jsonSerialization.deserialize(java.net.URI.class, borderValue6JsonSerialized.content, borderValue6JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.UrlAsserts.assertOneEquals(borderValue6, borderValue6JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue7Equality() throws IOException {
        final java.net.URI borderValue7 = com.dslplatform.ocd.test.TypeFactory.buildURI("tcp://localhost:8181/");
        final Bytes borderValue7JsonSerialized = jsonSerialization.serialize(borderValue7);
        final java.net.URI borderValue7JsonDeserialized = jsonSerialization.deserialize(java.net.URI.class, borderValue7JsonSerialized.content, borderValue7JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.UrlAsserts.assertOneEquals(borderValue7, borderValue7JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue8Equality() throws IOException {
        final java.net.URI borderValue8 = com.dslplatform.ocd.test.TypeFactory.buildURI("failover:(tcp://localhost:8181,tcp://localhost:8080/)");
        final Bytes borderValue8JsonSerialized = jsonSerialization.serialize(borderValue8);
        final java.net.URI borderValue8JsonDeserialized = jsonSerialization.deserialize(java.net.URI.class, borderValue8JsonSerialized.content, borderValue8JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.UrlAsserts.assertOneEquals(borderValue8, borderValue8JsonDeserialized);
    }
}
