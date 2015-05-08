package com.dslplatform.client.xml;

import com.dslplatform.client.JsonStatic;
import com.dslplatform.client.TestLogging;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class XMLHelpers extends TestLogging {
	public static File getFileForResource(final String resourcePath) throws URISyntaxException {
		final URL resourceURL = Xml2JsonRoundTripTest.class.getResource(resourcePath);

		if (resourceURL == null) return null;
		return new File(resourceURL.toURI());
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

	public static Document xmlDocumentFromJson(final String json) throws IOException, ParserConfigurationException {
		final Element xmlRootElement = JsonStatic.INSTANCE.jackson.deserialize(Element.class, json);

		final Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		final Node rootNode = xmlDocument.importNode(xmlRootElement, true);
		xmlDocument.appendChild(rootNode);
		return xmlDocument;
	}

	public static String jsonStringFromXml(final Document source_xml) throws IOException {
		return JsonStatic.INSTANCE.jackson.serializeString(source_xml.getDocumentElement());
	}
}
