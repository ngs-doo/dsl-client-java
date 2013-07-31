package com.dslplatform.client;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.dslplatform.patterns.Searchable;
import com.dslplatform.patterns.SearchableRepository;
import com.dslplatform.patterns.Specification;

class GenericSpecification<T extends Searchable> implements Specification<T> {

    private final HashMap<String, ArrayList<FilterPair>> filters = new HashMap<String, ArrayList<FilterPair>>();

    private final SearchableRepository<T> repository;
    private final JsonSerialization serializer;

    public static class FilterPair
    {
        public final Integer Key;
        public final String Value;

        public FilterPair(final int key, final String value) {
            this.Key = key;
            this.Value = value;
        }

        @SuppressWarnings("unused")
		private FilterPair() {
            this.Key = null;
            this.Value = null;
        }
    }

    private Integer limit;
    private Integer offset;
    private final ArrayList<Map.Entry<String, Boolean>> order = new ArrayList<Map.Entry<String, Boolean>>();

    static class GenericSearchFilter
    {
        public static final int EQUALS = 0;
        public static final int NOT_EQUALS = 1;
        public static final int LESS_THEN = 2;
        public static final int LESS_THEN_OR_EQUAL = 3;
        public static final int GREATER_THEN = 4;
        public static final int GREATER_THEN_OR_EQUAL = 5;
        public static final int VALUE_IN = 6;
        public static final int NOT_VALUE_IN = 7;
        public static final int IN_VALUE = 8;
        public static final int NOT_IN_VALUE = 9;
        public static final int STARTS_WITH_VALUE = 10;
        public static final int STARTS_WITH_CASE_INSENSITIVE_VALUE = 11;
        public static final int NOT_STARTS_WITH_VALUE = 12;
        public static final int NOT_STARTS_WITH_CASE_INSENSITIVE_VALUE = 13;
        public static final int VALUE_STARTS_WITH = 14;
        public static final int VALUE_STARTS_WITH_CASE_INSENSITIVE = 15;
        public static final int NOT_VALUE_STARTS_WITH = 16;
        public static final int NOT_VALUE_STARTS_WITH_CASE_INSENSITIVE = 17;
    }

    public final Class<T> Manifest;

    public GenericSpecification(
            final Class<T> manifest,
            final SearchableRepository<T> repository,
            final JsonSerialization serializer)    {
        this.Manifest = manifest;
        this.repository = repository;
        this.serializer = serializer;
    }

    public Object toFilter() {
        return filters;
    }

    public GenericSpecification<T> take(final int limit) {
        this.limit = limit;
        return this;
    }
    public GenericSpecification<T> limit(final int limit) { return take(limit); }

    public GenericSpecification<T> skip(final int offset) {
        this.offset = offset;
        return this;
    }
    public GenericSpecification<T> offset(final int offset) { return skip(offset); }

    public Future<List<T>> search() {
        return repository.search(this, limit, offset, order);
    }

    private GenericSpecification<T> orderBy(final String property, final Boolean direction) {
        if(property == null || property == "") throw new IllegalArgumentException("property can't be null");
        this.order.add(new AbstractMap.SimpleEntry<String, Boolean>(property, direction));
        return this;
    }

    public GenericSpecification<T> ascending(final String property) { return orderBy(property, true); }
    public GenericSpecification<T> descending(final String property) { return orderBy(property, false); }

    private GenericSpecification<T> filter(final String property, final int id, final Object value) throws IOException {
        if(property == null || property == "") throw new IllegalArgumentException("property can't be null");
        final String json = value != null ? serializer.serialize(value) : null;
        final ArrayList<FilterPair> pairs;
        if(!filters.containsKey(property)) {
            pairs = new ArrayList<FilterPair>();
            filters.put(property, pairs);
        }
        else pairs = filters.get(property);
        pairs.add(new FilterPair(id, json));
        return this;
    }

    public GenericSpecification<T> equal(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.EQUALS, value);
    }

    public GenericSpecification<T> nonEqual(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.NOT_EQUALS, value);
    }

    public GenericSpecification<T> lessThen(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.LESS_THEN, value);
    }

    public GenericSpecification<T> lessThenOrEqual(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.LESS_THEN_OR_EQUAL, value);
    }

    public GenericSpecification<T> greaterThen(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.GREATER_THEN, value);
    }

    public GenericSpecification<T> graterThenOrEqual(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.GREATER_THEN_OR_EQUAL, value);
    }

    public GenericSpecification<T> in(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.VALUE_IN, value);
    }

    public GenericSpecification<T> notIn(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.NOT_VALUE_IN, value);
    }

    public GenericSpecification<T> inValue(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.IN_VALUE, value);
    }

    public GenericSpecification<T> notInValue(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.NOT_IN_VALUE, value);
    }

    public GenericSpecification<T> startsWith(final String property, final String value) throws IOException {
        return filter(property, GenericSearchFilter.STARTS_WITH_VALUE, value);
    }

    public GenericSpecification<T> startsWith(final String property, final String value, boolean ignoreCase) throws IOException {
        if(ignoreCase) {
            return filter(property, GenericSearchFilter.STARTS_WITH_CASE_INSENSITIVE_VALUE, value);
        }
        else {
            return filter(property, GenericSearchFilter.STARTS_WITH_VALUE, value);
        }
    }

    public GenericSpecification<T> doesntStartsWith(final String property, final String value) throws IOException {
        return filter(property, GenericSearchFilter.NOT_STARTS_WITH_VALUE, value);
    }

    public GenericSpecification<T> doesntStartsWith(final String property, final String value, boolean ignoreCase) throws IOException {
        if(ignoreCase) {
            return filter(property, GenericSearchFilter.NOT_STARTS_WITH_CASE_INSENSITIVE_VALUE, value);
        }
        else {
            return filter(property, GenericSearchFilter.NOT_STARTS_WITH_VALUE, value);
        }
    }

    public GenericSpecification<T> valueStartsWith(final String property, final String value) throws IOException {
        return filter(property, GenericSearchFilter.VALUE_STARTS_WITH, value);
    }

    public GenericSpecification<T> valueStartsWith(final String property, final String value, boolean ignoreCase) throws IOException {
        if(ignoreCase) {
            return filter(property, GenericSearchFilter.VALUE_STARTS_WITH_CASE_INSENSITIVE, value);
        }
        else {
            return filter(property, GenericSearchFilter.VALUE_STARTS_WITH, value);
        }
    }

    public GenericSpecification<T> valueDoesntStartsWith(final String property, final String value) throws IOException {
        return filter(property, GenericSearchFilter.NOT_VALUE_STARTS_WITH, value);
    }

    public GenericSpecification<T> valueDoesntStartsWith(final String property, final String value, boolean ignoreCase) throws IOException {
        if(ignoreCase) {
            return filter(property, GenericSearchFilter.NOT_VALUE_STARTS_WITH_CASE_INSENSITIVE, value);
        }
        else {
            return filter(property, GenericSearchFilter.NOT_VALUE_STARTS_WITH, value);
        }
    }
}
