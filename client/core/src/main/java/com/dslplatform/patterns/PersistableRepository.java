package com.dslplatform.patterns;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * API for applying changes on {@link AggregateRoot aggregate root}
 * to the remote server.
 *
 * @param <T> type of {@link AggregateRoot aggregate root}
 */
public interface PersistableRepository<T extends AggregateRoot>
        extends Repository<T>{

    /**
     * Applies changes made locally to remote server.
     *
     * @param insert  objects to be inserted
     * @param update  objects to be updated
     * @param delete  objects to be deleted
     * @return  future uri value of affected roots
     */
    public Future<List<String>> persist(
            final Iterable<T> insert,
            final Iterable<Map.Entry<T, T>> update,
            final Iterable<T> delete);

    /**
     * Inserts a sequence of {@link AggregateRoot} to its repository.
     *
     * @param insert roots to insert
     * @return  future uri value of affected roots
     */
    public Future<List<String>> insert(final Iterable<T> insert);

    /**
     * Inserts a {@link AggregateRoot} to its repository.
     *
     * @param insert root to insert
     * @return  future uri value of affected root
     */
    public Future<String> insert(final T insert);

    /**
     * Updates remote server with changes to domain objects.
     * @param update  sequence of objects to update
     * @return  future with nothing inside
     */
    public Future<?> update(final Iterable<T> update);


    /**
     * Updates remote server with changes to domain objects.
     *
     * @param update  object to update
     * @return  future with nothing inside
     */
    public Future<?> update(final T update);

    /**
     * Deletes domain objects from remote server.
     *
     * @param delete  objects to delete
     * @return  future with nothing inside
     */
    public Future<?> delete(final Iterable<T> delete);

    /**
     * Deletes domain object from remote server.
     *
     * @param delete  object to delete
     * @return  future with nothing inside
     */
    public Future<?> delete(final T delete);
}
