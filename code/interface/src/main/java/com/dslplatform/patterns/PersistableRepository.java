package com.dslplatform.patterns;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Service for doing CRUD operations.
 * It can be used for applying changes on an
 * {@link AggregateRoot aggregate root} to the remote server.
 * <p>
 * It should be used when {@code Future} is the preferred way of interacting
 * with the remote server or bulk operations are required.
 *
 * @param <T> type of {@link AggregateRoot aggregate root}
 */
public interface PersistableRepository<T extends AggregateRoot> extends Repository<T> {
	/**
	 * Apply local changes to the remote server.
	 *
	 * @param inserts  new aggregate roots
	 * @param updates  pairs for updating old aggregates to a state
	 * @param deletes  aggregate roots which will be deleted
	 * @return         future with uris of newly created aggregates
	 */
	public Future<List<String>> persist(
			Iterable<T> inserts,
			Iterable<Map.Entry<T, T>> updates,
			Iterable<T> deletes);

	/** @see #persist(Iterable, Iterable, Iterable) */
	public Future<List<String>> persist(
			T[] inserts,
			Map.Entry<T, T>[] updates,
			T[] deletes);

	/**
	 * Bulk insert.
	 * Creates multiple new {@link AggregateRoot aggregates}.
	 *
	 * @param inserts  new aggregate roots
	 * @return         future with uris of created aggregate roots
	 */
	public Future<List<String>> insert(Iterable<T> inserts);

	/** @see #insert(Iterable) */
	public Future<List<String>> insert(T[] inserts);

	/**
	 * Insert a single {@link AggregateRoot aggregate}.
	 *
	 * @param insert  new aggregate root
	 * @return        future with uri of created aggregate root
	 */
	public Future<String> insert(T insert);

	/**
	 * Bulk update.
	 * Changing state of multiple {@link AggregateRoot aggregates}.
	 *
	 * @param updates  sequence of aggregate roots to update
	 * @return         future for error checking
	 */
	public Future<?> update(Iterable<T> updates);

	/** @see #update(Iterable) */
	public Future<?> update(T[] updates);

	/**
	 * Changing state of an aggregate root.
	 *
	 * @param update  aggregate root to update
	 * @return        future for error checking
	 */
	public Future<?> update(T update);

	/**
	 * Bulk delete.
	 * Remote multiple {@link AggregateRoot aggregates}.
	 *
	 * @param deletes  aggregate roots to delete
	 * @return         future for error checking
	 */
	public Future<?> delete(Iterable<T> deletes);

	/** @see #delete(Iterable) */
	public Future<?> delete(T[] deletes);

	/**
	 * Deleting an {@link AggregateRoot aggregate}.
	 *
	 * @param delete aggregate root to delete
	 * @return       future for error checking
	 */
	public Future<?> delete(T delete);
}
