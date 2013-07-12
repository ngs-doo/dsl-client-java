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

public interface DomainProxy {
    public <T extends Identifiable> Future<List<T>> find(
            final Class<T> manifest,
            final String[] uris);

    public <T extends Identifiable> Future<List<T>> find(
            final Class<T> manifest,
            final Iterable<String> uris);

    public <T extends Searchable> Future<List<T>> search(
            final Specification<T> specification,
            final Integer limit,
            final Integer offset,
            final Iterable<Map.Entry<String, Boolean>> order);

    public <T extends Searchable> Future<List<T>> search(
            final Specification<T> specification,
            final Integer limit,
            final Integer offset);

    public <T extends Searchable> Future<List<T>> search(
            final Specification<T> specification);

    public <T extends Searchable> Future<List<T>> findAll(
            final Class<T> manifest,
            final Integer limit,
            final Integer offset,
            final Iterable<Map.Entry<String, Boolean>> order);

    public <T extends Searchable> Future<List<T>> findAll(
            final Class<T> manifest,
            final Integer limit,
            final Integer offset);

    public <T extends Searchable> Future<List<T>> findAll(
            final Class<T> manifest);

    public <T extends Searchable> Future<Long> count(
            final Class<T> manifest);

    public <T extends Searchable> Future<Long> count(
            final Specification<T> specification);

    public <TEvent extends DomainEvent> Future<String> submit(
            final TEvent domainEvent);

    public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
            final TEvent domainEvent,
            final String uri);
}
