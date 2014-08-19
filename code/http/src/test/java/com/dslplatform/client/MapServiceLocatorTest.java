package com.dslplatform.client;

import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapServiceLocatorTest {

    @Test
    public void withDefaultLoggerAndEC() throws Exception {
        final Map<Class<?>, Object> initialComponents = new HashMap<Class<?>, Object>();
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        initialComponents.put(ExecutorService.class, executorService);
        final Logger logger = LoggerFactory.getLogger("test-logger");
        initialComponents.put(Logger.class, logger);

        final MapServiceLocator mapServiceLocator = new MapServiceLocator(initialComponents);
        assertSame("Executor matches", executorService, mapServiceLocator.resolve(ExecutorService.class));
        assertSame("Logger matches", logger, mapServiceLocator.resolve(Logger.class));
    }

}
