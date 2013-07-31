package com.dslplatform.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.dslplatform.patterns.SearchBuilder;
import com.dslplatform.patterns.Searchable;
import com.dslplatform.patterns.SearchableRepository;
import com.dslplatform.patterns.ServiceLocator;
import com.dslplatform.patterns.Specification;

/**
 * Common base implementation for SearchableRepository
 * It redirects calls to proxy services.
 * It shouldn't be used or resolved.
 * Instead domain model repositories should be resolved.
 * <p>
 * DSL example: 
 * <blockquote><pre>
 * module Todo {
 *   sql TaskInfo 'SELECT name, description FROM task' {
 *     string name;
 *     string description;
 *   }
 * }
 * </pre></blockquote>
 * Java usage:
 * <pre>
 * ServiceLocator locator;
 * SearchableRepository<Todo.TaskInfo> repository = locator.resolve(Todo.TaskInfoRepository.class);
 * </pre>
 * @param <T> domain object type 
 */
public class ClientSearchableRepository<T extends Searchable> implements SearchableRepository<T> {
    protected final Class<T> manifest;
    protected final DomainProxy domainProxy;

    public ClientSearchableRepository(
            final Class<T> manifest,
            final ServiceLocator locator) {
        this.manifest = manifest;
        this.domainProxy = locator.resolve(DomainProxy.class);
    }

    @Override
    public Future<List<T>> search(
            final Specification<T> specification,
            final Integer limit,
            final Integer offset,
            final Iterable<Map.Entry<String, Boolean>> order) {
        return domainProxy.search(specification, limit, offset, order);
    }

    @Override
    public Future<List<T>> search(
            final Specification<T> specification,
            final Integer limit,
            final Integer offset) {
        return domainProxy.search(specification, limit, offset);
    }

    @Override
    public Future<List<T>> search(
            final Specification<T> specification) {
        return domainProxy.search(specification);
    }

    @Override
    public Future<List<T>> findAll(
            final Integer limit,
            final Integer offset,
            final Iterable<Map.Entry<String, Boolean>> order) {
        return domainProxy.findAll(manifest, limit, offset, order);
    }

    @Override
    public Future<List<T>> findAll(
            final Integer limit,
            final Integer offset) {
        return domainProxy.findAll(manifest, limit, offset);
    }

    @Override
    public Future<List<T>> findAll() {
        return domainProxy.findAll(manifest);
    }

    @Override
    public Future<Long> count(
            final Specification<T> specification) {
        return domainProxy.count(specification);
    }

    @Override
    public Future<Long> countAll() {
        return domainProxy.count(manifest);
    }

    @Override
    public SearchBuilder<T> builder() {
        return new SearchBuilder<T>(this);
    }
}
