package com.dslplatform.client.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.dslplatform.patterns.ServiceLocator;

public final class JsonReader {
	private final byte[] buffer;
	private final int length;
	private final ServiceLocator locator;
	private final char[] tmp = new char[48];

	private int tokenStart;
	private int currentIndex = 0;
	private byte last = ' ';

	public JsonReader(final byte[] buffer, final ServiceLocator locator) {
		this.buffer = buffer;
		this.length = buffer.length;
		this.locator = locator;
	}

	public JsonReader(final byte[] buffer, final int length, final ServiceLocator locator) throws IOException {
		this.buffer = buffer;
		this.length = length;
		this.locator = locator;
		if (length > buffer.length) {
			throw new IOException("length can't be longer than buffer.length");
		}
	}

	public final byte read() throws IOException {
		if (currentIndex >= length) {
			throw new IOException("end of stream");
		}
		return last = buffer[currentIndex++];
	}

	public final byte last() {
		return last;
	}

	public final String readShortValue() throws IOException {
		char ch = (char) last;
		tmp[0] = ch;
		int i = 1;
		for (; ch != ',' && ch != '}' && ch != ']' && ch != '"' && i < tmp.length && currentIndex < length; i++, currentIndex++) {
			tmp[i] = ch = (char) buffer[currentIndex];
		}
		last = (byte) ch;
		return new String(tmp, 0, i - 1);
	}

	public final int getTokenStart() {
		return tokenStart;
	}

	public final int getCurrentIndex() {
		return currentIndex;
	}

	public final char[] readNumber() {
		tokenStart = currentIndex - 1;
		char ch = (char) last;
		tmp[0] = ch;
		for (int i = 1; ch != ',' && ch != '}' && ch != ']' && ch != '"' && i < tmp.length && currentIndex < length; i++, currentIndex++) {
			tmp[i] = ch = (char) buffer[currentIndex];
		}
		last = (byte) ch;
		return tmp;
	}

	public final String readSimpleString() throws IOException {
		if (last != '"')
			throw new IOException("Expecting '\"' at position " + positionInStream() + ". Found " + (char) last);
		final int start = currentIndex;
		int i = start;
		for (; i < length && buffer[i] != '"'; i++) {
			tmp[i - start] = (char) buffer[i];
		}
		currentIndex = i + 1;
		last = '"';
		return new String(tmp, 0, i - start);
	}

	public final char[] readSimpleQuote() throws IOException {
		if (last != '"')
			throw new IOException("Expecting '\"' at position " + positionInStream() + ". Found " + (char) last);
		final int start = tokenStart = currentIndex;
		int i = currentIndex;
		for (; i < length && buffer[i] != '"'; i++) {
			tmp[i - start] = (char) buffer[i];
		}
		currentIndex = i + 1;
		last = '"';
		return tmp;
	}

