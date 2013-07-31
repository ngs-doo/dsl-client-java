package com.dslplatform.patterns;

import java.util.List;
import java.util.concurrent.Future;

/**
 * API for retrieval of {@link Identifiable} domain object with its URI.
 *
 * @param <T> Identifiable domain object type
 */
public interface Repository<T extends Identifiable>
        extends SearchableRepository<T> {

  /**
   * Returns a list of objects uniquely represented with their URIs.
   *
   * @param uris  sequence of unique identifiers
   * @return  future value of resulting sequence
   */
    public Future<List<T>> find(final Iterable<String> uris);

    /**
     * Returns a domain object uniquely represented with its URI.
     *
     * @param uri
     * @return future value of resulting domain object
     */
    public Future<T> find(final String uri);
}
