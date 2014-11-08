package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;

public class BinaryConverter {
	static final boolean isAndroid;

	static {
		isAndroid = System.getProperty("java.runtime.name").toLowerCase().contains("android");
	}

	static class AndroidEncoding {
		private static String toBase64(final byte[] body) {
			return android.util.Base64.encodeToString(body, android.util.Base64.NO_WRAP);
		}
	}

	static class JavaEncoding {
		private static String toBase64(final byte[] body) {
			return javax.xml.bind.DatatypeConverter.printBase64Binary(body);
		}
	}

	public static void serialize(byte[] value, Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else if (value.length == 0)
			sw.write("\"\"");
		else
		{
			if (isAndroid) {
				sw.write(AndroidEncoding.toBase64(value));
			} else {
				sw.write(JavaEncoding.toBase64(value));
			}
		}
	}
}
