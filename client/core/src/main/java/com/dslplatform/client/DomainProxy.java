package com.dslplatform.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.dslplatform.patterns.AggregateDomainEvent;
import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.DomainEvent;
import com.dslplatform.patterns.Identifiable;
import com.dslplatform.patterns.Searchable;
import com.dslplatform.patterns.Specification;

/**
 * Proxy service to remote REST-like API for basic domain operations
 * such as searching, counting and event sourcing.
 * <p>
 * It is preferred to use domain patterns instead of this proxy service.
 */
public interface DomainProxy {
    /**
     * Returns a list of domain objects uniquely represented with their URIs.
     * Only found objects will be returned (list will be empty if no objects are found).
     *
     * @param manifest domain object class
     * @param uris     array of unique identifiers
     * @return         future to found domain objects
     */
    public <T extends Identifiable> Future<List<T>> find(
            final Class<T> manifest,
            final String[] uris);

    /**
     * Returns a list of domain objects uniquely represented with their URIs.
     * Only found objects will be returned (list will be empty if no objects are found).
     *
     * @param manifest domain object class
     * @param uris     sequence of unique identifiers
     * @return         future to found domain objects
     */
    public <T extends Identifiable> Future<List<T>> find(
            final Class<T> manifest,
            final Iterable<String> uris);

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
    public <T extends Searchable> Future<List<T>> search(
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
    public <T extends Searchable> Future<List<T>> search(
            final Specification<T> specification,
            final Integer limit,
            final Integer offset);

    /**
     * Returns a list of domain objects satisfying {@link Specification specification}
     *
     * @param specification search predicate
     * @return              future to domain objects which satisfy search predicate
     */
    public <T extends Searchable> Future<List<T>> search(
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
     * @param manifest domain object class
     * @param limit    maximum number of results
     * @param offset   number of results to be skipped
     * @param order    custom ordering
     * @return         future to found domain objects
     */
    public <T extends Searchable> Future<List<T>> findAll(
            final Class<T> manifest,
            final Integer limit,
            final Integer offset,
            final Iterable<Map.Entry<String, Boolean>> order);

    /**
     * Returns a list of all domain objects
     * with up to <code>limit</code> results.
     * <code>offset</code> can be used to skip initial results.
     *
     * @param manifest domain object class
     * @param limit    maximum number of results
     * @param offset   number of results to be skipped
     * @return         future to found domain objects
     */
    public <T extends Searchable> Future<List<T>> findAll(
            final Class<T> manifest,
            final Integer limit,
            final Integer offset);

    /**
     * Returns a list of all domain objects
     *
     * @param manifest domain object class
     * @return         future to found domain objects
     */
    public <T extends Searchable> Future<List<T>> findAll(
            final Class<T> manifest);

    /**
     * Returns a total number of domain objects.
     *
     * @param manifest domain object class
     * @return         future to number of domain objects
     */
    public <T extends Searchable> Future<Long> count(
            final Class<T> manifest);

    /**
     * Returns a number of elements satisfying provided specification.
     *
     * @param specification search predicate
     * @return              future to number of domain objects which satisfy specification
     */
    public <T extends Searchable> Future<Long> count(
            final Specification<T> specification);

    /**
     * Send domain event to the server. Server will return identity under which it was stored.
     * Events can't be modified once they are submitted. Only new events can be created.
     *
     * @param domainEvent event to raise
     * @return            future containing string value of event URI
     */
    public <TEvent extends DomainEvent> Future<String> submit(
            final TEvent domainEvent);

    /**
     * Apply domain event to a single aggregate. Server will return modified aggregate root.
     * Events can't be modified once they are submitted. Only new events can be created.
     *
     * @param domainEvent event to apply
     * @param uri         aggregate root uri
     * @return            future containing modified aggregate root
     */
    public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
            final TEvent domainEvent,
            final String uri);
}
