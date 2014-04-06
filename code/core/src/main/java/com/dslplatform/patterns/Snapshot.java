package com.dslplatform.patterns;

import org.joda.time.DateTime;

/**
 * Snapshot of some past state of an {@link AggregateRoot aggregate root}
 *
 * @param <T> type of aggregate root
 */
public class Snapshot<T extends AggregateRoot> implements Identifiable {
    private final String URI;
    private final DateTime At;
    private final String Action;
    private final T Value;

    @SuppressWarnings("unused")
    private Snapshot() {
        this(null, null, null, null);
    }

    public Snapshot(
            final String URI,
            final DateTime At,
            final String Action,
            final T Value) {
        this.URI = URI;
        this.At = At;
        this.Action = Action;
        this.Value = Value;
    }

    /**
     * Domain object identity.
     *
     * @return Identity
     */
    @Override
    public String getURI() {
        return URI;
    }

    /**
     * Date and time when snapshot was created.
     *
     * @return DateTime of snapshot
     */
    public DateTime getAt() {
        return At;
    }

    /**
     * Which action was performed (INSERT|UPDATE|DELETE)
     *
     * @return Action type
     */
    public String getAction() {
        return Action;
    }

    /**
     * Instance of an aggregate root at that time
     *
     * @return aggregate root snapshot
     */
    public T getValue() {
        return Value;
    }
}
