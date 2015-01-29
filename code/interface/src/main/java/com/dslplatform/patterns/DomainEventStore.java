package com.dslplatform.patterns;

import java.util.concurrent.Future;

/**
 * Service for submitting domain events to the application server.
 * <p>
 * It should be used when {@code Future} is the preferred way of interacting
 * with the remote server.
 */
public interface DomainEventStore {
	/**
	 * Sends a domain event to the server. Server will return an identity under
	 * which it was stored. Events can't be modified once they are submitted,
	 * only new events can be created.
	 *
	 * @param <T>    domain event type
	 * @param event  event to raise
	 * @return       future with a string value of the event URI
	 */
	public <T extends DomainEvent> Future<String> submit(T event);

	/**
	 * Applies a domain event to a single aggregate. Server will return the
	 * modified aggregate root. Events can't be modified once they are submitted, only new events can be created.
	 *
	 * @param <TAggregate>  aggregate root type
	 * @param <TEvent>      aggregate domain event type
	 * @param event         event to apply
	 * @param uri           aggregate root uri
	 * @return              future with the modified aggregate root
	 */
	public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
			TEvent event,
			String uri);

	/**
	 * Helper method for sending domain event to the server. Server will return modified aggregate root.
	 * Events can't be modified once they are submitted. Only new events can be created.
	 *
	 * @param <TAggregate>  aggregate root type
	 * @param <TEvent>      aggregate domain event type
	 * @param event         event to apply
	 * @param aggregate     aggregate root instance
	 * @return              future containing modified aggregate root
	 */
	public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
			TEvent event,
			TAggregate aggregate);
}
