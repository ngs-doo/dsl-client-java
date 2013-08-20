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
    private static final JsonSerializer<LocalDate> dateSerializer = new JsonSerializer<LocalDate>() {
        @Override
        public void serialize(
                final LocalDate value,
                final JsonGenerator generator,
                final SerializerProvider x) throws IOException, JsonProcessingException {
          generator.writeString(value.toString());
        }
    };

    private static final JsonDeserializer<LocalDate> dateDeserializer = new JsonDeserializer<LocalDate>() {
        @Override
        public LocalDate deserialize(
                final JsonParser parser,
                final DeserializationContext context) throws IOException, JsonProcessingException {
            return new DateTime(parser.getValueAsString()).toLocalDate();
        }
    };

 // -----------------------------------------------------------------------------

    private static final JsonSerializer<DateTime> timestampSerializer = new JsonSerializer<DateTime>() {
        @Override
        public void serialize(
                final DateTime value,
                final JsonGenerator gen,
                final SerializerProvider sP) throws IOException, JsonProcessingException {
          gen.writeString(value.toString());
        }
    };

    private static final JsonDeserializer<DateTime> timestampDeserializer = new JsonDeserializer<DateTime>() {
        @Override
        public DateTime deserialize(
                final JsonParser parser,
                final DeserializationContext context) throws IOException, JsonProcessingException {
            return new DateTime(parser.getValueAsString());
        }
    };

