package com.dslplatform.patterns;

/**
 * API for providing class instances.
 */
public interface ServiceLocator {
    /**
     * Returns an instance (or implementation) of
     * a class or an interface.
     *
     * @param clazz  class or interface
     * @return  instance of a class or an interface
     */
    public <T> T resolve(final Class<T> clazz);
}
