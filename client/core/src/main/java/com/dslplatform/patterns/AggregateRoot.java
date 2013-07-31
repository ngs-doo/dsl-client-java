package com.dslplatform.patterns;

/**
 * Aggregate root is a meaningful object in the domain.
 * It can be viewed as a write boundary for entities and value objects
 * that will maintain write consistency.
 * <p>
 * Usually it represents a single table, but can span several tables 
 * and can be used like document or similar data structure. 
 * Since every aggregate is also an entity, it has a unique
 * identification represented by its URI.
 * <p>
 * DSL example:
 * <blockquote><pre>
 * module Todo {
 *   aggregate Task {
 *     timestamp startedAt;
 *     timestamp? finishedAt;
 *     int? priority;
 *     List<Note> notes;
 *   }
 *   value Note {
 *     date entered;
 *     string remark;
 *   }
 * }
 * </pre></blockquote>
 *
 */
public interface AggregateRoot extends Identifiable {}
