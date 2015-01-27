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
	 * Populates a report. Sends a message to the server with a serialized
	 * report specification.
	 *
	 * @param <TReport>  report type
	 * @param <TResult>  result type
	 * @param result     result class
	 * @param report     specification
	 * @return           future with the populated results
	 */
	public <TReport extends Report<TResult>, TResult> Future<TResult> populate(
			Class<TResult> result,
			TReport report);

	/**
	 * Creates a document from a report. Sends a message to server with
	 * a serialized report specification.
	 * Server will return a template populated with found data.
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
	 * @param <TReport>  report type
	 * @param <TResult>  result type
	 * @param report     specification
	 * @param templater  templater name
	 * @return           future with the document content
	 */
	public <TReport extends Report<TResult>, TResult> Future<byte[]> createReport(
			TReport report,
			String templater);

	/**
	 * Perform data analysis on the specified data source.
	 * Data source is filtered using provided specification.
	 * Analysis is performed by grouping data by dimensions
	 * and aggregating information using specified facts.
	 *
	 * @param <TSource>      searchable type
	 * @param cubeName       olap cube name
	 * @param specification  filter data source
	 * @param templater      templater name
	 * @param dimensions     group by dimensions
	 * @param facts          analyze using facts
	 * @param order          custom order for result
	 * @return               future with the document content
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
	 * @param cubeName    olap cube name
	 * @param templater   templater name
	 * @param dimensions  group by dimensions
	 * @param facts       analyze using facts
	 * @param order       custom order for result
	 * @return            future with the document content
	 */
	public Future<byte[]> olapCube(
			String cubeName,
			String templater,
			Iterable<String> dimensions,
			Iterable<String> facts,
			Iterable<Map.Entry<String, Boolean>> order);

	/**
	 * Gets an aggregate root's history.
	 * {@link History History} is a collection of snapshots made at state changes.
	 *
	 * @param <TAggregate>  aggregate root type
	 * @param manifest      aggregate root class (for deserialization)
	 * @param uris          collection of aggregate identities
	 * @return              future with a collection of found aggregate histories
	 */
	public <TAggregate extends AggregateRoot> Future<List<History<TAggregate>>> getHistory(
			Class<TAggregate> manifest,
			Iterable<String> uris);

	/**
	 * Populates a template using a found domain object.
	 * Optionally converts the document to PDF.
	 *
	 * @param <T>      identifiable domain type
	 * @param manifest domain object type
	 * @param file     template file
	 * @param uri      domain object identity
	 * @param toPdf    convert populated document to PDF
	 * @return         future with the populated document
	 */
	public <T extends Identifiable> Future<byte[]> findTemplater(
			Class<T> manifest,
			String file,
			String uri,
			boolean toPdf);

	/**
	 * Populates a template using domain objects which satisfies a
	 * {@link Specification specification}.
	 * Optionally converts the document to PDF.
	 *
	 * @param <TSearchable>  searchable type
	 * @param manifest       domain object type
	 * @param file           template file
	 * @param specification  filter domain objects using specification
	 * @param toPdf          convert populated document to PDF
	 * @return               future with the populated document
	 */
	public <TSearchable extends Searchable> Future<byte[]> searchTemplater(
			Class<TSearchable> manifest,
			String file,
			Specification<TSearchable> specification,
			boolean toPdf);
}
