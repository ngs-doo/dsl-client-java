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

	private char[] buffer;
	public final char[] tmp = new char[48];
	protected byte[] result;
	protected int position;
	private ByteBuffer resultBuffer;

	private final CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();

	public JsonWriter() {
		this(512);
	}

	public JsonWriter(final int size) {
		this(new char[size], new byte[size * 4]);
	}

	public JsonWriter(final char[] buffer, final byte[] result) {
		this.buffer = buffer;
		this.result = result;
		this.resultBuffer = ByteBuffer.wrap(result);
	}

	private static final char[] NULL = new char[] { 'n', 'u', 'l', 'l' };
	public static final byte OBJECT_START = '{';
	public static final byte OBJECT_END = '}';
	public static final byte ARRAY_START = '[';
	public static final byte ARRAY_END = ']';
	public static final byte COMMA = ',';
	public static final byte SEMI = ':';
	public static final byte QUOTE = '"';

	public final void writeNull() {
		System.arraycopy(NULL, 0, buffer, position, 4);
		position += 4;
	}

	public final void writeByte(final byte c) {
		if (position == buffer.length) {
			buffer = Arrays.copyOf(buffer, buffer.length + buffer.length / 2);
		}
		buffer[position++] = (char)c;
	}

	public final void writeChars(final char[] buf, final int off, final int len) {
		if (position + len >= buffer.length) {
			buffer = Arrays.copyOf(buffer, buffer.length + buffer.length / 2 + len);
		}
		System.arraycopy(buf, off, buffer, position, len);
		position += len;
	}

	public final void writeString(final String str, final int off, final int len) {
		if (position + len >= buffer.length) {
			buffer = Arrays.copyOf(buffer, buffer.length + buffer.length / 2 + len);
		}
		str.getChars(off, off + len, buffer, position);
		position += len;
	}

	public final void writeAscii(final char[] buf, final int off, final int len) {
		writeChars(buf, off, len);
	}

	public final void writeAscii(final String str) {
		if (position + str.length() >= buffer.length) {
			buffer = Arrays.copyOf(buffer, buffer.length + buffer.length / 2 + str.length());
		}
		str.getChars(0, str.length(), buffer, position);
		position += str.length();
	}

	public final void writeBinary(final byte[] buf) {
		if (position + buf.length * 2 + 2 >= buffer.length) {
			buffer = Arrays.copyOf(buffer, buffer.length + buffer.length / 2 + buf.length * 2 + 2);
		}
		buffer[position++] = '"';
		position += Base64.encodeToChar(buf, buffer, position);
		buffer[position++] = '"';
	}

	@Override
	public String toString() {
		return new String(buffer, 0, position);
	}

	public static class Bytes {
		public final byte[] content;
		public final int length;
		public Bytes(final byte[] content, final int length) {
			this.content = content;
			this.length = length;
		}
	}

	private int convertToBytes() {
		if (result.length < position * 4) {
			result = Arrays.copyOf(result, position * 4);
			resultBuffer = ByteBuffer.wrap(result);
		} else {
			resultBuffer.rewind();
		}
		final CharBuffer cb = CharBuffer.wrap(buffer, 0, position);
		position = 0;

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
			return resultBuffer.position();
		} catch (CharacterCodingException x) {
			throw new Error(x);
		}
	}

	public Bytes toBytes() {
		final int len = convertToBytes();
		return new Bytes(result, len);
	}

	public byte[] toByteArray() {
		final int len = convertToBytes();
		return Arrays.copyOf(result, len);
	}

	@Override
	public void write(int c) throws IOException {
		tmp[0] = (char)c;
		writeChars(tmp, 0, 1);
	}

	@Override
	public void write(char[] cbuf, int off, int len) {
		writeChars(cbuf, off, len);
	}

	@Override
	public void write(String str, int off, int len) {
		if (position + len >= buffer.length) {
			buffer = Arrays.copyOf(buffer, buffer.length + buffer.length / 2 + len);
		}
		str.getChars(off, off + len, buffer, position);
		position += len;
	}

	@Override
	public void flush() throws IOException { }

	@Override
	public void close() throws IOException {
		position = 0;
	}
}
