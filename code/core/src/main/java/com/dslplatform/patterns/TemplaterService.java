package com.dslplatform.patterns;

import java.util.concurrent.Future;

/**
 * Service for creating documents based on templates and data.
 * Data can be provided or specification can be sent so data is queried
 * on the server.
 * <p>
 * Byte array is returned from the server which will be a docx, xlsx, text
 * or a converted PDF file.
 * <p>
 * More info about Templater library can be found at http://templater.info/
 */
public interface TemplaterService {
	/**
	 * Returns a document generated from template named {@code file}
	 * populated with {@code aggregate}.
	 *
	 * @param <T>           identifiable domain type
	 * @param file          template document
	 * @param identifiable  data to populate with
	 * @return              future with the document content
	 */
	public <T extends Identifiable> Future<byte[]> populate(String file, T identifiable);

	/**
	 * Returns a document generated from template named {@code file}
	 * populated with {@code aggregate} and converted to PDF format.
	 *
	 * @param <T>           identifiable domain type
	 * @param file          template document
	 * @param identifiable  data to populate with
	 * @return              future with the document content
	 */
	public <T extends Identifiable> Future<byte[]> populatePdf(String file, T identifiable);

	/**
	 * Returns a document generated from template named {@code file}
	 * populated with {@code aggregate}.
	 *
	 * @param <T>       searchable type
	 * @param manifest  data to populate with
	 * @param file      template document
	 * @return          future with the document content
	 */
	public <T extends Searchable> Future<byte[]> populate(Class<T> manifest, String file);

	/**
	 * Returns a document generated from template named {@code file}
	 * populated with {@code manifest} and converted to PDF format.
	 *
	 * @param <T>       searchable type
	 * @param manifest  data to populate with
	 * @param file      template document
	 * @return          future with the document content
	 */
	public <T extends Searchable> Future<byte[]> populatePdf(Class<T> manifest, String file);

	/**
	 * Returns a document generated from template named {@code file}
	 * populated with data which satisfies {@link Specification search predicate}.
	 *
	 * @param <T>            searchable type
	 * @param file           template document
	 * @param specification  search predicate
	 * @return               future with the document content
	 */
	public <T extends Searchable> Future<byte[]> populate(String file, Specification<T> specification);

	/**
	 * Returns a document generated from template named {@code file}
	 * populated with data described with {@link Specification search predicate}
	 * and converted to PDF format.
	 *
	 * @param <T>            searchable type
	 * @param file           template document
	 * @param specification  search predicate
	 * @return               future with the document content
	 */
	public <T extends Searchable> Future<byte[]> populatePdf(String file, Specification<T> specification);
}
