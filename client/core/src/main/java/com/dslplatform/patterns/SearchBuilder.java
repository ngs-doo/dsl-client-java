package com.dslplatform.patterns;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class SearchBuilder<T extends Searchable> {

    private final SearchableRepository<T> repository;
    private Specification<T> specification;
    private Integer limit;
    private Integer offset;
    private final ArrayList<Map.Entry<String, Boolean>> order = new ArrayList<Map.Entry<String, Boolean>>();

    public SearchBuilder(final SearchableRepository<T> repository) {
        this.repository = repository;
    }

    public SearchBuilder<T> with(final Specification<T> specification) {
        this.specification = specification;
        return this;
    }

    public SearchBuilder<T> limit(final int limit) { return take(limit); }
    public SearchBuilder<T> take(final int limit) {
        this.limit = Integer.valueOf(limit);
        return this;
    }

    public SearchBuilder<T> offset(final int offset) { return skip(offset); }
    public SearchBuilder<T> skip(final int offset) {
        this.offset = Integer.valueOf(offset);
        return this;
    }

    private SearchBuilder<T> orderBy(String property, boolean direction) {
        if (property == null || property == "")
            throw new IllegalArgumentException("property can't be empty");
        Map.Entry<String, Boolean> pair =
          new AbstractMap.SimpleEntry<String, Boolean>(
            property, Boolean.valueOf(direction));
        order.add(pair);
        return this;
    }

    public SearchBuilder<T> ascending(String property) { return orderBy(property, true); }
    public SearchBuilder<T> descending(String property) { return orderBy(property, false); }

    public Future<List<T>> search() {
        return specification == null
            ? repository.findAll(limit, offset, order)
            : repository.search(specification, limit, offset, order);
    }
}
