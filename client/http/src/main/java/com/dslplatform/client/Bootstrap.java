package com.dslplatform.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
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

// -----------------------------------------------------------------------------

    private static final Properties versionInfo = new Properties();

    private static final String getVersionInfo(final String section) {
        if (versionInfo.isEmpty()) {
            try {
                versionInfo.load(Bootstrap.class
                        .getResourceAsStream("dsl-client.ini"));
            } catch (final Throwable t) {
                t.printStackTrace();
            }
        }

        return versionInfo.getProperty(section);
    }

    public static String getVersion() {
        return getVersionInfo("version");
    }

    public static String getReleaseDate() {
        return getVersionInfo("date");
    }

    public static void main(final String[] args) {
        final String versionString = String.format(
                "dsl-client-%s.jar (released on: %s)",
                getVersion(),
                getReleaseDate());

        System.out.println();
        System.out.println(versionString);
        System.out.println(versionString.replaceAll(".", "-"));
        System.out.println();

        System.out.println("This is the Java version of the DSL Platform client");
        System.out.println("and is not indended to be run as a standalone application.");
        System.out.println();
        System.out.println("For more information, visit https://dsl-platform.com/");
        System.out.println();
    }
}
