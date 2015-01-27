package com.dslplatform.patterns;

/**
 * Domain event represents an meaningful business event that occurred in the system.
 * It is a message that back-end system knows how to process and that will
 * change the state of the system.
 * <p>
 * They are the preferred way of manipulating data instead of simple CUD
 * operations (create, update, delete).
 * Unlike the {@link AggregateDomainEvent aggregate domain event} which is tied
 * to a change in a single {@link AggregateRoot aggregate root}, domain event
 * should be used when an action will result in modifications to multiple
 * aggregates, an external call (like sending an email) or some other action.
 * <p>
 * By default, events will be applied immediately.
 * If {@code async} is used, event will be stored immediately, but applied later.
 *
 * DomainEvent is defined in DSL with keyword {@code event}.
 *
 * <blockquote><pre>
 * module Todo {
 *   aggregate Task;
 *   event MarkDone {
 *     Task task;
 *   }
 * }
 * </pre></blockquote>
 */
public interface DomainEvent extends Identifiable {}
