package com.dslplatform.patterns;

/**
 * {@link DomainEvent} which should be used when there is an action
 * to be applied on a single aggregate root.
 */
public interface AggregateDomainEvent<T extends AggregateRoot> extends Identifiable {}
