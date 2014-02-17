package com.dslplatform.patterns;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Service for doing CRUD operations.
 * It can be used for applying changes on {@link AggregateRoot aggregate root}
 * to the remote server.
 * <p>
 * It should be used when Future is a preferred way of interacting with the remote server
 * or bulk operations are required.
 *
 * @param <T> type of {@link AggregateRoot aggregate root}
 */
public interface PersistableRepository<T extends AggregateRoot> extends
        Repository<T> {

    /**
     * Apply local changes to the remote server.
     *
     * @param inserts new aggregate roots
     * @param updates pairs for updating old aggregate to new state
     * @param deletes aggregate roots which will be deleted
     * @return       future uris of newly created aggregates
     */
    public Future<List<String>> persist(
            final Iterable<T> inserts,
            final Iterable<Map.Entry<T, T>> updates,
            final Iterable<T> deletes);

    /** @see PersistableRepository#persist(Iterable, Iterable, Iterable) */
    public Future<List<String>> persist(
            final T[] inserts,
            final Map.Entry<T, T>[] updates,
            final T[] deletes);

    /**
     * Bulk insert.
     * Create multiple new {@link AggregateRoot aggregates}.
     *
     * @param inserts new aggregate roots
     * @return       future uris of created aggregate roots
     */
    public Future<List<String>> insert(final Iterable<T> inserts);

    /** @see PersistableRepository#insert(Iterable) */
    public Future<List<String>> insert(final T[] inserts);

    /**
     * Insert a single {@link AggregateRoot aggregate}.
     *
     * @param insert new aggregate root
     * @return       future uri of created aggregate root
     */
    public Future<String> insert(final T insert);

    /**
     * Bulk update.
     * Changing state of multiple {@link AggregateRoot aggregates}.
     *
     * @param updates sequence of aggregate roots to update
     * @return       future for error checking
     */
    public Future<?> update(final Iterable<T> updates);

    /** @see PersistableRepository#update(Iterable) */
    public Future<?> update(final T[] updates);

    /**
     * Changing state of an aggregate root.
     *
     * @param update aggregate root to update
     * @return       future for error checking
     */
    public Future<?> update(final T update);

    /**
     * Bulk delete.
     * Remote multiple {@link AggregateRoot aggregates}.
     *
     * @param deletes aggregate roots to delete
     * @return       future for error checking
     */
    public Future<?> delete(final Iterable<T> deletes);

    /** @see #delete(Iterable) */
    public Future<?> delete(final T[] deletes);

    /**
     * Deleting an {@link AggregateRoot aggregate}.
     *
     * @param delete aggregate root to delete
     * @return       future for error checking
     */
    public Future<?> delete(final T delete);
}
