package com.dslplatform.json;

import com.dslplatform.client.TreePath;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public abstract class TreePathConverter {
	public static final JsonReader.ReadObject<TreePath> Reader = new JsonReader.ReadObject<TreePath>() {
		@Override
		public TreePath read(JsonReader reader) throws IOException {
			return TreePathConverter.deserialize(reader);
		}
	};
	public static final JsonWriter.WriteObject<TreePath> Writer = new JsonWriter.WriteObject<TreePath>() {
		@Override
		public void write(JsonWriter writer, TreePath value) {
			serializeNullable(value, writer);;
		}
	};

	public static void serializeNullable(final TreePath value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			serialize(value, sw);
		}
	}

	public static void serialize(final TreePath value, final JsonWriter sw) {
		sw.writeByte(JsonWriter.QUOTE);
		sw.writeAscii(value.toString());
		sw.writeByte(JsonWriter.QUOTE);
	}

	public static TreePath deserialize(final JsonReader reader) throws IOException {
		try {
			return TreePath.create(reader.readString());
		} catch(final IOException e) {
			throw new IOException("Can't parse JSON TreePath at position " + reader.positionInStream(), e);
		}
	}

	public static ArrayList<TreePath> deserializeCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(Reader);
	}

	public static void deserializeCollection(final JsonReader reader, final Collection<TreePath> res) throws IOException {
		reader.deserializeCollection(Reader, res);
	}

	public static ArrayList<TreePath> deserializeNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(Reader);
	}

	public static void deserializeNullableCollection(final JsonReader reader, final Collection<TreePath> res) throws IOException {
		reader.deserializeNullableCollection(Reader, res);
	}
}
