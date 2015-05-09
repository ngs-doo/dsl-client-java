package com.dslplatform.client;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.History;

import java.io.IOException;
import java.util.List;

/**
 * JSON serialization library wrapper.
 */
public interface JsonSerialization {

	/**
	 * Serialize object if possible.
     * Returning byte array and total length, to allow for byte array reuse.
     *
	 * @param value object instance to serialize
	 * @return JSON as byte array (actual length might differ)
	 * @throws IOException
	 */
	public Bytes serialize(Object value) throws IOException;

	/**
	 * Try to deserialize provided JSON byte array input into specified object.
	 * Allow byte[] reuse by asking for content and size
	 *
	 * @param manifest object manifest
	 * @param content JSON array
	 * @param size specify length
	 * @param <T> expected object type
	 * @return object instance
	 * @throws IOException
	 */
	public <T> T deserialize(
			final Class<T> manifest,
			final byte[] content,
			final int size) throws IOException;

	/**
	 * To work around JVM erasure specify list element type.
	 * Specified collection will be deserialized.
	 * Allow byte[] reuse by asking for content and size
	 *
	 * @param manifest object manifest
	 * @param content JSON array
	 * @param size specify length
	 * @param <T> expected object type
	 * @return list instance
	 * @throws IOException
	 */
	public <T> List<T> deserializeList(
			final Class<T> manifest,
			final byte[] content,
			final int size) throws IOException;

	/**
	 * To work around JVM erasure specify history element type.
	 * Specified collection will be deserialized.
	 * Allow byte[] reuse by asking for content and size
	 *
	 * @param manifest object manifest
	 * @param content JSON array
	 * @param size specify length
	 * @param <T> expected object type
	 * @return list instance of history objects
	 * @throws IOException
	 */
	public <T extends AggregateRoot> List<History<T>> deserializeHistoryList(
			final Class<T> manifest,
			final byte[] content,
			final int size) throws IOException;
}
