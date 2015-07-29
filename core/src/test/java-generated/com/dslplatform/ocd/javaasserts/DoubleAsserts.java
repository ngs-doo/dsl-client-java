package com.dslplatform.ocd.javaasserts;

import org.junit.Assert;

public class DoubleAsserts {
    static void assertSingleEquals(final String message, final double expected, final double actual, final int ulps) {
        if (Double.doubleToRawLongBits(expected) == Double.doubleToRawLongBits(actual)) return;

        if (ulps == 0) {
            Assert.fail(message + "expected was \"" + expected + "\", but actual was \"" + actual + "\" - WARNING: You are comparing the bits of double values - not using an epsilon value!");
        }

        final double epsilon = Math.ulp(expected) * Math.abs(ulps);
        if (expected >= actual - epsilon && expected <= actual + epsilon) return;
        Assert.fail(message + "expected was \"" + expected + "\", but actual was \"" + actual + "\" (using epsilon value of \"" + epsilon + "\")");
    }

    static void assertOneEquals(final String message, final double expected, final double actual, final int ulps) {
        assertSingleEquals(message, expected, actual, ulps);
    }

    public static void assertOneEquals(final double expected, final double actual, final int ulps) {
        assertOneEquals("OneDouble mismatch: ", expected, actual, ulps);
    }

    public static void assertOneEquals(final double expected, final double actual) {
        assertOneEquals(expected, actual, 0);
    }

    private static void assertNullableEquals(final String message, final Double expected, final Double actual, final int ulps) {
        if (expected == actual) return;
        if (expected == null) Assert.fail(message + "expected was <null>, but actual was \"" + actual + "\"");
        if (actual == null) Assert.fail(message + "expected was \"" + expected + "\", but actual was <null>");
        assertSingleEquals(message, expected, actual, ulps);
    }

    public static void assertNullableEquals(final Double expected, final Double actual, final int ulps) {
        assertNullableEquals("NullableDouble mismatch: ", expected, actual, ulps);
    }

    public static void assertNullableEquals(final Double expected, final Double actual) {
        assertNullableEquals(expected, actual, 0);
    }

