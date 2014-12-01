package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class NetConverter {
	public static void serializeNullable(final URI value, final Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(final URI value, final Writer sw) throws IOException {
		StringConverter.serialize(value.toString(), sw);
	}

	public static URI deserializeUri(final JsonReader reader) throws IOException {
		return URI.create(reader.readString());
	}

	private static JsonReader.ReadObject<URI> UriReader = new JsonReader.ReadObject<URI>() {
		@Override
		public URI read(JsonReader reader) throws IOException {
			return deserializeUri(reader);
		}
	};

	public static ArrayList<URI> deserializeUriCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithGet(UriReader);
	}

	public static void deserializeUriCollection(final JsonReader reader, final Collection<URI> res) throws IOException {
		reader.deserializeCollectionWithGet(UriReader, res);
	}

	public static ArrayList<URI> deserializeUriNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithGet(UriReader);
	}

	public static void deserializeUriNullableCollection(final JsonReader reader, final Collection<URI> res) throws IOException {
		reader.deserializeNullableCollectionWithGet(UriReader, res);
	}

	public static void serializeNullable(final InetAddress value, final Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(final InetAddress value, final Writer sw) throws IOException {
		StringConverter.serialize(value.toString(), sw);
	}

	public static InetAddress deserializeIp(final JsonReader reader) throws IOException {
		return InetAddress.getByName(reader.readSimpleString());
	}

	private static JsonReader.ReadObject<InetAddress> AddressReader = new JsonReader.ReadObject<InetAddress>() {
		@Override
		public InetAddress read(JsonReader reader) throws IOException {
			return deserializeIp(reader);
		}
	};

	public static ArrayList<InetAddress> deserializeIpCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithGet(AddressReader);
	}

	public static void deserializeIpCollection(final JsonReader reader, final Collection<InetAddress> res) throws IOException {
		reader.deserializeCollectionWithGet(AddressReader, res);
	}

	public static ArrayList<InetAddress> deserializeIpNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithGet(AddressReader);
	}

	public static void deserializeIpNullableCollection(final JsonReader reader, final Collection<InetAddress> res) throws IOException {
		reader.deserializeNullableCollectionWithGet(AddressReader, res);
	}
}
