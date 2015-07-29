package com.dslplatform.client.json.Xml;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class OneSetOfOneXmlsDefaultValueTurtle {
    private static JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.Set<org.w3c.dom.Element> defaultValue = new java.util.HashSet<org.w3c.dom.Element>(0);
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<org.w3c.dom.Element> deserializedTmpList = jsonSerialization.deserializeList(org.w3c.dom.Element.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        final java.util.Set<org.w3c.dom.Element> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<org.w3c.dom.Element>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.XmlAsserts.assertOneSetOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<org.w3c.dom.Element> borderValue1 = new java.util.HashSet<org.w3c.dom.Element>(java.util.Arrays.asList(com.dslplatform.ocd.test.Utils.stringToElement("<ns3000:NamespacedElement/>")));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<org.w3c.dom.Element> deserializedTmpList = jsonSerialization.deserializeList(org.w3c.dom.Element.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        final java.util.Set<org.w3c.dom.Element> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<org.w3c.dom.Element>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.XmlAsserts.assertOneSetOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<org.w3c.dom.Element> borderValue2 = new java.util.HashSet<org.w3c.dom.Element>(java.util.Arrays.asList(com.dslplatform.ocd.test.Utils.stringToElement("<document/>"), com.dslplatform.ocd.test.Utils.stringToElement("<TextElement>some text &amp; &lt;stuff&gt;</TextElement>"), com.dslplatform.ocd.test.Utils.stringToElement("<ElementWithCData>&lt;?xml?&gt;&lt;xml&gt;&lt;!xml!&gt;</ElementWithCData>"), com.dslplatform.ocd.test.Utils.stringToElement("<AtributedElement foo=\"bar\" qwe=\"poi\"/>"), com.dslplatform.ocd.test.Utils.stringToElement("<NestedTextElement><FirstNest><SecondNest>bird</SecondNest></FirstNest></NestedTextElement>"), com.dslplatform.ocd.test.Utils.stringToElement("<ns3000:NamespacedElement/>")));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<org.w3c.dom.Element> deserializedTmpList = jsonSerialization.deserializeList(org.w3c.dom.Element.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        final java.util.Set<org.w3c.dom.Element> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<org.w3c.dom.Element>(deserializedTmpList);
        com.dslplatform.ocd.javaasserts.XmlAsserts.assertOneSetOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }
}
