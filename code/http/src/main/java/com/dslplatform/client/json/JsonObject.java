package com.dslplatform.client.json;

public interface JsonObject {
	void serialize(final JsonWriter writer, final boolean minimal);
}
