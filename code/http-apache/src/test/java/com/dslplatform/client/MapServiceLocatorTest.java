package com.dslplatform.client;

import static org.junit.Assert.assertTrue;

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
        final Map<Class<?>, Object> initialComponents =
                new HashMap<Class<?>, Object>();
        initialComponents.put(ExecutorService.class,
                Executors.newSingleThreadExecutor());
        final String testLoggerName = "testLogger";
        initialComponents.put(Logger.class,
                LoggerFactory.getLogger(testLoggerName));
        final MapServiceLocator mapServiceLocator =
                new MapServiceLocator(initialComponents);
        final ExecutorService executorService =
                mapServiceLocator.resolve(ExecutorService.class);
        final Logger logger = mapServiceLocator.resolve(Logger.class);

        System.out.println(logger.getName());
        assertTrue("Executor matches",
                executorService instanceof ExecutorService);
        assertTrue("Logger matches", logger.getName().equals(testLoggerName));
    }

}
