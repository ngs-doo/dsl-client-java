package com.dslplatform.ocd.test.javatest.property.turtles.Xml;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneXmlDefaultValueTurtle {

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
        final org.w3c.dom.Element defaultValue = com.dslplatform.ocd.test.Utils.stringToElement("<document/>");
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final org.w3c.dom.Element defaultValueJsonDeserialized = jsonSerialization.deserialize(org.w3c.dom.Element.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.XmlAsserts.assertOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final org.w3c.dom.Element borderValue1 = com.dslplatform.ocd.test.Utils.stringToElement("<TextElement>some text &amp; &lt;stuff&gt;</TextElement>");
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final org.w3c.dom.Element borderValue1JsonDeserialized = jsonSerialization.deserialize(org.w3c.dom.Element.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.XmlAsserts.assertOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final org.w3c.dom.Element borderValue2 = com.dslplatform.ocd.test.Utils.stringToElement("<ElementWithCData>&lt;?xml?&gt;&lt;xml&gt;&lt;!xml!&gt;</ElementWithCData>");
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final org.w3c.dom.Element borderValue2JsonDeserialized = jsonSerialization.deserialize(org.w3c.dom.Element.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.XmlAsserts.assertOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final org.w3c.dom.Element borderValue3 = com.dslplatform.ocd.test.Utils.stringToElement("<AtributedElement foo=\"bar\" qwe=\"poi\"/>");
        final Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final org.w3c.dom.Element borderValue3JsonDeserialized = jsonSerialization.deserialize(org.w3c.dom.Element.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.XmlAsserts.assertOneEquals(borderValue3, borderValue3JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final org.w3c.dom.Element borderValue4 = com.dslplatform.ocd.test.Utils.stringToElement("<NestedTextElement><FirstNest><SecondNest>bird</SecondNest></FirstNest></NestedTextElement>");
        final Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final org.w3c.dom.Element borderValue4JsonDeserialized = jsonSerialization.deserialize(org.w3c.dom.Element.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.XmlAsserts.assertOneEquals(borderValue4, borderValue4JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final org.w3c.dom.Element borderValue5 = com.dslplatform.ocd.test.Utils.stringToElement("<ns3000:NamespacedElement/>");
        final Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final org.w3c.dom.Element borderValue5JsonDeserialized = jsonSerialization.deserialize(org.w3c.dom.Element.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.XmlAsserts.assertOneEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
