package com.dslplatform.client.json;

import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.dslplatform.client.Utils;
import com.dslplatform.patterns.*;
import com.dslplatform.client.JsonSerialization;
import com.dslplatform.storage.S3;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.w3c.dom.Element;

public class DslJsonSerialization implements JsonSerialization {

	private final ServiceLocator locator;

	public DslJsonSerialization(final ServiceLocator locator) {
		this.locator = locator;

		registerReader(byte[].class, BinaryConverter.Base64Reader);
		registerWriter(byte[].class, BinaryConverter.Base64Writer);
		registerReader(boolean.class, BoolConverter.BooleanReader);
		registerReader(Boolean.class, BoolConverter.BooleanReader);
		registerWriter(boolean.class, BoolConverter.BooleanWriter);
		registerWriter(Boolean.class, BoolConverter.BooleanWriter);
		//TODO: localdate/datetime should be optional
		registerReader(LocalDate.class, DateConverter.LocalDateReader);
		registerWriter(LocalDate.class, DateConverter.LocalDateWriter);
		registerReader(DateTime.class, DateConverter.DateTimeReader);
		registerWriter(DateTime.class, DateConverter.DateTimeWriter);
		if (Utils.IS_ANDROID) {
			registerAndroidSpecifics();
		} else {
			registerJavaSpecifics();
		}
		registerReader(Map.class, MapConverter.MapReader);
		registerWriter(Map.class, MapConverter.MapWriter);
		registerWriter(HashMap.class, MapConverter.HashMapWriter);
		registerReader(URI.class, NetConverter.UriReader);
		registerWriter(URI.class, NetConverter.UriWriter);
		registerReader(InetAddress.class, NetConverter.AddressReader);
		registerWriter(InetAddress.class, NetConverter.AddressWriter);
		registerReader(double.class, NumberConverter.DoubleReader);
		registerWriter(double.class, NumberConverter.DoubleWriter);
		registerReader(Double.class, NumberConverter.DoubleReader);
		registerWriter(Double.class, NumberConverter.DoubleWriter);
		registerReader(float.class, NumberConverter.FloatReader);
		registerWriter(float.class, NumberConverter.FloatWriter);
		registerReader(Float.class, NumberConverter.FloatReader);
		registerWriter(float.class, NumberConverter.FloatWriter);
		registerReader(int.class, NumberConverter.IntReader);
		registerWriter(int.class, NumberConverter.IntWriter);
		registerReader(Integer.class, NumberConverter.IntReader);
		registerWriter(Integer.class, NumberConverter.IntWriter);
		registerReader(long.class, NumberConverter.LongReader);
		registerWriter(long.class, NumberConverter.LongWriter);
		registerReader(Long.class, NumberConverter.LongReader);
		registerWriter(Long.class, NumberConverter.LongWriter);
		registerReader(BigDecimal.class, NumberConverter.DecimalReader);
		registerWriter(BigDecimal.class, NumberConverter.DecimalWriter);
		registerReader(String.class, StringConverter.Reader);
		registerWriter(String.class, StringConverter.Writer);
		registerReader(UUID.class, UUIDConverter.Reader);
		registerWriter(UUID.class, UUIDConverter.Writer);
		registerReader(Element.class, XmlConverter.Reader);
		registerWriter(Element.class, XmlConverter.Writer);
		registerReader(S3.class, StorageConverter.S3Reader);
		registerWriter(S3.class, StorageConverter.S3Writer);
	}

	void registerAndroidSpecifics() {
		registerReader(android.graphics.PointF.class, AndroidGeomConverter.LocationReader);
		registerWriter(android.graphics.PointF.class, AndroidGeomConverter.LocationWriter);
		registerReader(android.graphics.Point.class, AndroidGeomConverter.PointReader);
		registerWriter(android.graphics.Point.class, AndroidGeomConverter.PointWriter);
		registerReader(android.graphics.Rect.class, AndroidGeomConverter.RectangleReader);
		registerWriter(android.graphics.Rect.class, AndroidGeomConverter.RectangleWriter);
		registerReader(android.graphics.Bitmap.class, AndroidGeomConverter.ImageReader);
		registerWriter(android.graphics.Bitmap.class, AndroidGeomConverter.ImageWriter);
	}