	public final String readString() throws IOException {

		final int startIndex = currentIndex;
		// At this point, buffer cannot be empty or null, it is safe to read first character
		if (last != '"') {
			throw new IOException("JSON string must start with a double quote! Instead found: " + byteDetails(buffer[currentIndex - 1]));
		}

		byte bb = 0;
		for (int pos = 0; pos < tmp.length; pos++) {
			if (currentIndex >= length) {
				throw new IOException("JSON string was not closed with a double quote!");
			}
			bb = buffer[currentIndex++];
			if (bb == '"') {
				last = '"';
				return new String(tmp, 0, pos);
			}
			// If we encounter a backslash, which is a beginning of an escape sequence
			// or a high bit was set - indicating an UTF-8 encoded multibyte character,
			// there is no chance that we can decode the string without instantiating
			// a temporary buffer, so quit this loop
			if ((bb ^ '\\') < 1) break;
			tmp[pos] = (char) bb;
		}

		// If the buffer contains an ASCII string (no high bit set) without any escape codes "\n", "\t", etc...,
		// there is no need to instantiate any temporary buffers, we just decode the original buffer directly
		// via ISO-8859-1 encoding since it is the fastest encoding which is guaranteed to retain all ASCII characters
		while (true) {
			if (currentIndex >= length) {
				throw new IOException("JSON string was not closed with a double quote!");
			}
			// If we encounter a backslash, which is a beginning of an escape sequence
			// or a high bit was set - indicating an UTF-8 encoded multibyte character,
			// there is no chance that we can decode the string without instantiating
			// a temporary buffer, so quit this loop
			if ((bb ^ '\\') < 1) break;
			bb = buffer[currentIndex++];
			if (bb == '"') {
				last = '"';
				return new String(buffer, startIndex, currentIndex - startIndex - 1, "ISO-8859-1");
			}
		}

		// temporary buffer, will resize if need be
		int soFar = --currentIndex - startIndex;
		char[] chars = new char[soFar + 256];

		// copy all the ASCII characters so far
		for (int i = soFar - 1; i >= 0; i--) {
			chars[i] = (char) buffer[startIndex + i];
		}

		while (currentIndex < length) {
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
					case 'b':
						bc = '\b';
						break;
					case 't':
						bc = '\t';
						break;
					case 'n':
						bc = '\n';
						break;
					case 'f':
						bc = '\f';
						break;
					case 'r':
						bc = '\r';
						break;
					case '"':
					case '/':
					case '\\':
						break;
					case 'u':
						bc =
								(hexToInt(buffer[currentIndex++]) << 12) +
										(hexToInt(buffer[currentIndex++]) << 8) +
										(hexToInt(buffer[currentIndex++]) << 4) +
										hexToInt(buffer[currentIndex++]);
						break;

					default:
						throw new IOException("Could not parse String, got invalid escape combination '\\" + bc + "'");
				}
			} else if ((bc & 0x80) != 0) {
				final int u2 = buffer[currentIndex++];
				if ((bc & 0xE0) == 0xC0) {
					bc = ((bc & 0x1F) << 6) + (u2 & 0x3F);
				} else {
					final int u3 = buffer[currentIndex++];
					if ((bc & 0xF0) == 0xE0) {
						bc = ((bc & 0x0F) << 12) + ((u2 & 0x3F) << 6) + (u3 & 0x3F);
					} else {
						final int u4 = buffer[currentIndex++];
						if ((bc & 0xF8) == 0xF0) {
							bc = ((bc & 0x07) << 18) + ((u2 & 0x3F) << 12) + ((u3 & 0x3F) << 6) + (u4 & 0x3F);
						} else {
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
		throw new IOException("JSON string was not closed with a double quote!");
	}

	private static int hexToInt(final byte value) throws IOException {
		if (value >= '0' && value <= '9') return value - 0x30;
		if (value >= 'A' && value <= 'F') return value - 0x37;
		if (value >= 'a' && value <= 'f') return value - 0x57;
		throw new IOException("Could not parse unicode escape, expected a hexadecimal digit, got '" + value + "'");
	}

	private String byteDetails(final byte c) {
		return "'" + ((char) c) + "'" + "(" + c + ")";
	}

	private boolean wasWhiteSpace() {
		if(last=='"' || last==','){
			return false;
		}
		switch (last) {
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 32:
			case -96:
				return true;
			case -31:
				return currentIndex + 2 < length && buffer[currentIndex + 1] == -102 && buffer[currentIndex + 2] == -128;
			case -30:
				if (currentIndex + 2 < length) {
					final byte b1 = buffer[currentIndex + 1];
					final byte b2 = buffer[currentIndex + 2];
					if (b1 == -127 && b2 == -97) return true;
					if (b1 != -128) return false;
					switch (b2) {
						case -128:
						case -127:
						case -126:
						case -125:
						case -124:
						case -123:
						case -122:
						case -121:
						case -120:
						case -119:
						case -118:
						case -88:
						case -87:
						case -81:
							return true;
						default:
							return false;
					}
				} else {
					return false;
				}
			case -29:
				return currentIndex + 2 < length && buffer[currentIndex + 1] == -128 && buffer[currentIndex + 2] == -128;
			default:
				return false;
		}
	}

	public final byte getNextToken() throws IOException {
		read();
		while (wasWhiteSpace())
			read();
		return last;
	}

	public final byte moveToNextToken() throws IOException {
		while (wasWhiteSpace())
			read();
		return last;
	}

	public final long positionInStream() {
		return currentIndex;
	}

	public final int fillName() throws IOException {
		if (last != '"')
			throw new IOException("Expecting '\"' at position " + positionInStream() + ". Found " + (char) last);
		tokenStart = currentIndex;
		byte c = read();
		long hash = 0x811c9dc5;
		for (; c != '"'; c = read()) {
			hash ^= 0xFF & c;
			hash *= 0x1000193;
		}
		if (read() != ':')
			throw new IOException("Expecting ':' at position " + positionInStream() + ". Found " + (char) last);
		return (int) hash;
	}

	public final int calcHash() throws IOException {
		if (last != '"')
			throw new IOException("Expecting '\"' at position " + positionInStream() + ". Found " + (char) last);
		tokenStart = currentIndex;
		byte c = read();
		long hash = 0x811c9dc5;
		do {
			hash ^= 0xFF & c;
			hash *= 0x1000193;
		} while ((c = read()) != '"');
		return (int) hash;
	}

	public final boolean wasLastName(final String name) {
		if (name.length() != currentIndex - tokenStart) {
			return false;
		}
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) != buffer[tokenStart + i]) {
				return false;
			}
		}
		return true;
	}

