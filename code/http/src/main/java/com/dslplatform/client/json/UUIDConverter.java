package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;
import java.util.UUID;

public class UUIDConverter {
	public static void serializeNullable(UUID value, Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(UUID value, Writer sw) throws IOException {
		sw.write('"');
		sw.write(value.toString());
		sw.write('"');
	}
}
