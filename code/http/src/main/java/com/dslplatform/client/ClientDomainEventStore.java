package com.dslplatform.client;

import java.util.concurrent.Future;

import com.dslplatform.patterns.AggregateDomainEvent;
import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.DomainEvent;
import com.dslplatform.patterns.DomainEventStore;

class ClientDomainEventStore implements DomainEventStore {
	protected final DomainProxy domainProxy;

	public ClientDomainEventStore(final DomainProxy domainProxy) {
		this.domainProxy = domainProxy;
	}

	@Override
	public <TEvent extends DomainEvent> Future<String> submit(final TEvent event) {
		return domainProxy.submit(event);
	}

	@Override
	public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
			final TEvent event,
			final String uri) {
		return domainProxy.submit(event, uri);
	}

	@Override
	public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
			final TEvent event,
			final TAggregate aggregate) {
		return submit(event, aggregate.getURI());
	}
}
