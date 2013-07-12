package com.dslplatform.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.Searchable;
import com.dslplatform.patterns.Specification;

public interface StandardProxy {
    public <TAggregate extends AggregateRoot> Future<List<String>> persist(
            final Iterable<TAggregate> inserts,
            final Iterable<Map.Entry<TAggregate, TAggregate>> updates,
            final Iterable<TAggregate> deletes);

    public <TDomainObject extends Searchable, TResult> Future<List<TResult>> olapCube(
            final Class<TResult> clazz,
            final String cubeName,
            final Specification<TDomainObject> specification,
            final Iterable<String> dimensions,
            final Iterable<String> facts,
            final Iterable<Map.Entry<String, Boolean>> order);

    public <TResult> Future<List<TResult>> olapCube(
            final Class<TResult> clazz,
            final String cubeName,
            final Iterable<String> dimensions,
            final Iterable<String> facts,
            final Iterable<Map.Entry<String, Boolean>> order);

    public <TArgument, TResult> Future<TResult> execute(
            final Class<TResult> clazz,
            final String command,
            final TArgument argument);
}
