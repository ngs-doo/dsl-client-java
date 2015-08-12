package com.dslplatform.client.json;

import com.dslplatform.client.TestLogging;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class StringConverterTest extends TestLogging {
	@Test
	public void testCharacterParsing() throws IOException {
		// setup
		final int from = Character.MIN_VALUE;
		final int to = Character.MAX_VALUE;
		final long range = to - from + 1;

		for (long value = from; value <= to; value++) {
			// log
			if ((value & 0xfff) == 0xfff) {
				final long progress = value - from;
				debug("Exhaustive character parsing test [%d,%d] (%.2f%%)", from, to, (float) progress * 100 / range);
			}

			// The Unicode standard permanently reserves these code point values for UTF-16 encoding of the high and low surrogates,
			// and they will never be assigned a character, so there should be no reason to encode them.
			if (value >= Character.MIN_SURROGATE && value <= Character.MAX_SURROGATE) continue;

			// Do not test BOM markers (unicode non-characters)
			if (value == 0xfffe || value == 0xffff) continue;

			// init
			final char ch = (char) value;

			final String escaped =
					ch == '\b' ? "\b" :
					ch == '\t' ? "\t" :
					ch == '\n' ? "\n" :
					ch == '\f' ? "\f" :
					ch == '\r' ? "\r" :
					ch == '\\' ? "\\\\" :
					ch == '"' ? "\\\"" : String.valueOf(ch);

			final String replaced =
					ch == '\b' ? "\\b" :
					ch == '\t' ? "\\t" :
					ch == '\n' ? "\\n" :
					ch == '\f' ? "\\f" :
					ch == '\r' ? "\\r" :
					ch == '/' ? "\\/" :
					ch == '\\' ? "\\\\" :
					ch == '"' ? "\\\"" : String.valueOf(ch);

			// Tests all four possible representations of a character, e.g.:
			// "codepoint [47] == escaped [/] == replaced [\/] == unicode (lower) [\u002f] == unicode (upper) [\u002F]"
			// "codepoint [92] == escaped [\\] == replaced [\\] == unicode (lower) [\u005c] == unicode (upper) [\u005C]"
			final String text = String.format(
					"\"codepoint [%d] == escaped [%s] == replaced [%s] == unicode (lower) [\\u%1$04x] == unicode (upper) [\\u%1$04X]\"",
					value,
					escaped,
					replaced);

			// deserialization
			final byte[] buf = text.getBytes("UTF-8");
			final JsonReader jr = new JsonReader(buf, null);
			Assert.assertEquals(jr.read(), '"');
			final String read = jr.readString();
			Assert.assertEquals(buf.length, jr.getCurrentIndex()); // test for end of stream

			// check without unicode escapes or replacements, as they will not be present in the result string
			final String expected = String.format(
					"codepoint [%d] == escaped [%s] == replaced [%2$s] == unicode (lower) [%2$s] == unicode (upper) [%2$s]",
					value,
					ch);

			Assert.assertEquals(expected, read);
		}
	}

	@Test
	public void testCharacterPrinting() throws IOException {
		// setup
		final byte[] buf = new byte[1024];
		final JsonWriter jw = new JsonWriter(buf);

		final int from = 0;
		final int to = Character.MAX_VALUE;
		final long range = to - from + 1;

		for (long value = from; value <= to; value++) {
			// log
			if ((value & 0xfff) == 0xfff) {
				final long progress = value - from;
				debug("Exhaustive character printing test [%d,%d] (%.2f%%)", from, to, (float) progress * 100 / range);
			}

			// The Unicode standard permanently reserves these code point values for UTF-16 encoding of the high and low surrogates,
			// and they will never be assigned a character, so there should be no reason to encode them.
			if (value >= Character.MIN_SURROGATE && value <= Character.MAX_SURROGATE) continue;

			// Do not test BOM markers (unicode non-characters)
			if (value == 0xfffe || value == 0xffff) continue;

			// init
			final char ch = (char) value;

			final String text = String.format(
					"codepoint [%d] == replaced [%c]",
					value,
					ch);

			// serialization
			jw.reset();
			jw.writeString(text);

			// check
			final String read = new String(buf, 0, jw.toBytes().length, "UTF-8");

			// solidus will not be escaped "/"
			// characters < 32 will be unicode escaped "\\u00.."
			final String replaced =
					ch == '\b' ? "\\b" :
					ch == '\t' ? "\\t" :
					ch == '\n' ? "\\n" :
					ch == '\f' ? "\\f" :
					ch == '\r' ? "\\r" :
					ch == '\\' ? "\\\\" :
					ch == '"' ? "\\\"" :
					ch < ' ' ? String.format("\\u%04X", value) : String.valueOf(ch);

			final String expected = String.format(
					"\"codepoint [%d] == replaced [%s]\"",
					value,
					replaced);

			Assert.assertEquals(expected, read);
		}
	}

/*
	@Test
	public void testCharacterPairParsing() throws IOException{
		// setup
		final int from = Character.MIN_SUPPLEMENTARY_CODE_POINT;
		final int to = Character.MAX_CODE_POINT;
		final long range = to - from + 1;

		for (long value = from; value <= to; value++) {
			// log
			if ((value & 0xfff) == 0xfff) {
				final long progress = value - from;
				debug("Exhaustive surrogate character pair parsing test [%d,%d] (%.2f%%)", from, to ,(float) progress * 100 / range);
			}

			final char[] pair = Character.toChars((int) value);
			final String text = String.format(
					"\"codepoint [%d] == inline [%c%c] == unicode (lower) [\\u%04x\\u%04x] == unicode (upper) [\\u%4$04X\\u%5$04X]\"",
					value,
					pair[0], pair[1],
					(int) pair[0], (int) pair[1]);

			// deserialization
			final byte[] buf = text.getBytes("UTF-8");
			final JsonReader jr = new JsonReader(buf, null);
			Assert.assertEquals(jr.read(), '"');
			final String read = jr.readString();
			Assert.assertEquals(buf.length, jr.getCurrentIndex()); // test for end of stream

			// check without unicode escapes, as they will not be present in the result string
			final String expected = String.format(
					"codepoint [%d] == inline [%c%c] == unicode (lower) [%2$c%3$c] == unicode (upper) [%2$c%3$c]",
					value,
					pair[0], pair[1]);

			Assert.assertEquals(expected, read);
		}
	}
*/
}