	public final String getLastName() throws IOException {
		return new String(buffer, tokenStart, currentIndex - tokenStart - 1, "ISO-8859-1");
	}

	private byte skipString() throws IOException {
		byte c = read();
		byte prev = c;
		while (c != '"' || prev == '\\') {
			prev = c;
			c = read();
		}
		return getNextToken();
	}

	public final byte skip() throws IOException {
		if (last == '"') return skipString();
		else if (last == '{') {
			byte nextToken = getNextToken();
			if (nextToken == '}') return getNextToken();
			if (nextToken == '"') nextToken = skipString();
			else
				throw new IOException("Expecting '\"' at position " + positionInStream() + ". Found " + (char) nextToken);
			if (nextToken != ':')
				throw new IOException("Expecting ':' at position " + positionInStream() + ". Found " + (char) nextToken);
			getNextToken();
			nextToken = skip();
			while (nextToken == ',') {
				nextToken = getNextToken();
				if (nextToken == '"') nextToken = skipString();
				else
					throw new IOException("Expecting '\"' at position " + positionInStream() + ". Found " + (char) nextToken);
				if (nextToken != ':')
					throw new IOException("Expecting ':' at position " + positionInStream() + ". Found " + (char) nextToken);
				getNextToken();
				nextToken = skip();
			}
			if (nextToken != '}')
				throw new IOException("Expecting '}' at position " + positionInStream() + ". Found " + (char) nextToken);
			return getNextToken();
		} else if (last == '[') {
			getNextToken();
			byte nextToken = skip();
			while (nextToken == ',') {
				getNextToken();
				nextToken = skip();
			}
			if (nextToken != ']')
				throw new IOException("Expecting ']' at position " + positionInStream() + ". Found " + (char) nextToken);
			return getNextToken();
		} else {
			while (last != ',' && last != '}' && last != ']')
				read();
			return last;
		}
	}

	public final String readNext() throws IOException {
		final int start = currentIndex - 1;
		skip();
		return new String(buffer, start, currentIndex - start - 1, "UTF-8");
	}

	public final byte[] readBase64() throws IOException {
		if (last != '"')
			throw new IOException("Expecting '\"' at position " + positionInStream() + " at base64 start. Found " + (char) last);
		final int start = currentIndex;
		currentIndex = Base64.findEnd(buffer, start);
		last = buffer[currentIndex++];
		if (last != '"') {
			throw new IOException("Expecting '\"' at position " + positionInStream() + " at base64 end. Found " + (char) last);
		}
		return Base64.decodeFast(buffer, start, currentIndex - 1);
	}

	public static interface ReadObject<T> {
		T read(final JsonReader reader) throws IOException;
	}

	public static interface ReadJsonObject<T extends JsonObject> {
		T deserialize(final JsonReader reader, final ServiceLocator locator) throws IOException;
	}

	public final boolean wasNull() throws IOException {
		if (last == 'n') {
			if (currentIndex + 2 < length && buffer[currentIndex] == 'u'
					&& buffer[currentIndex + 1] == 'l' && buffer[currentIndex + 2] == 'l') {
				currentIndex += 3;
				return true;
			}
			throw new IOException("Invalid null value found at: " + currentIndex);
		}
		return false;
	}

	public final boolean wasTrue() throws IOException {
		if (last == 't') {
			if (currentIndex + 2 < length && buffer[currentIndex] == 'r'
					&& buffer[currentIndex + 1] == 'u' && buffer[currentIndex + 2] == 'e') {
				currentIndex += 3;
				return true;
			}
			throw new IOException("Invalid boolean value found at: " + currentIndex);
		}
		return false;
	}

	public final boolean wasFalse() throws IOException {
		if (last == 'f') {
			if (currentIndex + 3 < length && buffer[currentIndex] == 'a'
					&& buffer[currentIndex + 1] == 'l' && buffer[currentIndex + 2] == 's'
					&& buffer[currentIndex + 3] == 'e') {
				currentIndex += 4;
				return true;
			}
			throw new IOException("Invalid boolean value found at: " + currentIndex);
		}
		return false;
	}

	public final <T> ArrayList<T> deserializeCollectionWithGet(final ReadObject<T> readObject) throws IOException {
		final ArrayList<T> res = new ArrayList<T>();
		deserializeCollectionWithGet(readObject, res);
		return res;
	}

	public final <T> ArrayList<T> deserializeCollectionWithMove(final ReadObject<T> readObject) throws IOException {
		final ArrayList<T> res = new ArrayList<T>();
		deserializeCollectionWithMove(readObject, res);
		return res;
	}

