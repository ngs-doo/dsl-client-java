package com.dslplatform.ocd.javaasserts;

import org.junit.Assert;

public class DecimalAsserts {
    static void assertSingleEquals(final String message, final java.math.BigDecimal expected, final java.math.BigDecimal actual) {
        if (expected == actual || expected.compareTo(actual) == 0) return;
        Assert.fail(message + "expected was \"" + expected + "\", but actual was \"" + actual + "\"");
    }

    static void assertOneEquals(final String message, final java.math.BigDecimal expected, final java.math.BigDecimal actual) {
        if (expected == null) Assert.fail(message + "expected was <null> - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
        if (expected == actual) return;
        if (actual == null) Assert.fail(message + "expected was \"" + expected + "\", but actual was <null>");
        assertSingleEquals(message, expected, actual);
    }

    public static void assertOneEquals(final java.math.BigDecimal expected, final java.math.BigDecimal actual) {
        assertOneEquals("OneDecimal mismatch: ", expected, actual);
    }

    private static void assertNullableEquals(final String message, final java.math.BigDecimal expected, final java.math.BigDecimal actual) {
        if (expected == actual) return;
        if (expected == null) Assert.fail(message + "expected was <null>, but actual was \"" + actual + "\"");
        if (actual == null) Assert.fail(message + "expected was \"" + expected + "\", but actual was <null>");
        assertSingleEquals(message, expected, actual);
    }

    public static void assertNullableEquals(final java.math.BigDecimal expected, final java.math.BigDecimal actual) {
        assertNullableEquals("NullableDecimal mismatch: ", expected, actual);
    }

    private static void assertArrayOfOneEquals(final String message, final java.math.BigDecimal[] expecteds, final java.math.BigDecimal[] actuals) {
        if (expecteds.length != actuals.length) {
            Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
        }

        for (int i = 0; i < expecteds.length; i++) {
            assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i]);
        }
    }

    private static void assertOneArrayOfOneEquals(final String message, final java.math.BigDecimal[] expecteds, final java.math.BigDecimal[] actuals) {
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

    public static void assertOneArrayOfOneEquals(final java.math.BigDecimal[] expecteds, final java.math.BigDecimal[] actuals) {
        assertOneArrayOfOneEquals("OneArrayOfOneDecimal mismatch: ", expecteds, actuals);
    }

    private static void assertNullableArrayOfOneEquals(final String message, final java.math.BigDecimal[] expecteds, final java.math.BigDecimal[] actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
        if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableArrayOfOneEquals(final java.math.BigDecimal[] expecteds, final java.math.BigDecimal[] actuals) {
        assertNullableArrayOfOneEquals("NullableArrayOfOneDecimal mismatch: ", expecteds, actuals);
    }

    private static void assertArrayOfNullableEquals(final String message, final java.math.BigDecimal[] expecteds, final java.math.BigDecimal[] actuals) {
        if (expecteds.length != actuals.length) {
            Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
        }

        for (int i = 0; i < expecteds.length; i++) {
            assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i]);
        }
    }

    private static void assertOneArrayOfNullableEquals(final String message, final java.math.BigDecimal[] expecteds, final java.math.BigDecimal[] actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneArrayOfNullableEquals(final java.math.BigDecimal[] expecteds, final java.math.BigDecimal[] actuals) {
        assertOneArrayOfNullableEquals("OneArrayOfNullableDecimal mismatch: ", expecteds, actuals);
    }

    private static void assertNullableArrayOfNullableEquals(final String message, final java.math.BigDecimal[] expecteds, final java.math.BigDecimal[] actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
        if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableArrayOfNullableEquals(final java.math.BigDecimal[] expecteds, final java.math.BigDecimal[] actuals) {
        assertNullableArrayOfNullableEquals("NullableArrayOfNullableDecimal mismatch: ", expecteds, actuals);
    }

    private static void assertListOfOneEquals(final String message, final java.util.List<java.math.BigDecimal> expecteds, final java.util.List<java.math.BigDecimal> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
        }

        final java.util.Iterator<java.math.BigDecimal> expectedsIterator = expecteds.iterator();
        final java.util.Iterator<java.math.BigDecimal> actualsIterator = actuals.iterator();
        for (int i = 0; i < expectedsSize; i++) {
            final java.math.BigDecimal expected = expectedsIterator.next();
            final java.math.BigDecimal actual = actualsIterator.next();
            assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expected, actual);
        }
    }

    private static void assertOneListOfOneEquals(final String message, final java.util.List<java.math.BigDecimal> expecteds, final java.util.List<java.math.BigDecimal> actuals) {
        int i = 0;
        for (final java.math.BigDecimal expected : expecteds) {
            if (expected == null) {
                Assert.fail(message + "element mismatch occurred at index " + i + ": expected was <null> - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
            }
            i++;
        }
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfOneEquals(message, expecteds, actuals);
    }

    public static void assertOneListOfOneEquals(final java.util.List<java.math.BigDecimal> expecteds, final java.util.List<java.math.BigDecimal> actuals) {
        assertOneListOfOneEquals("OneListOfOneDecimal mismatch: ", expecteds, actuals);
    }

    private static void assertNullableListOfOneEquals(final String message, final java.util.List<java.math.BigDecimal> expecteds, final java.util.List<java.math.BigDecimal> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableListOfOneEquals(final java.util.List<java.math.BigDecimal> expecteds, final java.util.List<java.math.BigDecimal> actuals) {
        assertNullableListOfOneEquals("NullableListOfOneDecimal mismatch: ", expecteds, actuals);
    }

    private static void assertListOfNullableEquals(final String message, final java.util.List<java.math.BigDecimal> expecteds, final java.util.List<java.math.BigDecimal> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
        }

        final java.util.Iterator<java.math.BigDecimal> expectedsIterator = expecteds.iterator();
        final java.util.Iterator<java.math.BigDecimal> actualsIterator = actuals.iterator();
        for (int i = 0; i < expectedsSize; i++) {
            final java.math.BigDecimal expected = expectedsIterator.next();
            final java.math.BigDecimal actual = actualsIterator.next();
            assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expected, actual);
        }
    }

    private static void assertOneListOfNullableEquals(final String message, final java.util.List<java.math.BigDecimal> expecteds, final java.util.List<java.math.BigDecimal> actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneListOfNullableEquals(final java.util.List<java.math.BigDecimal> expecteds, final java.util.List<java.math.BigDecimal> actuals) {
        assertOneListOfNullableEquals("OneListOfNullableDecimal mismatch: ", expecteds, actuals);
    }

    private static void assertNullableListOfNullableEquals(final String message, final java.util.List<java.math.BigDecimal> expecteds, final java.util.List<java.math.BigDecimal> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableListOfNullableEquals(final java.util.List<java.math.BigDecimal> expecteds, final java.util.List<java.math.BigDecimal> actuals) {
        assertNullableListOfNullableEquals("NullableListOfNullableDecimal mismatch: ", expecteds, actuals);
    }

    private static void assertSetOfOneEquals(final String message, final java.util.Set<java.math.BigDecimal> expecteds, final java.util.Set<java.math.BigDecimal> actuals) {
        if (actuals.contains(null)) {
            Assert.fail(message + "actuals contained a <null> element");
        }

        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
        }

        expectedsLoop: for (final java.math.BigDecimal expected : expecteds) {
            if (actuals.contains(expected)) continue;
            for (final java.math.BigDecimal actual : actuals) {
                try {
                    assertOneEquals(expected, actual);
                    continue expectedsLoop;
                }
                catch (final AssertionError e) {}
            }
            Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
        }
    }

    private static void assertOneSetOfOneEquals(final String message, final java.util.Set<java.math.BigDecimal> expecteds, final java.util.Set<java.math.BigDecimal> actuals) {
        if (expecteds.contains(null)) {
            Assert.fail(message + "expecteds contained a <null> element - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
        }
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfOneEquals(message, expecteds, actuals);
    }

    public static void assertOneSetOfOneEquals(final java.util.Set<java.math.BigDecimal> expecteds, final java.util.Set<java.math.BigDecimal> actuals) {
        assertOneSetOfOneEquals("OneSetOfOneDecimal mismatch: ", expecteds, actuals);
    }

    private static void assertNullableSetOfOneEquals(final String message, final java.util.Set<java.math.BigDecimal> expecteds, final java.util.Set<java.math.BigDecimal> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableSetOfOneEquals(final java.util.Set<java.math.BigDecimal> expecteds, final java.util.Set<java.math.BigDecimal> actuals) {
        assertNullableSetOfOneEquals("NullableSetOfOneDecimal mismatch: ", expecteds, actuals);
    }

    private static void assertSetOfNullableEquals(final String message, final java.util.Set<java.math.BigDecimal> expecteds, final java.util.Set<java.math.BigDecimal> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
        }

        expectedsLoop: for (final java.math.BigDecimal expected : expecteds) {
            if (actuals.contains(expected)) continue;
            for (final java.math.BigDecimal actual : actuals) {
                try {
                    assertNullableEquals(expected, actual);
                    continue expectedsLoop;
                }
                catch (final AssertionError e) {}
            }
            Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
        }
    }

    private static void assertOneSetOfNullableEquals(final String message, final java.util.Set<java.math.BigDecimal> expecteds, final java.util.Set<java.math.BigDecimal> actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneSetOfNullableEquals(final java.util.Set<java.math.BigDecimal> expecteds, final java.util.Set<java.math.BigDecimal> actuals) {
        assertOneSetOfNullableEquals("OneSetOfNullableDecimal mismatch: ", expecteds, actuals);
    }

    private static void assertNullableSetOfNullableEquals(final String message, final java.util.Set<java.math.BigDecimal> expecteds, final java.util.Set<java.math.BigDecimal> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableSetOfNullableEquals(final java.util.Set<java.math.BigDecimal> expecteds, final java.util.Set<java.math.BigDecimal> actuals) {
        assertNullableSetOfNullableEquals("NullableSetOfNullableDecimal mismatch: ", expecteds, actuals);
    }
}
