package com.dslplatform.client.json;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class JsonWriter extends Writer {

	public final StringBuilder buffer;
	public final char[] tmp = new char[48];

	public JsonWriter() {
		buffer = new StringBuilder(512);
	}

	public JsonWriter(final int size) {
		buffer = new StringBuilder(size);
	}

	public JsonWriter(final StringBuilder sb) {
		buffer = sb;
	}

	@Override
	public void write(int c) throws IOException {
		buffer.append((char)c);
	}

	@Override
	public void write(char[] buf, int off, int len) throws IOException {
		buffer.append(buf, off, len);
	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		buffer.append(str, off, len);
	}

	@Override
	public void flush() {}

	@Override
	public String toString() {
		return buffer.toString();
	}

	public byte[] toBytes() throws UnsupportedEncodingException {
		byte[] res = buffer.toString().getBytes("UTF-8");
		buffer.setLength(0);
		return res;
	}

	@Override
	public void close() {
		buffer.setLength(0);
	}
}
