package com.dslplatform.patterns;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Service for searching and counting domain objects.
 * Search can be performed using {@link Specification specification},
 * paged using limit and offset arguments.
 * Custom sort can be provided using list of property->direction pairs.
 * <p>
 * Specification can be declared in DSL or custom search can be built on client
 * and sent to server.
 * <p>
 * When permissions are applied, server can restrict which results will be returned to the client.
 * Service should be used when Future is a preferred way of interacting with the remote server.
 *
 * @param <T> domain object type.
 */
public interface SearchableRepository<T extends Searchable> {

    /**
     * Returns a list of domain objects satisfying {@link Specification specification}
     * with up to <code>limit</code> results.
     * <code>offset</code> can be used to skip initial results.
     * <code>order</code> should be given as a list of pairs of
     * <code>{@literal <String, Boolean>}</code>
     * where first is a property name and second is whether it should be sorted
     * ascending over this property.
     *
     * @param specification search predicate
     * @param limit         maximum number of results
     * @param offset        number of results to be skipped
     * @param order         custom ordering
     * @return              future to domain objects which satisfy search predicate
     */
    public Future<List<T>> search(
            final Specification<T> specification,
            final Integer limit,
            final Integer offset,
            final Iterable<Map.Entry<String, Boolean>> order);

    /**
     * Returns a list of domain objects satisfying {@link Specification specification}
     * with up to <code>limit</code> results.
     * <code>offset</code> can be used to skip initial results.
     *
     * @param specification search predicate
     * @param limit         maximum number of results
     * @param offset        number of results to be skipped
     * @return              future to domain objects which satisfy search predicate
     */
    public Future<List<T>> search(
            final Specification<T> specification,
            final Integer limit,
            final Integer offset);

    /**
     * Returns a list of domain objects satisfying {@link Specification specification}
     *
     * @param specification search predicate
     * @return              future to domain objects which satisfy search predicate
     */
    public Future<List<T>> search(
            final Specification<T> specification);

    /**
     * Returns a list of all domain objects
     * with up to <code>limit</code> results.
     * <code>offset</code> can be used to skip initial results.
     * <code>order</code> should be given as a list of pairs of
     * <code>{@literal <String, Boolean>}</code>
     * where first is a property name and second is whether it should be sorted
     * ascending over this property.
     *
     * @param limit  maximum number of results
     * @param offset number of results to be skipped
     * @param order  custom ordering
     * @return       future to found domain objects
     */
    public Future<List<T>> findAll(
            final Integer limit,
            final Integer offset,
            final Iterable<Map.Entry<String, Boolean>> order);

    /**
     * Returns a list of all domain objects
     * with up to <code>limit</code> results.
     * <code>offset</code> can be used to skip initial results.
     *
     * @param limit  maximum number of results
     * @param offset number of results to be skipped
     * @return       future to found domain objects
     */
    public Future<List<T>> findAll(
            final Integer limit,
            final Integer offset);

    /**
     * Returns a list of all domain objects
     *
     * @return future found domain objects
     */
    public Future<List<T>> findAll();

    /**
     * Returns a number of elements satisfying provided specification.
     *
     * @param specification search predicate
     * @return              future to number of domain objects which satisfy specification
     */
    public Future<Long> count(
            final Specification<T> specification);

    /**
     * Returns a total number of domain objects.
     *
     * @return future to number of domain objects
     */
    public Future<Long> countAll();

    /**
     * Returns an instance of {@link SearchBuilder search builder} for this repository.
     * Search builder is helper class with fluent API for building search.
     *
     * @return utility class for building a search.
     */
    public SearchBuilder<T> builder();
}
