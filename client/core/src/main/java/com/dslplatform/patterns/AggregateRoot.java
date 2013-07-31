package com.dslplatform.patterns;

/**
* Aggregate root is collection of objects bound together under entity. Usually
* it represents a single table, but can be used like document or similar data
* structure. Since every aggregate is also an entity, it has a unique
* identification represented by its URI.
*/
public interface AggregateRoot extends Identifiable {}
