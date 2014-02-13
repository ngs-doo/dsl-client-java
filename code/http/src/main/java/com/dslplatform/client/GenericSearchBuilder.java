package com.dslplatform.client;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.dslplatform.patterns.Searchable;
import com.dslplatform.patterns.ServiceLocator;

/**
 * In case when specification is not defined on the server,
 * client side generic search builder can be used.
 * It should be used for testing and in rare cases when server can't be updated.
 * <p>
 * It is preferable to use server side specification.
 *
 * @param <T> type of domain object
 */
public class GenericSearchBuilder<T extends Searchable> {

    private final HashMap<String, ArrayList<FilterPair>> filters = new HashMap<String, ArrayList<FilterPair>>();

    private final Class<T> manifest;
    private final String domainName;
    private final HttpClient httpClient;

    static class FilterPair
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

    /**
     * Create new instance of generic search builder by providing
     * domain object type and locator
     *
     * @param manifest domain object type
     * @param locator  service locator with registered services
     */
    public GenericSearchBuilder(
            final Class<T> manifest,
            final ServiceLocator locator) {
        this.manifest = manifest;
        this.httpClient = locator.resolve(HttpClient.class);
        this.domainName = httpClient.getDslName(manifest);
    }

    /**
     * Limit the number of results which will be performed.
     *
     * @param limit maximum number of results
     * @return      itself
     */
    public GenericSearchBuilder<T> take(final int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Limit the number of results which will be performed.
     *
     * @param limit maximum number of results
     * @return      itself
     */
    public GenericSearchBuilder<T> limit(final int limit) { return take(limit); }

    /**
     * Skip initial number of results.
     *
     * @param offset number of skipped results
     * @return       itself
     */
    public GenericSearchBuilder<T> skip(final int offset) {
        this.offset = offset;
        return this;
    }
    /**
     * Skip initial number of results.
     *
     * @param offset number of skipped results
     * @return       itself
     */
    public GenericSearchBuilder<T> offset(final int offset) { return skip(offset); }

    /**
     * Ask server to provide domain objects which satisfy defined conditions
     * in requested order if custom order was provided.
     * Limit and offset will be applied on results if provided.
     *
     * @return future to list of found domain object
     */
    public Future<List<T>> search() {

        final String url =
            Utils.appendLimitOffsetOrder(
                domainName,
                limit,
                offset,
                order,
                false);

        return
            httpClient.sendRequest(
                JsonSerialization.buildCollectionType(ArrayList.class, manifest),
                "Domain.svc/search-generic/" + url,
                "PUT",
                filters,
                new int[] { 200 });
    }

    private GenericSearchBuilder<T> orderBy(final String property, final Boolean direction) {
        if(property == null || property.isEmpty()) throw new IllegalArgumentException("property can't be null");
        this.order.add(new AbstractMap.SimpleEntry<String, Boolean>(property, direction));
        return this;
    }

    /**
     * Order results ascending by specified property.
     *
     * @param property name of property
     * @return         itself
     */
    public GenericSearchBuilder<T> ascending(final String property) { return orderBy(property, true); }

    /**
     * Order results descending by specified property.
     *
     * @param property name of property
     * @return         itself
     */
    public GenericSearchBuilder<T> descending(final String property) { return orderBy(property, false); }

    private GenericSearchBuilder<T> filter(final String property, final int id, final Object value) throws IOException {
        if(property == null || property.isEmpty()) throw new IllegalArgumentException("property can't be null");
        final String json = value != null ? JsonSerialization.serialize(value) : null;
        final ArrayList<FilterPair> pairs;
        if(!filters.containsKey(property)) {
            pairs = new ArrayList<FilterPair>();
            filters.put(property, pairs);
        }
        else pairs = filters.get(property);
        pairs.add(new FilterPair(id, json));
        return this;
    }

    /**
     * Define equal (=) condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of property to compare
     * @param value    check equality with provided value
     * @return         itself
     */
    public GenericSearchBuilder<T> equal(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.EQUALS, value);
    }

