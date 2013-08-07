package com.dslplatform.client;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import com.dslplatform.patterns.Identifiable;
import com.dslplatform.patterns.Repository;
import com.dslplatform.patterns.ServiceLocator;

/**
 * Common base implementation for {@link Repository repository}.
 * It redirects calls to proxy services.
 * It shouldn't be used or resolved.
 * Instead domain model repositories should be resolved.
 * <p>
 * DSL example:
 * <blockquote><pre>
 * module Todo {
 *   aggregate Task;
 *   snowflake&lt;Task&gt; TaskList;
 * }
 * </pre></blockquote>
 * Java usage:
 * <pre>
 * ServiceLocator locator;
 * Repository&lt;Todo.TaskList&gt; repository = locator.resolve(Todo.TaskListRepository.class);
 * </pre>
 * @param <T> domain object type
 */
public abstract class ClientRepository<T extends Identifiable>
        extends ClientSearchableRepository<T>
        implements Repository<T> {
    protected final CrudProxy crudProxy;

    /**
     * Generated class will provide class manifest and locator
     *
     * @param manifest domain object type
     * @param locator  context in which domain object lives
     */
    public ClientRepository(
            final Class<T> manifest,
            final ServiceLocator locator) {
        super(manifest, locator);
        this.crudProxy = locator.resolve(CrudProxy.class);
    }

    @Override
    public Future<List<T>> find(final Iterable<String> uris) {
        return domainProxy.find(manifest, uris);
    }

    @Override
    public Future<List<T>> find(final String ... uris) {
      return find(Arrays.asList(uris));
    }

    @Override
    public Future<T> find(final String uri) {
        return crudProxy.read(manifest, uri);
    }
}
