package com.dslplatform.client;

import java.util.List;
import java.util.concurrent.Future;

import com.dslplatform.patterns.Identifiable;
import com.dslplatform.patterns.Repository;
import com.dslplatform.patterns.ServiceLocator;

public class ClientRepository<T extends Identifiable>
        extends ClientSearchableRepository<T>
        implements Repository<T> {
    protected final CrudProxy crudProxy;

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
    public Future<T> find(final String uri) {
        return crudProxy.read(manifest, uri);
    }
}
