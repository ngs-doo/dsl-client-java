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
	 * @param <T>       identifiable domain type
	 * @param manifest  domain object class
	 * @param uris      array of unique identifiers
	 * @return          future with found domain objects
	 */
	public <T extends Identifiable> Future<List<T>> find(
			Class<T> manifest,
			String[] uris);

	/**
	 * Returns a list of domain objects uniquely represented with their URIs.
	 * Only found objects will be returned (list will be empty if no objects are found).
	 *
	 * @param <T>       identifiable domain type
	 * @param manifest  domain object class
	 * @param uris      sequence of unique identifiers
	 * @return          future with found domain objects
	 */
	public <T extends Identifiable> Future<List<T>> find(
			Class<T> manifest,
			Iterable<String> uris);

	/**
	 * Returns a list of domain objects satisfying {@link Specification specification}
	 * with up to {@code limit} results.
	 * {@code offset} can be used to skip initial results.
	 * {@code order} should be given as a list of pairs of {@code <String, Boolean>}
	 * where first is a property name and second is whether the results should
	 * be sorted ascending over this property.
	 *
	 * @param <T>            searchable type
	 * @param specification  search predicate
	 * @param limit          maximum number of results
	 * @param offset         number of results to be skipped
	 * @param order          custom ordering
	 * @return               future with domain objects which satisfy the search predicate
	 */
	public <T extends Searchable> Future<List<T>> search(
			Specification<T> specification,
			Integer limit,
			Integer offset,
			Iterable<Map.Entry<String, Boolean>> order);

	/**
	 * Returns a list of domain objects satisfying
	 * {@link Specification specification} with up to {@code limit} results.
	 * {@code offset} can be used to skip initial results.
	 *
	 * @param <T>            searchable type
	 * @param specification  search predicate
	 * @param limit          maximum number of results
	 * @param offset         number of results to be skipped
	 * @return               future with domain objects which satisfy the search predicate
	 */
	public <T extends Searchable> Future<List<T>> search(
			Specification<T> specification,
			Integer limit,
			Integer offset);

	/**
	 * Returns a list of domain objects satisfying {@link Specification specification}
	 *
	 * @param <T>            searchable type
	 * @param specification  search predicate
	 * @return               future with domain objects which satisfy the search predicate
	 */
	public <T extends Searchable> Future<List<T>> search(
			Specification<T> specification);

	/**
	 * Returns a list of all domain objects with up to {@code limit} results.
	 * {@code offset} can be used to skip initial results.
	 * {@code order} should be given as a list of pairs of {@code <String, Boolean>}
	 * where first is a property name and second is whether the results should
	 * be sorted ascending over this property.
	 *
	 * @param <T>       searchable type
	 * @param manifest  domain object class
	 * @param limit     maximum number of results
	 * @param offset    number of results to be skipped
	 * @param order     custom ordering
	 * @return          future with found domain objects
	 */
	public <T extends Searchable> Future<List<T>> search(
			Class<T> manifest,
			Integer limit,
			Integer offset,
			Iterable<Map.Entry<String, Boolean>> order);

	/**
	 * Returns a list of all domain objects with up to {@code limit} results.
	 * {@code offset} can be used to skip initial results.
	 *
	 * @param <T>       searchable type
	 * @param manifest  domain object class
	 * @param limit     maximum number of results
	 * @param offset    number of results to be skipped
	 * @return          future with found domain objects
	 */
	public <T extends Searchable> Future<List<T>> search(
			Class<T> manifest,
			Integer limit,
			Integer offset);

	/**
	 * Returns a list of all domain objects
	 *
	 * @param <T>       searchable type
	 * @param manifest  domain object class
	 * @return          future with found domain objects
	 */
	public <T extends Searchable> Future<List<T>> search(
			Class<T> manifest);

	/**
	 * Returns a total number of domain objects.
	 *
	 * @param <T>       searchable type
	 * @param manifest  domain object class
	 * @return          future with the number of domain objects
	 */
	public <T extends Searchable> Future<Long> count(
			Class<T> manifest);

	/**
	 * Returns a number of elements satisfying provided specification.
	 *
	 * @param <T>            searchable type
	 * @param specification  search predicate
	 * @return               future with the number of domain objects which
	 *                       satisfy the specification
	 */
	public <T extends Searchable> Future<Long> count(
			Specification<T> specification);

	/**
	 * Sends a domain event to the server. Server will return the identity under
	 * which it was stored. Events can't be modified once they are submitted,
	 * only new events can be created.
	 *
	 * @param <TEvent>     domain event type
	 * @param domainEvent  event to raise
	 * @return             future with a string value of the event URI
	 */
	public <TEvent extends DomainEvent> Future<String> submit(
			TEvent domainEvent);

	/**
	 * Apply domain event to a single aggregate. Server will return the modified
	 * aggregate root. Events can't be modified once they are submitted, only
	 * new events can bec reated.
	 *
	 * @param <TAggregate> aggregate root type
	 * @param <TEvent>     aggregate domain event type
	 * @param domainEvent  event to apply
	 * @param uri          aggregate root uri
	 * @return             future with the modified aggregate root
	 */
	public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
			TEvent domainEvent,
			String uri);
}
