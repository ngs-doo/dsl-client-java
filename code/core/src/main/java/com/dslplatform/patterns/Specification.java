package com.dslplatform.patterns;

/**
 * Search predicate which can be used to filter domain objects from the remote
 * server using {@link SearchableRepository searchable repository}.
 * <p>
 * Specification is defined in DSL with keyword {@code specification}
 * and a predicate.
 * Server can convert specification to SQL query on the fly or call
 * database function created at compile time. Other optimization techniques
 * can be used too.
 * <p>
 * DSL example:
 * <blockquote><pre>
 * module Todo {
 *   aggregate Task {
 *       Timestamp createdOn;
 *       specification findBetween
 *           'it =&gt; it.createdOn &gt;= after &amp;&amp; it.createdOn &lt;= before' {
 *         Date after;
 *         Date before;
 *       }
 * }
 * </pre></blockquote>
 *
 * @param <T> domain object on which search will be performed.
 */
public interface Specification<T extends Searchable> {}
