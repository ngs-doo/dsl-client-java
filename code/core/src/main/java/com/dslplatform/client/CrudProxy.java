package com.dslplatform.client;

import java.util.concurrent.Future;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.Identifiable;

/**
 * Proxy service to remote CRUD REST-like API.
 * Single aggregate root instance can be used.
 * New object instance will be returned when doing modifications.
 * Use {@link StandardProxy standard proxy} if a response is not required from the server.
 * <p>
 * It is preferred to use domain patterns instead of this proxy service.
 */
public interface CrudProxy {
	/**
	 * Gets a domain object from the remote server using provided identity.
	 * If a domain object is not found an exception will be thrown.
	 *
	 * @param <T>       identifiable domain type
	 * @param manifest  domain object class (for deserialization)
	 * @param uri       domain object identity
	 * @return          future with the found domain object
	 */
	public <T extends Identifiable> Future<T> read(
			Class<T> manifest,
			String uri);

	/**
	 * Creates a new aggregate root on the remote server.
	 * Created object will be returned with a new identity and all calculated
	 * properties evaluated.
	 *
	 * @param <TAggregate>  aggregate root type
	 * @param aggregate     new aggregate root
	 * @return              future with the aggregate root with a new identity
	 */
	public <TAggregate extends AggregateRoot> Future<TAggregate> create(
			TAggregate aggregate);

	/**
	 * Modifies an existing aggregate root on the remote server.
	 * Aggregate root will be saved and all calculated properties evaluated.
	 *
	 * @param <TAggregate>  aggregate root type
	 * @param               aggregate modified aggregate root
	 * @return              future with the aggregate root with updated attributes
	 */
	public <TAggregate extends AggregateRoot> Future<TAggregate> update(
			TAggregate aggregate);

	/**
	 * Deletes an existing aggregate root from the remote server.
	 * If possible, aggregate root will be deleted and its instance returned.
	 *
	 * @param <TAggregate>  aggregate root type
	 * @param manifest      aggregate root class (for deserialization)
	 * @param uri           aggregate root identity
	 * @return              future with the deleted aggregate root instance
	 */
	public <TAggregate extends AggregateRoot> Future<TAggregate> delete(
			Class<TAggregate> manifest,
			String uri);
}
