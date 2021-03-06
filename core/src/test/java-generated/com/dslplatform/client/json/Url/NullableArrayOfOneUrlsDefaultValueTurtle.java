package com.dslplatform.client.json.Url;

import com.dslplatform.client.JsonSerialization;
import com.dslplatform.patterns.Bytes;
import java.io.IOException;

public class NullableArrayOfOneUrlsDefaultValueTurtle {
	private static JsonSerialization jsonSerialization;

	@org.junit.BeforeClass
	public static void initializeJsonSerialization() throws IOException {
		jsonSerialization = com.dslplatform.client.StaticJson.getSerialization();
	}

	@org.junit.Test
	public void testDefaultValueEquality() throws IOException {
		final java.net.URI[] defaultValue = null;
		final Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
		final java.net.URI[] defaultValueJsonDeserialized = jsonSerialization.deserialize(java.net.URI[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
		com.dslplatform.ocd.javaasserts.UrlAsserts.assertNullableArrayOfOneEquals(defaultValue, defaultValueJsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue1Equality() throws IOException {
		final java.net.URI[] borderValue1 = new java.net.URI[] { com.dslplatform.ocd.test.TypeFactory.buildURI("failover:(tcp://localhost:8181,tcp://localhost:8080/)") };
		final Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
		final java.net.URI[] borderValue1JsonDeserialized = jsonSerialization.deserialize(java.net.URI[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.UrlAsserts.assertNullableArrayOfOneEquals(borderValue1, borderValue1JsonDeserialized);
	}

	@org.junit.Test
	public void testBorderValue2Equality() throws IOException {
		final java.net.URI[] borderValue2 = new java.net.URI[] { com.dslplatform.ocd.test.TypeFactory.buildURI("http://127.0.0.1/"), com.dslplatform.ocd.test.TypeFactory.buildURI("http://www.xyz.com/"), com.dslplatform.ocd.test.TypeFactory.buildURI("https://www.abc.com/"), com.dslplatform.ocd.test.TypeFactory.buildURI("ftp://www.pqr.com/"), com.dslplatform.ocd.test.TypeFactory.buildURI("https://localhost:8080/"), com.dslplatform.ocd.test.TypeFactory.buildURI("mailto:snail@mail.hu"), com.dslplatform.ocd.test.TypeFactory.buildURI("file:///~/opt/somefile.md"), com.dslplatform.ocd.test.TypeFactory.buildURI("tcp://localhost:8181/"), com.dslplatform.ocd.test.TypeFactory.buildURI("failover:(tcp://localhost:8181,tcp://localhost:8080/)") };
		final Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
		final java.net.URI[] borderValue2JsonDeserialized = jsonSerialization.deserialize(java.net.URI[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
		com.dslplatform.ocd.javaasserts.UrlAsserts.assertNullableArrayOfOneEquals(borderValue2, borderValue2JsonDeserialized);
	}
}
