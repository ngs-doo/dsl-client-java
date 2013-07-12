package com.dslplatform.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dslplatform.patterns.DomainEventStore;
import com.dslplatform.patterns.ServiceLocator;

public class Bootstrap {
    private static ServiceLocator staticLocator;

    public static ServiceLocator getLocator() {
        if (staticLocator == null)
            throw new RuntimeException("Bootstrap has not been initialized, call Bootstrap.init");

        return staticLocator;
    }

    public static ServiceLocator init(final InputStream iniStream) throws IOException {
        final MapServiceLocator locator = new MapServiceLocator();
        final ProjectSettings project = new ProjectSettings(iniStream);
        final JsonSerialization json = new JsonSerialization();
        final ExecutorService executorService = Executors.newCachedThreadPool();
        final HttpClient httpClient = new HttpClient(project, locator, json, executorService);
        final DomainProxy domainProxy = new HttpDomainProxy(httpClient);

        staticLocator = locator;

        return locator
            .register(JsonSerialization.class, json)
            .register(ProjectSettings.class, project)
            .register(HttpClient.class, httpClient)
            .register(ExecutorService.class, executorService)

            .register(ApplicationProxy.class, new HttpApplicationProxy(httpClient))
            .register(CrudProxy.class, new HttpCrudProxy(httpClient))
            .register(DomainProxy.class, domainProxy)
            .register(StandardProxy.class, new HttpStandardProxy(httpClient, json, executorService))
            .register(ReportingProxy.class, new HttpReportingProxy(httpClient))
            .register(DomainEventStore.class, new ClientDomainEventStore(domainProxy));

//            .register(S3Repository.class, new AmazonS3Repository(project))
    }

    public static ServiceLocator init(final String iniPath) throws IOException {
        final InputStream iniStream = new FileInputStream(iniPath);
        try {
            return init(iniStream);
        }
        finally {
            iniStream.close();
        }
    }
}
