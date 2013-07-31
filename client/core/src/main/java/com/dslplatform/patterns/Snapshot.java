package com.dslplatform.patterns;

import org.joda.time.DateTime;

/**
 * Holder of some past state of {@link AggregateRoot}
 *
 * @param <T> type of aggregate root
 */
public class Snapshot<T extends AggregateRoot> implements Identifiable {

    /**
     * Unique identifier.
     */
    public String URI;

    /**
     * Date and time of roots value.
     */
    public DateTime At;
    public String Action;

    /**
     * Instance of an aggregate root from {@link #At}
     */
    public T Value;

    /**
     * Snapshot constructor
     */
    public Snapshot(){}

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

    @Override
    public String getURI() {
        return URI;
    }

    public DateTime getAt() {
        return At;
    }

    public String getAction() {
        return Action;
    }

    public T getValue() {
        return Value;
    }
}
