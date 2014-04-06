package com.dslplatform.client;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.History;
import com.dslplatform.patterns.Identifiable;
import com.dslplatform.patterns.Searchable;
import com.dslplatform.patterns.Specification;

class HttpReportingProxy implements ReportingProxy {
    private final static String REPORTING_URI = "Reporting.svc/";
    private final static String APPLICATION_URI = "RestApplication.svc/";

    private final HttpClient client;

    public HttpReportingProxy(final HttpClient client) {
        this.client = client;
    }

    @Override
    public <TReport> Future<TReport> populate(final TReport report) {
        final Class<?> manifest = report.getClass();
        final String domainName = client.getDslName(manifest);
        return client.sendRequest(
                JsonSerialization.buildType(manifest),
                REPORTING_URI + "report/" + domainName,
                "PUT",
                report,
                new int[] { 200 });
    }

    @Override
    public <TReport> Future<byte[]> createReport(final TReport report, final String templater) {
        final String domainName = client.getDslName(report.getClass());
        return client.sendRequest(
                JsonSerialization.buildType(byte[].class),
                REPORTING_URI + "report/" + domainName + "/" + templater,
                "PUT",
                report,
                new int[] { 201 });
    }

    @Override
    public <T extends Searchable> Future<byte[]> olapCube(
            final String cubeName,
            final Specification<T> specification,
            final String templater,
            final Iterable<String> dimensions,
            final Iterable<String> facts,
            final Iterable<Map.Entry<String, Boolean>> order) {
        if (specification == null) return olapCube(cubeName, templater, dimensions, facts, order);
        final String args = Utils.buildOlapArguments(dimensions, facts, order);
        final Class<?> specClass = specification.getClass();
        final String parentName = client.getDslName(specClass.getDeclaringClass());
        final String specName = parentName == cubeName ? parentName + "/" : "";
        return client.sendRequest(
                JsonSerialization.buildType(byte[].class),
                REPORTING_URI + "olap/" + cubeName + '/' + specName + specClass.getSimpleName() + '/' + templater + args,
                "PUT",
                specification,
                new int[] { 200 });
    }

    @Override
    public Future<byte[]> olapCube(
            final String cubeName,
            final String templater,
            final Iterable<String> dimensions,
            final Iterable<String> facts,
            final Iterable<Map.Entry<String, Boolean>> order) {
        final String args = Utils.buildOlapArguments(dimensions, facts, order);
        return client.sendRequest(
                JsonSerialization.buildType(byte[].class),
                REPORTING_URI + "olap/" + cubeName + '/' + templater + args,
                "GET",
                null,
                new int[] { 200 });
    }

    private static class HistoryArg {
        @SuppressWarnings("unused")
        public final String Name;
        @SuppressWarnings("unused")
        public final ArrayList<String> Uri;

        @SuppressWarnings("unused")
        private HistoryArg() {
            Name = null;
            Uri = null;
        }

        public HistoryArg(final String name, final Iterable<String> uris) {
            Name = name;
            Uri = Utils.toArrayList(uris);
        }
    }

    @Override
    public <T extends AggregateRoot> Future<List<History<T>>> getHistory(
            final Class<T> manifest,
            final Iterable<String> uris) {
        final String domainName = client.getDslName(manifest);

        return client.sendRequest(
                JsonSerialization.buildCollectionType(
                        ArrayList.class,
                        JsonSerialization.buildGenericType(History.class, manifest)),
                APPLICATION_URI + "GetRootHistory",
                "POST",
                new HistoryArg(domainName, uris),
                new int[] { 200 });
    }

    @Override
    public <TIdentifiable extends Identifiable> Future<byte[]> findTemplater(
            final Class<TIdentifiable> manifest,
            final String file,
            final String uri,
            final boolean toPdf) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("file not specified");
        if (uri.isEmpty()) throw new IllegalArgumentException("uri not specified");
        final String domainName = client.getDslName(manifest);

        @SuppressWarnings("unchecked")
        final List<Map.Entry<String, String>> headers =
                Arrays.asList((Map.Entry<String, String>) new AbstractMap.SimpleEntry<String, String>(
                        "Accept",
                        toPdf ? "application/pdf" : "application/octet-stream"));

        return client.sendRawRequest(
                REPORTING_URI + "templater/" + file + "/" + domainName + "?uri=" + HttpClient.encode(uri),
                "GET",
                null,
                headers,
                new int[] { 200 });
    }

    @Override
    public <TSearchable extends Searchable> Future<byte[]> searchTemplater(
            final Class<TSearchable> manifest,
            final String file,
            final Specification<TSearchable> specification,
            final boolean toPdf) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("file not specified");
        if (specification == null && manifest == null)
            throw new IllegalArgumentException("specification or manifest must be provided");

        @SuppressWarnings("unchecked")
        final List<Map.Entry<String, String>> headers =
                Arrays.asList((Map.Entry<String, String>) new AbstractMap.SimpleEntry<String, String>(
                        "Accept",
                        toPdf ? "application/pdf" : "application/octet-stream"));

        // Branching if null!
        if (specification == null) {
            final String domainName = client.getDslName(manifest);
            return client.sendRawRequest(
                    REPORTING_URI + "templater/" + file + "/" + domainName,
                    "GET",
                    null,
                    headers,
                    new int[] { 200 });
        }
        final Class<?> specClass = specification.getClass();
        final Class<?> parentClass = specClass.getDeclaringClass();
        final String domainName = client.getDslName(parentClass);
        return client.sendRawRequest(
                REPORTING_URI + "templater/" + file + "/" + domainName + "/" + specClass.getSimpleName(),
                "PUT",
                specification,
                headers,
                new int[] { 200 });
    }
}
