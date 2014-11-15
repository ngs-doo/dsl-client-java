package com.dslplatform.client.json;

import com.dslplatform.patterns.ServiceLocator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class JsonReader {
	private final byte[] buffer;
	private final int length;
	private final ServiceLocator locator;

	private int nameStart;
	private int currentIndex;
	private byte last;

	public JsonReader(final byte[] buffer, final ServiceLocator locator) {
		this.buffer = buffer;
		this.length = buffer.length;
		this.locator = locator;
		this.currentIndex = 0;
		last = ' ';
	}

	public byte read() throws IOException {
		if (currentIndex >= length) {
			throw new IOException("end of stream");
		}
		return last = buffer[currentIndex++];
	}

	public byte last() { return last; }

	public String readShortValue() throws IOException {
		int start = currentIndex - 1;
		while (last != ',' && last != '}' && last != ']')
			read();
		return new String(buffer, start, currentIndex - start - 1, "ISO-8859-1");
	}

	public String readSimpleString() throws IOException {
		if (last != '"') throw new IOException("Expecting '\"' at position " + positionInStream() + ". Found " + (char) last);
		int start = currentIndex;
		do { read(); } while (last != '"');
		return new String(buffer, start, currentIndex - start - 1, "ISO-8859-1");
	}

	public String readString() throws UnsupportedEncodingException, IOException {

		final int startIndex = currentIndex;
		// At this point, buffer cannot be empty or null, it is safe to read first character
		if (last != '"') {
			throw new IOException("JSON string must start with a double quote! Instead found: " + byteDetails(buffer[currentIndex -1]));
		}

		// If the buffer contains an ASCII string (no high bit set) without any escape codes "\n", "\t", etc...,
		// there is no need to instantiate any temporary buffers, we just decode the original buffer directly
		// via ISO-8859-1 encoding since it is the fastest encoding which is guaranteed to retain all ASCII characters
		while (true) {
			if (currentIndex >= length) {
				throw new IOException("JSON string was not closed with a double quote!");
			}

			final byte bc = buffer[currentIndex++];
			if (bc == '"') {
				last = '"';
				return new String(buffer, startIndex, currentIndex - startIndex - 1, "ISO-8859-1");
			}

			// If we encounter a backslash, which is a beginning of an escape sequence
			// or a high bit was set - indicating an UTF-8 encoded multibyte character,
			// there is no chance that we can decode the string without instantiating
			// a temporary buffer, so quit this loop
			if ((bc ^ '\\') < 1) break;
		}

		// temporary buffer, will resize if need be
		int soFar = --currentIndex - startIndex - 1;
		char[] chars = new char[soFar + 256];

		// copy all the ASCII characters so far
		for (int i = soFar; i >= 0; i--) {
			chars[i] = (char) buffer[startIndex + i + 1];
		}

		while (true) {
			if (currentIndex >= length) {
				throw new IOException("JSON string was not closed with a double quote!");
			}

			int bc = buffer[currentIndex++];
			if (bc == '"') {
				last = '"';
				return new String(chars, 0, soFar);
			}

			// if we're running out of space, double the buffer capacity
			if (soFar >= chars.length - 3) {
				final char[] newChars = new char[chars.length << 1];
				System.arraycopy(chars, 0, newChars, 0, soFar);
				chars = newChars;
			}

			if (bc == '\\') {
				bc = buffer[currentIndex++];

				switch (bc) {
					case 'b': bc = '\b'; break;
					case 't': bc = '\t'; break;
					case 'n': bc = '\n'; break;
					case 'f': bc = '\f'; break;
					case 'r': bc = '\r'; break;
					case '"':
					case '/':
					case '\\': break;
					case 'u': bc =
							(hexToInt(buffer[currentIndex++]) << 12) +
									(hexToInt(buffer[currentIndex++]) <<  8) +
									(hexToInt(buffer[currentIndex++]) <<  4) +
									hexToInt(buffer[currentIndex++]); break;

					default: throw new IOException("Could not parse String, got invalid escape combination '\\" + bc + "'");
				}
			}
			else if ((bc & 0x80) != 0) {
				final int u2 = buffer[currentIndex++];
				if ((bc & 0xE0) == 0xC0) {
					bc = ((bc & 0x1F) << 6) + (u2 & 0x3F);
				}
				else {
					final int u3 = buffer[currentIndex++];
					if ((bc & 0xF0) == 0xE0) {
						bc = ((bc & 0x0F) << 12) + ((u2 & 0x3F) <<  6) + (u3 & 0x3F);
					}
					else {
						final int u4 = buffer[currentIndex++];
						if ((bc & 0xF8) == 0xF0) {
							bc = ((bc & 0x07) << 18) + ((u2 & 0x3F) << 12) + ((u3 & 0x3F) <<  6) + (u4 & 0x3F);
						}
						else {
							// there are legal 5 & 6 byte combinations, but none are _valid_
							throw new IOException();
						}

						if (bc >= 0x10000) {
							// check if valid unicode
							if (bc >= 0x110000) throw new IOException();

							// split surrogates
							final int sup = bc - 0x10000;
							chars[soFar++] = (char) ((sup >>> 10) + 0xd800);
							chars[soFar++] = (char) ((sup & 0x3ff) + 0xdc00);
						}
					}
				}
			}

			chars[soFar++] = (char) bc;
		}
	}

	private static int hexToInt(final byte value) throws IOException {
		if (value >= '0' && value <= '9') return value - 0x30;
		if (value >= 'A' && value <= 'F') return value - 0x37;
		if (value >= 'a' && value <= 'f') return value - 0x57;
		throw new IOException("Could not parse unicode escape, expected a hexadecimal digit, got '" + value + "'");
	}

	private String byteDetails(final byte c){
		return "'" + ((char) c) + "'" + "(" + c + ")";
	}

	public byte getNextToken() throws IOException {
		byte c = read();
		while (c == 13 || c == 10 || c == 32 || c == '\t')
			c = read();
		return c;
	}

	public byte moveToNextToken() throws IOException {
		byte c = last;
		while (c == 13 || c == 10 || c == 32 || c == '\t')
			c = read();
		return c;
	}

	public long positionInStream() { return currentIndex; }

	public int fillName() throws IOException {
		if (last != '"') throw new IOException("Expecting '\"' at position " + positionInStream() + ". Found " + (char) last);
		nameStart = currentIndex;
		byte c = read();
		long hash = 0x811c9dc5;
		for (; c != '"'; c = read()) {
			hash ^= 0xFF & c;
			hash *= 0x1000193;
		}
		if (read() != ':') throw new IOException("Expecting ':' at position " + positionInStream() + ". Found " + (char) last);
		return (int)hash;
	}

	public boolean wasLastName(final String name) {
		if (name.length() != currentIndex - nameStart) {
			return false;
		}
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) != buffer[nameStart + i]) {
				return false;
			}
		}
		return true;
	}

	public byte skip() { return last; }

	public String readNext() { return ""; }

	public byte[] readBase64() throws IOException {
		if (getNextToken() != '"') throw new IOException("Expecting '\"' at position " + positionInStream() + ". Found " + (char) last);
		final int start = currentIndex;
		int i = currentIndex;
		while (i < buffer.length && buffer[i] != '"') i++;
		last = buffer[i];
		currentIndex = i;
		if (last != ']') {
			if (currentIndex >= length) throw new IOException("Unexpected end of json in collection.");
			else throw new IOException("Expecting ']' at position " + positionInStream() + ". Found " + (char) last);
		}
		//TODO: convert
		return null;
	}

	public static interface ReadObject<T> {
		T read(final JsonReader reader) throws IOException;
	}

	public static interface ReadJsonObject<T extends JsonObject> {
		T deserialize(final JsonReader reader, final ServiceLocator locator) throws IOException;
	}

	public boolean wasNull() throws IOException {
		if (last == 'n') {
			if (currentIndex + 2 < buffer.length && buffer[currentIndex] == 'u'
					&& buffer[currentIndex + 1] == 'l' && buffer[currentIndex + 2] == 'l') {
				currentIndex += 3;
				return true;
			}
			throw new IOException("Invalid null value found at: " + currentIndex);
		}
		return false;
	}

	public boolean wasTrue() throws IOException {
		if (last == 't') {
			if (currentIndex + 2 < buffer.length && buffer[currentIndex] == 'r'
					&& buffer[currentIndex + 1] == 'u' && buffer[currentIndex + 2] == 'e') {
				currentIndex += 3;
				return true;
			}
			throw new IOException("Invalid boolean value found at: " + currentIndex);
		}
		return false;
	}

	public boolean wasFalse() throws IOException {
		if (last == 'f') {
			if (currentIndex + 3 < buffer.length && buffer[currentIndex] == 'a'
					&& buffer[currentIndex + 1] == 'l' && buffer[currentIndex + 2] == 's'
					&& buffer[currentIndex + 2] == 'e') {
				currentIndex += 4;
				return false;
			}
			throw new IOException("Invalid boolean value found at: " + currentIndex);
		}
		return false;
	}

	public <T> ArrayList<T> deserializeCollection(final ReadObject<T> readObject) throws IOException {
		ArrayList<T> res = new ArrayList<T>();
		res.add(readObject.read(this));
		while (moveToNextToken() == ',') {
			getNextToken();
			res.add(readObject.read(this));
		}
		if (last != ']') {
			if (currentIndex >= length) throw new IOException("Unexpected end of json in collection.");
			else throw new IOException("Expecting ']' at position " + positionInStream() + ". Found " + (char) last);
		}
		return res;
	}

	public <T> ArrayList<T> deserializeNullableCollection(final ReadObject<T> readObject) throws IOException {
		ArrayList<T> res = new ArrayList<T>();
		if (wasNull()) {
			res.add(null);
		} else {
			res.add(readObject.read(this));
		}
		while (moveToNextToken() == ',') {
			getNextToken();
			if (wasNull()) {
				res.add(null);
			} else {
				res.add(readObject.read(this));
			}
		}
		if (last != ']') {
			if (currentIndex >= length) throw new IOException("Unexpected end of json in collection.");
			else throw new IOException("Expecting ']' at position " + positionInStream() + ". Found " + (char) last);
		}
		return res;
	}

	public <T extends JsonObject> ArrayList<T> deserializeCollection(final ReadJsonObject<T> readObject) throws IOException {
		ArrayList<T> res = new ArrayList<T>();
		if (last == '{') {
			res.add(readObject.deserialize(this, locator));
		} else throw new IOException("Expecting '{' at position " + positionInStream() + ". Found " + (char) last);
		while (moveToNextToken() == ',') {
			if (getNextToken() == '{') {
				res.add(readObject.deserialize(this, locator));
			} else throw new IOException("Expecting '{' at position " + positionInStream() + ". Found " + (char) last);
		}
		if (last != ']') {
			if (currentIndex >= length) throw new IOException("Unexpected end of json in collection.");
			else throw new IOException("Expecting ']' at position " + positionInStream() + ". Found " + (char) last);
		}
		return res;
	}

	public <T extends JsonObject> ArrayList<T> deserializeNullableCollection(final ReadJsonObject<T> readObject) throws IOException {
		ArrayList<T> res = new ArrayList<T>();
		if (last == '{') {
			res.add(readObject.deserialize(this, locator));
		} else if (wasNull()) {
			res.add(null);
		} else throw new IOException("Expecting '{' at position " + positionInStream() + ". Found " + (char) last);
		while (moveToNextToken() == ',') {
			if (getNextToken() == '{') {
				res.add(readObject.deserialize(this, locator));
			} else if (wasNull()) {
				res.add(null);
			} else throw new IOException("Expecting '{' at position " + positionInStream() + ". Found " + (char) last);
		}
		if (last != ']') {
			if (currentIndex >= buffer.length) throw new IOException("Unexpected end of json in collection.");
			else throw new IOException("Expecting ']' at position " + positionInStream() + ". Found " + (char) last);
		}
		return res;
	}
}
