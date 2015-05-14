package com.dslplatform.client;

import com.dslplatform.client.json.DslJsonSerialization;
import com.dslplatform.client.json.JacksonJsonSerialization;
import com.dslplatform.test.simple.E;
import com.dslplatform.test.simple.SimpleRoot;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class JacksonTest {
	@Test
	public void customType() throws IOException {
		final JsonSerialization json = new JacksonJsonSerialization(null);
		DateTime now1 = DateTime.now();
		byte[] res = json.serialize(now1).toByteArray();
		DateTime now2 = json.deserialize(DateTime.class, res, res.length);
		assertEquals(now1.toDate().getTime(), now2.toDate().getTime());
	}

	static class NoEmpty {
		public final int i;
		public NoEmpty(int i) {
			this.i = i;
		}
	}

	@Test
	public void nonDefaultCtor() throws IOException {
		final JsonSerialization json = new JacksonJsonSerialization(null);
		String res = json.serialize(new NoEmpty(556)).toUtf8();
		assertTrue(res.contains("556"));
	}
}
