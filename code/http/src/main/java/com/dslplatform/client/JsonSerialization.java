package com.dslplatform.client;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonSerialization {

    /* Constants for XML deserialization */
    private static final String textNodeTag = "#text";
    private static final String commentNodeTag = "#comment";
    private static final String cDataNodeTag = "#cdata-section";
    private static final String whitespaceNodeTag = "#whitespace";
    private static final String significantWhitespaceNodeTag =
            "#significant-whitespace";

    private static final JsonSerializer<LocalDate> dateSerializer =
            new JsonSerializer<LocalDate>() {
                @Override
                public void serialize(
                        final LocalDate value,
                        final JsonGenerator generator,
                        final SerializerProvider x) throws IOException,
                        JsonProcessingException {
                    generator.writeString(value.toString());
                }
            };

    private static final JsonDeserializer<LocalDate> dateDeserializer =
            new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(
                        final JsonParser parser,
                        final DeserializationContext context)
                        throws IOException, JsonProcessingException {
                    return new DateTime(parser.getValueAsString())
                            .toLocalDate();
                }
            };

    // -----------------------------------------------------------------------------

    private static final JsonSerializer<DateTime> timestampSerializer =
            new JsonSerializer<DateTime>() {
                @Override
                public void serialize(
                        final DateTime value,
                        final JsonGenerator gen,
                        final SerializerProvider sP) throws IOException,
                        JsonProcessingException {
                    gen.writeString(value.toString());
                }
            };

    private static final JsonDeserializer<DateTime> timestampDeserializer =
            new JsonDeserializer<DateTime>() {
                @Override
                public DateTime deserialize(
                        final JsonParser parser,
                        final DeserializationContext context)
                        throws IOException, JsonProcessingException {
                    return new DateTime(parser.getValueAsString());
                }
            };

    // -----------------------------------------------------------------------------

    private static final JsonSerializer<Element> xmlSerializer =
            new JsonSerializer<Element>() {
                @Override
                public void serialize(
                        final Element value,
                        final JsonGenerator gen,
                        final SerializerProvider sP) throws IOException,
                        JsonProcessingException {

                    /*
                     * The Xml needs to be cleaned from whitespace text nodes, otherwise the
                     * converted document won't match Json.Net's conversion
                     */
                    trimWhitespaceTextNodes(value);

                    /* If the node is null, write nothing */
                    if (value == null) return;

                    final NodeList children = value.getChildNodes();
                    /* If the node has no children, only write the node name:value */
                    if (children.getLength() == 0) {
                        gen.writeStringField(value.getNodeName(),
                                value.getTextContent());
                    }
                    /*
                     * Otherwise write recursively build the JSON hashmap, and write it to the
                     * generator
                     */
                    else {
                        final HashMap<String, Object> hm =
                                new HashMap<String, Object>();
                        hm.put(value.getNodeName(), buildFromXml(value));
                        gen.writeObject(hm);
                    }
                }
            };

    private static void trimWhitespaceTextNodes(final org.w3c.dom.Node node) {
        if (node != null && node.hasChildNodes()) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                final org.w3c.dom.Node child = node.getChildNodes().item(i);
                if (child.getNodeType() == org.w3c.dom.Node.TEXT_NODE
                        && child.getNodeValue().trim().length() == 0) {
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

        final LinkedHashMap<String, LinkedList<Node>> childrenByName =
                new LinkedHashMap<String, LinkedList<Node>>();

        System.out.println("Doctype:");
        System.out.println(el.getOwnerDocument().getDoctype());

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
        if (childLen == 0 && el.getAttributes().getLength() == 0)
            return el.getTextContent() != ""
                    ? el.getTextContent()
                    : null;

        /* Put all the element's attributes into a hash map */
        final LinkedHashMap<String, Object> jsonHashMap =
                new LinkedHashMap<String, Object>();
        final int attLen = el.getAttributes().getLength();
        for (int i = 0; i < attLen; i++) {
            final Node a = el.getAttributes().item(i);
            jsonHashMap.put("@" + a.getNodeName(), a.getNodeValue());
        }

        /*
         * Put the child nodes to the hash map; Nodes are sorted by name, nodes
         * having the same name are put together into an array
         */
        for (final Map.Entry<String, LinkedList<Node>> name_ChildrenHavingName : childrenByName
                .entrySet()) {
            /* For all nodes having the current name */
            final Object[] items =
                    new Object[name_ChildrenHavingName.getValue().size()];
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
                    items[i] = n.getNodeValue() != ""
                            ? n.getNodeValue()
                            : null;
                }
            }
            /* Put the resulting object array / single object to the hash map */
            jsonHashMap.put(name_ChildrenHavingName.getKey(), items.length > 1
                    ? items
                    : items[0]);
        }

        /* If the element has a single child */
        if (jsonHashMap.size() == 1
                && childrenByName.keySet().iterator().hasNext()) {
            /* Get the single child's name */
            final String name = childrenByName.keySet().iterator().next();

            /* If it's a text node, the converted object is it's value */
            if (name.equals(textNodeTag)) return jsonHashMap.get(name);
            else {
                // Cleanup? If the single child has a single child with the same
                // name, return the grandchild?
                final Object parent = jsonHashMap.get(name);
                if (parent instanceof HashMap) {
                    @SuppressWarnings("unchecked")
                    final HashMap<String, Object> parentHm =
                            (HashMap<String, Object>) parent;
                    if (parentHm.size() == 1 && parentHm.containsKey(name))
                        return parentHm;
                }
            }
        }

        return jsonHashMap;
    }

    /**
     * Recursively builds an XML document subtree
     *
     * @param doc
     *          the document to be built up
     * @param subtreeRootElement
     *          the root of the subtree
     * @param elementContent
     *          the value of the subtree
     */
    private static void buildXmlFromHashMap(
            final Document doc,
            final Element subtreeRootElement,
            final Object elementContent) {
        if (elementContent instanceof HashMap) {
            @SuppressWarnings("unchecked")
            final HashMap<String, Object> elementContentMap =
                    (HashMap<String, Object>) elementContent;
            for (final Map.Entry<String, Object> childEntry : elementContentMap
                    .entrySet()) {
                if (childEntry.getKey().startsWith("@")) {
                    subtreeRootElement.setAttribute(childEntry.getKey()
                            .substring(1), childEntry.getValue().toString());
                } else if (childEntry.getKey().startsWith("#")) {
                    if (childEntry.getKey().equals(textNodeTag)) {
                        if (childEntry.getValue() instanceof List) {
                            buildTextNodeList(doc, subtreeRootElement,
                                    (List<String>) childEntry.getValue());
                        } else {
                            final Node textNode =
                                    doc.createTextNode(childEntry.getValue()
                                            .toString());
                            subtreeRootElement.appendChild(textNode);
                        }
                    } else if (childEntry.getKey().equals(cDataNodeTag)) {
                        if (childEntry.getValue() instanceof List) {
                            buildCDataList(doc, subtreeRootElement,
                                    (List<String>) childEntry.getValue());
                        } else {
                            final Node cDataNode =
                                    doc.createCDATASection(childEntry
                                            .getValue().toString());
                            subtreeRootElement.appendChild(cDataNode);
                        }
                    } else if (childEntry.getKey().equals(commentNodeTag)) {
                        if (childEntry.getValue() instanceof List) {
                            buildCommentList(doc, subtreeRootElement,
                                    (List<String>) childEntry.getValue());
                        } else {
                            final Node commentNode =
                                    doc.createComment(childEntry.getValue()
                                            .toString());
                            subtreeRootElement.appendChild(commentNode);
                        }
                    } else if (childEntry.getKey().equals(whitespaceNodeTag)
                            || childEntry.getKey().equals(
                                    significantWhitespaceNodeTag)) {
                        // Ignore
                    } else {
                        /*
                         * All other nodes whose name starts with a '#' are invalid XML
                         * nodes, and thus ignored:
                         */
                    }
                } else {
                    final Element newElement =
                            doc.createElement(childEntry.getKey());
                    subtreeRootElement.appendChild(newElement);
                    buildXmlFromHashMap(doc, newElement, childEntry.getValue());
                }
            }
        } else if (elementContent instanceof List) {
            buildXmlFromJsonArray(doc, subtreeRootElement,
                    (List) elementContent);
        } else {
            if (elementContent != null) {
                subtreeRootElement.setTextContent(elementContent.toString());
            }
        }
    }

    private static void buildTextNodeList(
            final Document doc,
            final Node subtreeRoot,
            final List<String> nodeValues) {

        final StringBuffer singleTextNodeValue = new StringBuffer();

        for (final String nodeValue : nodeValues) {
            singleTextNodeValue.append(nodeValue);
            // subtreeRoot.appendChild(doc.createTextNode(nodeValue));
        }

        subtreeRoot.appendChild(doc.createTextNode(singleTextNodeValue
                .toString()));
    }

    private static void buildCDataList(
            final Document doc,
            final Node subtreeRoot,
            final List<String> nodeValues) {
        for (final String nodeValue : nodeValues) {
            subtreeRoot.appendChild(doc.createCDATASection(nodeValue));
        }
    }

    private static void buildCommentList(
            final Document doc,
            final Node subtreeRoot,
            final List<String> nodeValues) {
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
     * @param doc
     *          The parent document
     * @param listHeadNode
     *          The first node of the list, needs to be created before this method
     *          call
     * @param listHeadElementName
     *          The name of the list's head element
     * @param elementContentList
     *          The actual list contents
     */
    private static void buildXmlFromJsonArray(
            final Document doc,
            final Node listHeadNode,
            final List<Object> elementContentList) {

        final Node subtreeRootNode = listHeadNode.getParentNode();
        /* The head node (already exists) */
        buildXmlFromHashMap(doc, (Element) listHeadNode,
                elementContentList.get(0));
        /* The rest of the list */
        for (final Object elementContent : elementContentList.subList(1,
                elementContentList.size())) {
            final Element newElement =
                    doc.createElement(listHeadNode.getNodeName());
            subtreeRootNode.appendChild(newElement);
            buildXmlFromHashMap(doc, newElement, elementContent);
        }
    }

    private static final JsonDeserializer<Element> xmlDeserializer =
            new JsonDeserializer<Element>() {
                @Override
                public Element deserialize(
                        final JsonParser parser,
                        final DeserializationContext context)
                        throws IOException, JsonProcessingException {

                    @SuppressWarnings("unchecked")
                    final HashMap<String, Object> hm =
                            parser.readValueAs(HashMap.class);

                    if (hm == null) return null;

                    /* The root is expected to be a single element */
                    final Set<String> xmlRootElementNames = hm.keySet();

                    if (xmlRootElementNames.size() > 1)
                        throw new IOException(
                                "Invalid XML. Expecting root element");

                    final String rootName =
                            xmlRootElementNames.iterator().next();

                    final Document document;
                    final Element rootElement;

                    /* Initialise the document with a root element */
                    try {
                        synchronized (this) {
                            final DocumentBuilderFactory factory =
                                    DocumentBuilderFactory.newInstance();
                            final DocumentBuilder builder =
                                    factory.newDocumentBuilder();
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

    // -----------------------------------------------------------------------------

    private static final SimpleModule serializationModule = new SimpleModule(
            "SerializationModule", new Version(0, 0, 0, "SNAPSHOT",
                    "com.dslplatform", "dsl-client"))
            .addSerializer(LocalDate.class, dateSerializer)
            .addSerializer(DateTime.class, timestampSerializer)
            .addSerializer(Element.class, xmlSerializer);

    private static final ObjectMapper serializationMapper = new ObjectMapper()
            .registerModule(serializationModule)
            .setSerializationInclusion(Include.NON_NULL)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    private static final TypeFactory typeFactory = serializationMapper
            .getTypeFactory();

    public static JavaType buildType(final Class<?> simple) {
        return typeFactory.constructType(simple);
    }

    public static JavaType buildGenericType(
            final Class<?> containerType,
            final Class<?>... element) {
        return typeFactory.constructParametricType(containerType, element);
    }

    @SuppressWarnings("rawtypes")
    public static JavaType buildCollectionType(
            final Class<? extends Collection> collection,
            final JavaType element) {
        return typeFactory.constructCollectionType(collection, element);
    }

    @SuppressWarnings("rawtypes")
    public static JavaType buildCollectionType(
            final Class<? extends Collection> collection,
            final Class<?> element) {
        return typeFactory.constructCollectionType(collection, element);
    }

    // -----------------------------------------------------------------------------

    private final ObjectMapper deserializationMapper;

    JsonSerialization(final ServiceLocator locator) {
        deserializationMapper = makeDeserializationObjectMapper(locator);
    }

    public <T> T deserialize(final JavaType type, final String data)
            throws IOException {
        return deserializationMapper.readValue(data, type);
    }

    // -----------------------------------------------------------------------------

    public static <T> String serialize(final T data) throws IOException {
        return serializationMapper.writer()
        /* .withDefaultPrettyPrinter() */.writeValueAsString(data);
    }

    private static final SimpleModule deserializationModule = new SimpleModule(
            "DeserializationModule", new Version(0, 0, 0, "SNAPSHOT",
                    "com.dslplatform", "dsl-client"))
            .addDeserializer(LocalDate.class, dateDeserializer)
            .addDeserializer(DateTime.class, timestampDeserializer)
            .addDeserializer(Element.class, xmlDeserializer);

    private static ObjectMapper makeDeserializationObjectMapper(
            final ServiceLocator locator) {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        false)
                .registerModule(deserializationModule)
                .setInjectableValues(
                        new InjectableValues.Std().addValue("_serviceLocator",
                                locator));
    }

    public static <T> T deserialize(
            final JavaType type,
            final String data,
            final ServiceLocator locator) throws IOException {
        return makeDeserializationObjectMapper(locator).readValue(data, type);
    }
}
