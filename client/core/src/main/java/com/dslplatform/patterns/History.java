package com.dslplatform.patterns;

import java.util.List;

/**
 * Aggregation of single {@link AggregateRoot aggregate roots} snapshots.
 *
 * @param <T> type of aggregate root
 */
public final class History<T extends AggregateRoot> implements Identifiable {

    /**
     * Sequence of snapthots
     */
    public List<Snapshot<T>> Snapshots;

    /**
     * Constructor
     */
    public History(){
    }

    public History(final List<Snapshot<T>> Snapshots) {
        this.Snapshots = Snapshots;
    }

    /**
     * Return URI of {@link AggregateRoot aggregate root} whose this history is.
     */
    public String getURI() {
        return this.Snapshots.get(0).getURI();
    }

    public List<Snapshot<T>> getSnapshots() {
        return this.Snapshots;
    }
}