	void registerJavaSpecifics() {
		registerReader(java.awt.geom.Point2D.class, GeomConverter.LocationReader);
		registerWriter(java.awt.geom.Point2D.class, GeomConverter.LocationWriter);
		registerWriter(java.awt.geom.Point2D.Double.class, GeomConverter.LocationWriterDouble);
		registerWriter(java.awt.geom.Point2D.Float.class, GeomConverter.LocationWriterFloat);
		registerReader(java.awt.Point.class, GeomConverter.PointReader);
		registerWriter(java.awt.Point.class, GeomConverter.PointWriter);
		registerReader(java.awt.geom.Rectangle2D.class, GeomConverter.RectangleReader);
		registerWriter(java.awt.geom.Rectangle2D.class, GeomConverter.RectangleWriter);
		registerWriter(java.awt.geom.Rectangle2D.Double.class, GeomConverter.RectangleWriterDouble);
		registerWriter(java.awt.geom.Rectangle2D.Float.class, GeomConverter.RectangleWriterFloat);
		registerReader(java.awt.image.BufferedImage.class, GeomConverter.ImageReader);
		registerWriter(java.awt.Image.class, GeomConverter.ImageWriter);
		registerWriter(java.awt.image.BufferedImage.class, GeomConverter.BufferedImageWriter);
	}

	private static boolean isNull(final int size, final byte[] body) {
		return size == 4
				&& body[0] == 'n'
				&& body[1] == 'u'
				&& body[2] == 'l'
				&& body[3] == 'l';
	}

	private final ConcurrentHashMap<Class<?>, JsonReader.ReadJsonObject<JsonObject>> jsonObjectReaders =
			new ConcurrentHashMap<Class<?>, JsonReader.ReadJsonObject<JsonObject>>();

	private final HashMap<Class<?>, JsonReader.ReadObject<?>> jsonReaders = new HashMap<Class<?>, JsonReader.ReadObject<?>>();

	<T> void registerReader(final Class<T> manifest, final JsonReader.ReadObject<T> reader) {
		jsonReaders.put(manifest, reader);
	}

	private final HashMap<Class<?>, JsonWriter.WriteObject<?>> jsonWriters = new HashMap<Class<?>, JsonWriter.WriteObject<?>>();

	<T> void registerWriter(final Class<T> manifest, final JsonWriter.WriteObject<T> writer) {
		jsonWriters.put(manifest, writer);
	}

