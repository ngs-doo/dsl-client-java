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
	 * Apply local changes to the remote server.
	 *
	 * @param inserts new aggregate roots
	 * @param updates pairs for updating old aggregate to new state
	 * @param deletes aggregate roots which will be deleted
	 * @return        future uris of newly created aggregates
	 */
	public <TAggregate extends AggregateRoot> Future<List<String>> persist(
			Iterable<TAggregate> inserts,
			Iterable<Map.Entry<TAggregate, TAggregate>> updates,
			Iterable<TAggregate> deletes);

	/**
	 * Perform data analysis on specified data source.
	 * Data source is filtered using provided specification.
	 * Analysis is performed by grouping data by dimensions
	 * and aggregating information using specified facts.
	 *
	 * @param manifest      deserialize result into provided type collection
	 * @param cubeName      olap cube name
	 * @param specification filter data source
	 * @param dimensions    group by dimensions
	 * @param facts         analyze using facts
	 * @param order         custom order for result
	 * @return              future with deserialized collection from analysis result
	 */
	public <TDomainObject extends Searchable, TResult> Future<List<TResult>> olapCube(
			Class<TResult> manifest,
			String cubeName,
			Specification<TDomainObject> specification,
			Iterable<String> dimensions,
			Iterable<String> facts,
			Iterable<Map.Entry<String, Boolean>> order);

	/**
	 * Perform data analysis on specified data source.
	 * Analysis is performed by grouping data by dimensions
	 * and aggregating information using specified facts.
	 *
	 * @param manifest      deserialize result into provided type collection
	 * @param cubeName      olap cube name
	 * @param dimensions    group by dimensions
	 * @param facts         analyze using facts
	 * @param order         custom order for result
	 * @return              future with deserialized collection from analysis result
	 */
	public <TResult> Future<List<TResult>> olapCube(
			Class<TResult> manifest,
			String cubeName,
			Iterable<String> dimensions,
			Iterable<String> facts,
			Iterable<Map.Entry<String, Boolean>> order);

	/**
	 * Execute remote service (server implementation for IServerService&lt;TArgument, TResult&gt;)
	 * Send message with serialized argument to remote service and deserialize response.
	 *
	 * @param manifest deserialize result into provided type
	 * @param command  remote service name
	 * @param argument remote service argument
	 * @return         future with deserialized result
	 */
	public <TArgument, TResult> Future<TResult> execute(
			Class<TResult> manifest,
			String command,
			TArgument argument);
}
