package com.dslplatform.client;

public enum JsonStatic {
	INSTANCE;

	public final JacksonJsonSerialization jackson = new JacksonJsonSerialization(null);
	public final DslJsonSerialization manual = new DslJsonSerialization(null);
}
