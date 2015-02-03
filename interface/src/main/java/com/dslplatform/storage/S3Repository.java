package com.dslplatform.storage;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * S3 can be used to offload large binaries from the application server.
 * Bucket and key are saved in the application server.
 * <p>
 * This service is used by S3 data type
 */
public interface S3Repository {
	/**
	 * Load remote stream using bucket and key
	 *
	 * @param bucket  bucket where stream is stored
	 * @param key     key in bucket for stream
	 * @return        future to stream
	 */
	Future<InputStream> get(String bucket, String key);

	/**
	 * Load remote stream using bucket and key
	 *
	 * @param bucket
	 *            bucket where stream is stored
	 * @param key
	 *            key in bucket for stream
	 * @param version
	 *            version id of object
	 * @return future to stream
	 */
	Future<InputStream> get(String bucket, String key, String version);

	/**
	 * Upload stream defined by bucket and key.
	 * Provide length of the stream and additional metadata.
	 *
	 * @param bucket    bucket where stream will be stored
	 * @param key       key inside a bucket for stream
	 * @param stream    provided stream
	 * @param length    size of stream
	 * @param metadata  additional metadata
	 * @return          future for error checking
	 */
	Future<?> upload(
			String bucket,
			String key,
			InputStream stream,
			long length,
			Map<String, String> metadata);

	/**
	 * Delete remote stream using bucket and key
	 *
	 * @param bucket bucket where stream is stored
	 * @param key    key in bucket for stream
	 * @return       future for error checking
	 */
	Future<?> delete(String bucket, String key);

	/**
	 * Checks if resource exists in storage
	 *
	 * @param bucket
	 * @param key
	 * @param versionID
	 * @return
	 */
	Future<Boolean> checkExists(String bucket, String key, String versionID);

}
