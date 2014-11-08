package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;

public class NetConverter {
	public static void serializeNullable(java.net.URI value, Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(java.net.URI value, Writer sw) throws IOException {
		StringConverter.serialize(value.toString(), sw);
	}

	public static void serializeNullable(java.net.InetAddress value, Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(java.net.InetAddress value, Writer sw) throws IOException {
		StringConverter.serialize(value.toString(), sw);
	}
}
