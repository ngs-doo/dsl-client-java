package com.dslplatform.patterns;

/**
 * {@link DomainEvent} which should be used when there is an action
 * to be applied on a single aggregate root.
 * <p>
 * When {@link DomainEvent} affects only a single aggregate, then we can use
 * specialized aggregate domain event. This event can't have side effects outside
 * aggregate, which allows it to be replayed when it's asynchronous.
 * This is useful in write intensive scenarios to minimize write load in the database,
 * but will increase read load, because reading aggregate will have to read all it's 
 * unapplied events and apply them during reconstruction.
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
