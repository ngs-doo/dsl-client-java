package com.dslplatform.ocd.test.javatest.property.turtles.Ip;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneIpDefaultValueTurtle {

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
        final java.net.InetAddress defaultValue = com.dslplatform.ocd.test.TypeFactory.buildIP("127.0.0.1");
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.net.InetAddress defaultValueJsonDeserialized = jsonSerialization.deserialize(java.net.InetAddress.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IpAsserts.assertOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.net.InetAddress borderValue1 = com.dslplatform.ocd.test.TypeFactory.buildIP("0");
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.net.InetAddress borderValue1JsonDeserialized = jsonSerialization.deserialize(java.net.InetAddress.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IpAsserts.assertOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.net.InetAddress borderValue2 = com.dslplatform.ocd.test.TypeFactory.buildIP("255.255.255.255");
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.net.InetAddress borderValue2JsonDeserialized = jsonSerialization.deserialize(java.net.InetAddress.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IpAsserts.assertOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.net.InetAddress borderValue3 = com.dslplatform.ocd.test.TypeFactory.buildIP("::1");
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.net.InetAddress borderValue3JsonDeserialized = jsonSerialization.deserialize(java.net.InetAddress.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IpAsserts.assertOneEquals(borderValue3, borderValue3JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.net.InetAddress borderValue4 = com.dslplatform.ocd.test.TypeFactory.buildIP("ffff::ffff");
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.net.InetAddress borderValue4JsonDeserialized = jsonSerialization.deserialize(java.net.InetAddress.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.IpAsserts.assertOneEquals(borderValue4, borderValue4JsonDeserialized);
    }
}
