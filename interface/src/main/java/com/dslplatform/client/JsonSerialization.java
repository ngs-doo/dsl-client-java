package com.dslplatform.client;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.History;

import java.io.IOException;
import java.util.List;

public interface JsonSerialization {

	public byte[] serialize(Object value) throws IOException;

	public <T> T deserialize(
			final Class<T> manifest,
			final byte[] content,
			final int size) throws IOException;

	public <T> List<T> deserializeList(
			final Class<T> manifest,
			final byte[] content,
			final int size) throws IOException;

	public <T extends AggregateRoot> List<History<T>> deserializeHistoryList(
			final Class<T> manifest,
			final byte[] content,
			final int size) throws IOException;
}
