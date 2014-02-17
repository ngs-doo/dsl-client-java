package com.dslplatform.patterns;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Service for finding identifiable domain objects.
 * Finding domain objects using their URI identity is the fastest way
 * retrieve an object from the remote server.
 *
 * @param <T> Identifiable domain object type
 */
public interface Repository<T extends Identifiable> extends
        SearchableRepository<T> {

    /**
     * Returns a list of domain objects uniquely represented with their URIs.
     * Only found objects will be returned (list will be empty if no objects are found).
     *
     * @param uris sequence of unique identifiers
     * @return     future to found domain objects
     */
    public Future<List<T>> find(final Iterable<String> uris);

    /** @see Repository#find(Iterable) */
    public Future<List<T>> find(final String[] uris);

    /**
     * Returns a domain object uniquely represented with its URI.
     * If object is not found, an exception will be thrown
     *
     * @param uri domain object identity
     * @return    future to found domain object
     */
    public Future<T> find(final String uri);
}
