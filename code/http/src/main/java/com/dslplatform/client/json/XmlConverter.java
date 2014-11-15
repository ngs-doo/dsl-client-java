package com.dslplatform.client.json;

import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.UUID;

public class XmlConverter {

	private static DocumentBuilder documentBuilder;

	static {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public static void serializeNullable(final Element value, final Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(final Element value, final Writer sw) throws IOException {
		Document document = value.getOwnerDocument();
		DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();
		LSSerializer serializer = domImplLS.createLSSerializer();
		LSOutput lsOutput = domImplLS.createLSOutput();
		lsOutput.setEncoding("UTF-8");
		StringWriter writer = new StringWriter();
		lsOutput.setCharacterStream(writer);
		serializer.write(document, lsOutput);
		StringConverter.serialize(writer.toString(), sw);
	}

	public static Element deserialize(final JsonReader reader) throws IOException {
		try {
			InputSource source = new InputSource(new StringReader(reader.readString()));
			return documentBuilder.parse(source).getDocumentElement();
		}catch (SAXException ex) {
			throw new IOException(ex);
		}
	}

	private static JsonReader.ReadObject<Element> Reader = new JsonReader.ReadObject<Element>() {
		@Override
		public Element read(JsonReader reader) throws IOException {
			return deserialize(reader);
		}
	};

	public static ArrayList<Element> deserializeCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(Reader);
	}

	public static ArrayList<Element> deserializeNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(Reader);
	}
}
