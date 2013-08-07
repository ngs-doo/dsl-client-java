package com.dslplatform.patterns;

/**
 * Domain object uniquely represented by its URI.
 * Entity and snowflake are example of domain objects which are
 * identified by its identity, instead of attributes.
 * While entity does not implement Identifiable, aggregate root does.
 */
public interface Identifiable extends Searchable {
    /**
     * Domain object identity.
     * This identity can be used to lookup domain object
     *
     * @return domain object identity
     */
    public String getURI();
}
