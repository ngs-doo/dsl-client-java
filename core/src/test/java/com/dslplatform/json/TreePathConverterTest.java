package com.dslplatform.json;

import com.dslplatform.client.TreePath;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

public class TreePathConverterTest {
	@Test
	public void testSerialization() throws IOException {
		final JsonWriter jw = new JsonWriter(null);

		final TreePath expectedPath = new TreePath("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
		final String expectedJson = "\"" + expectedPath + "\"";

		TreePathConverter.serialize(expectedPath, jw);
		final String serializedJson = new String(jw.getByteBuffer(), 0, jw.size(), "UTF-8");
		Assert.assertEquals(expectedJson, serializedJson);

		final JsonReader jr = new JsonReader(jw.getByteBuffer(), null);
		jr.getNextToken();
		final TreePath deserializedPath = TreePathConverter.deserialize(jr);
		Assert.assertEquals(expectedPath, deserializedPath);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testDeserializationErrors() throws IOException {
		final String malformedTreePathJson = "\"opr.s\u0161t.uvz\"";
		final JsonReader jr = new JsonReader(malformedTreePathJson.getBytes("UTF-8"), null);
		jr.getNextToken();
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid value for part: s\u0161t. Only [A-Za-z0-9] allowed for labels");
		TreePathConverter.deserialize(jr);
	}
}
