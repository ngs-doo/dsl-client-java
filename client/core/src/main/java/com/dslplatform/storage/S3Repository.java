package com.dslplatform.storage;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Future;

public interface S3Repository {
    Future<InputStream> get(
            final String bucket,
            final String key);

    Future<?> upload(
            final String bucket,
            final String key,
            final InputStream stream,
            final long length,
            final Map<String, String> metadata);

    Future<?> delete(
            final String bucket,
            final String key);
}
