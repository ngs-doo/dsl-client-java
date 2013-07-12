package com.dslplatform.client;

import java.util.concurrent.Future;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.Identifiable;

public interface CrudProxy {
    public <T extends Identifiable> Future<T> read(
            final Class<T> manifest,
            final String uri);

    public <TAggregate extends AggregateRoot> Future<TAggregate> create(
            final TAggregate aggregate);

    public <TAggregate extends AggregateRoot> Future<TAggregate> update(
            final TAggregate aggregate);

    public <TAggregate extends AggregateRoot> Future<TAggregate> delete(
            final Class<TAggregate> manifest,
            final String uri);
}
