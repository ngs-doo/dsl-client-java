package com.dslplatform.client;

import java.util.concurrent.Future;

/**
 * Proxy service to remote RPC-like API.
 * <p>
 * Remote services can be called using their name.
 */
public interface ApplicationProxy {
    /**
     * If remote service doesn't require any arguments it can be called using get method.
     * Provide class of result for deserialization.
     *
     * @param manifest       result type
     * @param command        remote service name
     * @param expectedStatus expected status from remote call
     * @return               future with deserialized result
     */
    public <TResult> Future<TResult> get(
            final Class<TResult> manifest,
            final String command,
            final int[] expectedStatus);

    /**
     * When remote service require an argument message with serialized payload will be sent.
     * Provide class of result for deserialization.
     *
     * @param manifest       result type
     * @param command        remote service name
     * @param argument       remote service argument
     * @param expectedStatus expected status from remote call
     * @return               future with deserialized result
     */
    public <TArgument, TResult> Future<TResult> post(
            final Class<TResult> manifest,
            final String command,
            final TArgument argument,
            final int[] expectedStatus);
}
