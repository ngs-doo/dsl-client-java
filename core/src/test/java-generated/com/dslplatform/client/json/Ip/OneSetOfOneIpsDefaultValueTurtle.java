package com.dslplatform.client.json.Ip;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneSetOfOneIpsDefaultValueTurtle {
    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.Set<java.net.InetAddress> defaultValue = new java.util.HashSet<java.net.InetAddress>(0);
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<java.net.InetAddress> deserializedTmpList = jsonSerialization.deserializeList(java.net.InetAddress.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        final java.util.Set<java.net.InetAddress> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.net.InetAddress>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.IpAsserts.assertOneSetOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<java.net.InetAddress> borderValue1 = new java.util.HashSet<java.net.InetAddress>(java.util.Arrays.asList(com.dslplatform.ocd.test.TypeFactory.buildIP("ffff::ffff")));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<java.net.InetAddress> deserializedTmpList = jsonSerialization.deserializeList(java.net.InetAddress.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        final java.util.Set<java.net.InetAddress> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.net.InetAddress>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.IpAsserts.assertOneSetOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<java.net.InetAddress> borderValue2 = new java.util.HashSet<java.net.InetAddress>(java.util.Arrays.asList(com.dslplatform.ocd.test.TypeFactory.buildIP("127.0.0.1"), com.dslplatform.ocd.test.TypeFactory.buildIP("0"), com.dslplatform.ocd.test.TypeFactory.buildIP("255.255.255.255"), com.dslplatform.ocd.test.TypeFactory.buildIP("::1"), com.dslplatform.ocd.test.TypeFactory.buildIP("ffff::ffff")));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<java.net.InetAddress> deserializedTmpList = jsonSerialization.deserializeList(java.net.InetAddress.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        final java.util.Set<java.net.InetAddress> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.net.InetAddress>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.IpAsserts.assertOneSetOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }
}
