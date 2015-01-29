package com.dslplatform.patterns;

/**
 * {@link DomainEvent Domain event} which should be used when there is an action
 * to be applied on a single {@link AggregateRoot aggregate root}.
 * <p>
 * When a {@link DomainEvent domain event} affects only a single aggregate,
 * a specialized aggregate domain event can be used.
 * This event can't have side effects outside the aggregate, which allows it to
 * be replayed when it's asynchronous.
 * This is useful in write-intensive scenarios to minimize write load in the
 * database, but will increase read load, because reading an aggregate will have
 * to read all its unapplied events and apply them during reconstruction.
 * <p>
 * AggregateDomainEvent is defined in DSL with keyword {@code event}.
 * <blockquote><pre>
 * module Todo {
 *   aggregate Task;
 *   event&lt;Task&gt; MarkDone;
 * }
 * </pre></blockquote>
 * @param <T> aggregate root type
 */
public interface AggregateDomainEvent<T extends AggregateRoot> extends Identifiable {}
