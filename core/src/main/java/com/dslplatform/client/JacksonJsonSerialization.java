package com.dslplatform.client;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

import javax.imageio.ImageIO;

import com.dslplatform.client.Utils;
import com.dslplatform.json.XmlConverter;
import com.dslplatform.patterns.*;
import com.dslplatform.client.JsonSerialization;
import com.dslplatform.storage.S3;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JacksonJsonSerialization implements JsonSerialization {
	private static final String TEXT_NODE_TAG = "#text";

	private static final JsonSerializer<LocalDate> dateSerializer = new JsonSerializer<LocalDate>() {
		@Override
		public void serialize(final LocalDate value, final JsonGenerator jg, final SerializerProvider _unused) throws IOException {
			jg.writeString(value.toString());
		}
	};
	private static final JsonDeserializer<LocalDate> dateDeserializer = new JsonDeserializer<LocalDate>() {
		@Override
		public LocalDate deserialize(final JsonParser parser, final DeserializationContext _unused) throws IOException {
			return DateTime.parse(parser.getValueAsString()).toLocalDate();
		}
	};

	private static final JsonSerializer<DateTime> timestampSerializer = new JsonSerializer<DateTime>() {
		@Override
		public void serialize(final DateTime value, final JsonGenerator jg, final SerializerProvider _unused) throws IOException {
			jg.writeString(value.toString());
		}
	};

	private static final JsonDeserializer<DateTime> timestampDeserializer = new JsonDeserializer<DateTime>() {
		@Override
		public DateTime deserialize(final JsonParser parser, final DeserializationContext _unused) throws IOException {
			return DateTime.parse(parser.getValueAsString());
		}
	};

	private static final JsonSerializer<S3> s3Serializer = new JsonSerializer<S3>() {
		@Override
		public void serialize(final S3 value, final JsonGenerator jg, final SerializerProvider _unused) throws IOException {
			jg.writeStartObject();
			jg.writeStringField("Bucket", value.getBucket());
			jg.writeStringField("Key", value.getKey());
			jg.writeNumberField("Length", value.getLength());
			jg.writeStringField("Name", value.getName());
			jg.writeStringField("MimeType", value.getMimeType());
			jg.writeObjectField("Metadata", value.getMetadata());
			jg.writeEndObject();
		}
	};

	private static final JsonDeserializer<S3> s3Deserializer = new JsonDeserializer<S3>() {
		@Override
		public S3 deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
			if (parser.getCurrentToken() != JsonToken.START_OBJECT) {
				throw new IOException("Expecting '{'. Found: " + parser.getCurrentToken());
			}
			String bucket = null;
			String key = null;
			long length = 0;
			String name = null;
			String mimeType = null;
			Map<String, String> metadata = null;
			while(parser.nextToken() == JsonToken.FIELD_NAME) {
				String field = parser.getCurrentName();
				if ("Bucket".equalsIgnoreCase(field)) {
					bucket = parser.nextTextValue();
				} else if ("Key".equalsIgnoreCase(field)) {
					key = parser.nextTextValue();
				} else if ("Length".equalsIgnoreCase(field)) {
					length = parser.nextLongValue(0);
				} else if ("Name".equalsIgnoreCase(field)) {
					name = parser.nextTextValue();
				} else if ("MimeType".equalsIgnoreCase(field)) {
					mimeType = parser.nextTextValue();
				} else if ("Metadata".equalsIgnoreCase(field)) {
					final JsonToken token = parser.nextToken();
					if (token == JsonToken.START_OBJECT) {
						metadata = (Map<String, String>) parser.readValueAs(HashMap.class);
					} else if (token == JsonToken.VALUE_NULL) {
						metadata = null;
					} else {
						throw new IOException("Expecting '}'. Found: " + parser.getCurrentToken());
					}
				} else {
					parser.skipChildren();
				}
			}
			if (parser.getCurrentToken() != JsonToken.END_OBJECT) {
				throw new IOException("Expecting '}'. Found: " + parser.getCurrentToken());
			}
			ServiceLocator locator = (ServiceLocator)context.findInjectableValue("_serviceLocator", null, null);
			return new S3(locator, bucket, key, length, name, mimeType, metadata);
		}
	};

	static class JavaConverters {
		private static void initJavaSerializers(final SimpleModule module) {
			module.addSerializer(java.awt.Point.class, new JsonSerializer<java.awt.Point>() {
				@Override
				public void serialize(final java.awt.Point value, final JsonGenerator jg, final SerializerProvider _unused) throws IOException {
					jg.writeStartObject();
					jg.writeNumberField("X", value.x);
					jg.writeNumberField("Y", value.y);
					jg.writeEndObject();
				}
			});
			module.addSerializer(java.awt.geom.Point2D.class, new JsonSerializer<java.awt.geom.Point2D>() {
				@Override
				public void serialize(final java.awt.geom.Point2D value, final JsonGenerator jg, final SerializerProvider _unused) throws IOException {
					jg.writeStartObject();
					jg.writeNumberField("X", value.getX());
					jg.writeNumberField("Y", value.getY());
					jg.writeEndObject();
				}
			});
			module.addSerializer(java.awt.geom.Rectangle2D.class, new JsonSerializer<java.awt.geom.Rectangle2D>() {
				@Override
				public void serialize(final java.awt.geom.Rectangle2D rect, final JsonGenerator jg, final SerializerProvider _unused) throws IOException {
					jg.writeStartObject();
					jg.writeNumberField("X", rect.getX());
					jg.writeNumberField("Y", rect.getY());
					jg.writeNumberField("Width", rect.getWidth());
					jg.writeNumberField("Height", rect.getHeight());
					jg.writeEndObject();
				}
			});
			module.addSerializer(java.awt.image.BufferedImage.class, new JsonSerializer<java.awt.image.BufferedImage>() {
				@Override
				public void serialize(final java.awt.image.BufferedImage image, final JsonGenerator jg, final SerializerProvider _unused) throws IOException {
					final ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(image, "png", baos);
					jg.writeBinary(baos.toByteArray());
				}
			});
		}

		private static void initJavaDeserializers(final SimpleModule module) {
			module.addDeserializer(java.awt.Point.class, new JsonDeserializer<java.awt.Point>() {
				@Override
				public java.awt.Point deserialize(final JsonParser parser, final DeserializationContext unused) throws IOException {
					final JsonNode tree = parser.getCodec().readTree(parser);
					JsonNode x = tree.get("X");
					if (x == null) x = tree.get("x");
					JsonNode y = tree.get("Y");
					if (y == null) y = tree.get("y");
					return new java.awt.Point(x != null ? x.asInt() : 0, y != null ? y.asInt() : 0);
				}
			});
			module.addDeserializer(java.awt.geom.Point2D.class, new JsonDeserializer<java.awt.geom.Point2D>() {
				@Override
				public java.awt.geom.Point2D deserialize(final JsonParser parser, final DeserializationContext unused) throws IOException {
					final JsonNode tree = parser.getCodec().readTree(parser);
					JsonNode x = tree.get("X");
					if (x == null) x = tree.get("x");
					JsonNode y = tree.get("Y");
					if (y == null) y = tree.get("y");
					return new java.awt.geom.Point2D.Double(x != null ? x.asDouble() : 0, y != null ? y.asDouble() : 0);
				}
			});
			module.addDeserializer(java.awt.geom.Rectangle2D.class, new JsonDeserializer<java.awt.geom.Rectangle2D>() {
				@Override
				public java.awt.geom.Rectangle2D deserialize(final JsonParser parser, final DeserializationContext _unused) throws IOException {
					final JsonNode tree = parser.getCodec().readTree(parser);
					JsonNode x = tree.get("X");
					if (x == null) x = tree.get("x");
					JsonNode y = tree.get("Y");
					if (y == null) y = tree.get("y");
					JsonNode width = tree.get("Width");
					if (width == null) width = tree.get("width");
					JsonNode height = tree.get("Height");
					if (height == null) height = tree.get("height");
					return new java.awt.geom.Rectangle2D.Double(
							x != null ? x.asDouble() : 0,
							y != null ? y.asDouble() : 0,
							width != null ? width.asDouble() : 0,
							height != null ? height.asDouble() : 0);
				}
			});
			module.addDeserializer(java.awt.image.BufferedImage.class, new JsonDeserializer<java.awt.image.BufferedImage>() {
				@Override
				public java.awt.image.BufferedImage deserialize(final JsonParser parser, final DeserializationContext _unused) throws IOException {
					final InputStream is = new ByteArrayInputStream(parser.getBinaryValue());
					return ImageIO.read(is);
				}
			});
		}
	}

	static class AndroidConverters {
		private static void initAndroidSerializers(final SimpleModule module) {
			module.addSerializer(android.graphics.Point.class, new JsonSerializer<android.graphics.Point>() {
				@Override
				public void serialize(final android.graphics.Point value, final JsonGenerator gen, final SerializerProvider _unused) throws IOException {
					gen.writeStartObject();
					gen.writeNumberField("X", value.x);
					gen.writeNumberField("Y", value.y);
					gen.writeEndObject();
				}
			});
			module.addSerializer(android.graphics.PointF.class, new JsonSerializer<android.graphics.PointF>() {
				@Override
				public void serialize(final android.graphics.PointF value, final JsonGenerator gen, final SerializerProvider _unused) throws IOException {
					gen.writeStartObject();
					gen.writeNumberField("X", value.x);
					gen.writeNumberField("Y", value.y);
					gen.writeEndObject();
				}
			});
			module.addSerializer(android.graphics.RectF.class, new JsonSerializer<android.graphics.RectF>() {
				@Override
				public void serialize(final android.graphics.RectF value, final JsonGenerator gen, final SerializerProvider _unused) throws IOException {
					gen.writeStartObject();
					gen.writeNumberField("X", value.right);
					gen.writeNumberField("Y", value.top);
					gen.writeNumberField("Width", value.width());
					gen.writeNumberField("Height", value.height());
					gen.writeEndObject();
				}
			});
			module.addSerializer(android.graphics.Bitmap.class, new JsonSerializer<android.graphics.Bitmap>() {
				@Override
				public void serialize(final android.graphics.Bitmap value, final JsonGenerator gen, final SerializerProvider _unused) throws IOException {
					final ByteArrayOutputStream baos = new ByteArrayOutputStream();
					value.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, baos);
					gen.writeBinary(baos.toByteArray());
				}
			});
		}

		private static void initAndroidDeserializers(final SimpleModule module) {
			module.addDeserializer(android.graphics.Point.class, new JsonDeserializer<android.graphics.Point>() {
				@Override
				public android.graphics.Point deserialize(final JsonParser parser, final DeserializationContext _unused) throws IOException {
					final JsonNode tree = parser.getCodec().readTree(parser);
					return new android.graphics.Point(tree.get("X").asInt(), tree.get("Y").asInt());
				}
			});
			module.addDeserializer(android.graphics.PointF.class, new JsonDeserializer<android.graphics.PointF>() {
				@Override
				public android.graphics.PointF deserialize(final JsonParser parser, final DeserializationContext _unused) throws IOException {
					final JsonNode tree = parser.getCodec().readTree(parser);
					return new android.graphics.PointF(tree.get("X").floatValue(), tree.get("Y").floatValue());
				}
			});
			module.addDeserializer(android.graphics.RectF.class, new JsonDeserializer<android.graphics.RectF>() {
				@Override
				public android.graphics.RectF deserialize(final JsonParser parser, final DeserializationContext _unused) throws IOException {
					final JsonNode tree = parser.getCodec().readTree(parser);
					final float top = tree.get("X").floatValue();
					final float left = tree.get("Y").floatValue();
					final float width = tree.get("Width").floatValue();
					final float height = tree.get("Height").floatValue();
					return new android.graphics.RectF(left, top, left + width, top - height);
				}
			});
			module.addDeserializer(android.graphics.Bitmap.class, new JsonDeserializer<android.graphics.Bitmap>() {
				@Override
				public android.graphics.Bitmap deserialize(final JsonParser parser, final DeserializationContext _unused)
						throws IOException {
					final InputStream is = new ByteArrayInputStream(parser.getBinaryValue());
					return android.graphics.BitmapFactory.decodeStream(is);
				}
			});
		}
	}

	private static final JsonSerializer<Element> xmlSerializer = new JsonSerializer<Element>() {
		@Override
		public void serialize(final Element value, final JsonGenerator gen, final SerializerProvider sP)
				throws IOException {
			/*
			 * The Xml needs to be cleaned from whitespace text nodes, otherwise the
			 * converted document won't match Json.Net's conversion
			 */
			trimWhitespaceTextNodes(value);

			/* If the node is null, write nothing */
			if (value == null) return;

			value.getChildNodes();
			final HashMap<String, Object> hm = new HashMap<String, Object>();

			hm.put(value.getNodeName(), buildFromXml(value));

			gen.writeObject(hm);
		}
	};

	/**
	 * History delegate used to map JSON to History object.
	 */
	private static class HistoryDelegate<T extends AggregateRoot> {
		public final List<SnapshotDelegate<T>> Snapshots;

		private HistoryDelegate() { this.Snapshots = null; }
	}

	private static class SnapshotDelegate<T extends AggregateRoot> {
		public final DateTime At;
		public final String Action;
		public final T Value;

		@SuppressWarnings("unused")
		private SnapshotDelegate() { this(null, null, null); }

		public SnapshotDelegate(
				final DateTime At,
				final String Action,
				final T Value) {
			this.At = At;
			this.Action = Action;
			this.Value = Value;
		}
	}

	// ---------------------------------------------------------------------------------------------------------

	private static void trimWhitespaceTextNodes(final org.w3c.dom.Node node) {
		if (node != null && node.hasChildNodes()) {
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				final org.w3c.dom.Node child = node.getChildNodes().item(i);
				if (child.getNodeType() == org.w3c.dom.Node.TEXT_NODE && child.getNodeValue().trim().length() == 0) {
					node.removeChild(child);
				}
				trimWhitespaceTextNodes(node.getChildNodes().item(i));
			}
		}
	}

	/**
	 * Recursively builds a JSON object from the given root element
	 */
	private static Object buildFromXml(final Element el) {
		final NodeList cn = el.getChildNodes();
		final int childLen = cn.getLength();

		final LinkedHashMap<String, LinkedList<Node>> childrenByName = new LinkedHashMap<String, LinkedList<Node>>();

		/* Sort the nodes in the hash map by children names */
		for (int i = 0; i < childLen; i++) {
			final Node n = cn.item(i);
			final String name = n.getNodeName();
			if (!childrenByName.containsKey(name)) {
				childrenByName.put(name, new LinkedList<Node>());
			}
			childrenByName.get(name).add(n);
		}

		/*
		 * If there are no children, and no attributes, just return the node's text
		 * content value
		 */
		if (childLen == 0 && el.getAttributes().getLength() == 0) {
			return !"".equals(el.getTextContent()) ? el.getTextContent() : null;
		}

		/* Put all the element's attributes into a hash map */
		final LinkedHashMap<String, Object> jsonHashMap = new LinkedHashMap<String, Object>();
		final int attLen = el.getAttributes().getLength();
		for (int i = 0; i < attLen; i++) {
			final Node a = el.getAttributes().item(i);
			jsonHashMap.put("@" + a.getNodeName(), a.getNodeValue());
		}

		/*
		 * Put the child nodes to the hash map; Nodes are sorted by name, nodes
		 * having the same name are put together into an array
		 */
		for (final Map.Entry<String, LinkedList<Node>> name_ChildrenHavingName : childrenByName.entrySet()) {
			/* For all nodes having the current name */
			final Object[] items = new Object[name_ChildrenHavingName.getValue().size()];
			for (int i = 0; i < items.length; i++) {
				final Node n = name_ChildrenHavingName.getValue().get(i);
				/*
				 * If the node is an XML element, recursively convert it's tree to an
				 * Object
				 */
				if (n instanceof Element) {
					items[i] = buildFromXml((Element) n);
				}
				/* Otherwise use the text value */
				else {
					items[i] = !"".equals(n.getNodeValue()) ? n.getNodeValue() : null;
				}
			}
			/* Put the resulting object array / single object to the hash map */
			jsonHashMap.put(name_ChildrenHavingName.getKey(), items.length > 1 ? items : items[0]);
		}

		/* If the element has a single child */
		if (jsonHashMap.size() == 1 && childrenByName.keySet().iterator().hasNext()) {
			/* Get the single child's name */
			final String name = childrenByName.keySet().iterator().next();

			/* If it's a text node, the converted object is it's value */
			if (name.equals(TEXT_NODE_TAG)) return jsonHashMap.get(name);
			else {
				// Cleanup? If the single child has a single child with the same
				// name, return the grandchild?
				final Object parent = jsonHashMap.get(name);
				if (parent instanceof HashMap) {
					@SuppressWarnings("unchecked")
					final HashMap<String, Object> parentHm = (HashMap<String, Object>) parent;
					if (parentHm.size() == 1 && parentHm.containsKey(name)) return parentHm;
				}
			}
		}

		return jsonHashMap;
	}

	private static final JsonDeserializer<Element> xmlDeserializer = new JsonDeserializer<Element>() {
		@Override
		public Element deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {

			@SuppressWarnings("unchecked")
			final HashMap<String, Object> hm = parser.readValueAs(HashMap.class);
			if (hm == null) return null;
			return XmlConverter.mapToXml(hm);
		}
	};

	private static final SimpleModule serializationModule =
			new SimpleModule("SerializationModule", new Version(1, 0, 0, "SNAPSHOT", "com.dslplatform", "dsl-client-java"))
					.addSerializer(LocalDate.class, dateSerializer)
					.addSerializer(Element.class, xmlSerializer)
					.addSerializer(S3.class, s3Serializer)
					.addSerializer(DateTime.class, timestampSerializer);

	private static final ObjectMapper serializationMapper = new ObjectMapper()
			.registerModule(serializationModule)
			.setSerializationInclusion(Include.NON_NULL)
			.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

	private static final TypeFactory typeFactory = serializationMapper.getTypeFactory();

	static JavaType buildType(final Class<?> simple) {
		return typeFactory.constructType(simple);
	}

	@SuppressWarnings("deprecation")
	static JavaType buildGenericType(final Class<?> containerType, final Class<?>... element) {
		return typeFactory.constructParametricType(containerType, element);
	}

	@SuppressWarnings("rawtypes")
	static JavaType buildCollectionType(final Class<? extends Collection> collection, final JavaType element) {
		return typeFactory.constructCollectionType(collection, element);
	}

	@SuppressWarnings("rawtypes")
	static JavaType buildCollectionType(final Class<? extends Collection> collection, final Class<?> element) {
		return typeFactory.constructCollectionType(collection, element);
	}

	private final ObjectMapper deserializationMapper;

	public JacksonJsonSerialization(final ServiceLocator locator) {
		addPlatformDependentDeserializerModules(deserializationModule);
		addPlatformDependentSerializerModules(serializationModule);
		deserializationMapper = makeDeserializationObjectMapper(locator);
	}

	public <T> T deserialize(final JavaType type, final String data) throws IOException {
		return deserializationMapper.readValue(data, type);
	}

	public <T> T deserialize(final JavaType type, final byte[] data) throws IOException {
		return deserializationMapper.readValue(data, type);
	}

	public <T> T deserialize(final JavaType type, final byte[] data, final int size) throws IOException {
		return deserializationMapper.readValue(data, 0, size, type);
	}

	public <T> T deserialize(final Type type, final byte[] data, final int size) throws IOException {
		final JavaType javaType = deserializationMapper.getTypeFactory().constructType(type);
		return deserializationMapper.readValue(data, 0, size, javaType);
	}

	public <T> T deserialize(final Class<T> clazz, final byte[] data) throws IOException {
		return deserializationMapper.readValue(data, clazz);
	}

	public <T> T deserialize(final Class<T> clazz, final String data) throws IOException {
		return deserializationMapper.readValue(data, clazz);
	}

	public <T> T deserialize(final JavaType type, final InputStream stream) throws IOException {
		return deserializationMapper.readValue(stream, type);
	}

	@Override
	public <T extends AggregateRoot> List<History<T>> deserializeHistoryList(
			final Class<T> manifest,
			final byte[] content,
			final int size) throws IOException {
		final JavaType ht =
				buildCollectionType(
						ArrayList.class,
						buildGenericType(HistoryDelegate.class, manifest));

		final List<HistoryDelegate<T>> historyDelegates = deserializationMapper.readValue(content, 0, size, ht);
		final ArrayList<History<T>> historyList = new ArrayList<History<T>>(historyDelegates.size());
		for (final HistoryDelegate<T> historyDelegate : historyDelegates) {
			final List<SnapshotDelegate<T>> snapshots = historyDelegate.Snapshots;
			final ArrayList<Snapshot<T>> snapshotList = new ArrayList<Snapshot<T>>(snapshots.size());
			for (final SnapshotDelegate<T> SnapshotDelegate : snapshots) {
				snapshotList.add(new Snapshot<T>(
						SnapshotDelegate.At,
						SnapshotDelegate.Action,
						SnapshotDelegate.Value));
			}
			historyList.add(new History<T>(snapshotList));
		}
		return historyList;
	}

	public static String serializeString(final Object data) throws IOException {
		return serializationMapper.writer().writeValueAsString(data);
	}

	public static byte[] serializeBytes(final Object data) throws IOException {
		return serializationMapper.writer().writeValueAsBytes(data);
	}

	public static void serialize(final OutputStream stream, final Object data) throws IOException {
		serializationMapper.writer().writeValue(stream, data);
	}

	public static void serializeTo(final Writer writer, final Object data) throws IOException {
		serializationMapper.writer().writeValue(writer, data);
	}

	private static final SimpleModule deserializationModule =
			new SimpleModule("DeserializationModule", new Version(1, 0, 0, "SNAPSHOT", "com.dslplatform", "dsl-client-java"))
					.addDeserializer(LocalDate.class, dateDeserializer)
					.addDeserializer(DateTime.class, timestampDeserializer)
					.addDeserializer(S3.class, s3Deserializer)
					.addDeserializer(Element.class, xmlDeserializer);

	private static ObjectMapper makeDeserializationObjectMapper(final ServiceLocator locator) {
		return new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(Feature.ALLOW_NON_NUMERIC_NUMBERS, true)
				.registerModule(deserializationModule)
				.setInjectableValues(new InjectableValues.Std().addValue("_serviceLocator", locator));
	}

	private static void addPlatformDependentSerializerModules(final SimpleModule serializationModule) {
		if (Utils.IS_ANDROID) {
			AndroidConverters.initAndroidSerializers(serializationModule);
		} else {
			JavaConverters.initJavaSerializers(serializationModule);
		}
	}

	private static void addPlatformDependentDeserializerModules(final SimpleModule deserializationModule) {
		if (Utils.IS_ANDROID) {
			AndroidConverters.initAndroidDeserializers(deserializationModule);
		} else {
			JavaConverters.initJavaDeserializers(deserializationModule);
		}
	}

	@Override
	public Bytes serialize(Object value) throws IOException {
		final byte[] result = serializationMapper.writer().writeValueAsBytes(value);
		return new Bytes(result, result.length);
	}

	@Override
	public void serialize(Writer writer, Object value) throws IOException {
		serializationMapper.writer().writeValue(writer, value);
	}

	@Override
	public <T> T deserialize(Class<T> manifest, byte[] content, int size) throws IOException {
		return deserializationMapper.readValue(content, 0, size, manifest);
	}

	@Override
	public <T> List<T> deserializeList(Class<T> manifest, byte[] content, int size) throws IOException {
		final JavaType type = typeFactory.constructCollectionType(ArrayList.class, manifest);
		return deserializationMapper.readValue(content, 0, size, type);
	}
}
