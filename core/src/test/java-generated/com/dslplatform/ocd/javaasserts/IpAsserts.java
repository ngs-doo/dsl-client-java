package com.dslplatform.ocd.javaasserts;

import org.junit.Assert;

public class IpAsserts {
    static void assertSingleEquals(final String message, final java.net.InetAddress expected, final java.net.InetAddress actual) {
        if (expected.equals(actual)) return;
        Assert.fail(message + "expected was \"" + expected + "\", but actual was \"" + actual + "\"");
    }

    static void assertOneEquals(final String message, final java.net.InetAddress expected, final java.net.InetAddress actual) {
        if (expected == null) Assert.fail(message + "expected was <null> - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
        if (expected == actual) return;
        if (actual == null) Assert.fail(message + "expected was \"" + expected + "\", but actual was <null>");
        assertSingleEquals(message, expected, actual);
    }

    public static void assertOneEquals(final java.net.InetAddress expected, final java.net.InetAddress actual) {
        assertOneEquals("OneIp mismatch: ", expected, actual);
    }

    private static void assertNullableEquals(final String message, final java.net.InetAddress expected, final java.net.InetAddress actual) {
        if (expected == actual) return;
        if (expected == null) Assert.fail(message + "expected was <null>, but actual was \"" + actual + "\"");
        if (actual == null) Assert.fail(message + "expected was \"" + expected + "\", but actual was <null>");
        assertSingleEquals(message, expected, actual);
    }

    public static void assertNullableEquals(final java.net.InetAddress expected, final java.net.InetAddress actual) {
        assertNullableEquals("NullableIp mismatch: ", expected, actual);
    }

    private static void assertArrayOfOneEquals(final String message, final java.net.InetAddress[] expecteds, final java.net.InetAddress[] actuals) {
        if (expecteds.length != actuals.length) {
            Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
        }

        for (int i = 0; i < expecteds.length; i++) {
            assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i]);
        }
    }

    private static void assertOneArrayOfOneEquals(final String message, final java.net.InetAddress[] expecteds, final java.net.InetAddress[] actuals) {
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

    public static void assertOneArrayOfOneEquals(final java.net.InetAddress[] expecteds, final java.net.InetAddress[] actuals) {
        assertOneArrayOfOneEquals("OneArrayOfOneIp mismatch: ", expecteds, actuals);
    }

    private static void assertNullableArrayOfOneEquals(final String message, final java.net.InetAddress[] expecteds, final java.net.InetAddress[] actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
        if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableArrayOfOneEquals(final java.net.InetAddress[] expecteds, final java.net.InetAddress[] actuals) {
        assertNullableArrayOfOneEquals("NullableArrayOfOneIp mismatch: ", expecteds, actuals);
    }

    private static void assertArrayOfNullableEquals(final String message, final java.net.InetAddress[] expecteds, final java.net.InetAddress[] actuals) {
        if (expecteds.length != actuals.length) {
            Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
        }

        for (int i = 0; i < expecteds.length; i++) {
            assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i]);
        }
    }

    private static void assertOneArrayOfNullableEquals(final String message, final java.net.InetAddress[] expecteds, final java.net.InetAddress[] actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneArrayOfNullableEquals(final java.net.InetAddress[] expecteds, final java.net.InetAddress[] actuals) {
        assertOneArrayOfNullableEquals("OneArrayOfNullableIp mismatch: ", expecteds, actuals);
    }

    private static void assertNullableArrayOfNullableEquals(final String message, final java.net.InetAddress[] expecteds, final java.net.InetAddress[] actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
        if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableArrayOfNullableEquals(final java.net.InetAddress[] expecteds, final java.net.InetAddress[] actuals) {
        assertNullableArrayOfNullableEquals("NullableArrayOfNullableIp mismatch: ", expecteds, actuals);
    }

    private static void assertListOfOneEquals(final String message, final java.util.List<java.net.InetAddress> expecteds, final java.util.List<java.net.InetAddress> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
        }

        final java.util.Iterator<java.net.InetAddress> expectedsIterator = expecteds.iterator();
        final java.util.Iterator<java.net.InetAddress> actualsIterator = actuals.iterator();
        for (int i = 0; i < expectedsSize; i++) {
            final java.net.InetAddress expected = expectedsIterator.next();
            final java.net.InetAddress actual = actualsIterator.next();
            assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expected, actual);
        }
    }

    private static void assertOneListOfOneEquals(final String message, final java.util.List<java.net.InetAddress> expecteds, final java.util.List<java.net.InetAddress> actuals) {
        int i = 0;
        for (final java.net.InetAddress expected : expecteds) {
            if (expected == null) {
                Assert.fail(message + "element mismatch occurred at index " + i + ": expected was <null> - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
            }
            i++;
        }
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfOneEquals(message, expecteds, actuals);
    }

    public static void assertOneListOfOneEquals(final java.util.List<java.net.InetAddress> expecteds, final java.util.List<java.net.InetAddress> actuals) {
        assertOneListOfOneEquals("OneListOfOneIp mismatch: ", expecteds, actuals);
    }

    private static void assertNullableListOfOneEquals(final String message, final java.util.List<java.net.InetAddress> expecteds, final java.util.List<java.net.InetAddress> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableListOfOneEquals(final java.util.List<java.net.InetAddress> expecteds, final java.util.List<java.net.InetAddress> actuals) {
        assertNullableListOfOneEquals("NullableListOfOneIp mismatch: ", expecteds, actuals);
    }

    private static void assertListOfNullableEquals(final String message, final java.util.List<java.net.InetAddress> expecteds, final java.util.List<java.net.InetAddress> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
        }

        final java.util.Iterator<java.net.InetAddress> expectedsIterator = expecteds.iterator();
        final java.util.Iterator<java.net.InetAddress> actualsIterator = actuals.iterator();
        for (int i = 0; i < expectedsSize; i++) {
            final java.net.InetAddress expected = expectedsIterator.next();
            final java.net.InetAddress actual = actualsIterator.next();
            assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expected, actual);
        }
    }

    private static void assertOneListOfNullableEquals(final String message, final java.util.List<java.net.InetAddress> expecteds, final java.util.List<java.net.InetAddress> actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneListOfNullableEquals(final java.util.List<java.net.InetAddress> expecteds, final java.util.List<java.net.InetAddress> actuals) {
        assertOneListOfNullableEquals("OneListOfNullableIp mismatch: ", expecteds, actuals);
    }

    private static void assertNullableListOfNullableEquals(final String message, final java.util.List<java.net.InetAddress> expecteds, final java.util.List<java.net.InetAddress> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableListOfNullableEquals(final java.util.List<java.net.InetAddress> expecteds, final java.util.List<java.net.InetAddress> actuals) {
        assertNullableListOfNullableEquals("NullableListOfNullableIp mismatch: ", expecteds, actuals);
    }

    private static void assertSetOfOneEquals(final String message, final java.util.Set<java.net.InetAddress> expecteds, final java.util.Set<java.net.InetAddress> actuals) {
        if (actuals.contains(null)) {
            Assert.fail(message + "actuals contained a <null> element");
        }

        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
        }

        for (final java.net.InetAddress expected : expecteds) {
            if (!actuals.contains(expected)) {
                Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
            }
        }
    }

    private static void assertOneSetOfOneEquals(final String message, final java.util.Set<java.net.InetAddress> expecteds, final java.util.Set<java.net.InetAddress> actuals) {
        if (expecteds.contains(null)) {
            Assert.fail(message + "expecteds contained a <null> element - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
        }
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfOneEquals(message, expecteds, actuals);
    }

    public static void assertOneSetOfOneEquals(final java.util.Set<java.net.InetAddress> expecteds, final java.util.Set<java.net.InetAddress> actuals) {
        assertOneSetOfOneEquals("OneSetOfOneIp mismatch: ", expecteds, actuals);
    }

    private static void assertNullableSetOfOneEquals(final String message, final java.util.Set<java.net.InetAddress> expecteds, final java.util.Set<java.net.InetAddress> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableSetOfOneEquals(final java.util.Set<java.net.InetAddress> expecteds, final java.util.Set<java.net.InetAddress> actuals) {
        assertNullableSetOfOneEquals("NullableSetOfOneIp mismatch: ", expecteds, actuals);
    }

    private static void assertSetOfNullableEquals(final String message, final java.util.Set<java.net.InetAddress> expecteds, final java.util.Set<java.net.InetAddress> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
        }

        for (final java.net.InetAddress expected : expecteds) {
            if (!actuals.contains(expected)) {
                Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
            }
        }
    }

    private static void assertOneSetOfNullableEquals(final String message, final java.util.Set<java.net.InetAddress> expecteds, final java.util.Set<java.net.InetAddress> actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneSetOfNullableEquals(final java.util.Set<java.net.InetAddress> expecteds, final java.util.Set<java.net.InetAddress> actuals) {
        assertOneSetOfNullableEquals("OneSetOfNullableIp mismatch: ", expecteds, actuals);
    }

    private static void assertNullableSetOfNullableEquals(final String message, final java.util.Set<java.net.InetAddress> expecteds, final java.util.Set<java.net.InetAddress> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableSetOfNullableEquals(final java.util.Set<java.net.InetAddress> expecteds, final java.util.Set<java.net.InetAddress> actuals) {
        assertNullableSetOfNullableEquals("NullableSetOfNullableIp mismatch: ", expecteds, actuals);
    }
}
