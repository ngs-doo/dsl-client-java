package com.dslplatform.client;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.PersistableRepository;
import com.dslplatform.patterns.ServiceLocator;

/**
 * Common base implementation for {@link PersistableRepository persistable repository}.
 * It redirects calls to proxy services.
 * It shouldn't be used or resolved.
 * Instead domain model repositories should be resolved.
 *
 * <p>
 * DSL example:
 * <blockquote><pre>
 *
 * module Todo {
 *   aggregate Task;
 * }
 * </blockquote></pre>
 * Java usage:
 * <pre>
 * ServiceLocator locator;
 * PersistableRepository&lt;Todo.Task&gt; repository = locator.resolve(Todo.TaskRepository.class);
 * </pre>
 *
 * @param <T> aggregate root type
 */
public abstract class ClientPersistableRepository<T extends AggregateRoot>
        extends ClientRepository<T> implements PersistableRepository<T> {
    protected final StandardProxy standardProxy;
    private final ExecutorService executorService;

    /**
     * Generated class will provide class manifest and locator
     *
     * @param manifest domain object type
     * @param locator  context in which domain object lives
     */
    public ClientPersistableRepository(
            final Class<T> manifest,
            final ServiceLocator locator) {
        super(manifest, locator);
        standardProxy = locator.resolve(StandardProxy.class);
        executorService = locator.resolve(ExecutorService.class);
    }

    @Override
    public Future<List<String>> persist(
            final Iterable<T> inserts,
            final Iterable<Map.Entry<T, T>> updates,
            final Iterable<T> deletes) {
        return standardProxy.persist(inserts, updates, deletes);
    }

    @Override
    public Future<List<String>> persist(
            final T[] inserts,
            final Map.Entry<T, T>[] updates,
            final T[] deletes) {
        return persist(Arrays.asList(inserts), Arrays.asList(updates),
                Arrays.asList(deletes));
    }

    @Override
    public Future<List<String>> insert(final T[] inserts) {
        return insert(Arrays.asList(inserts));
    }

    @Override
    public Future<List<String>> insert(final Iterable<T> inserts) {
        return standardProxy.persist(inserts, null, null);
    }

    @Override
    public Future<String> insert(final T insert) {
        final Future<T> result = crudProxy.create(insert);
        return executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return result.get().getURI();
            }
        });
    }

    @Override
    public Future<List<String>> update(final Iterable<T> updates) {
        final ArrayList<Map.Entry<T, T>> map = new ArrayList<Map.Entry<T, T>>();
        for (final T it : updates) {
            final Map.Entry<T, T> pair =
                    new AbstractMap.SimpleEntry<T, T>(null, it);
            map.add(pair);
        }
        return standardProxy.persist(null, map, null);
    }

    @Override
    public Future<List<String>> update(final T[] updates) {
        return update(Arrays.asList(updates));
    }

    @Override
    public Future<T> update(final T update) {
        return crudProxy.update(update);
    }

    @Override
    public Future<?> delete(final Iterable<T> deletes) {
        return standardProxy.persist(null, null, deletes);
    }

    @Override
    public final Future<?> delete(final T[] deletes) {
        return delete(Arrays.asList(deletes));
    }

    @Override
    public Future<?> delete(final T delete) {
        return crudProxy.delete(manifest, delete.getURI());
    }
}
