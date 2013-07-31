package com.dslplatform.patterns;

/**
 * Domain object uniquely represented by its URI.
 */
public interface Identifiable extends Searchable {
    public String getURI();
}