    /**
     * Define not equal (!=) condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of property to compare
     * @param value    check equality with provided value
     * @return         itself
     */
    public GenericSearchBuilder<T> nonEqual(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.NOT_EQUALS, value);
    }

    /**
     * Define less then (<) condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of property to compare
     * @param value    check ordering with provided value
     * @return         itself
     */
    public GenericSearchBuilder<T> lessThen(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.LESS_THEN, value);
    }

    /**
     * Define less then or equal (<=) condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of property to compare
     * @param value    check ordering and equality with provided value
     * @return         itself
     */
    public GenericSearchBuilder<T> lessThenOrEqual(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.LESS_THEN_OR_EQUAL, value);
    }

    /**
     * Define greater then (>) condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of property to compare
     * @param value    check ordering with provided value
     * @return         itself
     */
    public GenericSearchBuilder<T> greaterThen(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.GREATER_THEN, value);
    }

    /**
     * Define greater then or equal (>=) condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of property to compare
     * @param value    check ordering and equality with provided value
     * @return         itself
     */
    public GenericSearchBuilder<T> greaterThenOrEqual(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.GREATER_THEN_OR_EQUAL, value);
    }

    /**
     * Define in ( value in collection property ) condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of property to check
     * @param value    check collection for provided value
     * @return         itself
     */
    public GenericSearchBuilder<T> in(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.VALUE_IN, value);
    }

    /**
     * Define not in ( not value in collection property ) condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of property to check
     * @param value    check collection for provided value
     * @return         itself
     */
    public GenericSearchBuilder<T> notIn(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.NOT_VALUE_IN, value);
    }

    /**
     * Define in [ property in collection value ] condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of collection property to check
     * @param value    check if property is in provided collection value
     * @return         itself
     */
    public GenericSearchBuilder<T> inValue(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.IN_VALUE, value);
    }

    /**
     * Define in [ not property in collection value ] condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of collection property to check
     * @param value    check if property is not in provided collection value
     * @return         itself
     */
    public GenericSearchBuilder<T> notInValue(final String property, final Object value) throws IOException {
        return filter(property, GenericSearchFilter.NOT_IN_VALUE, value);
    }

    /**
     * Define startsWith [ property.startsWith(value) ] condition for specification.
     * Case sensitive comparison will be performed.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of property to check
     * @param value    comparison value
     * @return         itself
     */
    public GenericSearchBuilder<T> startsWith(final String property, final String value) throws IOException {
        return filter(property, GenericSearchFilter.STARTS_WITH_VALUE, value);
    }

    /**
     * Define startsWith and case sensitivity [ property.startsWith(value, case sensitivity) ] condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property   name of property to check
     * @param value      comparison value
     * @param ignoreCase should string comparison ignore casing
     * @return           itself
     */
    public GenericSearchBuilder<T> startsWith(final String property, final String value, boolean ignoreCase) throws IOException {
        if(ignoreCase) {
            return filter(property, GenericSearchFilter.STARTS_WITH_CASE_INSENSITIVE_VALUE, value);
        }
        else {
            return filter(property, GenericSearchFilter.STARTS_WITH_VALUE, value);
        }
    }

    /**
     * Define !startsWith [ not property.startsWith(value) ] condition for specification.
     * Case sensitive comparison will be performed.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of property to check
     * @param value    comparison value
     * @return         itself
     */
    public GenericSearchBuilder<T> doesntStartsWith(final String property, final String value) throws IOException {
        return filter(property, GenericSearchFilter.NOT_STARTS_WITH_VALUE, value);
    }

    /**
     * Define !startsWith and case sensitivity [ not property.startsWith(value, case sensitivity) ] condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property   name of property to check
     * @param value      comparison value
     * @param ignoreCase should string comparison ignore casing
     * @return           itself
     */
    public GenericSearchBuilder<T> doesntStartsWith(final String property, final String value, boolean ignoreCase) throws IOException {
        if(ignoreCase) {
            return filter(property, GenericSearchFilter.NOT_STARTS_WITH_CASE_INSENSITIVE_VALUE, value);
        }
        else {
            return filter(property, GenericSearchFilter.NOT_STARTS_WITH_VALUE, value);
        }
    }

    /**
     * Define startsWith [ value.startsWith(property) ] condition for specification.
     * Case sensitive comparison will be performed.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of property to check
     * @param value    comparison value
     * @return         itself
     */
    public GenericSearchBuilder<T> valueStartsWith(final String property, final String value) throws IOException {
        return filter(property, GenericSearchFilter.VALUE_STARTS_WITH, value);
    }

    /**
     * Define startsWith and case sensitivity [ value.startsWith(property, case sensitivity) ] condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property   name of property to check
     * @param value      comparison value
     * @param ignoreCase should string comparison ignore casing
     * @return           itself
     */
    public GenericSearchBuilder<T> valueStartsWith(final String property, final String value, boolean ignoreCase) throws IOException {
        if(ignoreCase) {
            return filter(property, GenericSearchFilter.VALUE_STARTS_WITH_CASE_INSENSITIVE, value);
        }
        else {
            return filter(property, GenericSearchFilter.VALUE_STARTS_WITH, value);
        }
    }

    /**
     * Define !startsWith [ not value.startsWith(property) ] condition for specification.
     * Case sensitive comparison will be performed.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property name of property to check
     * @param value    comparison value
     * @return         itself
     */
    public GenericSearchBuilder<T> valueDoesntStartsWith(final String property, final String value) throws IOException {
        return filter(property, GenericSearchFilter.NOT_VALUE_STARTS_WITH, value);
    }

    /**
     * Define !startsWith and case sensitivity [ not value.startsWith(property, case sensitivity) ] condition for specification.
     * Server will return only results that satisfy this and every other specified condition.
     *
     * @param property   name of property to check
     * @param value      comparison value
     * @param ignoreCase should string comparison ignore casing
     * @return           itself
     */
    public GenericSearchBuilder<T> valueDoesntStartsWith(final String property, final String value, boolean ignoreCase) throws IOException {
        if(ignoreCase) {
            return filter(property, GenericSearchFilter.NOT_VALUE_STARTS_WITH_CASE_INSENSITIVE, value);
        }
        else {
            return filter(property, GenericSearchFilter.NOT_VALUE_STARTS_WITH, value);
        }
    }
}
