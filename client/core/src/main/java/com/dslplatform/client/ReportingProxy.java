package com.dslplatform.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.dslplatform.patterns.*;

/** API for executing commands on remote server. */
public interface ReportingProxy {
    public <TReport> Future<TReport> populate(
            final TReport report);

    public <TReport> Future<byte[]> createReport(
            final TReport report,
            final String templater);

    public <TSource extends Searchable> Future<byte[]> olapCube(
            final String cubeName,
            final Specification<TSource> specification,
            final String templater,
            final Iterable<String> dimensions,
            final Iterable<String> facts,
            final Iterable<Map.Entry<String, Boolean>> order);

    public Future<byte[]> olapCube(
            final String cubeName,
            final String templater,
            final Iterable<String> dimensions,
            final Iterable<String> facts,
            final Iterable<Map.Entry<String, Boolean>> order);

    public <TAggregate extends AggregateRoot> Future<List<History<TAggregate>>> getHistory(
            final Class<TAggregate> manifest,
            final Iterable<String> uris);

    public <TIdentifiable extends Identifiable> Future<byte[]> findTemplater(
            final Class<TIdentifiable> manifest,
            final String file,
            final String uri,
            final boolean toPdf);

    public <TSearchable extends Searchable> Future<byte[]> searchTemplater(
            final Class<TSearchable> manifest,
            final String file,
            final Specification<TSearchable> specification,
            final boolean toPdf);
}
