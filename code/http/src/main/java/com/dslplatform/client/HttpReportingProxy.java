package com.dslplatform.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.dslplatform.patterns.*;
import com.fasterxml.jackson.databind.JavaType;

class HttpReportingProxy implements ReportingProxy {
	private final static String REPORTING_URI = "Reporting.svc/";
	private final static String APPLICATION_URI = "RestApplication.svc/";

	private final HttpClient client;
	private final ExecutorService executorService;
	private final JsonSerialization jsonSerialization;

	public HttpReportingProxy(
			final HttpClient client,
			final ExecutorService executorService,
			final JsonSerialization jsonSerialization) {
		this.client = client;
		this.executorService = executorService;
		this.jsonSerialization = jsonSerialization;
	}

	@Override
	public <TReport extends Report<TResult>, TResult> Future<TResult> populate(
			final Class<TResult> result,
			final TReport report) {
		final String domainName = client.getDslName(report.getClass());
		return client.sendRequest(
				result,
				REPORTING_URI + "report/" + domainName,
				"PUT",
				report,
				new int[]{200});
	}

	@Override
	public <TReport extends Report<TResult>, TResult> Future<byte[]> createReport(
			final TReport report,
			final String templater) {
		final String domainName = client.getDslName(report.getClass());
		return client.sendRawRequest(
				REPORTING_URI + "report/" + domainName + "/" + templater,
				"PUT",
				report,
				Utils.acceptAs("application/octet-stream"),
				new int[]{201});
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
		final String specName = parentName.equals(cubeName) ? parentName + "/" : "";
		return client.sendRawRequest(
				REPORTING_URI + "olap/" + cubeName + '/' + specName + specClass.getSimpleName() + '/' + templater + args,
				"PUT",
				specification,
				Utils.acceptAs("application/octet-stream"),
				new int[]{200});
	}

	@Override
	public Future<byte[]> olapCube(
			final String cubeName,
			final String templater,
			final Iterable<String> dimensions,
			final Iterable<String> facts,
			final Iterable<Map.Entry<String, Boolean>> order) {
		final String args = Utils.buildOlapArguments(dimensions, facts, order);
		return client.sendRawRequest(
				REPORTING_URI + "olap/" + cubeName + '/' + templater + args,
				"GET",
				null,
				Utils.acceptAs("application/octet-stream"),
				new int[]{200});
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

		return executorService.submit(new Callable<List<History<T>>>() {
			@Override
			public List<History<T>> call() throws Exception {
				final byte[] result = client.sendRawRequest(
						APPLICATION_URI + "GetRootHistory",
						"POST",
						new HistoryArg(domainName, uris),
						Utils.acceptAs("application/json"),
						new int[]{200}).get();
				final JavaType ht =
						JsonSerialization.buildCollectionType(
							ArrayList.class,
							JsonSerialization.buildGenericType(History.class, manifest));
				return jsonSerialization.deserialize(ht, result);
			}
		});
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

		return client.sendRawRequest(
				REPORTING_URI + "templater/" + file + "/" + domainName + "?uri=" + HttpClient.encode(uri),
				"GET",
				null,
				toPdf ? Utils.acceptAs("application/pdf") : Utils.acceptAs("application/octet-stream"),
				new int[]{200});
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

		// Branching if null!
		if (specification == null) {
			final String domainName = client.getDslName(manifest);
			return client.sendRawRequest(
					REPORTING_URI + "templater/" + file + "/" + domainName,
					"GET",
					null,
					toPdf ? Utils.acceptAs("application/pdf") : Utils.acceptAs("application/octet-stream"),
					new int[]{200});
		}
		final Class<?> specClass = specification.getClass();
		final Class<?> parentClass = specClass.getDeclaringClass();
		final String domainName = client.getDslName(parentClass);
		return client.sendRawRequest(
				REPORTING_URI + "templater/" + file + "/" + domainName + "?specification=" + specClass.getSimpleName(),
				"PUT",
				specification,
				toPdf ? Utils.acceptAs("application/pdf") : Utils.acceptAs("application/octet-stream"),
				new int[]{200});
	}
}
