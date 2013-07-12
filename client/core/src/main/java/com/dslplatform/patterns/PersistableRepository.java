package com.dslplatform.patterns;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public interface PersistableRepository<T extends AggregateRoot>
        extends Repository<T>{

    public Future<List<String>> persist(
            final Iterable<T> insert,
            final Iterable<Map.Entry<T, T>> update,
            final Iterable<T> delete);

    public Future<List<String>> insert(final Iterable<T> insert);
    public Future<String> insert(final T insert);

    public Future<?> update(final Iterable<T> update);
    public Future<?> update(final T update);

    public Future<?> delete(final Iterable<T> delete);
    public Future<?> delete(final T delete);
}
