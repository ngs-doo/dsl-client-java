package com.dslplatform.patterns;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Utility class for building a search over a {@link SearchableRepository searchable repository}.
 * Search can be performed using more fluent API,
 * by providing specification limit, offset and custom order
 *
 * @param <T> domain object type.
 */
public class SearchBuilder<T extends Searchable> {

    private final SearchableRepository<T> repository;
    private Specification<T> specification;
    private Integer limit;
    private Integer offset;
    private final List<Map.Entry<String, Boolean>> order;

    /**
     * Constructor for SearchBuilder which requires a repository to perform
     * a search.
     *
     * @param repository domain object repository
     */
    public SearchBuilder(
            final SearchableRepository<T> repository) {
        this.repository = repository;
        order = new ArrayList<Map.Entry<String, Boolean>>();
    }

    /**
     * Provide {@link Specification search predicate} for filtering results.
     *
     * @param specification search predicate
     * @return              itself
     */
    public SearchBuilder<T> with(final Specification<T> specification) {
        this.specification = specification;
        return this;
    }

    /**
     * Provide {@link Specification search predicate} for filtering results.
     *
     * @param specification search predicate
     * @return              itself
     */
    public SearchBuilder<T> filter(final Specification<T> specification) {
        this.specification = specification;
        return this;
    }

    /**
     * Define a maximum number of results
     *
     * @param limit maximum number of results
     * @return      itself
     */
    public SearchBuilder<T> limit(final int limit) {
        return take(limit);
    }

    /**
     * Define a maximum number of results.
     *
     * @param limit maximum number of results
     * @return      itself
     */
    public SearchBuilder<T> take(final int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Define a number of results to be skipped.
     *
     * @param offset number of results to be skipped
     * @return       itself
     */
    public SearchBuilder<T> offset(final int offset) {
        return skip(offset);
    }

    /**
     * Define a number of results to be skipped.
     *
     * @param offset number of results to be skipped
     * @return       itself
     */
    public SearchBuilder<T> skip(final int offset) {
        this.offset = offset;
        return this;
    }

    private SearchBuilder<T> orderBy(
            final String property,
            final boolean ascending) {
        if (property == null || property.isEmpty()) {
            throw new IllegalArgumentException("property can't be empty");
        }
        order.add(new AbstractMap.SimpleEntry<String, Boolean>(
                property, ascending));
        return this;
    }

    /**
     * Order result ascending using a provided property
     *
     * @param property name of domain objects property
     * @return         itself
     */
    public SearchBuilder<T> ascending(final String property) {
        return orderBy(property, true);
    }

    /**
     * Order result descending using a provided property
     *
     * @param property name of domain objects property
     * @return         itself
     */
    public SearchBuilder<T> descending(final String property) {
        return orderBy(property, false);
    }

    /**
     * Returns a list of domain objects which satisfy
     * {@link Specification specification} if it was set, otherwise all of them.
     * Parameters can be previously set to <code>limit</code> results,
     * skip <code>offset</code> of initial results and <code>order</code>
     * by some of this domain objects properties.
     *
     * @return  future value of the resulting sequence
     */
    public Future<List<T>> search() {
        return specification == null
                ? repository.findAll(limit, offset, order)
                : repository.search(specification, limit, offset, order);
    }
}
