package com.dslplatform.client;

import static com.dslplatform.client.HttpClient.encode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.dslplatform.patterns.AggregateDomainEvent;
import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.DomainEvent;
import com.dslplatform.patterns.Identifiable;
import com.dslplatform.patterns.Searchable;
import com.dslplatform.patterns.Specification;

class HttpDomainProxy implements DomainProxy {
	private final static String DOMAIN_URI = "Domain.svc/";
	private final static String APPLICATION_URI = "RestApplication.svc/";

	private final HttpClient client;

	public HttpDomainProxy(final HttpClient client) {
		this.client = client;
	}

	private static class GetArgument {
		@SuppressWarnings("unused")
		public final String Name;
		@SuppressWarnings("unused")
		public final ArrayList<String> Uri;

		@SuppressWarnings("unused")
		private GetArgument() {
			Name = null;
			Uri = null;
		}

		public GetArgument(final String name, final Iterable<String> uris) {
			Name = name;
			Uri = Utils.toArrayList(uris);
		}
	}

	@Override
	public <T extends Identifiable> Future<List<T>> find(final Class<T> manifest, final String[] uris) {
		return find(manifest, Arrays.asList(uris));
	}

	@Override
	public <T extends Identifiable> Future<List<T>> find(final Class<T> manifest, final Iterable<String> uris) {
		if (uris == null) throw new IllegalArgumentException("uris can't be null.");
		final String domainName = client.getDslName(manifest);
		final GetArgument arg = new GetArgument(domainName, uris);
		if (arg.Uri.isEmpty()) {
			return new FutureTask<List<T>>(new Callable<List<T>>() {
				@Override
				public List<T> call() throws Exception {
					return new ArrayList<T>(0);
				}
			});
		}

		return client.sendCollectionRequest(
				manifest,
				APPLICATION_URI + "GetDomainObject",
				"POST",
				arg,
				new int[]{200});
	}

	@Override
	public <T extends Searchable> Future<List<T>> search(
			final Specification<T> specification,
			final Integer limit,
			final Integer offset,
			final Iterable<Map.Entry<String, Boolean>> order) {

		if (specification == null) throw new IllegalArgumentException("Specification can't be null");
		final Class<?> specClass = specification.getClass();
		@SuppressWarnings("unchecked")
		final Class<T> manifest = (Class<T>) specClass.getDeclaringClass();
		final String parentName = client.getDslName(manifest);

		final String url =  Utils.appendLimitOffsetOrder(
				parentName + "?specification=" + specClass.getSimpleName(),
				limit,
				offset,
				order,
				false);

		return client.sendCollectionRequest(
				manifest,
				DOMAIN_URI + "search/" + url,
				"PUT",
				specification,
				new int[]{200});
	}

	@Override
	public <T extends Searchable> Future<List<T>> search(
			final Specification<T> specification,
			final Integer limit,
			final Integer offset) {
		return search(specification, limit, offset, null);
	}

	@Override
	public <T extends Searchable> Future<List<T>> search(final Specification<T> specification) {
		return search(specification, null, null, null);
	}

	@Override
	public <T extends Searchable> Future<List<T>> search(
			final Class<T> manifest,
			final Integer limit,
			final Integer offset,
			final Iterable<Map.Entry<String, Boolean>> order) {
		final String domainName = client.getDslName(manifest);

		final String url = Utils.appendLimitOffsetOrder(domainName, limit, offset, order, true);
		return client.sendCollectionRequest(
				manifest,
				DOMAIN_URI + "search/" + url,
				"GET",
				null,
				new int[]{200});
	}

	@Override
	public <T extends Searchable> Future<List<T>> search(
			final Class<T> manifest,
			final Integer limit,
			final Integer offset) {
		return search(manifest, limit, offset, null);
	}

	@Override
	public <T extends Searchable> Future<List<T>> search(final Class<T> manifest) {
		return search(manifest, null, null, null);
	}

	@Override
	public <T extends Searchable> Future<Long> count(final Class<T> manifest) {
		final String domainName = client.getDslName(manifest);
		return client.sendRequest(
				Long.class,
				DOMAIN_URI + "count/" + domainName,
				"GET",
				null,
				new int[] { 200 });
	}

	@Override
	public <T extends Searchable> Future<Long> count(final Specification<T> specification) {
		final Class<?> specClass = specification.getClass();
		final String parentName = client.getDslName(specClass.getDeclaringClass());
		return client.sendRequest(
				Long.class,
				DOMAIN_URI + "count/" + parentName + "?specification=" + specClass.getSimpleName(),
				"PUT",
				specification,
				new int[] { 200 });
	}

	@Override
	public <TEvent extends DomainEvent> Future<String> submit(final TEvent domainEvent) {
		final String domainName = client.getDslName(domainEvent.getClass());
		return client.sendRequest(
				String.class,
				DOMAIN_URI + "submit/" + domainName,
				"POST",
				domainEvent,
				new int[] { 201 });
	}

	@Override
	public <TAggregate extends AggregateRoot, TEvent extends AggregateDomainEvent<TAggregate>> Future<TAggregate> submit(
			final TEvent domainEvent,
			final String uri) {
		if (uri == null) throw new IllegalArgumentException("uri can't be null.");
		final Class<?> eventClazz = domainEvent.getClass();
		@SuppressWarnings("unchecked")
		final Class<TAggregate> manifest = (Class<TAggregate>) eventClazz.getEnclosingClass();
		final String domainName = client.getDslName(manifest);
		return client.sendRequest(
				manifest,
				DOMAIN_URI + "submit/" + domainName + "/" + eventClazz.getSimpleName() + "?uri=" + encode(uri),
				"POST",
				domainEvent,
				new int[] { 201 });
	}
}
