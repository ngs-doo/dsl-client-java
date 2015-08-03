package com.dslplatform.client;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.Bytes;
import com.dslplatform.patterns.History;

import java.io.IOException;
import java.io.Writer;
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
	Bytes serialize(Object value) throws IOException;

	/**
	 * Serialize object if possible to an existing Writer.
	 *
	 * @param value object instance to serialize
	 * @throws IOException
	 */
	void serialize(Writer writer, Object value) throws IOException;

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
	<T> T deserialize(
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
	<T> List<T> deserializeList(
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
	<T extends AggregateRoot> List<History<T>> deserializeHistoryList(
			final Class<T> manifest,
			final byte[] content,
			final int size) throws IOException;
}
