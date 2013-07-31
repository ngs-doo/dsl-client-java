package com.dslplatform.patterns;

import java.util.concurrent.Future;

/**
 * API for submitting events to remote server.
 */
public interface DomainEventStore {

  /**
   * Send domain event to server. By default event will be applied immediately.
   * If {@code async} is {@code enabled} event will be stored immediately but applied later.
   * @param event event to apply
   * @return future containg string value of events uri
   */
    public <T extends DomainEvent> Future<String> submit(final T event);

    public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
        final TEvent event, final String uri);
    public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
        final TEvent event, final TAggregate aggregate);
}
