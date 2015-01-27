package com.dslplatform.client;

import java.util.concurrent.Future;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.Identifiable;

/**
 * Proxy service to remote CRUD REST-like API.
 * Single aggregate root instance can be used.
 * New object instance will be returned when doing modifications.
 * Use {@link StandardProxy standard proxy} if response is not required from the server.
 * <p>
 * It is preferred to use domain patterns instead of this proxy service.
 */
public interface CrudProxy {
	/**
	 * Get domain object from remote server using provided identity.
	 * If domain object is not found an exception will be thrown.
	 *
	 * @param manifest domain object type
	 * @param uri      domain object identity
	 * @return         future to found domain object
	 */
	public <T extends Identifiable> Future<T> read(
			Class<T> manifest,
			String uri);

	/**
	 * Create new aggregate root on the remote server.
	 * Created object will be returned with its identity
	 * and all calculated properties evaluated.
	 *
	 * @param aggregate new aggregate root
	 * @return          future to aggregate root with new identity
	 */
	public <TAggregate extends AggregateRoot> Future<TAggregate> create(
			TAggregate aggregate);

	/**
	 * Modify existing aggregate root on the remote server.
	 * Aggregate root will be saved and all calculated properties evaluated.
	 *
	 * @param aggregate modified aggregate root
	 * @return          future to aggregate root with updated attributes
	 */
	public <TAggregate extends AggregateRoot> Future<TAggregate> update(
			TAggregate aggregate);

	/**
	 * Delete existing aggregate root from the remote server.
	 * If possible, aggregate root will be deleted and it's instance
	 * will be provided.
	 *
	 * @param manifest aggregate root type
	 * @param uri      aggregate root identity
	 * @return         future to deleted aggregate root instance
	 */
	public <TAggregate extends AggregateRoot> Future<TAggregate> delete(
			Class<TAggregate> manifest,
			String uri);
}
