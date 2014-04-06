package com.dslplatform.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Helpers {

    public static File getFileForResource(final String resourcePath) throws URISyntaxException {
        final URL resourceURL = Xml2JsonRoundTripTest.class.getResource(resourcePath);

        if (resourceURL == null) return null;
        else return new File(resourceURL.toURI());

    }

    public static Document parseXmlFile(final File file) throws SAXException, IOException, ParserConfigurationException {

        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

        doc.normalizeDocument();

        return doc;
    }

    public static String stringFromFile(final File file) throws IOException {
        final BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            final StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public static void printDocumentTree(final Node el) {
        System.out.println(el.toString());
        for (int i = 0; i < el.getChildNodes().getLength(); i++) {
            printDocumentTree(el.getChildNodes().item(i));
        }
    }

    public static void printXmlDocument(final Document doc) {
        System.out.println(xmlDocumentToString(doc));
    }

    public static String xmlDocumentToString(final Document doc) {
        try {
            final DOMSource domSource = new DOMSource(doc);
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            final TransformerFactory tf = TransformerFactory.newInstance();
            final Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (final TransformerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Document xmlDocumentFromJson(final String jSon) throws IOException, ParserConfigurationException {

        System.out.println("Json:");
        System.out.println(jSon);

        final Element xmlRootElement =
                new JsonSerialization(new com.dslplatform.client.MapServiceLocator()).<Element> deserialize(
                        JsonSerialization.buildType(org.w3c.dom.Element.class),
                        jSon);

        final Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        final Node rootNode = xmlDocument.importNode(xmlRootElement, true);
        xmlDocument.appendChild(rootNode);

        return xmlDocument;
    }

    public static String jsonStringFromXml(final Document source_xml) throws IOException {
        return JsonSerialization.<org.w3c.dom.Element> serialize(source_xml.getDocumentElement());
    }

}
