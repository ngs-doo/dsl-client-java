package com.dslplatform.client;

import org.junit.Test;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.ServiceLocator;
import com.dslplatform.patterns.Specification;

public class DomainProxyTest {
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void withNullSpecification() throws Exception {
		final ServiceLocator locator = Bootstrap.init(getClass().getResourceAsStream("/projectprops/mockproject.properties"));
		final DomainProxy dp = locator.resolve(DomainProxy.class);
		dp.search((Specification<AggregateRoot>) null);
	}
}