	@SuppressWarnings("unchecked")
	private JsonReader.ReadJsonObject<JsonObject> getObjectReader(final Class<?> manifest) {
		try {
			JsonReader.ReadJsonObject<JsonObject> reader = jsonObjectReaders.get(manifest);
			if (reader == null) {
				try {
					reader = (JsonReader.ReadJsonObject<JsonObject>) manifest.getField("JSON_READER").get(null);
				} catch (Exception ignore) {
					//log error!?
					return null;
				}
				jsonObjectReaders.putIfAbsent(manifest, reader);
			}
			return reader;
		} catch (final Exception ignore) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public <TResult> TResult deserialize(
			final Class<TResult> manifest,
			final byte[] body,
			final int size) throws IOException {
		if (isNull(size, body)) {
			return null;
		}
		if (size == 2 && body[0] == '{' && body[1] == '}') {
			try {
				return manifest.newInstance();
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
		if (JsonObject.class.isAssignableFrom(manifest)) {
			final JsonReader.ReadJsonObject<JsonObject> objectReader = getObjectReader(manifest);
			final JsonReader json = new JsonReader(body, size, locator);
			if (objectReader != null && json.getNextToken() == '{') {
				json.getNextToken();
				return (TResult) objectReader.deserialize(json, locator);
			}
		}
		final JsonReader.ReadObject<?> simpleReader = jsonReaders.get(manifest);
		if (simpleReader == null) {
			if (manifest.isArray()) {
				final Class<?> elementManifest = manifest.getComponentType();
				final List<?> list = deserializeList(elementManifest, body, size);
				if (list == null) {
					return null;
				}
				final Object result = Array.newInstance(elementManifest, list.size());
				for(int i = 0; i < list.size(); i++) {
					Array.set(result, i, list.get(i));
				}
				return (TResult)result;
			}
			throw new IOException("Unable to find reader for provided type: " + manifest);
		}
		final JsonReader json = new JsonReader(body, size, locator);
		json.getNextToken();
		if (json.wasNull()) {
			return null;
		}
		return (TResult) simpleReader.read(json);
	}

	@SuppressWarnings("unchecked")
	public <TResult> List<TResult> deserializeList(
			final Class<TResult> manifest,
			final byte[] body,
			final int size) throws IOException {
		if (isNull(size, body)) {
			return null;
		}
		if (size == 2 && body[0] == '[' && body[1] == ']') {
			return new ArrayList<TResult>(0);
		}
		final JsonReader json = new JsonReader(body, size, locator);
		if (json.getNextToken() != '[') {
			if (json.wasNull()) {
				return null;
			}
			throw new IOException("Expecting '[' as array start. Found: " + (char) json.last());
		}
		json.getNextToken();
		if (JsonObject.class.isAssignableFrom(manifest)) {
			final JsonReader.ReadJsonObject<JsonObject> reader = getObjectReader(manifest);
			if (reader != null) {
				return (List<TResult>) json.deserializeNullableCollection(reader);
			}
		}
		final JsonReader.ReadObject<?> simpleReader = jsonReaders.get(manifest);
		if (simpleReader == null) {
			throw new IOException("Unable to find reader for provided type: " + manifest);
		}
		return json.deserializeNullableCollection((JsonReader.ReadObject<TResult>) simpleReader);
	}

	public <TResult extends AggregateRoot> List<History<TResult>> deserializeHistoryList(
			final Class<TResult> manifest,
			final byte[] body,
			final int size) throws IOException {
		if (isNull(size, body)) {
			return null;
		}
		if (size == 2 && body[0] == '[' && body[1] == ']') {
			return new ArrayList<History<TResult>>(0);
		}
		final JsonReader json = new JsonReader(body, size, locator);
		if (json.getNextToken() != '[') {
			throw new IOException("Expecting '[' as array start. Found: " + (char) json.last());
		}
		if (json.getNextToken() == ']') {
			return new ArrayList<History<TResult>>(0);
		}
		final JsonReader.ReadJsonObject<JsonObject> reader = getObjectReader(manifest);
		if (reader == null) {
			throw new IOException("Unable to find reader for provided history type: " + manifest);
		}
		final ArrayList<History<TResult>> result = new ArrayList<History<TResult>>();
		do {
			if (json.last() != '{') {
				throw new IOException("Expecting '{' at " + json.getCurrentIndex());
			}
			ArrayList<Snapshot<TResult>> snapshots = null;
			json.getNextToken();
			String name = json.readString();
			if (json.getNextToken() != ':') {
				throw new IOException("Expecting ':' at " + json.getCurrentIndex());
			}
			if ("Snapshots".equalsIgnoreCase(name)) {
				snapshots = readSnaphosts(manifest, reader, json, locator);
			} else {
				json.skip();
			}
			while (json.getNextToken() == ',') {
				name = json.readString();
				if (json.getNextToken() != ':') {
					throw new IOException("Expecting ':' at" + json.getCurrentIndex());
				}
				if ("Snapshots".equalsIgnoreCase(name)) {
					snapshots = readSnaphosts(manifest, reader, json, locator);
				} else {
					json.skip();
				}
			}
			if (json.last() != '}') {
				throw new IOException("Expecting '}' at " + json.getCurrentIndex());
			}
			if (snapshots == null) {
				throw new IOException("Snapshots not provided. It can't be null. Error at " + json.getCurrentIndex());
			}
			result.add(new History<TResult>(snapshots));
			switch (json.getNextToken()) {
				case ']':
					return result;
				case ',':
					json.getNextToken();
					break;
				default:
					throw new IOException("Expecting ']' or ',' at " + json.getCurrentIndex());
			}
		} while (true);
	}

	@SuppressWarnings("unchecked")
	private static <TResult extends AggregateRoot> ArrayList<Snapshot<TResult>> readSnaphosts(
			final Class<TResult> manifest,
			final JsonReader.ReadJsonObject<JsonObject> reader,
			final JsonReader json,
			final ServiceLocator locator) throws IOException {
		if (json.getNextToken() != '[') {
			throw new IOException("Expecting '[' at " + json.getCurrentIndex());
		}
		if (json.getNextToken() == ']') {
			return new ArrayList<Snapshot<TResult>>(0);
		}
		final ArrayList<Snapshot<TResult>> result = new ArrayList<Snapshot<TResult>>();
		do {
			if (json.last() != '{') {
				throw new IOException("Expecting '{' at " + json.getCurrentIndex());
			}
			DateTime at = Utils.MIN_DATE_TIME;
			String action = null;
			TResult value = null;
			String name;
			json.getNextToken();
			name = json.readString();
			if (json.getNextToken() != ':') {
				throw new IOException("Expecting ':' at " + json.getCurrentIndex());
			}
			json.getNextToken();
			if ("at".equalsIgnoreCase(name)) {
				at = DateConverter.deserializeDateTime(json);
			} else if ("action".equalsIgnoreCase(name)) {
				action = StringConverter.deserialize(json);
			} else if ("value".equalsIgnoreCase(name)) {
				if (json.last() != '{') {
					throw new IOException("Expecting '{' at " + json.getCurrentIndex());
				}
				json.getNextToken();
				value = (TResult) reader.deserialize(json, locator);
			} else {
				json.skip();
			}
			while (json.getNextToken() == ',') {
				json.getNextToken();
				name = json.readString();
				if (json.getNextToken() != ':') {
					throw new IOException("Expecting ':' at " + json.getCurrentIndex());
				}
				json.getNextToken();
				if ("at".equalsIgnoreCase(name)) {
					at = DateConverter.deserializeDateTime(json);
				} else if ("action".equalsIgnoreCase(name)) {
					action = StringConverter.deserialize(json);
				} else if ("value".equalsIgnoreCase(name)) {
					if (json.last() != '{') {
						throw new IOException("Expecting '{' at " + json.getCurrentIndex());
					}
					json.getNextToken();
					value = (TResult) reader.deserialize(json, locator);
				} else {
					json.skip();
				}
			}
			if (json.last() != '}') {
				throw new IOException("Expecting '}' at " + json.getCurrentIndex());
			}
			if (action == null) {
				throw new IOException("Action not provided. It can't be null. Error at " + json.getCurrentIndex());
			}
			if (value == null) {
				throw new IOException("Value not provided. It can't be null. Error at " + json.getCurrentIndex());
			}
			result.add(new Snapshot<TResult>(at, action, value));
			switch (json.getNextToken()) {
				case ']':
					return result;
				case ',':
					json.getNextToken();
					break;
				default:
					throw new IOException("Expecting ']' or ',' at " + json.getCurrentIndex());
			}
		} while (true);
	}

	public <T extends JsonObject> void serialize(final JsonWriter writer, final T[] array) {
		if (array == null) {
			writer.writeNull();
			return;
		}
		writer.writeByte(JsonWriter.ARRAY_START);
		if (array.length != 0) {
			T item = array[0];
			if (item != null) {
				item.serialize(writer, false);
			} else {
				writer.writeNull();
			}
			for (int i = 1; i < array.length; i++) {
				writer.writeByte(JsonWriter.COMMA);
				item = array[i];
				if (item != null) {
					item.serialize(writer, false);
				} else {
					writer.writeNull();
				}
			}
		}
		writer.writeByte(JsonWriter.ARRAY_END);
	}

	public <T extends JsonObject> void serialize(final JsonWriter writer, final List<T> list) {
		if (list == null) {
			writer.writeNull();
			return;
		}
		writer.writeByte(JsonWriter.ARRAY_START);
		if (list.size() != 0) {
			T item = list.get(0);
			if (item != null) {
				item.serialize(writer, false);
			} else {
				writer.writeNull();
			}
			for (int i = 1; i < list.size(); i++) {
				writer.writeByte(JsonWriter.COMMA);
				item = list.get(i);
				if (item != null) {
					item.serialize(writer, false);
				} else {
					writer.writeNull();
				}
			}
		}
		writer.writeByte(JsonWriter.ARRAY_END);
	}

	public <T extends JsonObject> void serialize(final JsonWriter writer, final Collection<T> collection) {
		if (collection == null) {
			writer.writeNull();
			return;
		}
		writer.writeByte(JsonWriter.ARRAY_START);
		if (!collection.isEmpty()) {
			final Iterator<T> it = collection.iterator();
			T item = it.next();
			if (item != null) {
				item.serialize(writer, false);
			} else {
				writer.writeNull();
			}
			while (it.hasNext()) {
				writer.writeByte(JsonWriter.COMMA);
				item = it.next();
				if (item != null) {
					item.serialize(writer, false);
				} else {
					writer.writeNull();
				}
			}
		}
		writer.writeByte(JsonWriter.ARRAY_END);
	}

	@SuppressWarnings("unchecked")
	public <T> boolean serialize(final JsonWriter writer, final Class<?> manifest, final Object value) {
		if (value == null) {
			writer.writeNull();
			return true;
		}
		if (value instanceof JsonObject) {
			((JsonObject) value).serialize(writer, false);
			return true;
		}
		if (value instanceof JsonObject[]) {
			serialize(writer, (JsonObject[]) value);
			return true;
		}
		final JsonWriter.WriteObject<Object> simpleWriter = (JsonWriter.WriteObject<Object>) jsonWriters.get(manifest);
		if (simpleWriter != null) {
			simpleWriter.write(writer, value);
			return true;
		}
		if (manifest.isArray()) {
			if (Array.getLength(value) == 0) {
				writer.writeAscii("[]");
				return true;
			}
			final Class<?> elementManifest = manifest.getComponentType();
			if (elementManifest.isPrimitive()) {
				if (elementManifest == boolean.class) {
					BoolConverter.serialize((boolean[]) value, writer);
				} else if (elementManifest == int.class) {
					NumberConverter.serialize((int[]) value, writer);
				} else if (elementManifest == long.class) {
					NumberConverter.serialize((long[]) value, writer);
				} else if (elementManifest == byte.class) {
					BinaryConverter.serialize((byte[])value, writer);
				} else if (elementManifest == short.class) {
					NumberConverter.serialize((short[]) value, writer);
				} else if (elementManifest == float.class) {
					NumberConverter.serialize((float[]) value, writer);
				} else if (elementManifest == double.class) {
					NumberConverter.serialize((double[]) value, writer);
				} else if (elementManifest == char.class) {
					//TODO? char[] !?
					StringConverter.serialize(new String((char[]) value), writer);
				} else {
					return false;
				}
				return true;
			} else {
				final JsonWriter.WriteObject<Object> elementWriter = (JsonWriter.WriteObject<Object>) jsonWriters.get(elementManifest);
				if (elementWriter != null) {
					writer.serialize((Object[]) value, elementWriter);
					return true;
				}
			}
		}
		if (value instanceof Collection) {
			final Collection items = (Collection) value;
			if (items.isEmpty()) {
				writer.writeAscii("[]");
				return true;
			}
			Class<?> baseType = null;
			final Iterator iterator = items.iterator();
			do {
				final Object item = iterator.next();
				if (item != null) {
					Class<?> elementType = item.getClass();
					if (elementType != baseType) {
						if (baseType == null || elementType.isAssignableFrom(baseType)) {
							baseType = elementType;
						}
					}
				}
			} while (iterator.hasNext());
			if (baseType == null) {
				writer.writeByte(JsonWriter.ARRAY_START);
				writer.writeNull();
				for (int i = 1; i < items.size(); i++) {
					writer.writeAscii(",null");
				}
				writer.writeByte(JsonWriter.ARRAY_END);
				return true;
			}
			if (JsonObject.class.isAssignableFrom(baseType)) {
				serialize(writer, (Collection<JsonObject>) items);
				return true;
			}
			final JsonWriter.WriteObject<Object> elementWriter = (JsonWriter.WriteObject<Object>) jsonWriters.get(baseType);
			if (elementWriter != null) {
				writer.serialize(items, elementWriter);
				return true;
			}
		}
		return false;
	}

	private static final Bytes NULL = new Bytes(new byte[]{'n', 'u', 'l', 'l'}, 4);

	public final Bytes serialize(final Object value) throws IOException {
		if (value == null) return NULL;
		final JsonWriter jw = new JsonWriter();
		final Class<?> manifest = value.getClass();
		if (!serialize(jw, manifest, value)) {
			throw new IOException("Unable to serialize provided object. Failed to find serializer for: " + manifest);
		}
		return jw.toBytes();
	}

	@Override
	public final void serialize(final Writer writer, final Object value) throws IOException {
		if (value == null) {
			writer.write("NULL");
			return;
		}
		final Class<?> manifest = value.getClass();
		final boolean isJsonWriter = writer instanceof JsonWriter;
		final JsonWriter jw = isJsonWriter ? (JsonWriter) writer : new JsonWriter();
		if (!serialize(jw, manifest, value)) {
			throw new IOException("Unable to serialize provided object. Failed to find serializer for: " + manifest);
		}
		if (!isJsonWriter) {
			writer.write(jw.toString());
		}
	}
}
