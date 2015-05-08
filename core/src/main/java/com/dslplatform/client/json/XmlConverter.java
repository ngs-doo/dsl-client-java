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
import java.util.ArrayList;
import java.util.Collection;

public class XmlConverter {

	static JsonReader.ReadObject<Element> Reader = new JsonReader.ReadObject<Element>() {
		@Override
		public Element read(JsonReader reader) throws IOException {
			return deserialize(reader);
		}
	};
	static JsonWriter.WriteObject<Element> Writer = new JsonWriter.WriteObject<Element>() {
		@Override
		public void write(JsonWriter writer, Element value) {
			serializeNullable(value, writer);
		}
	};

	private static DocumentBuilder documentBuilder;

	static {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public static void serializeNullable(final Element value, final JsonWriter sw) {
		if (value == null)
			sw.writeNull();
		else
			serialize(value, sw);
	}

	public static void serialize(final Element value, final JsonWriter sw) {
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

	public static ArrayList<Element> deserializeCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(Reader);
	}

	public static void deserializeCollection(final JsonReader reader, final Collection<Element> res) throws IOException {
		reader.deserializeCollection(Reader, res);
	}

	public static ArrayList<Element> deserializeNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(Reader);
	}

	public static void deserializeNullableCollection(final JsonReader reader, final Collection<Element> res) throws IOException {
		reader.deserializeNullableCollection(Reader, res);
	}
}
