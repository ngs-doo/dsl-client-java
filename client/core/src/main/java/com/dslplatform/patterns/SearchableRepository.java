package com.dslplatform.patterns;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * API for searching and counting using {@link Specification Specification}
 *
 * @param <T> domain object type.
 */
public interface SearchableRepository<T extends Searchable> {

    /**
     * Returns a list of domain objects which satisfies {@link Specification specification}
     * with up to <code>limit</code> results.
     * <code>offset</code> can be used to skip initial results.
     *
     * <code>order</code> should be given as a list of pairs of
     * <code>{@literal <String, Boolean>}</code>
     * where first is a property name and second is whether it should be sorted
     * ascending over this property.
     *
     * @param specification  search predicate
     * @param limit  maximum number of results
     * @param offset  number of results to be skipped
     * @param order  custom ordering
     * @return  future value of the resulting sequence
     */
    public Future<List<T>> search(
            final Specification<T> specification,
            final Integer limit,
            final Integer offset,
            final Iterable<Map.Entry<String, Boolean>> order);

    /**
     * Returns a list of domain objects which satisfies {@link Specification specification}
     * with up to <code>limit</code> results.
     * <code>offset</code> can be used to skip initial results.
     *
     * @param specification  search predicate
     * @param limit  maximum number of results
     * @param offset  number of results to be skipped
     * @return  future value of the resulting sequence
     */
    public Future<List<T>> search(
            final Specification<T> specification,
            final Integer limit,
            final Integer offset);

    /**
     * Returns a list of domain objects which satisfies {@link Specification specification}
     * with up to <code>limit</code> results.
     * <code>offset</code> can be used to skip initial results.
     *
     * @param specification  search predicate
     * @return  future value of the resulting sequence
     */
    public Future<List<T>> search(
            final Specification<T> specification);

    /**
     * Returns a list of all domain objects
     * with up to <code>limit</code> results.
     * <code>offset</code> can be used to skip initial results.
     *
     * <code>order</code> should be given as a list of pairs of
     * <code>{@literal <String, Boolean>}</code>
     * where first is a property name and second is whether it should be sorted
     * ascending over this property.
     *
     * @param limit  maximum number of results
     * @param offset  number of results to be skipped
     * @param order  custom ordering
     * @return  future value of the resulting sequence
     */
    public Future<List<T>> findAll(//TODO: example
            final Integer limit,
            final Integer offset,
            final Iterable<Map.Entry<String, Boolean>> order);

    /**
     * Returns a list of all domain objects
     * with up to <code>limit</code> results.
     * <code>offset</code> can be used to skip initial results.
     *
     *
     * @param limit  maximum number of results
     * @param offset  number of results to be skipped
     * @return future value of the resulting sequence
     */
    public Future<List<T>> findAll(
            final Integer limit,
            final Integer offset);

    /**
     * Returns a list of all domain objects
     *
     * @return future value of the resulting sequence.
     */
    public Future<List<T>> findAll();

    /**
     * Returns a number of elements confirming with this specification.
     *
     * @param specification  search predicate
     * @return future value with number of elements
     */
    public Future<Long> count(
            final Specification<T> specification);

    /**
     * Returns a total number of domain objects.
     *
     * @return future value with number of elements
     */
    public Future<Long> countAll();

    /**
     * Returns an instance of {@link SearchBuilder} for this repository.
     *
     * @return utility class for building a search.
     */
    public SearchBuilder<T> builder();
}
