package com.dslplatform.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dslplatform.patterns.ServiceLocator;

public class BootstrapTest {
    @Test
    public void withDefaultLoggerAndEC() throws Exception {
        final ServiceLocator locator = Bootstrap.init(getClass().getResourceAsStream("/mockproject.ini"));
        final ProjectSettings ps = locator.resolve(ProjectSettings.class);
        assertEquals("Project id matches", ps.get("project-id"), "0e13d168-1e2d-6ced-82f0-b9e693acde3e");
    }

    @Test
    public void withCustomLoggerAndEC() throws Exception {
        final String loggerName = "testLogger";
        final Logger logger = LoggerFactory.getLogger(loggerName);
        final Map<Class<?>, Object> initialComponents = new HashMap<Class<?>, Object>();
        final ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
        initialComponents.put(ExecutorService.class, newSingleThreadExecutor);
        initialComponents.put(Logger.class, logger);

        final ServiceLocator locator =
                Bootstrap.init(getClass().getResourceAsStream("/mockproject.ini"), initialComponents);

        assertSame("Logger instance matches.", locator.resolve(Logger.class), logger);
        assertSame("ExecutionService matches.", locator.resolve(ExecutorService.class), newSingleThreadExecutor);
    }

    @Test
    public void withoutAuthHeaders() throws  Exception {
        final ServiceLocator locator =
                Bootstrap.init(getClass().getResourceAsStream("/withoutAuth.ini"));
        final HttpAuthorization httpAuthorization = locator.resolve(HttpAuthorization.class);
        assertEquals(0, httpAuthorization.getAuthorizationHeaders().size());
    }
}
