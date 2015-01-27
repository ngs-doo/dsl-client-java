package com.dslplatform.patterns;

/**
 * Aggregate root is a meaningful object in the domain.
 * It can be viewed as a write boundary for entities and value objects
 * that will maintain write consistency.
 * <p>
 * Usually it represents a single table, but can span several tables
 * and can be used like a document or a similar data structure.
 * Since every aggregate is also an entity, it has a unique identification
 * represented by its URI.
 * <p>
 * DSL example:
 * <blockquote><pre>
 * module Todo {
 *   aggregate Task {
 *     Timestamp startedAt;
 *     Timestamp? finishedAt;
 *     Int? priority;
 *     List&lt;Note&gt; notes;
 *   }
 *   value Note {
 *     Date entered;
 *     String remark;
 *   }
 * }
 * </pre></blockquote>
 */
public interface AggregateRoot extends Identifiable {}
