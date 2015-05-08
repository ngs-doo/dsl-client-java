package com.dslplatform.client;

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

	private static class PersistArg implements JsonObject {
		public final String RootName;
		public final String ToInsert;
		public final String ToUpdate;
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

	static class Pair implements JsonObject {
		private final JsonObject key;
		private final JsonObject value;

		public <T extends AggregateRoot> Pair(Map.Entry<T, T> update) {
			this.key = (JsonObject)update.getKey();
			this.value = (JsonObject)update.getValue();
		}

		@Override
		public void serialize(final JsonWriter jw, final boolean minimal) {
			jw.writeAscii("{\"Key\":");
			if (key != null) {
				key.serialize(jw, minimal);
			} else {
				jw.writeNull();
			}
			jw.writeAscii(",\"Value\":");
			if (value != null) {
				value.serialize(jw, minimal);
			} else {
				jw.writeNull();
			}
			jw.writeByte(JsonWriter.OBJECT_END);
		}
	}

	private static ArrayList<JsonObject> toJsonList(final Iterable<?> iterable) {
		final ArrayList<JsonObject> copy = new ArrayList<JsonObject>();
		for (final Object t : iterable) {
			copy.add((JsonObject)t);
		}
		return copy;
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

				final JsonWriter jw = new JsonWriter();
				String toInsert = null;
				if (inserts != null) {
					final List<JsonObject> list = toJsonList(inserts);
					if (!list.isEmpty()) {
						clazz = list.get(0).getClass();
						jw.serialize(list);
						toInsert = jw.toString();
						jw.reset();
					}
				}
				String toUpdate = null;
				if (updates != null) {
					final List<Pair> list = new ArrayList<Pair>();
					for (final Map.Entry<T, T> update : updates) {
						list.add(new Pair(update));
					}
					if (!list.isEmpty()) {
						clazz = list.get(0).value.getClass();
						jw.serialize(list);
						toUpdate = jw.toString();
						jw.reset();
					}
				}

				String toDelete = null;
				if (deletes != null) {
					final List<JsonObject> list = toJsonList(deletes);
					if (!list.isEmpty()) {
						clazz = list.get(0).getClass();
						jw.serialize(list);
						toDelete = jw.toString();
						jw.reset();
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
