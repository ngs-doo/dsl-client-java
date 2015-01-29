package com.dslplatform.client.json;

import org.junit.Assert;
import org.junit.Test;

public class NumberConverterTest {
	@Test
	public void testSerialization() {
		// setup
		final JsonWriter sw = new JsonWriter(40);

		final int from = -1000000;
		final int to = 1000000;
		final long range = to - from + 1;

		for (long value = from; value <= to; value++) {
			// log
			if ((value & 0xffff) == 0xffff) {
				final long progress = value - from;
				System.out.println(String.format("Exhaustive long serialization test [%d,%d] (%.2f%%)", from, to ,(float) progress * 100 / range));
			}

			// init
			sw.reset();

			// serialization
			NumberConverter.serialize(value, sw);

			// check
			final String valueString = sw.toString();
			final int valueParsed = Integer.valueOf(valueString);
			Assert.assertEquals(value, valueParsed);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	/** First 100 digits as high-low byte pairs packed in an integer array */
	private static final int[] DIGITS = new int[100];
	static {
		for (int i = 0; i < 100; i++) {
			DIGITS[i] = (i / 10 << 8) + i % 10 + '0' * 0x101;
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	/* Serializes two rightmost digits correctly only if value in range [0, 99] */
	private static void noop2(final int value, final byte[] buf, final int pos) {
		final int pair = DIGITS[value];
		buf[pos] = (byte) (pair >> 8);
		buf[pos + 1] = (byte) pair;
	}

	@Test
	public void testNoop2() {
		// setup
		final byte[] out = new byte[2];

		final long startAt = System.currentTimeMillis();
		for (int value = 0; value < 100; value++) {
			// log
			if ((value % 10) == 9) {
				System.out.println(String.format("noop2 test (%.2f%%)", value * 100f / 100));
			}

			// serialization
			noop2(value, out, 0);

			// check
			final int mod100 = value % 100;
			Assert.assertEquals(out[0], mod100 / 10 + '0');
			Assert.assertEquals(out[1], mod100 % 10 + '0');
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	/** Precalculated lookup table for fast modulo 100 digit lookup */
	private static final int[] LOOKUP = new int[1024];
	static {
		final long seed = 0x1a54a953a952a55L;
		int count, value, pos, dir = 1;
		count = value = pos = 0;

		for (int i = 0; i < 1024; i++) {
			if (count-- > 0) {
				value++;
			} else {
				count = 4 - (int) (seed >>> pos & 1);
				if ((pos += dir) < 0 || pos > 56) {
					pos += (dir = -dir) + (dir >> 31);
				}
			}

			LOOKUP[i] = (value / 10 % 10 << 8) + value % 10 + '0' * 0x101;
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	/* Serializes two rightmost digits correctly only if value in range [0, 0x10000000> */
	private static void fast2x(final int value, final byte[] buf, final int pos) {
		final int pair = LOOKUP[(0x28f5c29 * value >>> 25)];
		buf[pos] = (byte) (pair >> 8);
		buf[pos + 1] = (byte) pair;
	}

	@Test
	public void testFast2x() {
		// setup
		final byte[] out = new byte[2];

		final long startAt = System.currentTimeMillis();
		for (int value = 0; value < 0x10000000; value++) {
			// log
			if ((value & 0xffffff) == 0xffffff) {
				System.out.println(String.format("fast2x test (%.2f%%)", value * 100f / 0x10000000));
			}

			// serialization
			fast2x(value, out, 0);

			// check
			final int mod100 = value % 100;
			Assert.assertEquals(out[0], mod100 / 10 + '0');
			Assert.assertEquals(out[1], mod100 % 10 + '0');
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	/* Serializes two rightmost digits for all positive integers */
	private static void fast2(final int value, final byte[] buf, final int pos) {
		final int pair = LOOKUP[(0x28f5c29 * value >>> 25) + (value >>> 28 << 7)];
		buf[pos] = (byte) (pair >> 8);
		buf[pos + 1] = (byte) pair;
	}

	@Test
	public void testFast2() {
		// setup
		final byte[] out = new byte[2];

		final long startAt = System.currentTimeMillis();
		for (int value = 0; value >= 0; value++) {
			// log
			if ((value & 0xffffff) == 0xffffff) {
				System.out.println(String.format("fast2 test (%.2f%%)", value * 100f / Integer.MAX_VALUE));
			}

			// serialization
			fast2(value, out, 0);

			// check
			final int mod100 = value % 100;
			Assert.assertEquals(out[0], mod100 / 10 + '0');
			Assert.assertEquals(out[1], mod100 % 10 + '0');
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	/* Serializes two rightmost digits for all positive integers */
	private static void slow2(final int value, final byte[] buf, final int pos) {
		final int mod100 = value % 100;
		buf[pos] = (byte) (mod100 / 10 + '0');
		buf[pos + 1] = (byte) (mod100 % 10 + '0');
	}

	@Test
	public void testSlow2() {
		// setup
		final byte[] out = new byte[2];

		final long startAt = System.currentTimeMillis();
		for (int value = 0; value >= 0; value++) {
			// log
			if ((value & 0xffffff) == 0xffffff) {
				System.out.println(String.format("slow2 test (%.2f%%)", value * 100f / Integer.MAX_VALUE));
			}

			// serialization
			slow2(value, out, 0);

			// check
			final int mod100 = value % 100;
			Assert.assertEquals(out[0], mod100 / 10 + '0');
			Assert.assertEquals(out[1], mod100 % 10 + '0');
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	public static void main(final String[] args) {
		final byte[] out = new byte[2];

		final int mode = 1 | 4 | 8;

		for (int i = 0; i < 10; i++) {
			System.out.println("-------------------------------");

			if ((mode & 1) == 1) {
				final long startAt = System.currentTimeMillis();
				for (int value = 0; value < 0x10000000; value++) {
					noop2(99, out, 0);
				}
				final long endAt = System.currentTimeMillis();
				System.out.println(" noop2 #" + i + ", took: " + (endAt - startAt) + " ms");
			}

			if ((mode & 2) == 2) {
				final long startAt = System.currentTimeMillis();
				for (int value = 0; value < 0x10000000; value++) {
					fast2x(value, out, 0);
				}
				final long endAt = System.currentTimeMillis();
				System.out.println("fast2x #" + i + ", took: " + (endAt - startAt) + " ms");
			}

			if ((mode & 4) == 4) {
				final long startAt = System.currentTimeMillis();
				for (int value = 0; value < 0x10000000; value++) {
					fast2(value, out, 0);
				}
				final long endAt = System.currentTimeMillis();
				System.out.println(" fast2 #" + i + ", took: " + (endAt - startAt) + " ms");
			}

			if ((mode & 8) == 8) {
				final long startAt = System.currentTimeMillis();
				for (int value = 0; value < 0x10000000; value++) {
					slow2(value, out, 0);
				}
				final long endAt = System.currentTimeMillis();
				System.out.println(" slow2 #" + i + ", took: " + (endAt - startAt) + " ms");
			}
		}
	}
}
