package com.dslplatform.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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
            final ExecutorService excutorService) {
        this.client = client;
        this.executorService = excutorService;
    }

    @JsonSerialize
    private static class PersistArg {
        @SuppressWarnings("unused")
        public final String RootName;
        @SuppressWarnings("unused")
        public final String ToInsert;
        @SuppressWarnings("unused")
        public final String ToUpdate;
        @SuppressWarnings("unused")
        public final String ToDelete;

        public PersistArg(
                String rootName,
                String toInsert,
                String toUpdate,
                String toDelete) {
            RootName = rootName;
            ToInsert = toInsert;
            ToUpdate = toUpdate;
            ToDelete = toDelete;
        }
    }

    @SuppressWarnings("serial")
    private static class Pair<K, V> implements java.util.Map.Entry<K, V>,
            java.io.Serializable {

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
            this.value = value;
            return value;
        }
    }

    @Override
    public <T extends AggregateRoot> Future<List<String>> persist(
            final Iterable<T> inserts,
            final Iterable<Map.Entry<T, T>> updates,
            final Iterable<T> deletes) {

        return executorService.submit(new Callable<List<String>>() {
            @SuppressWarnings("unchecked")
            @Override
            public List<String> call() throws Exception {
                Class<?> clazz = null;

                String toInsert = null;
                if (inserts != null) {
                    final List<T> list = Utils.toArrayList(inserts);
                    if (!list.isEmpty()) {
                        toInsert = JsonSerialization.serialize(list);
                        clazz = list.get(0).getClass();
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
                        toUpdate = JsonSerialization.serialize(list);
                        clazz = list.get(0).value.getClass();
                    }
                }

                String toDelete = null;
                if (deletes != null) {
                    final List<T> list = Utils.toArrayList(deletes);
                    if (!list.isEmpty()) {

                        toDelete = JsonSerialization.serialize(list);
                        clazz = list.get(0).getClass();
                    }
                }

                if (clazz == null) {
                    return new ArrayList<String>();
                }

                final String domainName = client.getDslName(clazz);

                return (List<String>) client
                        .sendRequest(
                                JsonSerialization.buildCollectionType(
                                        ArrayList.class, String.class),
                                APPLICATION_URI + "PersistAggregateRoot",
                                "POST",
                                new PersistArg(domainName, toInsert, toUpdate,
                                        toDelete), new int[] { 200, 201 })
                        .get();
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
        final String specParent = client.getDslName(specClazz
                .getEnclosingClass());
        final String specificationName = cubeName.equals(specParent)
                ? specClazz.getSimpleName()
                : specParent + "%2B" + specClazz.getSimpleName();

        final String args = Utils.buildOlapArguments(dimensions, facts, order,
                specificationName);

        return client
                .sendRequest(JsonSerialization.buildCollectionType(
                        java.util.List.class, manifest), STANDARD_URI + "olap/"
                        + cubeName + args, "PUT", specification, new int[] {
                        200, 201 });
    }

    @Override
    public <TResult> Future<List<TResult>> olapCube(
            final Class<TResult> manifest,
            final String cubeName,
            final Iterable<String> dimensions,
            final Iterable<String> facts,
            final Iterable<Map.Entry<String, Boolean>> order) {
        final String args = Utils.buildOlapArguments(dimensions, facts, order);

        return client.sendRequest(JsonSerialization.buildCollectionType(
                java.util.List.class, manifest), STANDARD_URI + "olap/"
                + cubeName + args, "GET", null, new int[] { 200, 201 });
    }

    @Override
    public <TArgument, TResult> Future<TResult> execute(
            final Class<TResult> manifest,
            final String command,
            final TArgument argument) {
        return client.sendRequest(JsonSerialization.buildType(manifest),
                STANDARD_URI + "execute/" + command, "POST", argument,
                new int[] { 200, 201 });
    }
}
