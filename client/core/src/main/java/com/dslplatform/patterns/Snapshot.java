package com.dslplatform.patterns;

import org.joda.time.DateTime;

public class Snapshot<T extends AggregateRoot> implements Identifiable {
    public String URI;
    public DateTime At;
    public String Action;
    public T Value;

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
