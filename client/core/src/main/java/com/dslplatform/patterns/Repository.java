package com.dslplatform.patterns;

import java.util.List;
import java.util.concurrent.Future;

public interface Repository<T extends Identifiable>
        extends SearchableRepository<T> {
    public Future<List<T>> find(final Iterable<String> uris);

    public Future<T> find(final String uri);
}
