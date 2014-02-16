package com.dslplatform.client;

import org.junit.Test;

import com.dslplatform.patterns.ServiceLocator;

public class DomainProxyTest {

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void withNullSpecification() throws Exception {
        final ServiceLocator locator = Bootstrap.init(getClass()
                .getResourceAsStream("/mockproject.ini"));
        final DomainProxy dp = locator.resolve(DomainProxy.class);
        dp.search(null);
    }
}
