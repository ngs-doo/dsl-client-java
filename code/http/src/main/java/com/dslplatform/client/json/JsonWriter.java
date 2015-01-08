package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;

public class JsonWriter extends Writer {

	public final char[] tmp = new char[48];
	private byte[] result;
	private int position;
	private ByteBuffer resultBuffer;

	private final Charset utf8 = Charset.forName("UTF-8");
	private final CharsetEncoder encoder = utf8.newEncoder();

	public JsonWriter() {
		this(512);
	}

	public JsonWriter(final int size) {
		this(new byte[size]);
	}

	public JsonWriter(final byte[] result) {
		this.result = result;
		this.resultBuffer = ByteBuffer.wrap(result);
	}

	private static final byte[] NULL = new byte[] { 'n', 'u', 'l', 'l' };
	public static final byte OBJECT_START = '{';
	public static final byte OBJECT_END = '}';
	public static final byte ARRAY_START = '[';
	public static final byte ARRAY_END = ']';
	public static final byte COMMA = ',';
	public static final byte SEMI = ':';
	public static final byte QUOTE = '"';

	public final void writeNull() {
		System.arraycopy(NULL, 0, result, position, 4);
		position += 4;
	}

	public final void writeByte(final byte c) {
		if (position == result.length) {
			result = Arrays.copyOf(result, result.length + result.length / 2);
		}
		result[position++] = c;
	}

	public final void writeString(final String str, final int off, final int len) {
		if (position + len * 4 >= result.length) {
			result = Arrays.copyOf(result, result.length + result.length / 2 + len * 4);
			resultBuffer = ByteBuffer.wrap(result);
		} else if (resultBuffer.limit() != result.length) {
			resultBuffer = ByteBuffer.wrap(result);
		}
		resultBuffer.position(position);
		final CharBuffer cb = CharBuffer.wrap(str, off, len + off);

		try {
			encoder.reset();
			CoderResult cr = encoder.encode(cb, resultBuffer, true);
			if (!cr.isUnderflow()) {
				cr.throwException();
			}
			cr = encoder.flush(resultBuffer);
			if (!cr.isUnderflow()) {
				cr.throwException();
			}
			position = resultBuffer.position();
		} catch (CharacterCodingException x) {
			throw new Error(x);
		}
	}

	public final void writeAscii(final char[] buf, final int off, final int len) {
		if (position + len >= result.length) {
			result = Arrays.copyOf(result, result.length + result.length / 2 + len);
		}
		final int total = off + len;
		for(int i = off; i < total; i++) {
			result[position++] = (byte)buf[i];
		}
	}

	public final void writeAscii(final String str) {
		if (position + str.length() >= result.length) {
			result = Arrays.copyOf(result, result.length + result.length / 2 + str.length());
		}
		for(int i = 0; i < str.length(); i++) {
			result[position++] = (byte)str.charAt(i);
		}
	}

	public final void writeBinary(final byte[] buf) {
		if (position + buf.length * 2 + 2 >= result.length) {
			result = Arrays.copyOf(result, result.length + result.length / 2 + buf.length * 2 + 2);
		}
		result[position++] = '"';
		position += Base64.encodeToBytes(buf, result, position);
		result[position++] = '"';
	}

	@Override
	public String toString() {
		return new String(result, 0, position, utf8);
	}

	public static class Bytes {
		public final byte[] content;
		public final int length;
		public Bytes(final byte[] content, final int length) {
			this.content = content;
			this.length = length;
		}
	}

	public Bytes toBytes() {
		return new Bytes(result, position);
	}

	public byte[] toByteArray() {
		return Arrays.copyOf(result, position);
	}

	public void reset() { position = 0; }

	@Override
	public void write(int c) throws IOException {
		tmp[0] = (char)c;
		writeString(new String(tmp, 0, 1), 0, 1);
	}

	@Override
	public void write(char[] cbuf, int off, int len) {
		writeString(new String(cbuf, off, len), 0, len);
	}

	@Override
	public void write(String str, int off, int len) {
		writeString(str, off, len);
	}

	@Override
	public void flush() throws IOException { }

	@Override
	public void close() throws IOException {
		position = 0;
	}
}
