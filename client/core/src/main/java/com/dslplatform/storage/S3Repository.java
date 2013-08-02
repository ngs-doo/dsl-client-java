package com.dslplatform.storage;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * S3 can be used to off-load large binaries
 * from the application server.
 * Bucket and key are saved in the application server.
 * <p>
 * This service is used by S3 data type
 */
public interface S3Repository {
	/**
	 * Load remote stream using bucket and key
	 * 
	 * @param bucket bucket where stream is stored
	 * @param key    key in bucket for stream
	 * @return       future to stream
	 */
    Future<InputStream> get(
            final String bucket,
            final String key);

    /**
     * Upload stream defined by bucket and key.
     * Provide length of the stream and additional metadata.  
     * 
     * @param bucket   bucket where stream will be stored
     * @param key      key inside a bucket for stream
     * @param stream   provided stream 
     * @param length   size of stream
     * @param metadata additional metadata
     * @return         future for error checking
     */
    Future<?> upload(
            final String bucket,
            final String key,
            final InputStream stream,
            final long length,
            final Map<String, String> metadata);

    /**
     * Delete remote stream using bucket and key
     * 
     * @param bucket bucket where stream is stored
     * @param key    key in bucket for stream
     * @return       future for error checking
     */
    Future<?> delete(
            final String bucket,
            final String key);
}
