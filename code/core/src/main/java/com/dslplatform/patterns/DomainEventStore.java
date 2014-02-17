package com.dslplatform.patterns;

import java.util.concurrent.Future;

/**
 * Service for submitting domain events to the application server.
 * <p>
 * It should be used when Future is a preferred way of interacting with the remote server.
 */
public interface DomainEventStore {

    /**
     * Send domain event to the server. Server will return identity under which it was stored.
     * Events can't be modified once they are submitted. Only new events can be created.
     *
     * @param event event to raise
     * @return      future containing string value of event URI
     */
    public <T extends DomainEvent> Future<String> submit(final T event);

    /**
     * Apply domain event to a single aggregate. Server will return modified aggregate root.
     * Events can't be modified once they are submitted. Only new events can be created.
     *
     * @param event event to apply
     * @param uri   aggregate root uri
     * @return      future containing modified aggregate root
     */
    public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
            final TEvent event,
            final String uri);

    /**
     * Helper method for sending domain event to the server. Server will return modified aggregate root.
     * Events can't be modified once they are submitted. Only new events can be created.
     *
     * @param event     event to apply
     * @param aggregate aggregate root instance
     * @return          future containing modified aggregate root
     */
    public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
            final TEvent event,
            final TAggregate aggregate);
}
