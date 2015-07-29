package com.dslplatform.ocd.test.javatest.property.turtles.Xml;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.client.Bootstrap;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.ServiceLocator;
import java.io.IOException;

public class OneListOfOneXmlsDefaultValueTurtle {

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
        final java.util.List<org.w3c.dom.Element> defaultValue = new java.util.ArrayList<org.w3c.dom.Element>(0);
        final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<org.w3c.dom.Element> defaultValueJsonDeserialized = jsonSerialization.deserializeList(org.w3c.dom.Element.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        com.dslplatform.ocd.javaasserts.XmlAsserts.assertOneListOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.List<org.w3c.dom.Element> borderValue1 = new java.util.ArrayList<org.w3c.dom.Element>(java.util.Arrays.asList(com.dslplatform.ocd.test.Utils.stringToElement("<ns3000:NamespacedElement/>")));
        final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<org.w3c.dom.Element> borderValue1JsonDeserialized = jsonSerialization.deserializeList(org.w3c.dom.Element.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.XmlAsserts.assertOneListOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    /* Equality test to check if assertions / JSON serialization works */
    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.List<org.w3c.dom.Element> borderValue2 = new java.util.ArrayList<org.w3c.dom.Element>(java.util.Arrays.asList(com.dslplatform.ocd.test.Utils.stringToElement("<document/>"), com.dslplatform.ocd.test.Utils.stringToElement("<TextElement>some text &amp; &lt;stuff&gt;</TextElement>"), com.dslplatform.ocd.test.Utils.stringToElement("<ElementWithCData>&lt;?xml?&gt;&lt;xml&gt;&lt;!xml!&gt;</ElementWithCData>"), com.dslplatform.ocd.test.Utils.stringToElement("<AtributedElement foo=\"bar\" qwe=\"poi\"/>"), com.dslplatform.ocd.test.Utils.stringToElement("<NestedTextElement><FirstNest><SecondNest>bird</SecondNest></FirstNest></NestedTextElement>"), com.dslplatform.ocd.test.Utils.stringToElement("<ns3000:NamespacedElement/>")));
        final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<org.w3c.dom.Element> borderValue2JsonDeserialized = jsonSerialization.deserializeList(org.w3c.dom.Element.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        com.dslplatform.ocd.javaasserts.XmlAsserts.assertOneListOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }
}
