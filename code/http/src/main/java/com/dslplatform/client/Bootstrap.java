package com.dslplatform.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dslplatform.patterns.DomainEventStore;
import com.dslplatform.patterns.ServiceLocator;

/**
 * DSL client Java initialization.
 * Initialize {@link ServiceLocator locator} with services and
 * communication configuration, such as remote url and authentication.
 */
public class Bootstrap {
	private static ServiceLocator staticLocator;

	/**
	 * Static service locator which was initialized.
	 * In case of multiple projects and locator, you should avoid this method
	 * and provide instances of locator yourself.
	 * Calling static service locator should be avoided.
	 *
	 * @return	 last service locator which was initialized
	 */
	public static ServiceLocator getLocator() {
		if (staticLocator == null)
			throw new RuntimeException("Bootstrap has not been initialized, call Bootstrap.init");

		return staticLocator;
	}

	/**
	 * Initialize service locator using provided project.ini stream and
	 * components specified in initialComponents param. Use this constructor
	 * if you want to inject arbitrary instance of org.slf4j.Logger, and(or)
	 * java.util.concurrent.ExecutorService.
	 *
	 * @param iniStream				stream for project.ini
	 * @param initialComponents		components to initialize with
	 * @return						 initialized service locator
	 * @throws IOException			 in case of failure to read stream
	 */
	public static ServiceLocator init(
			final InputStream iniStream,
			final Map<Class<?>, Object> initialComponents) throws IOException {
		return init(iniStream, new MapServiceLocator(initialComponents));
	}

	/**
	 * Initialize service locator using provided project.ini stream.
	 *
	 * @param iniStream	stream for project.ini
	 * @return			 initialized service locator
	 * @throws IOException in case of failure to read stream
	 */
	public static ServiceLocator init(final InputStream iniStream) throws IOException {
		if (iniStream == null) throw new IOException("Provided input stream was null.");
		return init(iniStream, new MapServiceLocator());
	}

	private static ServiceLocator init(
			final InputStream iniStream,
			final MapServiceLocator locator) throws IOException {
		final JsonSerialization jsonDeserialization = new JsonSerialization(locator);
		final Logger logger;
		if (locator.contains(Logger.class)) {
			logger = locator.resolve(Logger.class);
		} else {
			logger = LoggerFactory.getLogger("dsl-client-http");
			locator.register(Logger.class, logger);
		}
		final ProjectSettings project = new ProjectSettings(logger, iniStream);
		locator.register(ProjectSettings.class, project);

		final ExecutorService executorService;
		if (locator.contains(ExecutorService.class)) {
			executorService = locator.resolve(ExecutorService.class);
		} else {
			executorService = Executors.newCachedThreadPool();
			locator.register(ExecutorService.class, executorService);
		}
		final HttpHeaderProvider headerProvider;
		if (locator.contains(HttpHeaderProvider.class)) {
			headerProvider = locator.resolve(HttpHeaderProvider.class);
		} else {
			headerProvider = new SettingsHeaderProvider(project);
			locator.register(HttpHeaderProvider.class, headerProvider);
		}
		final HttpClient httpClient =
				new HttpClient(project, jsonDeserialization, logger, headerProvider, executorService);
		final DomainProxy domainProxy = new HttpDomainProxy(httpClient);

		locator
				.register(JsonSerialization.class, jsonDeserialization)
				.register(HttpClient.class, httpClient)
				.register(ApplicationProxy.class, new HttpApplicationProxy(httpClient))
				.register(CrudProxy.class, HttpCrudProxy.class)
				.register(DomainProxy.class, domainProxy)
				.register(StandardProxy.class, new HttpStandardProxy(httpClient, executorService))
				.register(ReportingProxy.class, new HttpReportingProxy(httpClient))
				.register(DomainEventStore.class, new ClientDomainEventStore(domainProxy));
//				.register(S3Repository.class, new AmazonS3Repository(project))

		return staticLocator = locator;
	}

	/**
	 * Initialize service locator using provided project.ini path.
	 *
	 * @param iniPath	  path to project.ini
	 * @return			 initialized service locator
	 * @throws IOException in case of failure to read project.ini
	 */
	public static ServiceLocator init(final String iniPath) throws IOException {
		final InputStream iniStream = new FileInputStream(iniPath);
		try {
			return init(iniStream);
		} finally {
			iniStream.close();
		}
	}

// -----------------------------------------------------------------------------

	private static final Properties versionInfo = new Properties();

	private static final String getVersionInfo(final String section) {
		if (versionInfo.isEmpty()) {
			try {
				versionInfo.load(Bootstrap.class.getResourceAsStream("dsl-client.ini"));
			} catch (final Throwable t) {
				t.printStackTrace();
			}
		}

		return versionInfo.getProperty(section);
	}

	/**
	 * Get version info of this library.
	 * Useful for debugging purposes.
	 *
	 * @return version info
	 */
	public static String getVersion() {
		return getVersionInfo("version");
	}

	/**
	 * Get release date of this library.
	 * Useful for debugging purposes.
	 *
	 * @return release date
	 */
	public static String getReleaseDate() {
		return getVersionInfo("date");
	}

	/**
	 * This is a library and main should not be called.
	 * If main is called print short library description.
	 *
	 * @param args ignored
	 */
	public static void main(final String[] args) {
		final String versionString =
				String.format("dsl-client-%s.jar (released on: %s)", getVersion(), getReleaseDate());

		System.out.println();
		System.out.println(versionString);
		System.out.println(versionString.replaceAll(".", "-"));
		System.out.println();

		System.out.println("This is the Java (Android) version of the DSL Platform client library");
		System.out.println("and is not indented to be run as a standalone application.");
		System.out.println();
		System.out.println("For more information, visit https://dsl-platform.com/");
		System.out.println();
	}
}
