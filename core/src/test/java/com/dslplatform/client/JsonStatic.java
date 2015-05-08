package com.dslplatform.client;

import com.dslplatform.client.json.DslJsonSerialization;
import com.dslplatform.client.json.JacksonJsonSerialization;

public enum JsonStatic {
	INSTANCE;

	public final JacksonJsonSerialization jackson = new JacksonJsonSerialization(null);
	public final DslJsonSerialization manual = new DslJsonSerialization(null);
}
