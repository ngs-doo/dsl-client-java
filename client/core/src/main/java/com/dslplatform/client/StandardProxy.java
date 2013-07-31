package com.dslplatform.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.Searchable;
import com.dslplatform.patterns.Specification;

/**  
 * Proxy service to various domain operations such as bulk persistence, 
 * data analysis, and remote service calls counting and event sourcing.
 * <p>
 * It is preferred to use domain patterns instead of this proxy service.  
 */
public interface StandardProxy {
    /**
     * Apply local changes to the remote server.
     *
     * @param insert new aggregate roots
     * @param update pairs for updating old aggregate to new state
     * @param delete aggregate roots which will be deleted
     * @return       future uris of newly created aggregates
     */	
    public <TAggregate extends AggregateRoot> Future<List<String>> persist(
            final Iterable<TAggregate> inserts,
            final Iterable<Map.Entry<TAggregate, TAggregate>> updates,
            final Iterable<TAggregate> deletes);

    /**
     * Perform data analysis on specified data source.
     * Data source is filtered using provided specification.
     * Analysis is performed by grouping data by dimensions
     * and aggregating information using specified facts.
     * 
     * @param clazz         deserialize result into provided type collection
     * @param cubeName      olap cube name
     * @param specification filter data source
     * @param dimensions    group by dimensions
     * @param facts         analyze using facts
     * @param order         custom order for result
     * @return              future with deserialized collection from analysis result
     */
    public <TDomainObject extends Searchable, TResult> Future<List<TResult>> olapCube(
            final Class<TResult> clazz,
            final String cubeName,
            final Specification<TDomainObject> specification,
            final Iterable<String> dimensions,
            final Iterable<String> facts,
            final Iterable<Map.Entry<String, Boolean>> order);

    /**
     * Perform data analysis on specified data source.
     * Analysis is performed by grouping data by dimensions
     * and aggregating information using specified facts.
     * 
     * @param clazz         deserialize result into provided type collection
     * @param cubeName      olap cube name
     * @param dimensions    group by dimensions
     * @param facts         analyze using facts
     * @param order         custom order for result
     * @return              future with deserialized collection from analysis result
     */
    public <TResult> Future<List<TResult>> olapCube(
            final Class<TResult> clazz,
            final String cubeName,
            final Iterable<String> dimensions,
            final Iterable<String> facts,
            final Iterable<Map.Entry<String, Boolean>> order);

    /**
     * Execute remote service (server implementation for IServerService<TArgument, TResult>)
     * Send message with serialized argument to remote service and deserialize response. 
     * 
     * @param clazz    deserialize result into provided type
     * @param command  remote service name
     * @param argument remote service argument
     * @return         future with deserialized result
     */
    public <TArgument, TResult> Future<TResult> execute(
            final Class<TResult> clazz,
            final String command,
            final TArgument argument);
}
