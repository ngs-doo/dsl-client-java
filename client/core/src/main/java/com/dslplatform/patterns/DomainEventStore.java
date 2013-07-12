package com.dslplatform.patterns;

import java.util.concurrent.Future;

public interface DomainEventStore {
    public <T extends DomainEvent> Future<String> submit(final T event);
    public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
        final TEvent event, final String uri);
    public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
        final TEvent event, final TAggregate aggregate);
}
