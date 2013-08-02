package com.dslplatform.patterns;

import java.util.concurrent.Future;

/**
 * Service for creating documents based on templates and data.
 * Data can be provided or specification can be sent so data is queried
 * on the server.
 * <p>
 * Byte array is returned from the server which represents docx, xlsx,
 * text or converted pdf file.
 * <p>
 * More info about Templater library can be found at http://templater.info/
 */
public interface TemplaterService {

    /**
     * Returns a document generated from template named {@code file}
     * populated with {@code aggregate}.
     *
     * @param file      template document
     * @param aggregate data to populate with
     * @return          document content future
     */
    public <T extends Identifiable> Future<byte[]> populate(
            final String file,
            final T aggregate);

    /**
     * Returns a document generated from template named {@code file}
     * populated with {@code aggregate} and converted to PDF format.
     *
     * @param file      template document
     * @param aggregate data to populate with
     * @return          document content future
     */
    public <T extends Identifiable> Future<byte[]> populatePdf(
            final String file,
            final T aggregate);

    /**
     * Returns a document generated from template named {@code file}
     * populated with {@code aggregate}.
     *
     * @param file     template document
     * @param manifest data to populate with
     * @return         document content future
     */
    public <T extends Searchable> Future<byte[]> populate(
            final Class<T> manifest,
            final String file);

    /**
     * Returns a document generated from template named {@code file}
     * populated with {@code manifest} and converted to PDF format.
     *
     * @param file     template document
     * @param manifest data to populate with
     * @return         document content future
     */
    public <T extends Searchable> Future<byte[]> populatePdf(
            final Class<T> manifest,
            final String file);

    /**
     * Returns a document generated from template named {@code file}
     * populated with data which satisfies {@link Specification search predicate}.
     *
     * @param file          template document
     * @param specification search predicate
     * @return              document content future
     */
    public <T extends Searchable> Future<byte[]> populate(
            final String file,
            final Specification<T> specification);

    /**
     * Returns a document generated from template named {@code file}
     * populated with data described with {@link Specification search predicate}
     * and converted to PDF format.
     *
     * @param file          template document
     * @param specification search predicate
     * @return              document content future
     */
    public <T extends Searchable> Future<byte[]> populatePdf(
            final String file,
            final Specification<T> specification);
}
