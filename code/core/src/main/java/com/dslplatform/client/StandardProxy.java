package com.dslplatform.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.Searchable;
import com.dslplatform.patterns.Specification;

/**
 * Proxy service to various domain operations such as bulk persistence,
 * data analysis and remote service calls.
 * <p>
 * It is preferred to use domain patterns instead of this proxy service.
 */
public interface StandardProxy {
	/**
	 * Applies local changes to the remote server.
	 *
	 * @param <TAggregate>  aggregate root type
	 * @param inserts       new aggregate roots
	 * @param updates       pairs for updating old aggregate to new state
	 * @param deletes       aggregate roots which will be deleted
	 * @return              future with uris of newly created aggregates
	 */
	public <TAggregate extends AggregateRoot> Future<List<String>> persist(
			Iterable<TAggregate> inserts,
			Iterable<Map.Entry<TAggregate, TAggregate>> updates,
			Iterable<TAggregate> deletes);

	/**
	 * Performs data analysis on a specified data source.
	 * Data source is filtered using provided specification.
	 * Analysis is performed by grouping data by dimensions
	 * and aggregating information using specified facts.
	 *
	 * @param <TDomainObject>  domain object type
	 * @param <TResult>        result type
	 * @param manifest         deserialize result into provided type collection
	 * @param cubeName         olap cube name
	 * @param specification    filter data source
	 * @param dimensions       group by dimensions
	 * @param facts            analyze using facts
	 * @param order            custom order for result
	 * @return                 future with a collection from the analysis result
	 */
	public <TDomainObject extends Searchable, TResult> Future<List<TResult>> olapCube(
			Class<TResult> manifest,
			String cubeName,
			Specification<TDomainObject> specification,
			Iterable<String> dimensions,
			Iterable<String> facts,
			Iterable<Map.Entry<String, Boolean>> order);

	/**
	 * Performs data analysis on a specified data source.
	 * Analysis is performed by grouping data by dimensions
	 * and aggregating information using specified facts.
	 *
	 * @param <TResult>   result type
	 * @param manifest    deserialize result into provided type collection
	 * @param cubeName    olap cube name
	 * @param dimensions  group by dimensions
	 * @param facts       analyze using facts
	 * @param order       custom order for result
	 * @return            future with a collection from the analysis result
	 */
	public <TResult> Future<List<TResult>> olapCube(
			Class<TResult> manifest,
			String cubeName,
			Iterable<String> dimensions,
			Iterable<String> facts,
			Iterable<Map.Entry<String, Boolean>> order);

	/**
	 * Executes a remote service (server implementation for {@code IServerService<TArgument, TResult>}).
	 * Sends a message with a serialized argument to the remote service and deserialize response.
	 *
	 * @param <TArgument>  argument type
	 * @param <TResult>    result type
	 * @param manifest     deserialize result into provided type
	 * @param command      remote service name
	 * @param argument     remote service argument
	 * @return             future with a deserialized result
	 */
	public <TArgument, TResult> Future<TResult> execute(
			Class<TResult> manifest,
			String command,
			TArgument argument);
}
