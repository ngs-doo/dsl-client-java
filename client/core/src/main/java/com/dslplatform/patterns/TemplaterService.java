package com.dslplatform.patterns;

import java.util.concurrent.Future;

/**
 * API for populating Templater templates with their respective data.
 *
 */
public interface TemplaterService {

    /**
     * Returns a document generated from template named {@code file}
     * populated with {@code aggregate}.
     *
     * @param file  template to populate
     * @param aggregate  data to populate with
     * @return  future value of populated document
     */
    public <T extends Identifiable> Future<byte[]> populate(
            final String file,
            final T aggregate);

    /**
     * Returns a document generated from template named {@code file}
     * populated with {@code aggregate} and converted to PDF format.
     *
     * @param file  template to populate
     * @param aggregate  data to populate with
     * @return  future value of populated document
     */
    public <T extends Identifiable> Future<byte[]> populatePdf(
            final String file,
            final T aggregate);

    /**
     * Returns a document generated from template named {@code file}
     * populated with {@code aggregate}.
     *
     * @param file  template to populate
     * @param manifest  data to populate with
     * @return  future value of populated document
     */
    public <T extends Searchable> Future<byte[]> populate(
            final Class<T> manifest,
            final String file);

    /**
     * Returns a document generated from template named {@code file}
     * populated with {@code manifest} and converted to PDF format.
     *
     * @param file  template to populate
     * @param manifest  data to populate with
     * @return  future value of populated document
     */
    public <T extends Searchable> Future<byte[]> populatePdf(
            final Class<T> manifest,
            final String file);

    /**
     * Returns a document generated from template named {@code file}
     * populated with data described with {@link Specification search predicate}.
     *
     * @param file  template to populate
     * @param specification  {@link Specification search predicate}
     * @return  future value of populated document
     */
    public <T extends Searchable> Future<byte[]> populate(
            final String file,
            final Specification<T> specification);

    /**
     * Returns a document generated from template named {@code file}
     * populated with data described with {@link Specification search predicate}
     * and converted to PDF format.
     *
     * @param file  template to populate
     * @param specification  {@link Specification search predicate}
     * @return  future value of populated document
     */
    public <T extends Searchable> Future<byte[]> populatePdf(
            final String file,
            final Specification<T> specification);
}
