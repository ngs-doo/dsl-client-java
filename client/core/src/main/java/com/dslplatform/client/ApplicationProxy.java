package com.dslplatform.client;

import java.util.concurrent.Future;

public interface ApplicationProxy {
    public <TResult> Future<TResult> get(
            final Class<TResult> manifest,
            final String command,
            final int[] expectedStatus);

    public <TArgument, TResult> Future<TResult> post(
            final Class<TResult> manifest,
            final String command,
            final TArgument argument,
            final int[] expectedStatus);
}
