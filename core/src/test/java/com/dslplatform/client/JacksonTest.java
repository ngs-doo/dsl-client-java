package com.dslplatform.client;

import org.joda.time.DateTime;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
