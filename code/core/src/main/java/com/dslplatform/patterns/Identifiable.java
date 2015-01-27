package com.dslplatform.patterns;

/**
 * A domain object is uniquely represented by its URI.
 * Entity and snowflake are example of domain objects which are
 * identified by their identity, instead of their attributes.
 * While entity does not implement {@link Identifiable}, an aggregate root does.
 */
public interface Identifiable extends Searchable {
	/**
	 * Domain object identity.
	 * This identity can be used to lookup a domain object.
	 *
	 * @return  domain object identity
	 */
	public String getURI();
}
