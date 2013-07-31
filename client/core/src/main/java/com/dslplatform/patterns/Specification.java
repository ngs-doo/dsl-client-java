package com.dslplatform.patterns;

/**
 * Search predicate which can be used to filter domain objects using
 * {@link SearchableRepository SearchableRepository} from the remote server.
 *
 * @param <T> domain object on which search will be performed.
 */
public interface Specification<T extends Searchable> {}
