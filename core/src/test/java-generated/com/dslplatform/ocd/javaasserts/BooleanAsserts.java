package com.dslplatform.ocd.javaasserts;

import org.junit.Assert;

public class BooleanAsserts {
    static void assertSingleEquals(final String message, final boolean expected, final boolean actual) {
        if (expected == actual) return;
        Assert.fail(message + "expected was \"" + expected + "\", but actual was \"" + actual + "\"");
    }

    static void assertOneEquals(final String message, final boolean expected, final boolean actual) {
        assertSingleEquals(message, expected, actual);
    }

    public static void assertOneEquals(final boolean expected, final boolean actual) {
        assertOneEquals("OneBoolean mismatch: ", expected, actual);
    }

    private static void assertNullableEquals(final String message, final Boolean expected, final Boolean actual) {
        if (expected == actual) return;
        if (expected == null) Assert.fail(message + "expected was <null>, but actual was \"" + actual + "\"");
        if (actual == null) Assert.fail(message + "expected was \"" + expected + "\", but actual was <null>");
        assertSingleEquals(message, expected, actual);
    }

    public static void assertNullableEquals(final Boolean expected, final Boolean actual) {
        assertNullableEquals("NullableBoolean mismatch: ", expected, actual);
    }

    private static void assertArrayOfOneEquals(final String message, final boolean[] expecteds, final boolean[] actuals) {
        if (expecteds.length != actuals.length) {
            Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
        }

        for (int i = 0; i < expecteds.length; i++) {
            assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i]);
        }
    }

    private static void assertOneArrayOfOneEquals(final String message, final boolean[] expecteds, final boolean[] actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfOneEquals(message, expecteds, actuals);
    }

    public static void assertOneArrayOfOneEquals(final boolean[] expecteds, final boolean[] actuals) {
        assertOneArrayOfOneEquals("OneArrayOfOneBoolean mismatch: ", expecteds, actuals);
    }

    private static void assertNullableArrayOfOneEquals(final String message, final boolean[] expecteds, final boolean[] actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
        if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableArrayOfOneEquals(final boolean[] expecteds, final boolean[] actuals) {
        assertNullableArrayOfOneEquals("NullableArrayOfOneBoolean mismatch: ", expecteds, actuals);
    }

    private static void assertArrayOfNullableEquals(final String message, final Boolean[] expecteds, final Boolean[] actuals) {
        if (expecteds.length != actuals.length) {
            Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
        }

        for (int i = 0; i < expecteds.length; i++) {
            assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i]);
        }
    }

    private static void assertOneArrayOfNullableEquals(final String message, final Boolean[] expecteds, final Boolean[] actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneArrayOfNullableEquals(final Boolean[] expecteds, final Boolean[] actuals) {
        assertOneArrayOfNullableEquals("OneArrayOfNullableBoolean mismatch: ", expecteds, actuals);
    }

    private static void assertNullableArrayOfNullableEquals(final String message, final Boolean[] expecteds, final Boolean[] actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
        if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableArrayOfNullableEquals(final Boolean[] expecteds, final Boolean[] actuals) {
        assertNullableArrayOfNullableEquals("NullableArrayOfNullableBoolean mismatch: ", expecteds, actuals);
    }

    private static void assertListOfOneEquals(final String message, final java.util.List<Boolean> expecteds, final java.util.List<Boolean> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
        }

        final java.util.Iterator<Boolean> expectedsIterator = expecteds.iterator();
        final java.util.Iterator<Boolean> actualsIterator = actuals.iterator();
        for (int i = 0; i < expectedsSize; i++) {
            final Boolean expected = expectedsIterator.next();
            final Boolean actual = actualsIterator.next();
            if (actual == null) {
                Assert.fail(message + "element mismatch occurred at index " + i + ": expected was \"" + expected + "\", but actual was <null>");
            }
            assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expected.booleanValue(), actual.booleanValue());
        }
    }

    private static void assertOneListOfOneEquals(final String message, final java.util.List<Boolean> expecteds, final java.util.List<Boolean> actuals) {
        int i = 0;
        for (final Boolean expected : expecteds) {
            if (expected == null) {
                Assert.fail(message + "element mismatch occurred at index " + i + ": expected was <null> - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
            }
            i++;
        }
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfOneEquals(message, expecteds, actuals);
    }

    public static void assertOneListOfOneEquals(final java.util.List<Boolean> expecteds, final java.util.List<Boolean> actuals) {
        assertOneListOfOneEquals("OneListOfOneBoolean mismatch: ", expecteds, actuals);
    }

    private static void assertNullableListOfOneEquals(final String message, final java.util.List<Boolean> expecteds, final java.util.List<Boolean> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableListOfOneEquals(final java.util.List<Boolean> expecteds, final java.util.List<Boolean> actuals) {
        assertNullableListOfOneEquals("NullableListOfOneBoolean mismatch: ", expecteds, actuals);
    }

    private static void assertListOfNullableEquals(final String message, final java.util.List<Boolean> expecteds, final java.util.List<Boolean> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
        }

        final java.util.Iterator<Boolean> expectedsIterator = expecteds.iterator();
        final java.util.Iterator<Boolean> actualsIterator = actuals.iterator();
        for (int i = 0; i < expectedsSize; i++) {
            final Boolean expected = expectedsIterator.next();
            final Boolean actual = actualsIterator.next();
            assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expected, actual);
        }
    }

    private static void assertOneListOfNullableEquals(final String message, final java.util.List<Boolean> expecteds, final java.util.List<Boolean> actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneListOfNullableEquals(final java.util.List<Boolean> expecteds, final java.util.List<Boolean> actuals) {
        assertOneListOfNullableEquals("OneListOfNullableBoolean mismatch: ", expecteds, actuals);
    }

    private static void assertNullableListOfNullableEquals(final String message, final java.util.List<Boolean> expecteds, final java.util.List<Boolean> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableListOfNullableEquals(final java.util.List<Boolean> expecteds, final java.util.List<Boolean> actuals) {
        assertNullableListOfNullableEquals("NullableListOfNullableBoolean mismatch: ", expecteds, actuals);
    }

    private static void assertSetOfOneEquals(final String message, final java.util.Set<Boolean> expecteds, final java.util.Set<Boolean> actuals) {
        if (actuals.contains(null)) {
            Assert.fail(message + "actuals contained a <null> element");
        }

        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
        }

        for (final Boolean expected : expecteds) {
            if (!actuals.contains(expected)) {
                Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
            }
        }
    }

    private static void assertOneSetOfOneEquals(final String message, final java.util.Set<Boolean> expecteds, final java.util.Set<Boolean> actuals) {
        if (expecteds.contains(null)) {
            Assert.fail(message + "expecteds contained a <null> element - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
        }
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfOneEquals(message, expecteds, actuals);
    }

    public static void assertOneSetOfOneEquals(final java.util.Set<Boolean> expecteds, final java.util.Set<Boolean> actuals) {
        assertOneSetOfOneEquals("OneSetOfOneBoolean mismatch: ", expecteds, actuals);
    }

    private static void assertNullableSetOfOneEquals(final String message, final java.util.Set<Boolean> expecteds, final java.util.Set<Boolean> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableSetOfOneEquals(final java.util.Set<Boolean> expecteds, final java.util.Set<Boolean> actuals) {
        assertNullableSetOfOneEquals("NullableSetOfOneBoolean mismatch: ", expecteds, actuals);
    }

    private static void assertSetOfNullableEquals(final String message, final java.util.Set<Boolean> expecteds, final java.util.Set<Boolean> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
        }

        for (final Boolean expected : expecteds) {
            if (!actuals.contains(expected)) {
                Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
            }
        }
    }

    private static void assertOneSetOfNullableEquals(final String message, final java.util.Set<Boolean> expecteds, final java.util.Set<Boolean> actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneSetOfNullableEquals(final java.util.Set<Boolean> expecteds, final java.util.Set<Boolean> actuals) {
        assertOneSetOfNullableEquals("OneSetOfNullableBoolean mismatch: ", expecteds, actuals);
    }

    private static void assertNullableSetOfNullableEquals(final String message, final java.util.Set<Boolean> expecteds, final java.util.Set<Boolean> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableSetOfNullableEquals(final java.util.Set<Boolean> expecteds, final java.util.Set<Boolean> actuals) {
        assertNullableSetOfNullableEquals("NullableSetOfNullableBoolean mismatch: ", expecteds, actuals);
    }
}
