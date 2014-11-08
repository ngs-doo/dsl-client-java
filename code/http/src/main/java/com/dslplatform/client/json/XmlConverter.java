package com.dslplatform.client.json;

import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class XmlConverter {
	public static void serializeNullable(Element value, Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(Element value, Writer sw) throws IOException {
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
}