    private static void assertArrayOfOneEquals(final String message, final double[] expecteds, final double[] actuals, final int ulps) {
        if (expecteds.length != actuals.length) {
            Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
        }

        for (int i = 0; i < expecteds.length; i++) {
            assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i], ulps);
        }
    }

    private static void assertOneArrayOfOneEquals(final String message, final double[] expecteds, final double[] actuals, final int ulps) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfOneEquals(message, expecteds, actuals, ulps);
    }

    public static void assertOneArrayOfOneEquals(final double[] expecteds, final double[] actuals, final int ulps) {
        assertOneArrayOfOneEquals("OneArrayOfOneDouble mismatch: ", expecteds, actuals, ulps);
    }

    public static void assertOneArrayOfOneEquals(final double[] expecteds, final double[] actuals) {
        assertOneArrayOfOneEquals(expecteds, actuals, 0);
    }

    private static void assertNullableArrayOfOneEquals(final String message, final double[] expecteds, final double[] actuals, final int ulps) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
        if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfOneEquals(message, expecteds, actuals, ulps);
    }

    public static void assertNullableArrayOfOneEquals(final double[] expecteds, final double[] actuals, final int ulps) {
        assertNullableArrayOfOneEquals("NullableArrayOfOneDouble mismatch: ", expecteds, actuals, ulps);
    }

    public static void assertNullableArrayOfOneEquals(final double[] expecteds, final double[] actuals) {
        assertNullableArrayOfOneEquals(expecteds, actuals, 0);
    }

    private static void assertArrayOfNullableEquals(final String message, final Double[] expecteds, final Double[] actuals, final int ulps) {
        if (expecteds.length != actuals.length) {
            Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
        }

        for (int i = 0; i < expecteds.length; i++) {
            assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i], ulps);
        }
    }

    private static void assertOneArrayOfNullableEquals(final String message, final Double[] expecteds, final Double[] actuals, final int ulps) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfNullableEquals(message, expecteds, actuals, ulps);
    }

    public static void assertOneArrayOfNullableEquals(final Double[] expecteds, final Double[] actuals, final int ulps) {
        assertOneArrayOfNullableEquals("OneArrayOfNullableDouble mismatch: ", expecteds, actuals, ulps);
    }

    public static void assertOneArrayOfNullableEquals(final Double[] expecteds, final Double[] actuals) {
        assertOneArrayOfNullableEquals(expecteds, actuals, 0);
    }

    private static void assertNullableArrayOfNullableEquals(final String message, final Double[] expecteds, final Double[] actuals, final int ulps) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
        if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfNullableEquals(message, expecteds, actuals, ulps);
    }

    public static void assertNullableArrayOfNullableEquals(final Double[] expecteds, final Double[] actuals, final int ulps) {
        assertNullableArrayOfNullableEquals("NullableArrayOfNullableDouble mismatch: ", expecteds, actuals, ulps);
    }

    public static void assertNullableArrayOfNullableEquals(final Double[] expecteds, final Double[] actuals) {
        assertNullableArrayOfNullableEquals(expecteds, actuals, 0);
    }

    private static void assertListOfOneEquals(final String message, final java.util.List<Double> expecteds, final java.util.List<Double> actuals, final int ulps) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
        }

        final java.util.Iterator<Double> expectedsIterator = expecteds.iterator();
        final java.util.Iterator<Double> actualsIterator = actuals.iterator();
        for (int i = 0; i < expectedsSize; i++) {
            final Double expected = expectedsIterator.next();
            final Double actual = actualsIterator.next();
            if (actual == null) {
                Assert.fail(message + "element mismatch occurred at index " + i + ": expected was \"" + expected + "\", but actual was <null>");
            }
            assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expected.doubleValue(), actual.doubleValue(), ulps);
        }
    }

    private static void assertOneListOfOneEquals(final String message, final java.util.List<Double> expecteds, final java.util.List<Double> actuals, final int ulps) {
        int i = 0;
        for (final Double expected : expecteds) {
            if (expected == null) {
                Assert.fail(message + "element mismatch occurred at index " + i + ": expected was <null> - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
            }
            i++;
        }
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfOneEquals(message, expecteds, actuals, ulps);
    }

    public static void assertOneListOfOneEquals(final java.util.List<Double> expecteds, final java.util.List<Double> actuals, final int ulps) {
        assertOneListOfOneEquals("OneListOfOneDouble mismatch: ", expecteds, actuals, ulps);
    }

    public static void assertOneListOfOneEquals(final java.util.List<Double> expecteds, final java.util.List<Double> actuals) {
        assertOneListOfOneEquals(expecteds, actuals, 0);
    }

    private static void assertNullableListOfOneEquals(final String message, final java.util.List<Double> expecteds, final java.util.List<Double> actuals, final int ulps) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfOneEquals(message, expecteds, actuals, ulps);
    }

    public static void assertNullableListOfOneEquals(final java.util.List<Double> expecteds, final java.util.List<Double> actuals, final int ulps) {
        assertNullableListOfOneEquals("NullableListOfOneDouble mismatch: ", expecteds, actuals, ulps);
    }

    public static void assertNullableListOfOneEquals(final java.util.List<Double> expecteds, final java.util.List<Double> actuals) {
        assertNullableListOfOneEquals(expecteds, actuals, 0);
    }

    private static void assertListOfNullableEquals(final String message, final java.util.List<Double> expecteds, final java.util.List<Double> actuals, final int ulps) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
        }

        final java.util.Iterator<Double> expectedsIterator = expecteds.iterator();
        final java.util.Iterator<Double> actualsIterator = actuals.iterator();
        for (int i = 0; i < expectedsSize; i++) {
            final Double expected = expectedsIterator.next();
            final Double actual = actualsIterator.next();
            assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expected, actual, ulps);
        }
    }

    private static void assertOneListOfNullableEquals(final String message, final java.util.List<Double> expecteds, final java.util.List<Double> actuals, final int ulps) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfNullableEquals(message, expecteds, actuals, ulps);
    }

    public static void assertOneListOfNullableEquals(final java.util.List<Double> expecteds, final java.util.List<Double> actuals, final int ulps) {
        assertOneListOfNullableEquals("OneListOfNullableDouble mismatch: ", expecteds, actuals, ulps);
    }

    public static void assertOneListOfNullableEquals(final java.util.List<Double> expecteds, final java.util.List<Double> actuals) {
        assertOneListOfNullableEquals(expecteds, actuals, 0);
    }

    private static void assertNullableListOfNullableEquals(final String message, final java.util.List<Double> expecteds, final java.util.List<Double> actuals, final int ulps) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfNullableEquals(message, expecteds, actuals, ulps);
    }

    public static void assertNullableListOfNullableEquals(final java.util.List<Double> expecteds, final java.util.List<Double> actuals, final int ulps) {
        assertNullableListOfNullableEquals("NullableListOfNullableDouble mismatch: ", expecteds, actuals, ulps);
    }

    public static void assertNullableListOfNullableEquals(final java.util.List<Double> expecteds, final java.util.List<Double> actuals) {
        assertNullableListOfNullableEquals(expecteds, actuals, 0);
    }

    private static void assertSetOfOneEquals(final String message, final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals, final int ulps) {
        if (actuals.contains(null)) {
            Assert.fail(message + "actuals contained a <null> element");
        }

        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
        }

        expectedsLoop: for (final Double expected : expecteds) {
            if (actuals.contains(expected)) continue;
            for (final Double actual : actuals) {
                try {
                    assertOneEquals(expected.doubleValue(), actual.doubleValue(), ulps);
                    continue expectedsLoop;
                }
                catch (final AssertionError e) {}
            }
            Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
        }
    }

    private static void assertOneSetOfOneEquals(final String message, final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals, final int ulps) {
        if (expecteds.contains(null)) {
            Assert.fail(message + "expecteds contained a <null> element - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
        }
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfOneEquals(message, expecteds, actuals, ulps);
    }

    public static void assertOneSetOfOneEquals(final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals, final int ulps) {
        assertOneSetOfOneEquals("OneSetOfOneDouble mismatch: ", expecteds, actuals, ulps);
    }

    public static void assertOneSetOfOneEquals(final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals) {
        assertOneSetOfOneEquals(expecteds, actuals, 0);
    }

    private static void assertNullableSetOfOneEquals(final String message, final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals, final int ulps) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfOneEquals(message, expecteds, actuals, ulps);
    }

    public static void assertNullableSetOfOneEquals(final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals, final int ulps) {
        assertNullableSetOfOneEquals("NullableSetOfOneDouble mismatch: ", expecteds, actuals, ulps);
    }

    public static void assertNullableSetOfOneEquals(final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals) {
        assertNullableSetOfOneEquals(expecteds, actuals, 0);
    }

    private static void assertSetOfNullableEquals(final String message, final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals, final int ulps) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
        }

        expectedsLoop: for (final Double expected : expecteds) {
            if (actuals.contains(expected)) continue;
            for (final Double actual : actuals) {
                try {
                    assertNullableEquals(expected, actual, ulps);
                    continue expectedsLoop;
                }
                catch (final AssertionError e) {}
            }
            Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
        }
    }

    private static void assertOneSetOfNullableEquals(final String message, final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals, final int ulps) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfNullableEquals(message, expecteds, actuals, ulps);
    }

    public static void assertOneSetOfNullableEquals(final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals, final int ulps) {
        assertOneSetOfNullableEquals("OneSetOfNullableDouble mismatch: ", expecteds, actuals, ulps);
    }

    public static void assertOneSetOfNullableEquals(final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals) {
        assertOneSetOfNullableEquals(expecteds, actuals, 0);
    }

    private static void assertNullableSetOfNullableEquals(final String message, final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals, final int ulps) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfNullableEquals(message, expecteds, actuals, ulps);
    }

    public static void assertNullableSetOfNullableEquals(final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals, final int ulps) {
        assertNullableSetOfNullableEquals("NullableSetOfNullableDouble mismatch: ", expecteds, actuals, ulps);
    }

    public static void assertNullableSetOfNullableEquals(final java.util.Set<Double> expecteds, final java.util.Set<Double> actuals) {
        assertNullableSetOfNullableEquals(expecteds, actuals, 0);
    }
}
