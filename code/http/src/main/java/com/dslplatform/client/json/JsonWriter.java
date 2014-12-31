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
	private byte[] result;
	private int position;
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

	@Override
	public void write(final int c) throws IOException {
		if (position == buffer.length) {
			buffer = Arrays.copyOf(buffer, buffer.length + buffer.length / 2);
		}
		buffer[position++] = (char)c;
	}

	@Override
	public void write(final char[] buf, final int off, final int len) throws IOException {
		if (position + len >= buffer.length) {
			buffer = Arrays.copyOf(buffer, buffer.length + buffer.length / 2 + len);
		}
		System.arraycopy(buf, off, buffer, position, len);
		position += len;
	}

	@Override
	public void write(final String str, final int off, final int len) throws IOException {
		if (position + len >= buffer.length) {
			buffer = Arrays.copyOf(buffer, buffer.length + buffer.length / 2 + len);
		}
		final int total = off + len;
		for(int i = off; i < total; i++) {
			buffer[position++] = str.charAt(i);
		}
	}

	@Override
	public void flush() {}

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
	public void close() {
		position = 0;
	}
}
