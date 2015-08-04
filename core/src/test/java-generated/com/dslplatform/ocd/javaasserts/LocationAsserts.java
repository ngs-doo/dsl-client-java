package com.dslplatform.ocd.javaasserts;

import org.junit.Assert;

public class LocationAsserts {
	static void assertSingleEquals(final String message, final java.awt.geom.Point2D expected, final java.awt.geom.Point2D actual, final int ulps) {
		FloatAsserts.assertOneEquals(message + ": comparing X coordinate: ", (float) expected.getX(), (float) actual.getX(), ulps);
		FloatAsserts.assertOneEquals(message + ": comparing Y coordinate: ", (float) expected.getY(), (float) actual.getY(), ulps);
	}

	static void assertOneEquals(final String message, final java.awt.geom.Point2D expected, final java.awt.geom.Point2D actual, final int ulps) {
		if (expected == null) Assert.fail(message + "expected was <null> - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
		if (expected == actual) return;
		if (actual == null) Assert.fail(message + "expected was \"" + expected + "\", but actual was <null>");
		assertSingleEquals(message, expected, actual, ulps);
	}

	public static void assertOneEquals(final java.awt.geom.Point2D expected, final java.awt.geom.Point2D actual, final int ulps) {
		assertOneEquals("OneLocation mismatch: ", expected, actual, ulps);
	}

	public static void assertOneEquals(final java.awt.geom.Point2D expected, final java.awt.geom.Point2D actual) {
		assertOneEquals(expected, actual, 0);
	}

	private static void assertNullableEquals(final String message, final java.awt.geom.Point2D expected, final java.awt.geom.Point2D actual, final int ulps) {
		if (expected == actual) return;
		if (expected == null) Assert.fail(message + "expected was <null>, but actual was \"" + actual + "\"");
		if (actual == null) Assert.fail(message + "expected was \"" + expected + "\", but actual was <null>");
		assertSingleEquals(message, expected, actual, ulps);
	}

	public static void assertNullableEquals(final java.awt.geom.Point2D expected, final java.awt.geom.Point2D actual, final int ulps) {
		assertNullableEquals("NullableLocation mismatch: ", expected, actual, ulps);
	}

	public static void assertNullableEquals(final java.awt.geom.Point2D expected, final java.awt.geom.Point2D actual) {
		assertNullableEquals(expected, actual, 0);
	}

	private static void assertArrayOfOneEquals(final String message, final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals, final int ulps) {
		if (expecteds.length != actuals.length) {
			Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
		}

		for (int i = 0; i < expecteds.length; i++) {
			assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i], ulps);
		}
	}

	private static void assertOneArrayOfOneEquals(final String message, final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals, final int ulps) {
		if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
		for (int i = 0; i < expecteds.length; i ++) {
			if (expecteds[i] == null) {
				Assert.fail(message + "expecteds contained a <null> element at index " + i + " - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
			}
		}
		if (expecteds == actuals) return;
		if (actuals == null) Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
		assertArrayOfOneEquals(message, expecteds, actuals, ulps);
	}

	public static void assertOneArrayOfOneEquals(final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals, final int ulps) {
		assertOneArrayOfOneEquals("OneArrayOfOneLocation mismatch: ", expecteds, actuals, ulps);
	}

	public static void assertOneArrayOfOneEquals(final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals) {
		assertOneArrayOfOneEquals(expecteds, actuals, 0);
	}

	private static void assertNullableArrayOfOneEquals(final String message, final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals, final int ulps) {
		if (expecteds == actuals) return;
		if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
		if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
		assertArrayOfOneEquals(message, expecteds, actuals, ulps);
	}

	public static void assertNullableArrayOfOneEquals(final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals, final int ulps) {
		assertNullableArrayOfOneEquals("NullableArrayOfOneLocation mismatch: ", expecteds, actuals, ulps);
	}

	public static void assertNullableArrayOfOneEquals(final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals) {
		assertNullableArrayOfOneEquals(expecteds, actuals, 0);
	}

	private static void assertArrayOfNullableEquals(final String message, final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals, final int ulps) {
		if (expecteds.length != actuals.length) {
			Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
		}

		for (int i = 0; i < expecteds.length; i++) {
			assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i], ulps);
		}
	}

	private static void assertOneArrayOfNullableEquals(final String message, final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals, final int ulps) {
		if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
		if (expecteds == actuals) return;
		if (actuals == null) Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
		assertArrayOfNullableEquals(message, expecteds, actuals, ulps);
	}

	public static void assertOneArrayOfNullableEquals(final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals, final int ulps) {
		assertOneArrayOfNullableEquals("OneArrayOfNullableLocation mismatch: ", expecteds, actuals, ulps);
	}

	public static void assertOneArrayOfNullableEquals(final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals) {
		assertOneArrayOfNullableEquals(expecteds, actuals, 0);
	}

	private static void assertNullableArrayOfNullableEquals(final String message, final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals, final int ulps) {
		if (expecteds == actuals) return;
		if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
		if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
		assertArrayOfNullableEquals(message, expecteds, actuals, ulps);
	}

	public static void assertNullableArrayOfNullableEquals(final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals, final int ulps) {
		assertNullableArrayOfNullableEquals("NullableArrayOfNullableLocation mismatch: ", expecteds, actuals, ulps);
	}

	public static void assertNullableArrayOfNullableEquals(final java.awt.geom.Point2D[] expecteds, final java.awt.geom.Point2D[] actuals) {
		assertNullableArrayOfNullableEquals(expecteds, actuals, 0);
	}

	private static void assertListOfOneEquals(final String message, final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals, final int ulps) {
		final int expectedsSize = expecteds.size();
		final int actualsSize = actuals.size();
		if (expectedsSize != actualsSize) {
			Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
		}

		final java.util.Iterator<java.awt.geom.Point2D> expectedsIterator = expecteds.iterator();
		final java.util.Iterator<java.awt.geom.Point2D> actualsIterator = actuals.iterator();
		for (int i = 0; i < expectedsSize; i++) {
			final java.awt.geom.Point2D expected = expectedsIterator.next();
			final java.awt.geom.Point2D actual = actualsIterator.next();
			assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expected, actual, ulps);
		}
	}

	private static void assertOneListOfOneEquals(final String message, final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals, final int ulps) {
		int i = 0;
		for (final java.awt.geom.Point2D expected : expecteds) {
			if (expected == null) {
				Assert.fail(message + "element mismatch occurred at index " + i + ": expected was <null> - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
			}
			i++;
		}
		if (expecteds == actuals) return;
		if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
		assertListOfOneEquals(message, expecteds, actuals, ulps);
	}

	public static void assertOneListOfOneEquals(final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals, final int ulps) {
		assertOneListOfOneEquals("OneListOfOneLocation mismatch: ", expecteds, actuals, ulps);
	}

	public static void assertOneListOfOneEquals(final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals) {
		assertOneListOfOneEquals(expecteds, actuals, 0);
	}

	private static void assertNullableListOfOneEquals(final String message, final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals, final int ulps) {
		if (expecteds == actuals) return;
		if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
		if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
		assertListOfOneEquals(message, expecteds, actuals, ulps);
	}

	public static void assertNullableListOfOneEquals(final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals, final int ulps) {
		assertNullableListOfOneEquals("NullableListOfOneLocation mismatch: ", expecteds, actuals, ulps);
	}

	public static void assertNullableListOfOneEquals(final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals) {
		assertNullableListOfOneEquals(expecteds, actuals, 0);
	}

	private static void assertListOfNullableEquals(final String message, final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals, final int ulps) {
		final int expectedsSize = expecteds.size();
		final int actualsSize = actuals.size();
		if (expectedsSize != actualsSize) {
			Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
		}

		final java.util.Iterator<java.awt.geom.Point2D> expectedsIterator = expecteds.iterator();
		final java.util.Iterator<java.awt.geom.Point2D> actualsIterator = actuals.iterator();
		for (int i = 0; i < expectedsSize; i++) {
			final java.awt.geom.Point2D expected = expectedsIterator.next();
			final java.awt.geom.Point2D actual = actualsIterator.next();
			assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expected, actual, ulps);
		}
	}

	private static void assertOneListOfNullableEquals(final String message, final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals, final int ulps) {
		if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
		if (expecteds == actuals) return;
		if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
		assertListOfNullableEquals(message, expecteds, actuals, ulps);
	}

	public static void assertOneListOfNullableEquals(final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals, final int ulps) {
		assertOneListOfNullableEquals("OneListOfNullableLocation mismatch: ", expecteds, actuals, ulps);
	}

	public static void assertOneListOfNullableEquals(final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals) {
		assertOneListOfNullableEquals(expecteds, actuals, 0);
	}

	private static void assertNullableListOfNullableEquals(final String message, final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals, final int ulps) {
		if (expecteds == actuals) return;
		if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
		if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
		assertListOfNullableEquals(message, expecteds, actuals, ulps);
	}

	public static void assertNullableListOfNullableEquals(final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals, final int ulps) {
		assertNullableListOfNullableEquals("NullableListOfNullableLocation mismatch: ", expecteds, actuals, ulps);
	}

	public static void assertNullableListOfNullableEquals(final java.util.List<java.awt.geom.Point2D> expecteds, final java.util.List<java.awt.geom.Point2D> actuals) {
		assertNullableListOfNullableEquals(expecteds, actuals, 0);
	}

	private static void assertSetOfOneEquals(final String message, final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals, final int ulps) {
		if (actuals.contains(null)) {
			Assert.fail(message + "actuals contained a <null> element");
		}

		final int expectedsSize = expecteds.size();
		final int actualsSize = actuals.size();
		if (expectedsSize != actualsSize) {
			Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
		}

		expectedsLoop: for (final java.awt.geom.Point2D expected : expecteds) {
			if (actuals.contains(expected)) continue;
			for (final java.awt.geom.Point2D actual : actuals) {
				try {
					assertOneEquals(expected, actual, ulps);
					continue expectedsLoop;
				}
				catch (final AssertionError e) {}
			}
			Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
		}
	}

	private static void assertOneSetOfOneEquals(final String message, final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals, final int ulps) {
		if (expecteds.contains(null)) {
			Assert.fail(message + "expecteds contained a <null> element - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
		}
		if (expecteds == actuals) return;
		if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
		assertSetOfOneEquals(message, expecteds, actuals, ulps);
	}

	public static void assertOneSetOfOneEquals(final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals, final int ulps) {
		assertOneSetOfOneEquals("OneSetOfOneLocation mismatch: ", expecteds, actuals, ulps);
	}

	public static void assertOneSetOfOneEquals(final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals) {
		assertOneSetOfOneEquals(expecteds, actuals, 0);
	}

	private static void assertNullableSetOfOneEquals(final String message, final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals, final int ulps) {
		if (expecteds == actuals) return;
		if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
		if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
		assertSetOfOneEquals(message, expecteds, actuals, ulps);
	}

	public static void assertNullableSetOfOneEquals(final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals, final int ulps) {
		assertNullableSetOfOneEquals("NullableSetOfOneLocation mismatch: ", expecteds, actuals, ulps);
	}

	public static void assertNullableSetOfOneEquals(final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals) {
		assertNullableSetOfOneEquals(expecteds, actuals, 0);
	}

	private static void assertSetOfNullableEquals(final String message, final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals, final int ulps) {
		final int expectedsSize = expecteds.size();
		final int actualsSize = actuals.size();
		if (expectedsSize != actualsSize) {
			Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
		}

		expectedsLoop: for (final java.awt.geom.Point2D expected : expecteds) {
			if (actuals.contains(expected)) continue;
			for (final java.awt.geom.Point2D actual : actuals) {
				try {
					assertNullableEquals(expected, actual, ulps);
					continue expectedsLoop;
				}
				catch (final AssertionError e) {}
			}
			Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
		}
	}

	private static void assertOneSetOfNullableEquals(final String message, final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals, final int ulps) {
		if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
		if (expecteds == actuals) return;
		if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
		assertSetOfNullableEquals(message, expecteds, actuals, ulps);
	}

	public static void assertOneSetOfNullableEquals(final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals, final int ulps) {
		assertOneSetOfNullableEquals("OneSetOfNullableLocation mismatch: ", expecteds, actuals, ulps);
	}

	public static void assertOneSetOfNullableEquals(final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals) {
		assertOneSetOfNullableEquals(expecteds, actuals, 0);
	}

	private static void assertNullableSetOfNullableEquals(final String message, final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals, final int ulps) {
		if (expecteds == actuals) return;
		if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
		if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
		assertSetOfNullableEquals(message, expecteds, actuals, ulps);
	}

	public static void assertNullableSetOfNullableEquals(final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals, final int ulps) {
		assertNullableSetOfNullableEquals("NullableSetOfNullableLocation mismatch: ", expecteds, actuals, ulps);
	}

	public static void assertNullableSetOfNullableEquals(final java.util.Set<java.awt.geom.Point2D> expecteds, final java.util.Set<java.awt.geom.Point2D> actuals) {
		assertNullableSetOfNullableEquals(expecteds, actuals, 0);
	}
}
