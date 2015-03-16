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
import com.dslplatform.storage.S3Repository;

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
	 * @return last service locator which was initialized
	 */
	public static ServiceLocator getLocator() {
		if (staticLocator == null)
			throw new RuntimeException("Bootstrap has not been initialized, call Bootstrap.init");

		return staticLocator;
	}

	/**
	 * Initialize service locator using provided Properties configuration
	 * and components specified in initialComponents param. Use this constructor
	 * if you want to inject arbitrary instance of org.slf4j.Logger, and(or)
	 * java.util.concurrent.ExecutorService.
	 * If you register Class into item key, it will be registered into container as such.
	 * This means you can take dependencies on other services from it's constructor,
	 * since it will be resolved at a later time. If you provide instance as key,
	 * all services must be resolved upfront.
	 *
	 * @param properties        project settings
	 * @param initialComponents components to initialize with
	 * @return                  initialized service locator
	 * @throws IOException      in case of failure to read stream
	 */
	public static ServiceLocator init(
			final Properties properties,
			final Map<Class<?>, Object> initialComponents) throws IOException {
		return init(properties, new MapServiceLocator(initialComponents));
	}

	/**
	 * Initialize service locator using provided Properties configuration.
	 *
	 * @param properties   project settings
	 * @return             initialized service locator
	 * @throws IOException in case of failure to read stream
	 */
	public static ServiceLocator init(final Properties properties) throws IOException {
		return init(properties, new MapServiceLocator());
	}

	/**
	 * Initialize service locator using provided dsl-project.properties stream.
	 *
	 * @param propertiesStream stream for dsl-project.properties
	 * @return                 initialized service locator
	 * @throws IOException     in case of failure to read stream
	 */
	public static ServiceLocator init(final InputStream propertiesStream) throws IOException {
		if (propertiesStream == null) throw new IOException("Provided input stream was null.");
		final Properties properties = new Properties();
		properties.load(propertiesStream);
		return init(properties, new MapServiceLocator());
	}

	private static ServiceLocator init(
			final Properties properties,
			final MapServiceLocator locator) throws IOException {
		if (properties == null) throw new IOException("Provided properties was null.");
		locator.register(Properties.class, properties);
		final JsonSerialization json =
				locator.resolveOrRegister(JsonSerialization.class, new MapServiceLocator.LazyInstance<JsonSerialization>() {
					@Override public JsonSerialization create() { return new JsonSerialization(locator); }
				});
		final Logger logger =
				locator.resolveOrRegister(Logger.class, new MapServiceLocator.LazyInstance<Logger>() {
					@Override public Logger create() { return LoggerFactory.getLogger("dsl-client-http"); }
				});

		final ExecutorService executorService =
				locator.resolveOrRegister(ExecutorService.class, new MapServiceLocator.LazyInstance<ExecutorService>() {
					@Override public ExecutorService create() { return Executors.newCachedThreadPool(); }
				});
		final HttpHeaderProvider headerProvider =
				locator.resolveOrRegister(HttpHeaderProvider.class, new MapServiceLocator.LazyInstance<HttpHeaderProvider>() {
					@Override public HttpHeaderProvider create() { return new SettingsHeaderProvider(properties); }
				});
		final HttpClient httpClient =
				locator.resolveOrRegister(HttpClient.class, new MapServiceLocator.LazyInstance<HttpClient>() {
					@Override
					public HttpClient create() {
						return new HttpClient(properties, json, locator, logger, headerProvider, executorService);
					}
				});
		final DomainProxy domainProxy =
				locator.resolveOrRegister(DomainProxy.class, new MapServiceLocator.LazyInstance<DomainProxy>() {
					@Override public DomainProxy create() { return new HttpDomainProxy(httpClient); }
				});
		locator.resolveOrRegister(ApplicationProxy.class, new MapServiceLocator.LazyInstance<ApplicationProxy>() {
			@Override public ApplicationProxy create() { return new HttpApplicationProxy(httpClient); }
		});
		locator.resolveOrRegister(CrudProxy.class, new MapServiceLocator.LazyInstance<CrudProxy>() {
			@Override public CrudProxy create() { return new HttpCrudProxy(httpClient); }
		});
		locator.resolveOrRegister(StandardProxy.class, new MapServiceLocator.LazyInstance<StandardProxy>() {
			@Override public StandardProxy create() { return new HttpStandardProxy(httpClient, executorService); }
		});
		locator.resolveOrRegister(ReportingProxy.class, new MapServiceLocator.LazyInstance<ReportingProxy>() {
			@Override public ReportingProxy create() { return new HttpReportingProxy(httpClient, executorService, json); }
		});
		locator.resolveOrRegister(DomainEventStore.class, new MapServiceLocator.LazyInstance<DomainEventStore>() {
			@Override
			public DomainEventStore create() {
				return new ClientDomainEventStore(domainProxy);
			}
		});
		if (properties.getProperty("s3-user") != null) {
			registerS3(locator, properties, executorService);
		}

		return staticLocator = locator;
	}

	private static void registerS3(
			MapServiceLocator locator,
			final Properties properties,
			final ExecutorService executorService){
		locator.resolveOrRegister(S3Repository.class, new MapServiceLocator.LazyInstance<S3Repository>() {
			@Override public S3Repository create() { return new AmazonS3Repository(properties, executorService); }
		});
	}


	/**
	 * Initialize service locator using provided dsl-project.properties path.
	 *
	 * @param propertiesPath  path to dsl-project.properties
	 * @return initialized service locator
	 * @throws IOException in case of failure to read dsl-project.properties
	 */
	public static ServiceLocator init(final String propertiesPath) throws IOException {
		if (propertiesPath == null) throw new IOException("Provided path to properties file was null.");
		final InputStream iniStream = new FileInputStream(propertiesPath);
		try {
			return init(iniStream);
		} finally {
			iniStream.close();
		}
	}

// -----------------------------------------------------------------------------

	private static final Properties versionInfo = new Properties();

	private static String getVersionInfo(final String section) {
		if (versionInfo.isEmpty()) {
			try {
				versionInfo.load(Bootstrap.class.getResourceAsStream("dsl-client.properties"));
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
				String.format("dsl-client-java-%s.jar (released on: %s)", getVersion(), getReleaseDate());

		System.out.println();
		System.out.println(versionString);
		System.out.println(versionString.replaceAll(".", "-"));
		System.out.println();

		System.out.println("This is the Java version of the DSL Platform client library");
		System.out.println("and is not indented to be run as a standalone application.");
		System.out.println();
		System.out.println("For more information, visit https://dsl-platform.com/");
		System.out.println();
	}
}
