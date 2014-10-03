package com.dslplatform.client;

import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dslplatform.patterns.ServiceLocator;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonSerialization {
	private static final String TEXT_NODE_TAG = "#text";
	private static final String COMMENT_NODE_TAG = "#comment";
	private static final String CDATA_NODE_TAG = "#cdata-section";
	//private static final String WHITESPACE_NODE_TAG = "#whitespace";
	//private static final String SIGNIFICANT_WHITESPACE_NODE_TAG = "#significant-whitespace";

	private static final JsonSerializer<LocalDate> dateSerializer = new JsonSerializer<LocalDate>() {
		@Override
		public void serialize(final LocalDate value, final JsonGenerator jg, SerializerProvider _) throws IOException {
			jg.writeString(value.toString());
		}
	};
	private static final JsonDeserializer<LocalDate> dateDeserializer = new JsonDeserializer<LocalDate>() {
		@Override
		public LocalDate deserialize(final JsonParser parser, DeserializationContext _) throws IOException {
			return new DateTime(parser.getValueAsString()).toLocalDate();
		}
	};

	private static final JsonSerializer<DateTime> timestampSerializer = new JsonSerializer<DateTime>() {
		@Override
		public void serialize(final DateTime value, final JsonGenerator jg, SerializerProvider _) throws IOException {
			jg.writeString(value.toString());
		}
	};

	private static final JsonDeserializer<DateTime> timestampDeserializer = new JsonDeserializer<DateTime>() {
		@Override
		public DateTime deserialize(final JsonParser parser, DeserializationContext _) throws IOException {
			return new DateTime(parser.getValueAsString());
		}
	};

	static class JavaConverters {
		private static void initJavaSerializers(final SimpleModule module) {
			module.addSerializer(java.awt.Point.class, new JsonSerializer<java.awt.Point>() {
				@Override
				public void serialize(final java.awt.Point value, final JsonGenerator jg, SerializerProvider _) throws IOException {
					jg.writeStartObject();
					jg.writeNumberField("X", value.x);
					jg.writeNumberField("Y", value.y);
					jg.writeEndObject();
				}
			});
			module.addSerializer(java.awt.geom.Point2D.class, new JsonSerializer<java.awt.geom.Point2D>() {
				@Override
				public void serialize(final java.awt.geom.Point2D value, final JsonGenerator jg, SerializerProvider _) throws IOException {
					jg.writeStartObject();
					jg.writeNumberField("X", value.getX());
					jg.writeNumberField("Y", value.getY());
					jg.writeEndObject();
				}
			});
			module.addSerializer(java.awt.geom.Rectangle2D.class, new JsonSerializer<java.awt.geom.Rectangle2D>() {
				@Override
				public void serialize(final java.awt.geom.Rectangle2D rect, final JsonGenerator jg, SerializerProvider _) throws IOException {
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
				public void serialize(final java.awt.image.BufferedImage image, final JsonGenerator jg, SerializerProvider _) throws IOException {
					final ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(image, "png", baos);
					jg.writeBinary(baos.toByteArray());
				}
			});
		}

		private static void initJavaDeserializers(final SimpleModule module) {
			module.addDeserializer(java.awt.Point.class, new JsonDeserializer<java.awt.Point>() {
				@Override
				public java.awt.Point deserialize(final JsonParser parser, DeserializationContext _) throws IOException {
					final JsonNode tree = parser.getCodec().readTree(parser);
					return new java.awt.Point(tree.get("X").asInt(), tree.get("Y").asInt());
				}
			});
			module.addDeserializer(java.awt.geom.Point2D.class, new JsonDeserializer<java.awt.geom.Point2D>() {
				@Override
				public java.awt.geom.Point2D deserialize(final JsonParser parser, DeserializationContext _) throws IOException {
					final JsonNode tree = parser.getCodec().readTree(parser);
					return new java.awt.geom.Point2D.Double(tree.get("X").asDouble(), tree.get("Y").asDouble());
				}
			});
			module.addDeserializer(java.awt.geom.Rectangle2D.class, new JsonDeserializer<java.awt.geom.Rectangle2D>() {
				@Override
				public java.awt.geom.Rectangle2D deserialize(final JsonParser parser, DeserializationContext _) throws IOException {
					final JsonNode tree = parser.getCodec().readTree(parser);
					return new java.awt.geom.Rectangle2D.Double(
							tree.get("X").asDouble(),
							tree.get("Y").asDouble(),
							tree.get("Width").asDouble(),
							tree.get("Height").asDouble());
				}
			});
			module.addDeserializer(java.awt.image.BufferedImage.class, new JsonDeserializer<java.awt.image.BufferedImage>() {
				@Override
				public java.awt.image.BufferedImage deserialize(final JsonParser parser, DeserializationContext _) throws IOException {
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
				public void serialize(final android.graphics.Point value, final JsonGenerator gen, SerializerProvider _) throws IOException {
					gen.writeStartObject();
					gen.writeNumberField("X", value.x);
					gen.writeNumberField("Y", value.y);
					gen.writeEndObject();
				}
			});
			module.addSerializer(android.graphics.PointF.class, new JsonSerializer<android.graphics.PointF>() {
				@Override
				public void serialize(final android.graphics.PointF value, final JsonGenerator gen, SerializerProvider _) throws IOException {
					gen.writeStartObject();
					gen.writeNumberField("X", value.x);
					gen.writeNumberField("Y", value.y);
					gen.writeEndObject();
				}
			});
			module.addSerializer(android.graphics.RectF.class, new JsonSerializer<android.graphics.RectF>() {
				@Override
				public void serialize(final android.graphics.RectF value, final JsonGenerator gen, SerializerProvider _) throws IOException {
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
				public void serialize(final android.graphics.Bitmap value, final JsonGenerator gen, SerializerProvider _) throws IOException {
					final ByteArrayOutputStream baos = new ByteArrayOutputStream();
					value.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, baos);
					gen.writeBinary(baos.toByteArray());
				}
			});
		}

		private static void initAndroidDeserializers(final SimpleModule module) {
			module.addDeserializer(android.graphics.Point.class, new JsonDeserializer<android.graphics.Point>() {
				@Override
				public android.graphics.Point deserialize(final JsonParser parser, DeserializationContext _) throws IOException {
					final JsonNode tree = parser.getCodec().readTree(parser);
					return new android.graphics.Point(tree.get("X").asInt(), tree.get("Y").asInt());
				}
			});
			module.addDeserializer(android.graphics.PointF.class, new JsonDeserializer<android.graphics.PointF>() {
				@Override
				public android.graphics.PointF deserialize(final JsonParser parser, DeserializationContext _) throws IOException {
					final JsonNode tree = parser.getCodec().readTree(parser);
					return new android.graphics.PointF(tree.get("X").floatValue(), tree.get("Y").floatValue());
				}
			});
			module.addDeserializer(android.graphics.RectF.class, new JsonDeserializer<android.graphics.RectF>() {
				@Override
				public android.graphics.RectF deserialize(final JsonParser parser, DeserializationContext _) throws IOException {
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
				public android.graphics.Bitmap deserialize(final JsonParser parser, DeserializationContext _)
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
				throws IOException  {
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

	/**
	 * Recursively builds an XML document subtree
	 *
	 * @param doc				the document to be built up
	 * @param subtreeRootElement the root of the subtree
	 * @param elementContent	 the value of the subtree
	 */
	@SuppressWarnings("unchecked")
	private static void buildXmlFromHashMap(
			final Document doc,
			final Element subtreeRootElement,
			final Object elementContent) {
		if (elementContent instanceof HashMap) {
			final HashMap<String, Object> elementContentMap = (HashMap<String, Object>) elementContent;
			for (final Map.Entry<String, Object> childEntry : elementContentMap.entrySet()) {
				final String key = childEntry.getKey();
				if (key.startsWith("@")) {
					subtreeRootElement.setAttribute(key.substring(1), childEntry.getValue().toString());
				} else if (key.startsWith("#")) {
					if (key.equals(TEXT_NODE_TAG)) {
						if (childEntry.getValue() instanceof List) {
							buildTextNodeList(doc, subtreeRootElement, (List<String>) childEntry.getValue());
						} else {
							final Node textNode = doc.createTextNode(childEntry.getValue().toString());
							subtreeRootElement.appendChild(textNode);
						}
					} else if (key.equals(CDATA_NODE_TAG)) {
						if (childEntry.getValue() instanceof List) {
							buildCDataList(doc, subtreeRootElement, (List<String>) childEntry.getValue());
						} else {
							final Node cDataNode = doc.createCDATASection(childEntry.getValue().toString());
							subtreeRootElement.appendChild(cDataNode);
						}
					} else if (key.equals(COMMENT_NODE_TAG)) {
						if (childEntry.getValue() instanceof List) {
							buildCommentList(doc, subtreeRootElement, (List<String>) childEntry.getValue());
						} else {
							final Node commentNode = doc.createComment(childEntry.getValue().toString());
							subtreeRootElement.appendChild(commentNode);
						}
					} //else if (key.equals(WHITESPACE_NODE_TAG)
						//	|| key.equals(SIGNIFICANT_WHITESPACE_NODE_TAG)) {
						// Ignore
					//} else {
						/*
						 * All other nodes whose name starts with a '#' are invalid XML
						 * nodes, and thus ignored:
						 */
					//}
				} else {
					final Element newElement = doc.createElement(key);
					subtreeRootElement.appendChild(newElement);
					buildXmlFromHashMap(doc, newElement, childEntry.getValue());
				}
			}
		} else if (elementContent instanceof List) {
			buildXmlFromJsonArray(doc, subtreeRootElement, (List<Object>) elementContent);
		} else {
			if (elementContent != null) {
				subtreeRootElement.setTextContent(elementContent.toString());
			}
		}
	}

	private static void buildTextNodeList(final Document doc, final Node subtreeRoot, final List<String> nodeValues) {
		final StringBuilder sb = new StringBuilder();
		for (final String nodeValue : nodeValues) {
			sb.append(nodeValue);
		}
		subtreeRoot.appendChild(doc.createTextNode(sb.toString()));
	}

	private static void buildCDataList(final Document doc, final Node subtreeRoot, final List<String> nodeValues) {
		for (final String nodeValue : nodeValues) {
			subtreeRoot.appendChild(doc.createCDATASection(nodeValue));
		}
	}

	private static void buildCommentList(final Document doc, final Node subtreeRoot, final List<String> nodeValues) {
		for (final String nodeValue : nodeValues) {
			subtreeRoot.appendChild(doc.createComment(nodeValue));
		}
	}

	/**
	 * Builds an XML subtree out of a list and a head element. The list elements
	 * are serialized into a series of nodes whose title is
	 * <code>listHeadElementName</code>. The head element need be created before
	 * this method call.
	 *
	 * @param doc				 The parent document
	 * @param listHeadNode		The first node of the list, needs to be created before this method
	 *							call
	 * @param elementContentList  The actual list contents
	 */
	private static void buildXmlFromJsonArray(
			final Document doc,
			final Node listHeadNode,
			final List<Object> elementContentList) {

		final Node subtreeRootNode = listHeadNode.getParentNode();
		/* The head node (already exists) */
		buildXmlFromHashMap(doc, (Element) listHeadNode, elementContentList.get(0));
		/* The rest of the list */
		for (final Object elementContent : elementContentList.subList(1, elementContentList.size())) {
			final Element newElement = doc.createElement(listHeadNode.getNodeName());
			subtreeRootNode.appendChild(newElement);
			buildXmlFromHashMap(doc, newElement, elementContent);
		}
	}

	private static final JsonDeserializer<Element> xmlDeserializer = new JsonDeserializer<Element>() {
		@Override
		public Element deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {

			@SuppressWarnings("unchecked")
			final HashMap<String, Object> hm = parser.readValueAs(HashMap.class);

			if (hm == null) return null;

			/* The root is expected to be a single element */
			final Set<String> xmlRootElementNames = hm.keySet();

			if (xmlRootElementNames.size() > 1) throw new IOException("Invalid XML. Expecting root element");

			final String rootName = xmlRootElementNames.iterator().next();

			final Document document;
			final Element rootElement;

			/* Initialise the document with a root element */
			try {
				synchronized (this) {
					final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					final DocumentBuilder builder = factory.newDocumentBuilder();
					document = builder.newDocument();
					rootElement = document.createElement(rootName);
					document.appendChild(rootElement);
				}
			} catch (final Exception e) {
				throw new IOException(e);
			}

			buildXmlFromHashMap(document, rootElement, hm.get(rootName));
			return rootElement;

		}
	};

	private static final SimpleModule serializationModule =
			new SimpleModule("SerializationModule", new Version(1, 0, 0, "SNAPSHOT", "com.dslplatform", "dsl-client-java"))
					.addSerializer(LocalDate.class, dateSerializer)
					.addSerializer(Element.class, xmlSerializer)
					.addSerializer(DateTime.class, timestampSerializer);

	private static final ObjectMapper serializationMapper = new ObjectMapper()
			.registerModule(serializationModule)
			.setSerializationInclusion(Include.NON_NULL)
			.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

	private static final TypeFactory typeFactory = serializationMapper.getTypeFactory();

	public static JavaType buildType(final Class<?> simple) {
		return typeFactory.constructType(simple);
	}

	public static JavaType buildGenericType(final Class<?> containerType, final Class<?>... element) {
		return typeFactory.constructParametricType(containerType, element);
	}

	@SuppressWarnings("rawtypes")
	public static JavaType buildCollectionType(final Class<? extends Collection> collection, final JavaType element) {
		return typeFactory.constructCollectionType(collection, element);
	}

	@SuppressWarnings("rawtypes")
	public static JavaType buildCollectionType(final Class<? extends Collection> collection, final Class<?> element) {
		return typeFactory.constructCollectionType(collection, element);
	}

	private final ObjectMapper deserializationMapper;

	JsonSerialization(final ServiceLocator locator) {
		deserializationMapper = makeDeserializationObjectMapper(locator);
	}

	public <T> T deserialize(final JavaType type, final String data) throws IOException {
		return deserializationMapper.readValue(data, type);
	}

	public <T> T deserialize(final JavaType type, final byte[] data) throws IOException {
		return deserializationMapper.readValue(data, type);
	}

	public <T> T deserialize(final JavaType type, final InputStream stream) throws IOException {
		return deserializationMapper.readValue(stream, type);
	}

	public static String serialize(final Object data) throws IOException {
		return serializationMapper.writer().writeValueAsString(data);
	}

	public static byte[] serializeBytes(final Object data) throws IOException {
		return serializationMapper.writer().writeValueAsBytes(data);
	}

	public static void serialize(final OutputStream stream, final Object data) throws IOException {
		serializationMapper.writer().writeValue(stream, data);
	}

	private static final SimpleModule deserializationModule =
			new SimpleModule("DeserializationModule", new Version(1, 0, 0, "SNAPSHOT", "com.dslplatform", "dsl-client-java"))
					.addDeserializer(LocalDate.class, dateDeserializer)
					.addDeserializer(DateTime.class, timestampDeserializer)
					.addDeserializer(Element.class, xmlDeserializer);

	private static ObjectMapper makeDeserializationObjectMapper(final ServiceLocator locator) {
		return new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.registerModule(deserializationModule)
				.setInjectableValues(new InjectableValues.Std().addValue("_serviceLocator", locator));
	}

	public static <T> T deserialize(final JavaType type, final String data, final ServiceLocator locator) throws IOException {
		return makeDeserializationObjectMapper(locator).readValue(data, type);
	}

	public static <T> T deserialize(final JavaType type, final byte[] data, final ServiceLocator locator) throws IOException {
		return makeDeserializationObjectMapper(locator).readValue(data, type);
	}

	public static <T> T deserialize(final JavaType type, final InputStream stream, final ServiceLocator locator) throws IOException {
		return makeDeserializationObjectMapper(locator).readValue(stream, type);
	}

	static {
		addPlatformDependentDeserializerModules(deserializationModule);
		addPlatformDependentSerializerModules(serializationModule);
	}

	private static void addPlatformDependentSerializerModules(final SimpleModule serializationModule) {
		if (Utils.isAndroid) {
			AndroidConverters.initAndroidSerializers(serializationModule);
		} else {
			JavaConverters.initJavaSerializers(serializationModule);
		}
	}

	private static void addPlatformDependentDeserializerModules(final SimpleModule deserializationModule) {
		if (Utils.isAndroid) {
			AndroidConverters.initAndroidDeserializers(deserializationModule);
		} else {
			JavaConverters.initJavaDeserializers(deserializationModule);
		}
	}
}
