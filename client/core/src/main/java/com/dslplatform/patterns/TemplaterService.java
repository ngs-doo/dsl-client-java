package com.dslplatform.patterns;

import java.util.concurrent.Future;

public interface TemplaterService {
    public <T extends Identifiable> Future<byte[]> populate(
            final String file,
            final T aggregate);

    public <T extends Identifiable> Future<byte[]> populatePdf(
            final String file,
            final T aggregate);

    public <T extends Searchable> Future<byte[]> populate(
            final Class<T> manifest,
            final String file);

    public <T extends Searchable> Future<byte[]> populatePdf(
            final Class<T> manifest,
            final String file);

    public <T extends Searchable> Future<byte[]> populate(
            final String file,
            final Specification<T> specification);

    public <T extends Searchable> Future<byte[]> populatePdf(
            final String file,
            final Specification<T> specification);
}
