package com.dslplatform.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.dslplatform.client.json.JsonObject;
import com.dslplatform.client.json.JsonWriter;
import com.dslplatform.client.json.StringConverter;
import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.Searchable;
import com.dslplatform.patterns.Specification;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

class HttpStandardProxy implements StandardProxy {
	private final static String STANDARD_URI = "Commands.svc/";
	private final static String APPLICATION_URI = "RestApplication.svc/";

	private final HttpClient client;
	private final ExecutorService executorService;

	public HttpStandardProxy(
			final HttpClient client,
			final ExecutorService executorService) {
		this.client = client;
		this.executorService = executorService;
	}

	@JsonSerialize
	private static class PersistArg implements JsonObject {
		@SuppressWarnings("unused")
		public final String RootName;
		@SuppressWarnings("unused")
		public final String ToInsert;
		@SuppressWarnings("unused")
		public final String ToUpdate;
		@SuppressWarnings("unused")
		public final String ToDelete;

		public PersistArg(
				final String rootName,
				final String toInsert,
				final String toUpdate,
				final String toDelete) {
			RootName = rootName;
			ToInsert = toInsert;
			ToUpdate = toUpdate;
			ToDelete = toDelete;
		}

		static <T> String serialize(final Class<?> clazz, final List<T> items) throws IOException {
			if (JsonObject.class.isAssignableFrom(clazz)) {
				final JsonWriter writer = new JsonWriter();
				writer.writeByte((byte)'[');
				((JsonObject) items.get(0)).serialize(writer, false);
				for (int i = 1; i < items.size(); i++) {
					writer.writeByte((byte)',');
					((JsonObject) items.get(i)).serialize(writer, false);
				}
				writer.writeByte((byte)']');
				return writer.toString();
			}
			return JsonSerialization.serialize(items);
		}

		static <T> String serializePairs(final Class<?> clazz, final List<Pair<T, T>> items) throws IOException {
			if (JsonObject.class.isAssignableFrom(clazz)) {
				final JsonWriter writer = new JsonWriter();
				final Pair<T, T> pair = items.get(0);
				writer.writeAscii("[{\"Key\":");
				((JsonObject) pair.key).serialize(writer, false);
				writer.writeAscii(",\"Value\":");
				((JsonObject) pair.value).serialize(writer, false);
				for (int i = 1; i < items.size(); i++) {
					final Pair<T, T> it = items.get(i);
					writer.writeAscii("},{\"Key\":");
					((JsonObject) it.key).serialize(writer, false);
					writer.writeAscii(",\"Value\":");
					((JsonObject) it.value).serialize(writer, false);
				}
				writer.writeAscii("}]");
				return writer.toString();
			}
			return JsonSerialization.serialize(items);
		}

		@Override
		public void serialize(JsonWriter writer, boolean minimal) {
			writer.writeByte((byte) '{');
			writer.writeAscii("\"RootName\":\"");
			writer.writeAscii(RootName);
			writer.writeAscii("\"");
			if (ToInsert != null) {
				writer.writeAscii(",\"ToInsert\":");
				StringConverter.serialize(ToInsert, writer);
			}
			if (ToUpdate != null) {
				writer.writeAscii(",\"ToUpdate\":");
				StringConverter.serialize(ToUpdate, writer);
			}
			if (ToDelete != null) {
				writer.writeAscii(",\"ToDelete\":");
				StringConverter.serialize(ToDelete, writer);
			}
			writer.writeByte((byte)'}');
		}
	}

	@SuppressWarnings("serial")
	private static class Pair<K, V> implements java.util.Map.Entry<K, V>, java.io.Serializable {
		public K key;
		public V value;

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(final V value) {
			return this.value = value;
		}
	}

	@Override
	public <T extends AggregateRoot> Future<List<String>> persist(
			final Iterable<T> inserts,
			final Iterable<Map.Entry<T, T>> updates,
			final Iterable<T> deletes) {

		return executorService.submit(new Callable<List<String>>() {
			@Override
			public List<String> call() throws Exception {
				Class<?> clazz = null;

				String toInsert = null;
				if (inserts != null) {
					final List<T> list = Utils.toArrayList(inserts);
					if (!list.isEmpty()) {
						clazz = list.get(0).getClass();
						toInsert = PersistArg.serialize(clazz, list);
					}
				}
				String toUpdate = null;
				if (updates != null) {
					final List<Pair<T, T>> list = new ArrayList<Pair<T, T>>();
					for (final Map.Entry<T, T> update : updates) {
						final Pair<T, T> pair = new Pair<T, T>();
						pair.key = update.getKey();
						pair.value = update.getValue();
						list.add(pair);
					}
					if (!list.isEmpty()) {
						clazz = list.get(0).value.getClass();
						toUpdate = PersistArg.serializePairs(clazz, list);
					}
				}

				String toDelete = null;
				if (deletes != null) {
					final List<T> list = Utils.toArrayList(deletes);
					if (!list.isEmpty()) {
						clazz = list.get(0).getClass();
						toDelete = PersistArg.serialize(clazz, list);
					}
				}

				if (clazz == null) return new ArrayList<String>(0);

				final String domainName = client.getDslName(clazz);

				return client.sendCollectionRequest(
						String.class,
						APPLICATION_URI + "PersistAggregateRoot",
						"POST",
						new PersistArg(domainName, toInsert, toUpdate, toDelete),
						new int[] { 200, 201 }).get();
			}
		});
	}

	@Override
	public <TDomainObject extends Searchable, TResult> Future<List<TResult>> olapCube(
			final Class<TResult> manifest,
			final String cubeName,
			final Specification<TDomainObject> specification,
			final Iterable<String> dimensions,
			final Iterable<String> facts,
			final Iterable<Map.Entry<String, Boolean>> order) {

		final Class<?> specClazz = specification.getClass();
		final String specParent = client.getDslName(specClazz.getEnclosingClass());
		final String specificationName = cubeName.equals(specParent)
				? specClazz.getSimpleName()
				: specParent + "%2B" + specClazz.getSimpleName();

		final String args = Utils.buildOlapArguments(dimensions, facts, order, specificationName);

		return client.sendCollectionRequest(
				manifest,
				STANDARD_URI + "olap/" + cubeName + args,
				"PUT",
				specification,
				new int[] { 200, 201 });
	}

	@Override
	public <TResult> Future<List<TResult>> olapCube(
			final Class<TResult> manifest,
			final String cubeName,
			final Iterable<String> dimensions,
			final Iterable<String> facts,
			final Iterable<Map.Entry<String, Boolean>> order) {
		final String args = Utils.buildOlapArguments(dimensions, facts, order);

		return client.sendCollectionRequest(
				manifest,
				STANDARD_URI + "olap/" + cubeName + args,
				"GET",
				null,
				new int[] { 200, 201 });
	}

	@Override
	public <TArgument, TResult> Future<TResult> execute(
			final Class<TResult> manifest,
			final String command,
			final TArgument argument) {
		return client.sendRequest(
				manifest,
				STANDARD_URI + "execute/" + command,
				"POST",
				argument,
				new int[] { 200, 201 });
	}
}
