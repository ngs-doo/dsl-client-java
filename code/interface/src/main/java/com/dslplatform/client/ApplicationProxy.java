package com.dslplatform.client;

import java.util.concurrent.Future;

/**
 * Proxy service to remote RPC-like API.
 * <p>
 * Remote services can be called using their name.
 */
public interface ApplicationProxy {
	/**
	 * When the remote service doesn't require any arguments it can be called
	 * using the {@code get} method. The class needs to be provided for
	 * deserialization.
	 *
	 * @param <TResult>       result type
	 * @param manifest        result class for deserialization
	 * @param command         remote service name
	 * @param expectedStatus  expected status from remote call
	 * @return                future with the deserialized result
	 */
	public <TResult> Future<TResult> get(
			Class<TResult> manifest,
			String command,
			int[] expectedStatus);

	/**
	 * When the remote service requires arguments a message with the serialized
	 * payload will be sent. The class needs to be provided for deserialization.
	 *
	 * @param <TArgument>     argument type
	 * @param <TResult>       result type
	 * @param manifest        result class for deserialization
	 * @param command         remote service name
	 * @param argument        remote service argument
	 * @param expectedStatus  expected status from remote call
	 * @return                future with the deserialized result
	 */
	public <TArgument, TResult> Future<TResult> post(
			Class<TResult> manifest,
			String command,
			TArgument argument,
			int[] expectedStatus);
}
