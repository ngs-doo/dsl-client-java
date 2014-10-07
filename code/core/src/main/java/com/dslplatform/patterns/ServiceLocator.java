package com.dslplatform.patterns;

/**
 * Service for resolving other services.
 * One locator per project should be used.
 * <p>
 * When multiple projects are used, locator must be passed around
 * to resolve appropriate service.
 * <p>
 * Custom classes can be resolved if their dependencies can be satisfied.
 */
public interface ServiceLocator {
	/**
	 * Resolve a service registered in the locator.
	 *
	 * @param clazz class or interface
	 * @return      registered implementation
	 */
	public <T> T resolve(final Class<T> clazz);
}
