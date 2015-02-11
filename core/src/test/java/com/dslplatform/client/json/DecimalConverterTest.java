package com.dslplatform.client.json;

import com.dslplatform.client.TestLogging;
import org.junit.Assert;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

public class DecimalConverterTest extends TestLogging {
	private static final String VALUES =
			"0e-0, 0e-1, 0e-12, 0e+0, 0e+1, 0e+12, 0E-0, 0E-1, 0E-12, 0E+0, 0E+1, 0E+12, 0.0e-0, 0.0e-1, 0.0e-12," +
			"0.0e+0, 0.0e+1, 0.0e+12, 0.0E-0, 0.0E-1, 0.0E-12, 0.0E+0, 0.0E+1, 0.0E+12, 0.12e-0, 0.12e-1, 0.12e-12," +
			"0.12e+0, 0.12e+1, 0.12e+12, 0.12E-0, 0.12E-1, 0.12E-12, 0.12E+0, 0.12E+1, 0.12E+12, 1e-0, 1e-1, 1e-12," +
			"1e+0, 1e+1, 1e+12, 1E-0, 1E-1, 1E-12, 1E+0, 1E+1, 1E+12, 1.0e-0, 1.0e-1, 1.0e-12, 1.0e+0, 1.0e+1, 1.0e+12," +
			"1.0E-0, 1.0E-1, 1.0E-12, 1.0E+0, 1.0E+1, 1.0E+12, 1.12e-0, 1.12e-1, 1.12e-12, 1.12e+0, 1.12e+1, 1.12e+12," +
			"1.12E-0, 1.12E-1, 1.12E-12, 1.12E+0, 1.12E+1, 1.12E+12, 12e-0, 12e-1, 12e-12, 12e+0, 12e+1, 12e+12, 12E-0," +
			"12E-1, 12E-12, 12E+0, 12E+1, 12E+12, 12.0e-0, 12.0e-1, 12.0e-12, 12.0e+0, 12.0e+1, 12.0e+12, 12.0E-0, 12.0E-1," +
			"12.0E-12, 12.0E+0, 12.0E+1, 12.0E+12, 12.12e-0, 12.12e-1, 12.12e-12, 12.12e+0, 12.12e+1, 12.12e+12, 12.12E-0," +
			"12.12E-1, 12.12E-12, 12.12E+0, 12.12E+1, 12.12E+12, -0e-0, -0e-1, -0e-12, -0e+0, -0e+1, -0e+12, -0E-0, -0E-1," +
			"-0E-12, -0E+0, -0E+1, -0E+12, -0.0e-0, -0.0e-1, -0.0e-12, -0.0e+0, -0.0e+1, -0.0e+12, -0.0E-0, -0.0E-1, -0.0E-12," +
			"-0.0E+0, -0.0E+1, -0.0E+12, -0.12e-0, -0.12e-1, -0.12e-12, -0.12e+0, -0.12e+1, -0.12e+12, -0.12E-0, -0.12E-1," +
			"-0.12E-12, -0.12E+0, -0.12E+1, -0.12E+12, -1e-0, -1e-1, -1e-12, -1e+0, -1e+1, -1e+12, -1E-0, -1E-1, -1E-12, -1E+0," +
			"-1E+1, -1E+12, -1.0e-0, -1.0e-1, -1.0e-12, -1.0e+0, -1.0e+1, -1.0e+12, -1.0E-0, -1.0E-1, -1.0E-12, -1.0E+0, -1.0E+1," +
			"-1.0E+12, -1.12e-0, -1.12e-1, -1.12e-12, -1.12e+0, -1.12e+1, -1.12e+12, -1.12E-0, -1.12E-1, -1.12E-12, -1.12E+0," +
			"-1.12E+1, -1.12E+12, -12e-0, -12e-1, -12e-12, -12e+0, -12e+1, -12e+12, -12E-0, -12E-1, -12E-12, -12E+0, -12E+1," +
			"-12E+12, -12.0e-0, -12.0e-1, -12.0e-12, -12.0e+0, -12.0e+1, -12.0e+12, -12.0E-0, -12.0E-1, -12.0E-12, -12.0E+0," +
			"-12.0E+1, -12.0E+12, -12.12e-0, -12.12e-1, -12.12e-12, -12.12e+0, -12.12e+1, -12.12e+12, -12.12E-0, -12.12E-1," +
			"-12.12E-12, -12.12E+0, -12.12E+1, -12.12E+12";

	@Test
	public void testSerialization() throws IOException {
		// setup
		final String[] values = VALUES.split(", *");
		final int count = values.length;

		final byte[] buf = new byte[1024];
		final JsonWriter jw = new JsonWriter(buf);

		for (int i = 0; i < count; i ++) {
			// setup
			final BigDecimal direct = new BigDecimal(values[i]);
			jw.reset();

			// serialization
			NumberConverter.serialize(direct, jw);

			// check
			final BigDecimal current = new BigDecimal(jw.toString());
			final boolean equality = direct.compareTo(current) == 0;
			if (!equality) {
				Assert.fail("Written BigDecimal was not equal to the test value; " + direct + " != " + current);
			}
		}
	}

	@Test
	public void testDeserialization() throws IOException {
		// setup
		final String[] values = VALUES.split(", *");
		final int count = values.length;

		final byte[] buf = VALUES.getBytes(Charset.forName("ISO-8859-1"));
		final JsonReader jr = new JsonReader(buf, null);

		// first digit in values
		Assert.assertEquals('0', jr.getNextToken());

		for (int i = 0; i < count; i ++) {
			if (i > 0) jr.getNextToken();

			// setup
			final BigDecimal direct = new BigDecimal(values[i]);

			// deserialiaztion
			final BigDecimal current = NumberConverter.deserializeDecimal(jr);

			//check
			final boolean equality = direct.compareTo(current) == 0;
			if (!equality) {
				Assert.fail("Parsed BigDecimal was not equal to the test value; " + direct + " != " + current);
			}
		}
	}
}
