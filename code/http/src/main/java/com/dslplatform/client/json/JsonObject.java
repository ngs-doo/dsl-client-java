package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;

public interface JsonObject {
	void serialize(final Writer writer, final boolean minimal) throws IOException;
}