// -----------------------------------------------------------------------------

    private static final JsonSerializer<Element> xmlSerializer = new JsonSerializer<Element>() {
        @Override
        public void serialize(
                final Element value,
                final JsonGenerator gen,
                final SerializerProvider sP) throws IOException, JsonProcessingException {

            if(value == null) return;

            final NodeList children = value.getChildNodes();
            if(children.getLength() == 0) {
                gen.writeStringField(value.getNodeName(), value.getTextContent());
            }
            else {
                final HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put(value.getNodeName(), buildFromXml(value));
                gen.writeObject(hm);
            }
        }
    };

    private static Object buildFromXml(final Element el) {
        final NodeList cn = el.getChildNodes();
        final int childLen = cn.getLength();
        final LinkedHashMap<String, LinkedList<Node>> children = new LinkedHashMap<String, LinkedList<Node>>();
        for(int i = 0; i < childLen; i++) {
            final Node n = cn.item(i);
            final String name = n.getNodeName();
            if(!children.containsKey(name)) {
                children.put(name, new LinkedList<Node>());
            }
            children.get(name).add(n);
        }
        if(childLen == 0 && el.getAttributes().getLength() == 0) {
            return el.getTextContent();
        }
        final LinkedHashMap<String, Object> hm = new LinkedHashMap<String, Object>();
        final int attLen = el.getAttributes().getLength();
        for(int i = 0; i< attLen; i++) {
            final Node a = el.getAttributes().item(i);
            hm.put("@" + a.getNodeName(), a.getNodeValue());
        }
        for(final Map.Entry<String, LinkedList<Node>> kv: children.entrySet()) {
            final Object[] items = new Object[kv.getValue().size()];
            for(int i = 0; i < items.length; i++) {
                final Node n = kv.getValue().get(i);
                if(n instanceof Element) {
                    items[i] = buildFromXml((Element) n);
                }
                else {
                    items[i] = n.getNodeValue();
                }
            }
            hm.put(kv.getKey(), items.length > 1 ? items : items[0]);
        }
        if(hm.size() == 1) {
            final String name = children.keySet().iterator().next();
            if(name == "#text") {
                return hm.get(name);
            }
            else {
                final Object parent = hm.get(name);
                if(parent instanceof HashMap) {
                    @SuppressWarnings("unchecked")
                    final HashMap<String, Object> parentHm = (HashMap<String, Object>) parent;
                    if(parentHm.size() == 1 && parentHm.containsKey(name)) {
                        return parentHm;
                    }
                }
            }
        }
        return hm;
    }

    private static void buildXmlFromHashMap(final Document doc, final Element el, final Object value) {
        if(value instanceof HashMap) {
            @SuppressWarnings("unchecked")
            final HashMap<String, Object> hm = (HashMap<String, Object>) value;
            for(final Map.Entry<String, Object> kv: hm.entrySet()) {
                if(kv.getKey().startsWith("@")) {
                    el.setAttribute(kv.getKey().substring(1), kv.getValue().toString());
                }
                else if (kv.getKey() == "#text") {
                    el.setTextContent(kv.getValue().toString());
                }
                else {
                    final Element newElement = doc.createElement(kv.getKey());
                    el.appendChild(newElement);
                    buildXmlFromHashMap(doc, newElement, kv.getValue());
                }
            }
        }
        else if(value instanceof List) {
            @SuppressWarnings("unchecked")
            final List<Object> l = (List<Object>) value;
            for(final Object v : l) {
                final Element newElement = doc.createElement(el.getNodeName());
                el.appendChild(newElement);
                buildXmlFromHashMap(doc, newElement, v);
            }
        }
        else {
            if(value != null) {
                el.setTextContent(value.toString());
            }
        }
    }

    private static final JsonDeserializer<Element> xmlDeserializer = new JsonDeserializer<Element>() {
        @Override
        public Element deserialize(
                final JsonParser parser,
                final DeserializationContext context) throws IOException, JsonProcessingException {

            @SuppressWarnings("unchecked")
            final HashMap<String, Object> hm = (HashMap<String, Object>) parser.readValueAs(HashMap.class);

            if(hm == null) return null;

            final Set<String> xmlRoot = hm.keySet();
            if(xmlRoot.size() > 1) throw new IOException("Invalid XML. Expecting root element");
            final String rootName = xmlRoot.iterator().next();

            final Document document;
            final Element element;

            try {
                synchronized (this) {
                    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    final DocumentBuilder builder = factory.newDocumentBuilder();
                    document = builder.newDocument();
                    element = document.createElement(rootName);
                    document.appendChild(element);
                }
            }
            catch(final Exception e) {
                throw new IOException(e);
            }

            buildXmlFromHashMap(document, element, hm.get(rootName));
            return element;

        }
    };

 // -----------------------------------------------------------------------------

    private static final SimpleModule serializationModule =
        new SimpleModule("SerializationModule",
                new Version(0, 0, 0, "SNAPSHOT", "com.dslplatform", "dsl-client"))
            .addSerializer(LocalDate.class, dateSerializer)
            .addSerializer(DateTime.class, timestampSerializer)
            .addSerializer(Element.class, xmlSerializer);

    private static final ObjectMapper serializationMapper =
        new ObjectMapper()
            .registerModule(serializationModule)
            .setSerializationInclusion(Include.NON_EMPTY)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    private static final TypeFactory typeFactory = serializationMapper.getTypeFactory();

    public static JavaType buildType(Class<?> simple) {
        return typeFactory.constructType(simple);
    }

    public static JavaType buildGenericType(Class<?> containerType, Class<?>... element) {
        return typeFactory.constructParametricType(containerType, element);
    }

    @SuppressWarnings("rawtypes")
    public static JavaType buildCollectionType(Class<? extends Collection> collection, JavaType element) {
        return typeFactory.constructCollectionType(collection, element);
    }

    @SuppressWarnings("rawtypes")
    public static JavaType buildCollectionType(Class<? extends Collection> collection, Class<?> element) {
        return typeFactory.constructCollectionType(collection, element);
    }

    public <T> String serialize(final T data) throws IOException {
        return serializationMapper.writer()/*.withDefaultPrettyPrinter()*/.writeValueAsString(data);
    }

    private static final SimpleModule deserializationModule =
            new SimpleModule("DeserializationModule",
                    new Version(0, 0, 0, "SNAPSHOT", "com.dslplatform", "dsl-client"))
                .addDeserializer(LocalDate.class, dateDeserializer)
                .addDeserializer(DateTime.class, timestampDeserializer)
                .addDeserializer(Element.class, xmlDeserializer);

    public <T> T deserialize(
            final JavaType type,
            final String data,
            final ServiceLocator locator) throws IOException {
        final ObjectMapper oM = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(deserializationModule)
            .setInjectableValues(new InjectableValues.Std().addValue("_serviceLocator", locator));

        return oM.readValue(data, type);
    }
}
