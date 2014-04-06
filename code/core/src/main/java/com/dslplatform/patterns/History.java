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
    private final List<Snapshot<T>> Snapshots;

    @SuppressWarnings("unused")
    private History() {
        this.Snapshots = null;
    }

    public History(final List<Snapshot<T>> Snapshots) {
        this.Snapshots = Snapshots;
    }

    /**
     * {@link AggregateRoot aggregate root} identity
     *
     * @return URI found in first snapshot
     */
    @Override
    public String getURI() {
        return Snapshots.get(0).getURI();
    }

    /**
     * Sequence of persisted snapshots.
     *
     * @return List of snapshots captured for provided {@link AggregateRoot aggregate root}.
     */
    public List<Snapshot<T>> getSnapshots() {
        return Snapshots;
    }
}
