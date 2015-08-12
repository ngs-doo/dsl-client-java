package com.dslplatform.client;

import com.dslplatform.client.json.DslJsonSerialization;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.test.simple.E;
import com.dslplatform.test.simple.SimpleRoot;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import static org.junit.Assert.*;

public class ManualJsonTest {
	@Test
	public void simpleTypes() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		byte[] res = json.serialize("abcd").toByteArray();
		String abcd = json.deserialize(String.class, res, res.length);
		assertEquals("abcd", abcd);
		res = json.serialize(123456).toByteArray();
		int num = json.deserialize(int.class, res, res.length);
		assertEquals(123456, num);
	}

	@Test
	public void dateTypes() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		DateTime now1 = DateTime.now();
		LocalDate today1 = LocalDate.now();
		byte[] res = json.serialize(now1).toByteArray();
		DateTime now2 = json.deserialize(DateTime.class, res, res.length);
		assertTrue(now1.isEqual(now2));
		res = json.serialize(today1).toByteArray();
		LocalDate today2 = json.deserialize(LocalDate.class, res, res.length);
		assertTrue(today1.equals(today2));
	}

	@Test
	public void simpleBooleanCollections() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		boolean[] input1 = new boolean[]{true, false, false, true};
		byte[] res = json.serialize(input1).toByteArray();
		boolean[] output1 = json.deserialize(boolean[].class, res, res.length);
		assertArrayEquals(input1, output1);
		Boolean[] input2 = new Boolean[]{true, null, false, false, true, null};
		res = json.serialize(input2).toByteArray();
		Boolean[] output2 = json.deserialize(Boolean[].class, res, res.length);
		assertArrayEquals(input2, output2);
	}

	@Test
	public void simpleIntCollections() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		int[] input1 = new int[]{-1, 0, 1};
		byte[] res = json.serialize(input1).toByteArray();
		int[] output1 = json.deserialize(int[].class, res, res.length);
		assertArrayEquals(input1, output1);
		Integer[] input2 = new Integer[]{-1, null, 1};
		res = json.serialize(input2).toByteArray();
		Integer[] output2 = json.deserialize(Integer[].class, res, res.length);
		assertArrayEquals(input2, output2);
	}

	@Test
	public void simpleLongCollections() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		long[] input1 = new long[]{0};
		byte[] res = json.serialize(input1).toByteArray();
		long[] output1 = json.deserialize(long[].class, res, res.length);
		assertArrayEquals(input1, output1);
		Long[] input2 = new Long[]{null};
		res = json.serialize(input2).toByteArray();
		Long[] output2 = json.deserialize(Long[].class, res, res.length);
		assertArrayEquals(input2, output2);
	}

	@Test
	public void simpleIntList() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		List<Integer> input1 = Arrays.asList(Integer.MIN_VALUE, 0, Integer.MAX_VALUE);
		byte[] res = json.serialize(input1).toByteArray();
		List<Integer> output1 = json.deserializeList(Integer.class, res, res.length);
		assertEquals(input1, output1);
		List<Integer> input2 = Arrays.asList(Integer.MIN_VALUE, null, Integer.MAX_VALUE);
		res = json.serialize(input2).toByteArray();
		List<Integer> output2 = json.deserializeList(Integer.class, res, res.length);
		assertEquals(input2, output2);
	}

	@Test
	public void simpleDoubleList() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		String input = "[\"NaN\",1,2] ";
		List<Double> output = json.deserializeList(double.class, input.getBytes(), input.length());
		assertEquals(3, output.size());
		assertEquals(Double.NaN, output.get(0), 0);
		assertEquals(Double.parseDouble("1"), output.get(1), 0);
		assertEquals(Double.parseDouble("2"), output.get(2), 0);
	}

	@Test
	public void simpleFloatList() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		String input = "[null,\"NaN\",1.2]";
		List<Float> output = json.deserializeList(Float.class, input.getBytes(), input.length());
		assertEquals(3, output.size());
		assertNull(output.get(0));
		assertEquals(Float.NaN, output.get(1), 0);
		assertEquals(Float.parseFloat("1.2"), output.get(2), 0);
	}

	@Test
	public void emptyList() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		List<Object> input = new ArrayList<Object>();
		byte[] res = json.serialize(input).toByteArray();
		List<Object> output = json.deserializeList(Object.class, res, res.length);
		assertEquals(input, output);
	}

	@Test
	public void nullList() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		List<Object> input = Arrays.asList(null, null, null);
		byte[] res = json.serialize(input).toByteArray();
		List<Integer> output = json.deserializeList(Integer.class, res, res.length);
		assertEquals(input, output);
	}

	@Test
	public void simpleObject() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		SimpleRoot root1 = new SimpleRoot().setE(E.B).setI(3434).setS("a\\b");
		byte[] res = json.serialize(root1).toByteArray();
		SimpleRoot root2 = json.deserialize(SimpleRoot.class, res, res.length);
		assertEquals(root1, root2);
	}

	@Test
	public void javaGeomTypes() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		java.awt.Point orig_p = new java.awt.Point(3, 4);
		byte[] res = json.serialize(orig_p).toByteArray();
		java.awt.Point deser_p = json.deserialize(java.awt.Point.class, res, res.length);
		assertEquals(orig_p, deser_p);
		java.awt.geom.Rectangle2D orig_r = new java.awt.geom.Rectangle2D.Double(3, 4, 10, 20);
		res = json.serialize(orig_r).toByteArray();
		java.awt.geom.Rectangle2D deser_r = json.deserialize(java.awt.geom.Rectangle2D.class, res, res.length);
		assertEquals(orig_r, deser_r);
	}

	@Test
	public void emptyArray() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		int[] output = json.deserialize(int[].class, new byte[]{'[', ']'}, 2);
		assertEquals(0, output.length);
	}

	@Test
	public void emptyArrayWithSpaces() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		int[] output = json.deserialize(int[].class, new byte[]{'[', ' ', ' ', ']'}, 4);
		assertEquals(0, output.length);
	}

	@Test
	public void genericNumberDeserialization() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		Number output = json.deserialize(Number.class, new byte[]{'1', '2', '3', '4'}, 4);
		assertEquals(1234L, output);
	}

	@Test
	public void mapSerialization() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> nestedMap = new HashMap<String, Object>();
		nestedMap.put("xxx", "abc");
		nestedMap.put("yyy", "zzz");
		nestedMap.put("integers", Arrays.asList(1L, 2L, 3L));
		map.put("1", 1L);
		map.put("2-w", "2");
		map.put("3", 4.4d);
		map.put("4 f", null);
		map.put("5", new HashMap());
		map.put("6", nestedMap);
		map.put("List", Arrays.asList(null, 2.2d, 3.14159d, 10e5d ));
		Bytes result = json.serialize(map);
		Map output = json.deserialize(Map.class, result.content, result.length);
		assertEquals(map, output);
	}

	@Test
	public void linkedHashMapCheck() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("1", "1");
		map.put("2", 2L);
		map.put("3", null);
		Bytes result = json.serialize(map);
		LinkedHashMap output = json.deserialize(LinkedHashMap.class, result.content, result.length);
		assertEquals(map, output);
	}

	@Test
	public void plainHashMapCheck() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("1", "1");
		map.put("2", 2L);
		map.put("3", null);
		Bytes result = json.serialize(map);
		HashMap output = json.deserialize(HashMap.class, result.content, result.length);
		assertEquals(map, output);
	}

	public class CustomHashMap extends HashMap<String, Object> {
	}

	@Test
	public void customHashMapCheck() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		CustomHashMap map = new CustomHashMap();
		map.put("1", "1");
		map.put("2", 2L);
		map.put("3", null);
		Bytes result = json.serialize(map);
		try {
			json.deserialize(CustomHashMap.class, result.content, result.length);
			fail("Expecting IOException");
		}catch (IOException ex) {
			//assertTrue(ex.getMessage().contains("Found reader for: class java.util.HashMap"));
		}
	}

	@Test
	public void stringLimitCheck() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		try {
			json.deserialize(String.class, "\"abcd\"".getBytes(Charset.forName("UTF-8")), 2);
			fail("length check error");
		} catch (IOException ex) {
			assertTrue(ex.getMessage().contains("JSON string was not closed"));
		}
	}

	@Test
	public void numberLimitCheck() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		final int number = json.deserialize(int.class, "123456".getBytes(Charset.forName("UTF-8")), 3);
		assertEquals(123, number);
	}

	@Test
	public void arrayLimitCheck() throws IOException {
		final JsonSerialization json = new DslJsonSerialization(null);
		try {
			json.deserializeList(String.class, "[\"abcd\"]".getBytes(Charset.forName("UTF-8")), 4);
			fail("length check error");
		} catch (IOException ex) {
			assertTrue(ex.getMessage().contains("Unexpected end"));
		}
	}
}
