package com.dslplatform.patterns;

import java.util.List;

public final class History<T extends AggregateRoot> implements Identifiable {
    public List<Snapshot<T>> Snapshots;

    public History(){
    }

    public History(final List<Snapshot<T>> Snapshots) {
        this.Snapshots = Snapshots;
    }

    public String getURI() {
        return this.Snapshots.get(0).getURI();
    }

    public List<Snapshot<T>> getSnapshots() {
        return this.Snapshots;
    }
}
