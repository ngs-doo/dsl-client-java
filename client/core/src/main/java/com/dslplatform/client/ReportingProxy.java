package com.dslplatform.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.dslplatform.patterns.*;

/**  
 * Proxy service to reporting operations such as document generation,
 * report population and history lookup.
 * Report should be used to minimize calls to server.
 * <p>
 * It is preferred to use domain patterns instead of this proxy service.
 * <p>
 * DSL example:
 * <blockquote><pre>
 * module Todo {
 *   aggregate Task {
 *     timestamp createdAt;
 *     timestamp? finishedAt;
 *   }
 *   
 *   report LoadData {
 *     int maxUnfinished;
 *     List&lt;Task&gt; unfinishedTasks 'it => it.finishedAt == null' LIMIT maxUnfinished ORDER BY createdAt;
 *     List&lt;Task&gt; recentlyFinishedTasks 'it => it.finishedAt != null' LIMIT 10 ORDER BY finishedAt DESC;
 *   }
 * }
 * </pre></blockquote>
 */
public interface ReportingProxy {
	/**
	 * Populate report. Send message to server with serialized report specification.
	 * TODO: API change. New version has TReport and TResult
	 * 
	 * @param report specification
	 * @return       future to populated results
	 */
    public <TReport> Future<TReport> populate(
            final TReport report);

	/**
	 * Create document from report. Send message to server with serialized report specification.
	 * Server will return template populated with found data. 
	 * <p>
	 * DSL example:
	 * <blockquote><pre>
	 * module Todo {
     *   report LoadData {
     *     List&lt;Task&gt; unfinishedTasks 'it => it.finishedAt == null' ORDER BY createdAt;
     *     templater createDocument 'Tasks.docx' pdf;
     *   }
     * }
	 * </pre></blockquote>
	 * @param report    specification
	 * @param templater templater name
	 * @return          future to document content
	 */
    public <TReport> Future<byte[]> createReport(
            final TReport report,
            final String templater);

    /**
     * Perform data analysis on specified data source.
     * Data source is filtered using provided specification.
     * Analysis is performed by grouping data by dimensions
     * and aggregating information using specified facts.
     * 
     * @param cubeName      olap cube name
     * @param specification filter data source
     * @param templater     templater name
     * @param dimensions    group by dimensions
     * @param facts         analyze using facts
     * @param order         custom order for result
	 * @return              future to document content
     */
    public <TSource extends Searchable> Future<byte[]> olapCube(
            final String cubeName,
            final Specification<TSource> specification,
            final String templater,
            final Iterable<String> dimensions,
            final Iterable<String> facts,
            final Iterable<Map.Entry<String, Boolean>> order);

    /**
     * Perform data analysis on specified data source.
     * Analysis is performed by grouping data by dimensions
     * and aggregating information using specified facts.
     * 
     * @param cubeName   olap cube name
     * @param templater  templater name
     * @param dimensions group by dimensions
     * @param facts      analyze using facts
     * @param order      custom order for result
	 * @return           future to document content
     */
    public Future<byte[]> olapCube(
            final String cubeName,
            final String templater,
            final Iterable<String> dimensions,
            final Iterable<String> facts,
            final Iterable<Map.Entry<String, Boolean>> order);

	/**
	 * Get aggregate root history. 
	 * {@link History History} is collection of snapshots made at state changes. 
	 * 
	 * @param manifest aggregate root type
	 * @param uris     collection of aggregate identities
	 * @return         future to collection of found aggregate histories
	 */
    public <TAggregate extends AggregateRoot> Future<List<History<TAggregate>>> getHistory(
            final Class<TAggregate> manifest,
            final Iterable<String> uris);

    /**
	 * Populate template using found domain object. 
	 * Optionally convert document to PDF. 
     * 
     * @param manifest domain object type
     * @param file     template file
     * @param uri      domain object identity
     * @param toPdf    convert populated document to PDF
     * @return         future to populated document
     */
    public <TIdentifiable extends Identifiable> Future<byte[]> findTemplater(
            final Class<TIdentifiable> manifest,
            final String file,
            final String uri,
            final boolean toPdf);

    /**
	 * Populate template using domain objects which satisfies 
	 * {@link Specification specification}. 
	 * Optionally convert document to PDF. 
     * 
     * @param manifest      domain object type
     * @param file          template file
     * @param specification filter domain objects using specification
     * @param toPdf         convert populated document to PDF
     * @return              future to populated document
     */
    public <TSearchable extends Searchable> Future<byte[]> searchTemplater(
            final Class<TSearchable> manifest,
            final String file,
            final Specification<TSearchable> specification,
            final boolean toPdf);
}
