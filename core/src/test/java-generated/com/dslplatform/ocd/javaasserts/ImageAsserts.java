package com.dslplatform.ocd.javaasserts;

import org.junit.Assert;

public class ImageAsserts {
    static void assertSingleEquals(final String message, final java.awt.image.BufferedImage expected, final java.awt.image.BufferedImage actual) {
        IntegerAsserts.assertSingleEquals(message + "comparing image width: ", expected.getWidth(), actual.getWidth());
        IntegerAsserts.assertSingleEquals(message + "comparing image height: ", expected.getHeight(), actual.getHeight());

        final int[] expectedRaster = expected.getRGB(0, 0, expected.getWidth(), expected.getHeight(), null, 0, expected.getWidth());
        final int[] actualRaster = actual.getRGB(0, 0, actual.getWidth(), actual.getHeight(), null, 0, actual.getWidth());
        if (java.util.Arrays.equals(expectedRaster, actualRaster)) return;

        Assert.fail(message + "image dimensions were identical, but pixel values are different");
    }

    static void assertOneEquals(final String message, final java.awt.image.BufferedImage expected, final java.awt.image.BufferedImage actual) {
        if (expected == null) Assert.fail(message + "expected was <null> - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
        if (expected == actual) return;
        if (actual == null) Assert.fail(message + "expected was \"" + expected + "\", but actual was <null>");
        assertSingleEquals(message, expected, actual);
    }

    public static void assertOneEquals(final java.awt.image.BufferedImage expected, final java.awt.image.BufferedImage actual) {
        assertOneEquals("OneImage mismatch: ", expected, actual);
    }

    private static void assertNullableEquals(final String message, final java.awt.image.BufferedImage expected, final java.awt.image.BufferedImage actual) {
        if (expected == actual) return;
        if (expected == null) Assert.fail(message + "expected was <null>, but actual was \"" + actual + "\"");
        if (actual == null) Assert.fail(message + "expected was \"" + expected + "\", but actual was <null>");
        assertSingleEquals(message, expected, actual);
    }

    public static void assertNullableEquals(final java.awt.image.BufferedImage expected, final java.awt.image.BufferedImage actual) {
        assertNullableEquals("NullableImage mismatch: ", expected, actual);
    }

    private static void assertArrayOfOneEquals(final String message, final java.awt.image.BufferedImage[] expecteds, final java.awt.image.BufferedImage[] actuals) {
        if (expecteds.length != actuals.length) {
            Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
        }

        for (int i = 0; i < expecteds.length; i++) {
            assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i]);
        }
    }

    private static void assertOneArrayOfOneEquals(final String message, final java.awt.image.BufferedImage[] expecteds, final java.awt.image.BufferedImage[] actuals) {
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

    public static void assertOneArrayOfOneEquals(final java.awt.image.BufferedImage[] expecteds, final java.awt.image.BufferedImage[] actuals) {
        assertOneArrayOfOneEquals("OneArrayOfOneImage mismatch: ", expecteds, actuals);
    }

    private static void assertNullableArrayOfOneEquals(final String message, final java.awt.image.BufferedImage[] expecteds, final java.awt.image.BufferedImage[] actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
        if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableArrayOfOneEquals(final java.awt.image.BufferedImage[] expecteds, final java.awt.image.BufferedImage[] actuals) {
        assertNullableArrayOfOneEquals("NullableArrayOfOneImage mismatch: ", expecteds, actuals);
    }

    private static void assertArrayOfNullableEquals(final String message, final java.awt.image.BufferedImage[] expecteds, final java.awt.image.BufferedImage[] actuals) {
        if (expecteds.length != actuals.length) {
            Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was an array of length " + actuals.length);
        }

        for (int i = 0; i < expecteds.length; i++) {
            assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expecteds[i], actuals[i]);
        }
    }

    private static void assertOneArrayOfNullableEquals(final String message, final java.awt.image.BufferedImage[] expecteds, final java.awt.image.BufferedImage[] actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneArrayOfNullableEquals(final java.awt.image.BufferedImage[] expecteds, final java.awt.image.BufferedImage[] actuals) {
        assertOneArrayOfNullableEquals("OneArrayOfNullableImage mismatch: ", expecteds, actuals);
    }

    private static void assertNullableArrayOfNullableEquals(final String message, final java.awt.image.BufferedImage[] expecteds, final java.awt.image.BufferedImage[] actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was an array of length " + actuals.length);
        if (actuals == null) Assert.fail(message + " expecteds was an array of length " + expecteds.length + ", but actuals was <null>");
        assertArrayOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableArrayOfNullableEquals(final java.awt.image.BufferedImage[] expecteds, final java.awt.image.BufferedImage[] actuals) {
        assertNullableArrayOfNullableEquals("NullableArrayOfNullableImage mismatch: ", expecteds, actuals);
    }

    private static void assertListOfOneEquals(final String message, final java.util.List<java.awt.image.BufferedImage> expecteds, final java.util.List<java.awt.image.BufferedImage> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
        }

        final java.util.Iterator<java.awt.image.BufferedImage> expectedsIterator = expecteds.iterator();
        final java.util.Iterator<java.awt.image.BufferedImage> actualsIterator = actuals.iterator();
        for (int i = 0; i < expectedsSize; i++) {
            final java.awt.image.BufferedImage expected = expectedsIterator.next();
            final java.awt.image.BufferedImage actual = actualsIterator.next();
            assertOneEquals(message + "element mismatch occurred at index " + i + ": ", expected, actual);
        }
    }

    private static void assertOneListOfOneEquals(final String message, final java.util.List<java.awt.image.BufferedImage> expecteds, final java.util.List<java.awt.image.BufferedImage> actuals) {
        int i = 0;
        for (final java.awt.image.BufferedImage expected : expecteds) {
            if (expected == null) {
                Assert.fail(message + "element mismatch occurred at index " + i + ": expected was <null> - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
            }
            i++;
        }
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfOneEquals(message, expecteds, actuals);
    }

    public static void assertOneListOfOneEquals(final java.util.List<java.awt.image.BufferedImage> expecteds, final java.util.List<java.awt.image.BufferedImage> actuals) {
        assertOneListOfOneEquals("OneListOfOneImage mismatch: ", expecteds, actuals);
    }

    private static void assertNullableListOfOneEquals(final String message, final java.util.List<java.awt.image.BufferedImage> expecteds, final java.util.List<java.awt.image.BufferedImage> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableListOfOneEquals(final java.util.List<java.awt.image.BufferedImage> expecteds, final java.util.List<java.awt.image.BufferedImage> actuals) {
        assertNullableListOfOneEquals("NullableListOfOneImage mismatch: ", expecteds, actuals);
    }

    private static void assertListOfNullableEquals(final String message, final java.util.List<java.awt.image.BufferedImage> expecteds, final java.util.List<java.awt.image.BufferedImage> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a list of size " + expectedsSize + ", but actuals was a list of size " + actualsSize);
        }

        final java.util.Iterator<java.awt.image.BufferedImage> expectedsIterator = expecteds.iterator();
        final java.util.Iterator<java.awt.image.BufferedImage> actualsIterator = actuals.iterator();
        for (int i = 0; i < expectedsSize; i++) {
            final java.awt.image.BufferedImage expected = expectedsIterator.next();
            final java.awt.image.BufferedImage actual = actualsIterator.next();
            assertNullableEquals(message + "element mismatch occurred at index " + i + ": ", expected, actual);
        }
    }

    private static void assertOneListOfNullableEquals(final String message, final java.util.List<java.awt.image.BufferedImage> expecteds, final java.util.List<java.awt.image.BufferedImage> actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneListOfNullableEquals(final java.util.List<java.awt.image.BufferedImage> expecteds, final java.util.List<java.awt.image.BufferedImage> actuals) {
        assertOneListOfNullableEquals("OneListOfNullableImage mismatch: ", expecteds, actuals);
    }

    private static void assertNullableListOfNullableEquals(final String message, final java.util.List<java.awt.image.BufferedImage> expecteds, final java.util.List<java.awt.image.BufferedImage> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a list of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a list of size " + expecteds.size() + ", but actuals was <null>");
        assertListOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableListOfNullableEquals(final java.util.List<java.awt.image.BufferedImage> expecteds, final java.util.List<java.awt.image.BufferedImage> actuals) {
        assertNullableListOfNullableEquals("NullableListOfNullableImage mismatch: ", expecteds, actuals);
    }

    private static void assertSetOfOneEquals(final String message, final java.util.Set<java.awt.image.BufferedImage> expecteds, final java.util.Set<java.awt.image.BufferedImage> actuals) {
        if (actuals.contains(null)) {
            Assert.fail(message + "actuals contained a <null> element");
        }

        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
        }

        expectedsLoop: for (final java.awt.image.BufferedImage expected : expecteds) {
            if (actuals.contains(expected)) continue;
            for (final java.awt.image.BufferedImage actual : actuals) {
                try {
                    assertOneEquals(expected, actual);
                    continue expectedsLoop;
                }
                catch (final AssertionError e) {}
            }
            Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
        }
    }

    private static void assertOneSetOfOneEquals(final String message, final java.util.Set<java.awt.image.BufferedImage> expecteds, final java.util.Set<java.awt.image.BufferedImage> actuals) {
        if (expecteds.contains(null)) {
            Assert.fail(message + "expecteds contained a <null> element - WARNING: This is a preconditions failure in expected, this assertion will never succeed!");
        }
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfOneEquals(message, expecteds, actuals);
    }

    public static void assertOneSetOfOneEquals(final java.util.Set<java.awt.image.BufferedImage> expecteds, final java.util.Set<java.awt.image.BufferedImage> actuals) {
        assertOneSetOfOneEquals("OneSetOfOneImage mismatch: ", expecteds, actuals);
    }

    private static void assertNullableSetOfOneEquals(final String message, final java.util.Set<java.awt.image.BufferedImage> expecteds, final java.util.Set<java.awt.image.BufferedImage> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfOneEquals(message, expecteds, actuals);
    }

    public static void assertNullableSetOfOneEquals(final java.util.Set<java.awt.image.BufferedImage> expecteds, final java.util.Set<java.awt.image.BufferedImage> actuals) {
        assertNullableSetOfOneEquals("NullableSetOfOneImage mismatch: ", expecteds, actuals);
    }

    private static void assertSetOfNullableEquals(final String message, final java.util.Set<java.awt.image.BufferedImage> expecteds, final java.util.Set<java.awt.image.BufferedImage> actuals) {
        final int expectedsSize = expecteds.size();
        final int actualsSize = actuals.size();
        if (expectedsSize != actualsSize) {
            Assert.fail(message + "expecteds was a set of size " + expectedsSize + ", but actuals was a set of size " + actualsSize);
        }

        expectedsLoop: for (final java.awt.image.BufferedImage expected : expecteds) {
            if (actuals.contains(expected)) continue;
            for (final java.awt.image.BufferedImage actual : actuals) {
                try {
                    assertNullableEquals(expected, actual);
                    continue expectedsLoop;
                }
                catch (final AssertionError e) {}
            }
            Assert.fail(message + "actuals did not contain the expecteds element \"" + expected + "\"");
        }
    }

    private static void assertOneSetOfNullableEquals(final String message, final java.util.Set<java.awt.image.BufferedImage> expecteds, final java.util.Set<java.awt.image.BufferedImage> actuals) {
        if (expecteds == null) Assert.fail(message + "expecteds was <null> - WARNING: This is a preconditions failure in expecteds, this assertion will never succeed!");
        if (expecteds == actuals) return;
        if (actuals == null) Assert.fail(message + "expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertOneSetOfNullableEquals(final java.util.Set<java.awt.image.BufferedImage> expecteds, final java.util.Set<java.awt.image.BufferedImage> actuals) {
        assertOneSetOfNullableEquals("OneSetOfNullableImage mismatch: ", expecteds, actuals);
    }

    private static void assertNullableSetOfNullableEquals(final String message, final java.util.Set<java.awt.image.BufferedImage> expecteds, final java.util.Set<java.awt.image.BufferedImage> actuals) {
        if (expecteds == actuals) return;
        if (expecteds == null) Assert.fail(message + "expecteds was <null>, but actuals was a set of size " + actuals.size());
        if (actuals == null) Assert.fail(message + " expecteds was a set of size " + expecteds.size() + ", but actuals was <null>");
        assertSetOfNullableEquals(message, expecteds, actuals);
    }

    public static void assertNullableSetOfNullableEquals(final java.util.Set<java.awt.image.BufferedImage> expecteds, final java.util.Set<java.awt.image.BufferedImage> actuals) {
        assertNullableSetOfNullableEquals("NullableSetOfNullableImage mismatch: ", expecteds, actuals);
    }
}
