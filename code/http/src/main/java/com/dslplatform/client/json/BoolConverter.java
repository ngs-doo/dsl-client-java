package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;

public class BoolConverter {
	public static void serializeNullable(Boolean value, Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else if (value)
			sw.write("true");
		else
			sw.write("false");
	}

	public static void serialize(boolean value, Writer sw) throws IOException {
		if (value)
			sw.write("true");
		else
			sw.write("false");
	}
}