	public final <T> void deserializeCollectionWithGet(final ReadObject<T> readObject, final Collection<T> res) throws IOException {
		res.add(readObject.read(this));
		while (getNextToken() == ',') {
			getNextToken();
			res.add(readObject.read(this));
		}
		if (last != ']') {
			if (currentIndex >= length) throw new IOException("Unexpected end of json in collection.");
			else throw new IOException("Expecting ']' at position " + positionInStream() + ". Found " + (char) last);
		}
	}

	public final <T> void deserializeCollectionWithMove(final ReadObject<T> readObject, final Collection<T> res) throws IOException {
		res.add(readObject.read(this));
		while (moveToNextToken() == ',') {
			getNextToken();
			res.add(readObject.read(this));
		}
		if (last != ']') {
			if (currentIndex >= length) throw new IOException("Unexpected end of json in collection.");
			else throw new IOException("Expecting ']' at position " + positionInStream() + ". Found " + (char) last);
		}
	}

	public final <T> ArrayList<T> deserializeNullableCollectionWithGet(final ReadObject<T> readObject) throws IOException {
		final ArrayList<T> res = new ArrayList<T>();
		deserializeNullableCollectionWithGet(readObject, res);
		return res;
	}

	public final <T> ArrayList<T> deserializeNullableCollectionWithMove(final ReadObject<T> readObject) throws IOException {
		final ArrayList<T> res = new ArrayList<T>();
		deserializeNullableCollectionWithMove(readObject, res);
		return res;
	}

	public final <T> void deserializeNullableCollectionWithGet(final ReadObject<T> readObject, final Collection<T> res) throws IOException {
		if (wasNull()) {
			res.add(null);
		} else {
			res.add(readObject.read(this));
		}
		while (getNextToken() == ',') {
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
	}

	public final <T> void deserializeNullableCollectionWithMove(final ReadObject<T> readObject, final Collection<T> res) throws IOException {
		if (wasNull()) {
			res.add(null);
			getNextToken();
		} else {
			res.add(readObject.read(this));
			moveToNextToken();
		}
		while (last == ',') {
			getNextToken();
			if (wasNull()) {
				res.add(null);
				getNextToken();
			} else {
				res.add(readObject.read(this));
				moveToNextToken();
			}
		}
		if (last != ']') {
			if (currentIndex >= length) throw new IOException("Unexpected end of json in collection.");
			else throw new IOException("Expecting ']' at position " + positionInStream() + ". Found " + (char) last);
		}
	}

	public final <T extends JsonObject> ArrayList<T> deserializeCollection(final ReadJsonObject<T> readObject) throws IOException {
		final ArrayList<T> res = new ArrayList<T>();
		deserializeCollection(readObject, res);
		return res;
	}

	public final <T extends JsonObject> void deserializeCollection(final ReadJsonObject<T> readObject, final Collection<T> res) throws IOException {
		if (last == '{') {
			res.add(readObject.deserialize(this, locator));
		} else throw new IOException("Expecting '{' at position " + positionInStream() + ". Found " + (char) last);
		while (getNextToken() == ',') {
			if (getNextToken() == '{') {
				res.add(readObject.deserialize(this, locator));
			} else throw new IOException("Expecting '{' at position " + positionInStream() + ". Found " + (char) last);
		}
		if (last != ']') {
			if (currentIndex >= length) throw new IOException("Unexpected end of json in collection.");
			else throw new IOException("Expecting ']' at position " + positionInStream() + ". Found " + (char) last);
		}
	}

	public final <T extends JsonObject> ArrayList<T> deserializeNullableCollection(final ReadJsonObject<T> readObject) throws IOException {
		final ArrayList<T> res = new ArrayList<T>();
		deserializeNullableCollection(readObject, res);
		return res;
	}

	public final <T extends JsonObject> void deserializeNullableCollection(final ReadJsonObject<T> readObject, final Collection<T> res) throws IOException {
		if (last == '{') {
			res.add(readObject.deserialize(this, locator));
		} else if (wasNull()) {
			res.add(null);
		} else throw new IOException("Expecting '{' at position " + positionInStream() + ". Found " + (char) last);
		while (getNextToken() == ',') {
			if (getNextToken() == '{') {
				res.add(readObject.deserialize(this, locator));
			} else if (wasNull()) {
				res.add(null);
			} else throw new IOException("Expecting '{' at position " + positionInStream() + ". Found " + (char) last);
		}
		if (last != ']') {
			if (currentIndex >= length) throw new IOException("Unexpected end of json in collection.");
			else throw new IOException("Expecting ']' at position " + positionInStream() + ". Found " + (char) last);
		}
	}
}
