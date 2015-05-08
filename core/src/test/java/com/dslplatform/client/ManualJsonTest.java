package com.dslplatform.client;

import com.dslplatform.client.json.DslJsonSerialization;
import com.dslplatform.client.json.JacksonJsonSerialization;
import com.dslplatform.client.json.JsonObject;
import com.dslplatform.client.json.JsonWriter;
import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.History;
import com.dslplatform.patterns.ServiceLocator;
import com.dslplatform.patterns.Snapshot;
import com.dslplatform.test.simple.E;
import com.dslplatform.test.simple.SimpleRoot;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

public class ManualJsonTest {
	@Test
	public void simpleTypes() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		byte[] res = json.serialize("abcd");
		String abcd = json.deserialize(String.class, res, res.length);
		assertEquals("abcd", abcd);
		res = json.serialize(123456);
		int num = json.deserialize(int.class, res, res.length);
		assertEquals(123456, num);
	}

	@Test
	public void dateTypes() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		DateTime now1 = DateTime.now();
		LocalDate today1 = LocalDate.now();
		byte[] res = json.serialize(now1);
		DateTime now2 = json.deserialize(DateTime.class, res, res.length);
		assertTrue(now1.isEqual(now2));
		res = json.serialize(today1);
		LocalDate today2 = json.deserialize(LocalDate.class, res, res.length);
		assertTrue(today1.equals(today2));
	}

	@Test
	public void simpleBooleanCollections() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		boolean[] input1 = new boolean[]{true, false, false, true};
		byte[] res = json.serialize(input1);
		boolean[] output1 = json.deserialize(boolean[].class, res, res.length);
		assertArrayEquals(input1, output1);
		Boolean[] input2 = new Boolean[]{true, null, false, false, true, null};
		res = json.serialize(input2);
		Boolean[] output2 = json.deserialize(Boolean[].class, res, res.length);
		assertArrayEquals(input2, output2);
	}

	@Test
	public void simpleIntCollections() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		int[] input1 = new int[]{-1,0,1};
		byte[] res = json.serialize(input1);
		int[] output1 = json.deserialize(int[].class, res, res.length);
		assertArrayEquals(input1, output1);
		Integer[] input2 = new Integer[]{-1,null,1};
		res = json.serialize(input2);
		Integer[] output2 = json.deserialize(Integer[].class, res, res.length);
		assertArrayEquals(input2, output2);
	}

	@Test
	public void simpleLongCollections() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		long[] input1 = new long[]{0};
		byte[] res = json.serialize(input1);
		long[] output1 = json.deserialize(long[].class, res, res.length);
		assertArrayEquals(input1, output1);
		Long[] input2 = new Long[]{null};
		res = json.serialize(input2);
		Long[] output2 = json.deserialize(Long[].class, res, res.length);
		assertArrayEquals(input2, output2);
	}

	@Test
	public void simpleObject() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		SimpleRoot root1 = new SimpleRoot().setE(E.B).setI(3434).setS("a\\b");
		byte[] res = json.serialize(root1);
		SimpleRoot root2 = json.deserialize(SimpleRoot.class, res, res.length);
		assertEquals(root1, root2);
	}
}
