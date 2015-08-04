package com.dslplatform.ocd.javaasserts;

import org.junit.Assert;

public class BinaryAsserts {
	static void assertSingleEquals(final String message, final byte[] expected, final byte[] actual) {
		if (expected.length != actual.length) {
			Assert.fail(message + "expected was a Binary of " + expected.length + " bytes, but actual was a Binary of " + actual.length + " bytes");
		}

		for (int i = 0; i < expected.length; i++) {
			if (expected[i] != actual[i]) {
				Assert.fail(message + "Binary differs at index " + i + ": expected was \"" + expected[i] + "\", but actual was \"" + actual[i] + "\"");
			}
		}
	}

	static void assertOneEquals(final String message, final byte[] expected, final byte[] actual) {
		if (expected == null) Assert.fail(message + "expected was <null> - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
		if (expected == actual) return;
		if (actual == null) Assert.fail(message + "expected was a Binary of " + expected.length + " bytes, but actual was <null>");
		assertSingleEquals(message, expected, actual);
	}

	public static void assertOneEquals(final byte[] expected, final byte[] actual) {
		assertOneEquals("OneBinary mismatch: ", expected, actual);
	}

	private static void assertNullableEquals(final String message, final byte[] expected, final byte[] actual) {
		if (expected == actual) return;
		if (expected == null) Assert.fail(message + "expected was <null>, but actual was a Binary of " + actual.length + " bytes");
		if (actual == null) Assert.fail(message + "expected was a Binary of " + expected.length + " bytes, but actual was <null>");
		assertSingleEquals(message, expected, actual);
	}

	public static void assertNullableEquals(final byte[] expected, final byte[] actual) {
		assertNullableEquals("NullableBinary mismatch: ", expected, actual);
	}

	private static void assertArrayOfOneEquals(final String message, final byte[][] expecteds, final byte[][] actuals) {
		if (expecteds.length != actuals.length) {
			Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
		}

		for (int i = 0; i < expecteds.length; i++) {
			assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i]);
		}
	}

	private static void assertOneArrayOfOneEquals(final String message, final byte[][] expecteds, final byte[][] actuals) {
		if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
		for (int i = 0; i < expecteds.length; i ++) {
			if (expecteds[i] == null) {
				Assert.fail(message + "expecteds contained a <null> element at index " + i + " - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
			}
		}
		if (expecteds == actuals) return;
		if (actuals == null) Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
		assertArrayOfOneEquals(message, expecteds, actuals);
	}

	public static void assertOneArrayOfOneEquals(final byte[][] expecteds, final byte[][] actuals) {
		assertOneArrayOfOneEquals("OneArrayOfOneBinary mismatch: ", expecteds, actuals);
	}

	private static void assertNullableArrayOfOneEquals(final String message, final byte[][] expecteds, final byte[][] actuals) {
		if (expecteds == actuals) return;
		if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
		if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
		assertArrayOfOneEquals(message, expecteds, actuals);
	}

	public static void assertNullableArrayOfOneEquals(final byte[][] expecteds, final byte[][] actuals) {
		assertNullableArrayOfOneEquals("NullableArrayOfOneBinary mismatch: ", expecteds, actuals);
	}

	private static void assertArrayOfNullableEquals(final String message, final byte[][] expecteds, final byte[][] actuals) {
		if (expecteds.length != actuals.length) {
			Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
		}

		for (int i = 0; i < expecteds.length; i++) {
			assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i]);
		}
	}

	private static void assertOneArrayOfNullableEquals(final String message, final byte[][] expecteds, final byte[][] actuals) {
		if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
		if (expecteds == actuals) return;
		if (actuals == null) Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
		assertArrayOfNullableEquals(message, expecteds, actuals);
	}

	public static void assertOneArrayOfNullableEquals(final byte[][] expecteds, final byte[][] actuals) {
		assertOneArrayOfNullableEquals("OneArrayOfNullableBinary mismatch: ", expecteds, actuals);
	}

	private static void assertNullableArrayOfNullableEquals(final String message, final byte[][] expecteds, final byte[][] actuals) {
		if (expecteds == actuals) return;
		if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
		if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
		assertArrayOfNullableEquals(message, expecteds, actuals);
	}

	public static void assertNullableArrayOfNullableEquals(final byte[][] expecteds, final byte[][] actuals) {
		assertNullableArrayOfNullableEquals("NullableArrayOfNullableBinary mismatch: ", expecteds, actuals);
	}

	private static void assertListOfOneEquals(final String message, final java.util.List<byte[]> expecteds, final java.util.List<byte[]> actuals) {
		final int expectedsSize = expecteds.size();
		final int actualsSize = actuals.size();
		if (expectedsSize != actualsSize) {
			Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
		}

		final java.util.Iterator<byte[]> expectedsIterator = expecteds.iterator();
		final java.util.Iterator<byte[]> actualsIterator = actuals.iterator();
		for (int i = 0; i < expectedsSize; i++) {
			final byte[] expected = expectedsIterator.next();
			final byte[] actual = actualsIterator.next();
			assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expected, actual);
		}
	}

	private static void assertOneListOfOneEquals(final String message, final java.util.List<byte[]> expecteds, final java.util.List<byte[]> actuals) {
		int i = 0;
		for (final byte[] expected : expecteds) {
			if (expected == null) {
				Assert.fail(message + "element mismatch occurred at index " + i + ": expected was <null> - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
			}
			i++;
		}
		if (expecteds == actuals) return;
		if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
		assertListOfOneEquals(message, expecteds, actuals);
	}

	public static void assertOneListOfOneEquals(final java.util.List<byte[]> expecteds, final java.util.List<byte[]> actuals) {
		assertOneListOfOneEquals("OneListOfOneBinary mismatch: ", expecteds, actuals);
	}

	private static void assertNullableListOfOneEquals(final String message, final java.util.List<byte[]> expecteds, final java.util.List<byte[]> actuals) {
		if (expecteds == actuals) return;
		if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
		if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
		assertListOfOneEquals(message, expecteds, actuals);
	}

	public static void assertNullableListOfOneEquals(final java.util.List<byte[]> expecteds, final java.util.List<byte[]> actuals) {
		assertNullableListOfOneEquals("NullableListOfOneBinary mismatch: ", expecteds, actuals);
	}

	private static void assertListOfNullableEquals(final String message, final java.util.List<byte[]> expecteds, final java.util.List<byte[]> actuals) {
		final int expectedsSize = expecteds.size();
		final int actualsSize = actuals.size();
		if (expectedsSize != actualsSize) {
			Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
		}

		final java.util.Iterator<byte[]> expectedsIterator = expecteds.iterator();
		final java.util.Iterator<byte[]> actualsIterator = actuals.iterator();
		for (int i = 0; i < expectedsSize; i++) {
			final byte[] expected = expectedsIterator.next();
			final byte[] actual = actualsIterator.next();
			assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expected, actual);
		}
	}

	private static void assertOneListOfNullableEquals(final String message, final java.util.List<byte[]> expecteds, final java.util.List<byte[]> actuals) {
		if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
		if (expecteds == actuals) return;
		if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
		assertListOfNullableEquals(message, expecteds, actuals);
	}

	public static void assertOneListOfNullableEquals(final java.util.List<byte[]> expecteds, final java.util.List<byte[]> actuals) {
		assertOneListOfNullableEquals("OneListOfNullableBinary mismatch: ", expecteds, actuals);
	}

	private static void assertNullableListOfNullableEquals(final String message, final java.util.List<byte[]> expecteds, final java.util.List<byte[]> actuals) {
		if (expecteds == actuals) return;
		if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
		if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
		assertListOfNullableEquals(message, expecteds, actuals);
	}

	public static void assertNullableListOfNullableEquals(final java.util.List<byte[]> expecteds, final java.util.List<byte[]> actuals) {
		assertNullableListOfNullableEquals("NullableListOfNullableBinary mismatch: ", expecteds, actuals);
	}

	private static void assertSetOfOneEquals(final String message, final java.util.Set<byte[]> expecteds, final java.util.Set<byte[]> actuals) {
		if (actuals.contains(null)) {
			Assert.fail(message + "actuals contained a <null> element");
		}

		final int expectedsSize = expecteds.size();
		final int actualsSize = actuals.size();
		if (expectedsSize != actualsSize) {
			Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
		}

		expectedsLoop: for (final byte[] expected : expecteds) {
			if (actuals.contains(expected)) continue;
			for (final byte[] actual : actuals) {
				try {
					assertOneEquals(expected, actual);
					continue expectedsLoop;
				}
				catch (final AssertionError e) {}
			}
			Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
		}
	}

	private static void assertOneSetOfOneEquals(final String message, final java.util.Set<byte[]> expecteds, final java.util.Set<byte[]> actuals) {
		if (expecteds.contains(null)) {
			Assert.fail(message + "expecteds contained a <null> element - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
		}
		if (expecteds == actuals) return;
		if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
		assertSetOfOneEquals(message, expecteds, actuals);
	}

	public static void assertOneSetOfOneEquals(final java.util.Set<byte[]> expecteds, final java.util.Set<byte[]> actuals) {
		assertOneSetOfOneEquals("OneSetOfOneBinary mismatch: ", expecteds, actuals);
	}

	private static void assertNullableSetOfOneEquals(final String message, final java.util.Set<byte[]> expecteds, final java.util.Set<byte[]> actuals) {
		if (expecteds == actuals) return;
		if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
		if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
		assertSetOfOneEquals(message, expecteds, actuals);
	}

	public static void assertNullableSetOfOneEquals(final java.util.Set<byte[]> expecteds, final java.util.Set<byte[]> actuals) {
		assertNullableSetOfOneEquals("NullableSetOfOneBinary mismatch: ", expecteds, actuals);
	}

	private static void assertSetOfNullableEquals(final String message, final java.util.Set<byte[]> expecteds, final java.util.Set<byte[]> actuals) {
		final int expectedsSize = expecteds.size();
		final int actualsSize = actuals.size();
		if (expectedsSize != actualsSize) {
			Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
		}

		expectedsLoop: for (final byte[] expected : expecteds) {
			if (actuals.contains(expected)) continue;
			for (final byte[] actual : actuals) {
				try {
					assertNullableEquals(expected, actual);
					continue expectedsLoop;
				}
				catch (final AssertionError e) {}
			}
			Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
		}
	}

	private static void assertOneSetOfNullableEquals(final String message, final java.util.Set<byte[]> expecteds, final java.util.Set<byte[]> actuals) {
		if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
		if (expecteds == actuals) return;
		if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
		assertSetOfNullableEquals(message, expecteds, actuals);
	}

	public static void assertOneSetOfNullableEquals(final java.util.Set<byte[]> expecteds, final java.util.Set<byte[]> actuals) {
		assertOneSetOfNullableEquals("OneSetOfNullableBinary mismatch: ", expecteds, actuals);
	}

	private static void assertNullableSetOfNullableEquals(final String message, final java.util.Set<byte[]> expecteds, final java.util.Set<byte[]> actuals) {
		if (expecteds == actuals) return;
		if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
		if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
		assertSetOfNullableEquals(message, expecteds, actuals);
	}

	public static void assertNullableSetOfNullableEquals(final java.util.Set<byte[]> expecteds, final java.util.Set<byte[]> actuals) {
		assertNullableSetOfNullableEquals("NullableSetOfNullableBinary mismatch: ", expecteds, actuals);
	}
}
