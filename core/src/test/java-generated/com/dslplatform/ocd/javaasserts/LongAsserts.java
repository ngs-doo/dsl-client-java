package com.dslplatform.ocd.javaasserts;

import org.junit.Assert;

public class LongAsserts {
    static void assertSingleEquals(final String message, final long expected, final long actual) {
        if (expected == actual) return;
        Assert.fail(message + "expected was \"" + expected + "\", but actual was \"" + actual + "\"");
    }

    static void assertOneEquals(final String message, final long expected, final long actual) {
        assertSingleEquals(message, expected, actual);
    }

    public static void assertOneEquals(final long expected, final long actual) {
        assertOneEquals("OneLong mismatch: ", expected, actual);
    }

    private static void assertNullableEquals(final String message, final Long expected, final Long actual) {
        if (expected == actual) return;
        if (expected == null) Assert.fail(message + "expected was <null>, but actual was \"" + actual + "\"");
        if (actual == null) Assert.fail(message + "expected was \"" + expected + "\", but actual was <null>");
        assertSingleEquals(message, expected, actual);
    }

    public static void assertNullableEquals(final Long expected, final Long actual) {
        assertNullableEquals("NullableLong mismatch: ", expected, actual);
    }

    private static void assertArrayOfOneEquals(final String message, final long[] expecteds, final long[] actuals) {
        if (expecteds.length != actuals.length) {
            Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
        }

        for (int i = 0; i < expecteds.length; i++) {
            assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i]);
        }
    }

    private static void assertOneArrayOfOneEquals(final String message, final long[] expecteds, final long[] actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfOneEquals(message, expecteds, actuals);
    }

    public static void assertOneArrayOfOneEquals(final long[] expecteds, final long[] actuals) {
        assertOneArrayOfOneEquals("OneArrayOfOneLong mismatch: ", expecteds, actuals);
    }

    private static void assertNullableArrayOfOneEquals(final String message, final long[] expecteds, final long[] actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
        if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableArrayOfOneEquals(final long[] expecteds, final long[] actuals) {
        assertNullableArrayOfOneEquals("NullableArrayOfOneLong mismatch: ", expecteds, actuals);
    }

    private static void assertArrayOfNullableEquals(final String message, final Long[] expecteds, final Long[] actuals) {
        if (expecteds.length != actuals.length) {
            Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
        }

        for (int i = 0; i < expecteds.length; i++) {
            assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i]);
        }
    }

    private static void assertOneArrayOfNullableEquals(final String message, final Long[] expecteds, final Long[] actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneArrayOfNullableEquals(final Long[] expecteds, final Long[] actuals) {
        assertOneArrayOfNullableEquals("OneArrayOfNullableLong mismatch: ", expecteds, actuals);
    }

    private static void assertNullableArrayOfNullableEquals(final String message, final Long[] expecteds, final Long[] actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
        if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableArrayOfNullableEquals(final Long[] expecteds, final Long[] actuals) {
        assertNullableArrayOfNullableEquals("NullableArrayOfNullableLong mismatch: ", expecteds, actuals);
    }

    private static void assertListOfOneEquals(final String message, final java.util.List<Long> expecteds, final java.util.List<Long> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
        }

        final java.util.Iterator<Long> expectedsIterator = expecteds.iterator();
        final java.util.Iterator<Long> actualsIterator = actuals.iterator();
        for (int i = 0; i < expectedsSize; i++) {
            final Long expected = expectedsIterator.next();
            final Long actual = actualsIterator.next();
            if (actual == null) {
                Assert.fail(message + "element mismatch occurred at index " + i + ": expected was \"" + expected + "\", but actual was <null>");
            }
            assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expected.longValue(), actual.longValue());
        }
    }

    private static void assertOneListOfOneEquals(final String message, final java.util.List<Long> expecteds, final java.util.List<Long> actuals) {
        int i = 0;
        for (final Long expected : expecteds) {
            if (expected == null) {
                Assert.fail(message + "element mismatch occurred at index " + i + ": expected was <null> - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
            }
            i++;
        }
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfOneEquals(message, expecteds, actuals);
    }

    public static void assertOneListOfOneEquals(final java.util.List<Long> expecteds, final java.util.List<Long> actuals) {
        assertOneListOfOneEquals("OneListOfOneLong mismatch: ", expecteds, actuals);
    }

    private static void assertNullableListOfOneEquals(final String message, final java.util.List<Long> expecteds, final java.util.List<Long> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableListOfOneEquals(final java.util.List<Long> expecteds, final java.util.List<Long> actuals) {
        assertNullableListOfOneEquals("NullableListOfOneLong mismatch: ", expecteds, actuals);
    }

    private static void assertListOfNullableEquals(final String message, final java.util.List<Long> expecteds, final java.util.List<Long> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
        }

        final java.util.Iterator<Long> expectedsIterator = expecteds.iterator();
        final java.util.Iterator<Long> actualsIterator = actuals.iterator();
        for (int i = 0; i < expectedsSize; i++) {
            final Long expected = expectedsIterator.next();
            final Long actual = actualsIterator.next();
            assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expected, actual);
        }
    }

    private static void assertOneListOfNullableEquals(final String message, final java.util.List<Long> expecteds, final java.util.List<Long> actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneListOfNullableEquals(final java.util.List<Long> expecteds, final java.util.List<Long> actuals) {
        assertOneListOfNullableEquals("OneListOfNullableLong mismatch: ", expecteds, actuals);
    }

    private static void assertNullableListOfNullableEquals(final String message, final java.util.List<Long> expecteds, final java.util.List<Long> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableListOfNullableEquals(final java.util.List<Long> expecteds, final java.util.List<Long> actuals) {
        assertNullableListOfNullableEquals("NullableListOfNullableLong mismatch: ", expecteds, actuals);
    }

    private static void assertSetOfOneEquals(final String message, final java.util.Set<Long> expecteds, final java.util.Set<Long> actuals) {
        if (actuals.contains(null)) {
            Assert.fail(message + "actuals contained a <null> element");
        }

        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
        }

        for (final Long expected : expecteds) {
            if (!actuals.contains(expected)) {
                Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
            }
        }
    }

    private static void assertOneSetOfOneEquals(final String message, final java.util.Set<Long> expecteds, final java.util.Set<Long> actuals) {
        if (expecteds.contains(null)) {
            Assert.fail(message + "expecteds contained a <null> element - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
        }
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfOneEquals(message, expecteds, actuals);
    }

    public static void assertOneSetOfOneEquals(final java.util.Set<Long> expecteds, final java.util.Set<Long> actuals) {
        assertOneSetOfOneEquals("OneSetOfOneLong mismatch: ", expecteds, actuals);
    }

    private static void assertNullableSetOfOneEquals(final String message, final java.util.Set<Long> expecteds, final java.util.Set<Long> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableSetOfOneEquals(final java.util.Set<Long> expecteds, final java.util.Set<Long> actuals) {
        assertNullableSetOfOneEquals("NullableSetOfOneLong mismatch: ", expecteds, actuals);
    }

    private static void assertSetOfNullableEquals(final String message, final java.util.Set<Long> expecteds, final java.util.Set<Long> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
        }

        for (final Long expected : expecteds) {
            if (!actuals.contains(expected)) {
                Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
            }
        }
    }

    private static void assertOneSetOfNullableEquals(final String message, final java.util.Set<Long> expecteds, final java.util.Set<Long> actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneSetOfNullableEquals(final java.util.Set<Long> expecteds, final java.util.Set<Long> actuals) {
        assertOneSetOfNullableEquals("OneSetOfNullableLong mismatch: ", expecteds, actuals);
    }

    private static void assertNullableSetOfNullableEquals(final String message, final java.util.Set<Long> expecteds, final java.util.Set<Long> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableSetOfNullableEquals(final java.util.Set<Long> expecteds, final java.util.Set<Long> actuals) {
        assertNullableSetOfNullableEquals("NullableSetOfNullableLong mismatch: ", expecteds, actuals);
    }
}
