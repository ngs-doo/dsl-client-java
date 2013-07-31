package com.dslplatform.patterns;

/**
 * Domain event represents an event that occurred in the system.
 * This is a message that your system knows how to process and that will
 * change the state of your system.
 * They are preferred way of manipulating data in place of simple
 * CRUD operations ( insert, update, delete).
 * Unlike {@link AggregateDomainEvent} which is tied to a change in a single
 * {@link AggregateRoot}, DomainEvent should be used when an action will result
 * in modifications to multiple aggregates or some other action.
 *
 * DomainEvent is defined in DSL with key word {@code event}.
 *
 * Lets say we have task list, and want to expose functionality of marking a task
 * complete.
 *
 * <p><blockquote><pre>
 * module Todo {
 *   root Task
 *   {
 *     string name;
 *     int proprity;
 *     bool isDone;
 *     timestamp created;
 *   }
 *
 *   event MarkDone
 *   {
 *     string taskURI;
 *   }
 * }
 *
 */
public interface DomainEvent extends Identifiable {}
