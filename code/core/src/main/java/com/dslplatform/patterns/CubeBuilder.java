package com.dslplatform.patterns;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dslplatform.client.StandardProxy;

/** Utility class for building olap cube analysis.*/

public class CubeBuilder<T extends Identifiable> {

    private final ServiceLocator locator;
    private final String cubeName;
    private Specification<T> specification;
    private final List<String> dimensions;
    private final List<String> facts;
    private final List<Map.Entry<String, Boolean>> order;

    public CubeBuilder(final String cubeName, final ServiceLocator locator) {
        this.cubeName = cubeName;
        this.locator = locator;
        dimensions = new LinkedList<String>();
        facts = new LinkedList<String>();
        order = new ArrayList<Map.Entry<String, Boolean>>();
    }

    public CubeBuilder<T> with(final Specification<T> specification) {
        this.specification = specification;
        return this;
    }

    public CubeBuilder<T> addDimension(final String dimension) {
        dimensions.add(dimension);
        return this;
    }

    public CubeBuilder<T> addDimensions(final Collection<String> dimensions) {
        dimensions.addAll(dimensions);
        return this;
    }

    public CubeBuilder<T> addFact(final String fact) {
        facts.add(fact);
        return this;
    }

    public CubeBuilder<T> addFacts(final Collection<String> facts) {
        facts.addAll(facts);
        return this;
    }

    private CubeBuilder<T> orderBy(
            final String property,
            final boolean ascending) {
        if (property == null || property.isEmpty())
            throw new IllegalArgumentException("property can't be empty");
        final Map.Entry<String, Boolean> pair =
                new AbstractMap.SimpleEntry<String, Boolean>(property,
                        Boolean.valueOf(ascending));
        order.add(pair);
        return this;
    }

    /**
     * Order result ascending using a provided property
     *
     * @param property name of domain objects property
     * @return         itself
     */
    public CubeBuilder<T> ascending(final String property) {
        return orderBy(property, true);
    }

    /**
     * Order result descending using a provided property
     *
     * @param property name of domain objects property
     * @return         itself
     */
    public CubeBuilder<T> descending(final String property) {
        return orderBy(property, false);
    }

    /**
     * Returns a list of domain objects which satisfy
     * {@link Specification specification} if it was set, otherwise all of them.
     * Parameters can be previously set to <code>limit</code> results,
     * skip <code>offset</code> of initial results and <code>order</code>
     * by some of this domain objects properties.
     *
     * @return  future value of the resulting sequence
     */
    public <TResult> java.util.List<TResult> analyze(final Class<TResult> clazz)
            throws java.io.IOException {
        final StandardProxy proxy = locator.resolve(StandardProxy.class);
        try {
            return specification == null
                    ? proxy.olapCube(clazz, cubeName, dimensions, facts, order)
                            .get()
                    : proxy.olapCube(clazz, cubeName, specification,
                            dimensions, facts, order).get();
        } catch (final InterruptedException e) {
            throw new java.io.IOException(e);
        } catch (final java.util.concurrent.ExecutionException e) {
            throw new java.io.IOException(e);
        }
    }
}
