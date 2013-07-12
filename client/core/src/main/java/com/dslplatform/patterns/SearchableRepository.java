package com.dslplatform.patterns;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface SearchableRepository<T extends Searchable> {
    public Future<List<T>> search(
            final Specification<T> specification,
            final Integer limit,
            final Integer offset,
            final Iterable<Map.Entry<String, Boolean>> order);

    public Future<List<T>> search(
            final Specification<T> specification,
            final Integer limit,
            final Integer offset);

    public Future<List<T>> search(
            final Specification<T> specification);

    public Future<List<T>> findAll(
            final Integer limit,
            final Integer offset,
            final Iterable<Map.Entry<String, Boolean>> order);

    public Future<List<T>> findAll(
            final Integer limit,
            final Integer offset);

    public Future<List<T>> findAll();

    public Future<Long> count(
            final Specification<T> specification);

    public Future<Long> countAll();

    public SearchBuilder<T> builder();
}
