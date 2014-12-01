package com.dslplatform.patterns;

import java.util.List;

/**
 * Aggregation of single {@link AggregateRoot aggregate root} snapshots.
 * Snapshot is created whenever aggregate is created, modified or deleted if
 * history concept is enabled.
 * <p>
 * DSL example:
 * <blockquote><pre>
 * module Blog {
 *   aggregate Post {
 *     String content;
 *     history;
 *   }
 * }
 * </pre></blockquote>
 * @param <T> aggregate root type
 */
public final class History<T extends AggregateRoot> implements Identifiable {
	private final List<Snapshot<T>> snapshots;

	@SuppressWarnings("unused")
	private History() {
		this.snapshots = null;
	}

	public History(final List<Snapshot<T>> snapshots) {
		this.snapshots = snapshots;
	}

	/**
	 * {@link AggregateRoot aggregate root} identity
	 *
	 * @return URI found in first snapshot
	 */
	@Override
	public String getURI() {
		return snapshots.get(0).getValue().getURI();
	}

	/**
	 * Sequence of persisted snapshots.
	 *
	 * @return List of snapshots captured for provided {@link AggregateRoot aggregate root}.
	 */
	public List<Snapshot<T>> getSnapshots() {
		return snapshots;
	}
}
