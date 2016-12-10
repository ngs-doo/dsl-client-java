package com.dslplatform.client.json;

import com.dslplatform.client.TreePath;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.TreePathConverter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TreePathConverterTest {
	@Test
	@SuppressWarnings("deprecation")
	public void testSerialization() throws IOException {
		final JsonWriter jw = new JsonWriter();

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
}
