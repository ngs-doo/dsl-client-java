package com.dslplatform.client;

import static com.dslplatform.client.HttpClient.encode;

import java.util.concurrent.Future;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.Identifiable;

class HttpCrudProxy implements CrudProxy {
	private final static String CRUD_URI = "Crud.svc/";

	private final HttpClient client;

	public HttpCrudProxy(final HttpClient client) {
		this.client = client;
	}

	@Override
	public <T extends Identifiable> Future<T> read(final Class<T> manifest, final String uri) {
		if (uri == null) throw new IllegalArgumentException("uri can't be null.");
		final String domainName = client.getDslName(manifest);
		return client.sendRequest(
				JsonSerialization.buildType(manifest),
				CRUD_URI + domainName + "?uri=" + encode(uri),
				"GET",
				null,
				new int[] { 200 });
	}

	@Override
	public <TAggregate extends AggregateRoot> Future<TAggregate> create(final TAggregate aggregate) {
		final Class<?> manifest = aggregate.getClass();
		final String domainName = client.getDslName(manifest);
		return client.sendRequest(
				JsonSerialization.buildType(manifest),
				CRUD_URI + domainName,
				"POST",
				aggregate,
				new int[] { 201 });
	}

	@Override
	public <TAggregate extends AggregateRoot> Future<TAggregate> update(final TAggregate aggregate) {
		final String uri = aggregate.getURI();
		if (uri == null) throw new IllegalArgumentException("uri can't be null.");
		final Class<?> manifest = aggregate.getClass();
		final String domainName = client.getDslName(manifest);
		return client.sendRequest(
				JsonSerialization.buildType(manifest),
				CRUD_URI + domainName + "?uri=" + encode(uri),
				"PUT",
				aggregate,
				new int[] { 200 });
	}

	@Override
	public <TAggregate extends AggregateRoot> Future<TAggregate> delete(
			final Class<TAggregate> manifest,
			final String uri) {
		if (uri == null) throw new IllegalArgumentException("uri can't be null.");
		final String domainName = client.getDslName(manifest);
		return client.sendRequest(
				JsonSerialization.buildType(manifest),
				CRUD_URI + domainName + "?uri=" + encode(uri),
				"DELETE",
				null,
				new int[] { 200 });
	}
}
