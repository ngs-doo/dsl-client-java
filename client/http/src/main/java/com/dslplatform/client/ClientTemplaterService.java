package com.dslplatform.client;

import java.util.concurrent.Future;

import com.dslplatform.patterns.*;

class ClientTemplaterService implements TemplaterService {
    protected final ReportingProxy proxy;

    public ClientTemplaterService(final ReportingProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public <T extends Identifiable> Future<byte[]> populate(
            final String file,
            final T aggregate) {
        if(aggregate == null) throw new IllegalArgumentException("aggregate can't be null");
        return proxy.findTemplater(aggregate.getClass(), file, aggregate.getURI(), false);
    }

    @Override
    public <T extends Identifiable> Future<byte[]> populatePdf(
            final String file,
            final T aggregate) {
        if(aggregate == null) throw new IllegalArgumentException("aggregate can't be null");
        return proxy.findTemplater(aggregate.getClass(), file, aggregate.getURI(), true);
    }

    @Override
    public <T extends Searchable> Future<byte[]> populate(
            final Class<T> manifest,
            final String file) {
        return proxy.searchTemplater(manifest, file, null, false);
    }

    @Override
    public <T extends Searchable> Future<byte[]> populatePdf(
            final Class<T> manifest,
            final String file) {
        return proxy.searchTemplater(manifest, file, null, true);
    }

    @Override
    public <T extends Searchable> Future<byte[]> populate(final String file, final Specification<T> specification) {
        if(specification == null) throw new IllegalArgumentException("specification can't be null");
        return proxy.searchTemplater(null, file, specification, false);
    }

    @Override
    public <T extends Searchable> Future<byte[]> populatePdf(final String file, final Specification<T> specification) {
        if(specification == null) throw new IllegalArgumentException("specification can't be null");
        return proxy.searchTemplater(null, file, specification, true);
    }
}
