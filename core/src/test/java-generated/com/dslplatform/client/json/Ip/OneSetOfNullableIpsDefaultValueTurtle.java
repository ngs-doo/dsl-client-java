package com.dslplatform.client.json.Ip;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneSetOfNullableIpsDefaultValueTurtle {
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
        com.dslplatform.ocd.javaasserts.IpAsserts.assertOneSetOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<java.net.InetAddress> borderValue1 = new java.util.HashSet<java.net.InetAddress>(java.util.Arrays.asList((java.net.InetAddress) null));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<java.net.InetAddress> deserializedTmpList = jsonSerialization.deserializeList(java.net.InetAddress.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        final java.util.Set<java.net.InetAddress> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.net.InetAddress>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.IpAsserts.assertOneSetOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<java.net.InetAddress> borderValue2 = new java.util.HashSet<java.net.InetAddress>(java.util.Arrays.asList(com.dslplatform.ocd.test.TypeFactory.buildIP("ffff::ffff")));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<java.net.InetAddress> deserializedTmpList = jsonSerialization.deserializeList(java.net.InetAddress.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        final java.util.Set<java.net.InetAddress> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.net.InetAddress>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.IpAsserts.assertOneSetOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<java.net.InetAddress> borderValue3 = new java.util.HashSet<java.net.InetAddress>(java.util.Arrays.asList(com.dslplatform.ocd.test.TypeFactory.buildIP("127.0.0.1"), com.dslplatform.ocd.test.TypeFactory.buildIP("0"), com.dslplatform.ocd.test.TypeFactory.buildIP("255.255.255.255"), com.dslplatform.ocd.test.TypeFactory.buildIP("::1"), com.dslplatform.ocd.test.TypeFactory.buildIP("ffff::ffff")));
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<java.net.InetAddress> deserializedTmpList = jsonSerialization.deserializeList(java.net.InetAddress.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        final java.util.Set<java.net.InetAddress> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.net.InetAddress>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.IpAsserts.assertOneSetOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.util.Set<java.net.InetAddress> borderValue4 = new java.util.HashSet<java.net.InetAddress>(java.util.Arrays.asList((java.net.InetAddress) null, com.dslplatform.ocd.test.TypeFactory.buildIP("127.0.0.1"), com.dslplatform.ocd.test.TypeFactory.buildIP("0"), com.dslplatform.ocd.test.TypeFactory.buildIP("255.255.255.255"), com.dslplatform.ocd.test.TypeFactory.buildIP("::1"), com.dslplatform.ocd.test.TypeFactory.buildIP("ffff::ffff")));
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.util.List<java.net.InetAddress> deserializedTmpList = jsonSerialization.deserializeList(java.net.InetAddress.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        final java.util.Set<java.net.InetAddress> borderValue4JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.net.InetAddress>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.IpAsserts.assertOneSetOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }
}
