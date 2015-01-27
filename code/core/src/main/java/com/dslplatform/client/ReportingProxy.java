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
 *     Timestamp createdAt;
 *     Timestamp? finishedAt;
 *   }
 *
 *   report LoadData {
 *     int maxUnfinished;
 *     List&lt;Task&gt; unfinishedTasks 'it =&gt; it.finishedAt == null' LIMIT maxUnfinished ORDER BY createdAt;
 *     List&lt;Task&gt; recentlyFinishedTasks 'it =&gt; it.finishedAt != null' LIMIT 10 ORDER BY finishedAt DESC;
 *   }
 * }
 * </pre></blockquote>
 */
public interface ReportingProxy {
	/**
	 * Populate report. Send message to server with serialized report specification.
	 *
	 * @param result result class
	 * @param report specification
	 * @return       future to populated results
	 */
	public <TReport extends Report<TResult>, TResult> Future<TResult> populate(
			Class<TResult> result,
			TReport report);

	/**
	 * Create document from report. Send message to server with serialized report specification.
	 * Server will return template populated with found data.
	 * <p>
	 * DSL example:
	 * <blockquote><pre>
	 * module Todo {
	 *   report LoadData {
	 *     List&lt;Task&gt; unfinishedTasks 'it =&gt; it.finishedAt == null' ORDER BY createdAt;
	 *     templater createDocument 'Tasks.docx' pdf;
	 *   }
	 * }
	 * </pre></blockquote>
	 * @param report    specification
	 * @param templater templater name
	 * @return          future to document content
	 */
	public <TReport extends Report<TResult>, TResult> Future<byte[]> createReport(
			TReport report,
			String templater);

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
			String cubeName,
			Specification<TSource> specification,
			String templater,
			Iterable<String> dimensions,
			Iterable<String> facts,
			Iterable<Map.Entry<String, Boolean>> order);

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
			String cubeName,
			String templater,
			Iterable<String> dimensions,
			Iterable<String> facts,
			Iterable<Map.Entry<String, Boolean>> order);

	/**
	 * Get aggregate root history.
	 * {@link History History} is collection of snapshots made at state changes.
	 *
	 * @param manifest aggregate root type
	 * @param uris     collection of aggregate identities
	 * @return         future to collection of found aggregate histories
	 */
	public <TAggregate extends AggregateRoot> Future<List<History<TAggregate>>> getHistory(
			Class<TAggregate> manifest,
			Iterable<String> uris);

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
			Class<TIdentifiable> manifest,
			String file,
			String uri,
			boolean toPdf);

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
			Class<TSearchable> manifest,
			String file,
			Specification<TSearchable> specification,
			boolean toPdf);
}
