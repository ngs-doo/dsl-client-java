package com.dslplatform.client.json;

import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
		registerJodaConverters(this);
		if (Utils.IS_ANDROID) {
			registerAndroidSpecifics(this);
		} else {
			registerJavaSpecifics(this);
		}
		registerReader(Map.class, ObjectConverter.MapReader);
		registerWriter(Map.class, ObjectConverter.MapWriter);
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
		registerWriter(Float.class, NumberConverter.FloatWriter);
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
		registerReader(Number.class, NumberConverter.NumberReader);
	}

	static void registerAndroidSpecifics(final DslJsonSerialization json) {
		json.registerReader(android.graphics.PointF.class, AndroidGeomConverter.LocationReader);
		json.registerWriter(android.graphics.PointF.class, AndroidGeomConverter.LocationWriter);
		json.registerReader(android.graphics.Point.class, AndroidGeomConverter.PointReader);
		json.registerWriter(android.graphics.Point.class, AndroidGeomConverter.PointWriter);
		json.registerReader(android.graphics.Rect.class, AndroidGeomConverter.RectangleReader);
		json.registerWriter(android.graphics.Rect.class, AndroidGeomConverter.RectangleWriter);
		json.registerReader(android.graphics.Bitmap.class, AndroidGeomConverter.ImageReader);
		json.registerWriter(android.graphics.Bitmap.class, AndroidGeomConverter.ImageWriter);
	}

	static void registerJodaConverters(final DslJsonSerialization json) {
		json.registerReader(LocalDate.class, DateConverter.LocalDateReader);
		json.registerWriter(LocalDate.class, DateConverter.LocalDateWriter);
		json.registerReader(DateTime.class, DateConverter.DateTimeReader);
		json.registerWriter(DateTime.class, DateConverter.DateTimeWriter);
	}

	static void registerJavaSpecifics(final DslJsonSerialization json) {
		json.registerReader(java.awt.geom.Point2D.class, GeomConverter.LocationReader);
		json.registerWriter(java.awt.geom.Point2D.class, GeomConverter.LocationWriter);
		json.registerReader(java.awt.Point.class, GeomConverter.PointReader);
		json.registerWriter(java.awt.Point.class, GeomConverter.PointWriter);
		json.registerReader(java.awt.geom.Rectangle2D.class, GeomConverter.RectangleReader);
		json.registerWriter(java.awt.geom.Rectangle2D.class, GeomConverter.RectangleWriter);
		json.registerReader(java.awt.Image.class, GeomConverter.ImageReader);
		json.registerWriter(java.awt.Image.class, GeomConverter.ImageWriter);
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
		readerMap.put(manifest, manifest);
		jsonReaders.put(manifest, reader);
	}

	private final HashMap<Class<?>, JsonWriter.WriteObject<?>> jsonWriters = new HashMap<Class<?>, JsonWriter.WriteObject<?>>();

	<T> void registerWriter(final Class<T> manifest, final JsonWriter.WriteObject<T> writer) {
		writerMap.put(manifest, manifest);
		jsonWriters.put(manifest, writer);
	}

	private final ConcurrentMap<Class<?>, Class<?>> readerMap = new ConcurrentHashMap<Class<?>, Class<?>>();
	private final ConcurrentMap<Class<?>, Class<?>> writerMap = new ConcurrentHashMap<Class<?>, Class<?>>();

	private JsonReader.ReadObject<?> tryFindReader(final Class<?> manifest) {
		Class<?> found = readerMap.get(manifest);
		if (found != null) {
			return jsonReaders.get(found);
		}
		final ArrayList<Class<?>> signatures = new ArrayList<Class<?>>();
		findAllSignatures(manifest, signatures);
		for (final Class<?> sig : signatures) {
			final JsonReader.ReadObject<?> writer = jsonReaders.get(sig);
			if (writer != null) {
				readerMap.putIfAbsent(manifest, sig);
				return writer;
			}
		}
		return null;
	}

	private JsonWriter.WriteObject<?> tryFindWriter(final Class<?> manifest) {
		Class<?> found = writerMap.get(manifest);
		if (found != null) {
			return jsonWriters.get(found);
		}
		final ArrayList<Class<?>> signatures = new ArrayList<Class<?>>();
		findAllSignatures(manifest, signatures);
		for (final Class<?> sig : signatures) {
			final JsonWriter.WriteObject<?> writer = jsonWriters.get(sig);
			if (writer != null) {
				writerMap.putIfAbsent(manifest, sig);
				return writer;
			}
		}
		return null;
	}

	private static void findAllSignatures(final Class<?> manifest, final ArrayList<Class<?>> found) {
		if (found.contains(manifest)) {
			return;
		}
		found.add(manifest);
		final Class<?> superClass = manifest.getSuperclass();
		if (superClass != null && superClass != Object.class) {
			findAllSignatures(superClass, found);
		}
		for (final Class<?> iface : manifest.getInterfaces()) {
			findAllSignatures(iface, found);
		}
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
		if (size == 2 && body[0] == '{' && body[1] == '}' && !manifest.isInterface()) {
			try {
				return manifest.newInstance();
			} catch (InstantiationException ignore) {
			} catch (IllegalAccessException e) {
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
		final JsonReader.ReadObject<?> simpleReader = tryFindReader(manifest);
		if (simpleReader == null) {
			if (manifest.isArray()) {
				final Class<?> elementManifest = manifest.getComponentType();
				final List<?> list = deserializeList(elementManifest, body, size);
				if (list == null) {
					return null;
				}
				final Object result = Array.newInstance(elementManifest, list.size());
				for (int i = 0; i < list.size(); i++) {
					Array.set(result, i, list.get(i));
				}
				return (TResult) result;
			}
			if (Utils.HAS_JACKSON) {
				return deserializeJackson(this, manifest, body, size);
			}
			throw new IOException("Unable to find reader for provided type: " + manifest + " and Jackson is not found on classpath.\n" +
					"Try initializing system with custom JsonSerialization.");
		}
		final JsonReader json = new JsonReader(body, size, locator);
		json.getNextToken();
		if (json.wasNull()) {
			return null;
		}
		return (TResult) simpleReader.read(json);
	}

	private Object jackson;

	private static <TResult> TResult deserializeJackson(
			final DslJsonSerialization json,
			final Class<TResult> manifest,
			final byte[] body,
			final int size) throws IOException {
		if (json.jackson == null) json.jackson = new JacksonJsonSerialization(json.locator);
		return ((JacksonJsonSerialization) json.jackson).deserialize(manifest, body, size);
	}

	private static <TResult> List<TResult> deserializeJacksonList(
			final DslJsonSerialization json,
			final Class<TResult> manifest,
			final byte[] body,
			final int size) throws IOException {
		if (json.jackson == null) json.jackson = new JacksonJsonSerialization(json.locator);
		return ((JacksonJsonSerialization) json.jackson).deserializeList(manifest, body, size);
	}

	private static Bytes serializeJackson(
			final DslJsonSerialization json,
			final Object value) throws IOException {
		if (json.jackson == null) json.jackson = new JacksonJsonSerialization(json.locator);
		return ((JacksonJsonSerialization) json.jackson).serialize(value);
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
		if (json.getNextToken() == ']') {
			return new ArrayList<TResult>(0);
		}
		if (JsonObject.class.isAssignableFrom(manifest)) {
			final JsonReader.ReadJsonObject<JsonObject> reader = getObjectReader(manifest);
			if (reader != null) {
				return (List<TResult>) json.deserializeNullableCollection(reader);
			}
		}
		final JsonReader.ReadObject<?> simpleReader = tryFindReader(manifest);
		if (simpleReader == null) {
			if (Utils.HAS_JACKSON) {
				return deserializeJacksonList(this, manifest, body, size);
			}
			throw new IOException("Unable to find reader for provided type: " + manifest + " and Jackson is not found on classpath.\n" +
					"Try initializing system with custom JsonSerialization.");
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
		final JsonWriter.WriteObject<Object> simpleWriter = (JsonWriter.WriteObject<Object>) tryFindWriter(manifest);
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
					BinaryConverter.serialize((byte[]) value, writer);
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
				final JsonWriter.WriteObject<Object> elementWriter = (JsonWriter.WriteObject<Object>) tryFindWriter(elementManifest);
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
			//TODO: pick lowest common denominator!?
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
			final JsonWriter.WriteObject<Object> elementWriter = (JsonWriter.WriteObject<Object>) tryFindWriter(baseType);
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
			if (Utils.HAS_JACKSON) {
				return serializeJackson(this, value);
			}
			throw new IOException("Unable to serialize provided object. Failed to find serializer for: " + manifest);
		}
		return jw.toBytes();
	}

	@Override
	public final void serialize(final Writer writer, final Object value) throws IOException {
		if (writer instanceof JsonWriter) {
			serialize((JsonWriter)writer, value);
		}
		else {
			final JsonWriter jw = new JsonWriter();
			serialize(jw, value);
			writer.write(jw.toString());
		}
	}

	public final void serialize(final JsonWriter writer, final Object value) throws IOException {
		if (value == null) {
			writer.writeNull();
			return;
		}
		final Class<?> manifest = value.getClass();
		if (!serialize(writer, manifest, value)) {
			if (Utils.HAS_JACKSON) {
				Bytes result = serializeJackson(this, value);
				writer.write(result.toUtf8());
			}
			throw new IOException("Unable to serialize provided object. Failed to find serializer for: " + manifest);
		}
	}

}
