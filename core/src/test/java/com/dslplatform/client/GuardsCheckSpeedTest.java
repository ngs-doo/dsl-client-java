package com.dslplatform.client;

import java.math.BigDecimal;
import java.util.*;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class GuardsCheckSpeedTest {
	private static List<BigDecimal> zeroes;
	private static List<String> empties;

	@BeforeClass
	public static void initializeZeroes() {
		zeroes = new ArrayList<BigDecimal>();
		empties = new ArrayList<String>();
		for (int i = 0; i < 50000; i ++) {
			zeroes.add(BigDecimal.ZERO);
			empties.add("");
		}
	}

	@Test
	public void testArrayCheckSpeed() {
		final BigDecimal[] numbers = zeroes.toArray(new BigDecimal[zeroes.size()]);
		final String[] strings = empties.toArray(new String[empties.size()]);
 		final long startAt = System.nanoTime();
		Guards.checkNulls(numbers);
		Guards.checkScale(numbers, 0);
		Guards.checkNulls(strings);
		Guards.checkLength(strings, 0);
		final long endAt = System.nanoTime();
		final long tookMs = (endAt - startAt) / 1000000;
		Assert.assertTrue("Checks took " + tookMs + "ms (should be ~0ms)", tookMs < 1000);
	}

	@Test
	public void testListCheckSpeed() {
		final List<BigDecimal> numbers = new ArrayList<BigDecimal>(zeroes);
		final List<String> strings = new ArrayList<String>(empties);
		final long startAt = System.nanoTime();
		Guards.checkNulls(numbers);
		Guards.checkScale(numbers, 0);
		Guards.checkNulls(strings);
		Guards.checkLength(strings, 0);
		final long endAt = System.nanoTime();
		final long tookMs = (endAt - startAt) / 1000000;
		Assert.assertTrue("Checks took " + tookMs + "ms (should be ~0ms)", tookMs < 1000);
	}

	@Test
	public void testSetCheckSpeed() {
		final Set<BigDecimal> numbers = new HashSet<BigDecimal>(zeroes);
		final Set<String> strings = new HashSet<String>(empties);
		final long startAt = System.nanoTime();
		Guards.checkNulls(numbers);
		Guards.checkScale(numbers, 0);
		Guards.checkNulls(strings);
		Guards.checkLength(strings, 0);
		final long endAt = System.nanoTime();
		final long tookMs = (endAt - startAt) / 1000000;
		Assert.assertTrue("Checks took " + tookMs + "ms (should be ~0ms)", tookMs < 1000);
	}

	@Test
	public void testQueueCheckSpeed() {
		final Queue<BigDecimal> numbers = new ArrayDeque<BigDecimal>(zeroes);
		final Queue<String> strings = new ArrayDeque<String>(empties);
		final long startAt = System.nanoTime();
		Guards.checkNulls(numbers);
		Guards.checkScale(numbers, 0);
		Guards.checkNulls(strings);
		Guards.checkLength(strings, 0);
		final long endAt = System.nanoTime();
		final long tookMs = (endAt - startAt) / 1000000;
		Assert.assertTrue("Checks took " + tookMs + "ms (should be ~0ms)", tookMs < 1000);
	}

	@Test
	public void testLinkedListCheckSpeed() {
		final LinkedList<BigDecimal> numbers = new LinkedList<BigDecimal>(zeroes);
		final LinkedList<String> strings = new LinkedList<String>(empties);
		final long startAt = System.nanoTime();
		Guards.checkNulls(numbers);
		Guards.checkScale(numbers, 0);
		Guards.checkNulls(strings);
		Guards.checkLength(strings, 0);
		final long endAt = System.nanoTime();
		final long tookMs = (endAt - startAt) / 1000000;
		Assert.assertTrue("Checks took " + tookMs + "ms (should be ~0ms)", tookMs < 1000);
	}

	@Test
	@SuppressWarnings("serial")
	public void testStackCheckSpeed() {
		final Stack<BigDecimal> numbers = new Stack<BigDecimal>() {{ addAll(zeroes); }};
		final Stack<String> strings = new Stack<String>() {{ addAll(empties); }};
		final long startAt = System.nanoTime();
		Guards.checkNulls(numbers);
		Guards.checkScale(numbers, 0);
		Guards.checkNulls(strings);
		Guards.checkLength(strings, 0);
		final long endAt = System.nanoTime();
		final long tookMs = (endAt - startAt) / 1000000;
		Assert.assertTrue("Checks took " + tookMs + "ms (should be ~0ms)", tookMs < 1000);
	}

	@Test
	public void testVectorCheckSpeed() {
		final Vector<BigDecimal> numbers = new Vector<BigDecimal>(zeroes);
		final Vector<String> strings = new Vector<String>(empties);
		final long startAt = System.nanoTime();
		Guards.checkNulls(numbers);
		Guards.checkScale(numbers, 0);
		Guards.checkNulls(strings);
		Guards.checkLength(strings, 0);
		final long endAt = System.nanoTime();
		final long tookMs = (endAt - startAt) / 1000000;
		Assert.assertTrue("Checks took " + tookMs + "ms (should be ~0ms)", tookMs < 1000);
	}
}
