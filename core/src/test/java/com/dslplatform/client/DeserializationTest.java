package com.dslplatform.client;

import com.dslplatform.client.json.JsonObject;
import com.dslplatform.client.json.JsonWriter;
import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.History;
import com.dslplatform.patterns.ServiceLocator;
import com.dslplatform.patterns.Snapshot;
import com.dslplatform.test.simple.SimpleRoot;
import com.fasterxml.jackson.databind.JavaType;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class DeserializationTest {
	/** Copy of private static inner class from JsonSerialization */
	private static class SnapshotDelegate<T extends AggregateRoot> {
		public final DateTime At;
		public final String Action;
		public final T Value;

		@SuppressWarnings("unused")
		private SnapshotDelegate() { this(null, null, null); }

		public SnapshotDelegate(
				final DateTime At,
				final String Action,
				final T Value) {
			this.At = At;
			this.Action = Action;
			this.Value = Value;
		}
	}

	// ---------------------------------------------------------------------------------------------------------
	@Test
	public void getSnapshots() throws Exception {
		final String jason = "[{\"At\":\"2014-11-27T13:37:53.249724+01:00\",\"Value\":{\"ID\":23040,\"s\":\"\",\"URI\":\"23040\",\"isOdd\":true},\"Action\":\"INSERT\"},{\"At\":\"2014-11-27T13:37:53.408438+01:00\",\"Value\":{\"ID\":23040,\"i\":5,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"},{\"At\":\"2014-11-27T13:37:53.438207+01:00\",\"Value\":{\"ID\":23040,\"i\":7,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"},{\"At\":\"2014-11-27T13:37:53.453703+01:00\",\"Value\":{\"ID\":23040,\"i\":11,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"}]";
		final JavaType st = JsonSerialization.buildCollectionType(
				ArrayList.class,
				JsonSerialization.buildGenericType(SnapshotDelegate.class, SimpleRoot.class));
		final List<SnapshotDelegate<SimpleRoot>> snapshotList = JsonStatic.INSTANCE.jsonSerialization.deserialize(st, jason);
		assertEquals(4, snapshotList.size());
		assertEquals("INSERT", snapshotList.get(0).Action);
		assertEquals("UPDATE", snapshotList.get(1).Action);
		assertEquals("UPDATE", snapshotList.get(2).Action);
		assertEquals("UPDATE", snapshotList.get(3).Action);
	}

	@Test
	public void getHistoryList() throws Exception {
		final String jason = "[{\"Snapshots\":[{\"At\":\"2014-11-27T13:37:53.249724+01:00\",\"Value\":{\"ID\":23040,\"s\":\"\",\"URI\":\"23040\",\"isOdd\":true},\"Action\":\"INSERT\"},{\"At\":\"2014-11-27T13:37:53.408438+01:00\",\"Value\":{\"ID\":23040,\"i\":5,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"},{\"At\":\"2014-11-27T13:37:53.438207+01:00\",\"Value\":{\"ID\":23040,\"i\":7,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"},{\"At\":\"2014-11-27T13:37:53.453703+01:00\",\"Value\":{\"ID\":23040,\"i\":11,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"}]}]";
		final List<History<SimpleRoot>> historyList = JsonStatic.INSTANCE.jsonSerialization.deserializeHistoryList(SimpleRoot.class, jason.getBytes());
		final List<Snapshot<SimpleRoot>> snapshots = historyList.get(0).getSnapshots();
		assertEquals(4, snapshots.size());
		final Snapshot<SimpleRoot> simpleRootSnapshot = snapshots.get(1);
		final String action = simpleRootSnapshot.getAction();
		assertEquals("UPDATE", action);
		final int i = simpleRootSnapshot.getValue().getI();
		assertEquals(5, i);
	}

	static class MockFuture<T> implements Future<T> {
		@Override
		public boolean cancel(boolean mayInterruptIfRunning) { return false; }
		@Override
		public boolean isCancelled() { return false; }
		@Override
		public boolean isDone() { return true; }
		@Override
		public T get() throws InterruptedException, ExecutionException { return null; }
		@Override
		public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			return null;
		}
	}

	static class MockClient extends HttpClient {
		public MockClient(final ServiceLocator locator) {
			super(locator.resolve(Properties.class), null, locator, locator.resolve(Logger.class), null, null);
		}

		public static Object lastContent;

		@Override
		public <TArgument, TResult> Future<List<TResult>> sendCollectionRequest(
				final Class<TResult> manifest,
				final String service,
				final String method,
				final TArgument content,
				final int[] expected) {
			lastContent = content;
			return new MockFuture<List<TResult>>();
		}
	}

	@Test
	public void testPersistArg() throws Exception {
		final Properties props = new Properties();
		props.put("api-url", "http://localhost");
		props.put("package-name", "com.dslplatform.test");
		final Map<Class<?>, Object> components = new HashMap<Class<?>, Object>();
		components.put(HttpClient.class, MockClient.class);
		final ServiceLocator locator = Bootstrap.init(props, components);
		final StandardProxy proxy = locator.resolve(StandardProxy.class);
		final List<SimpleRoot> roots = new ArrayList<SimpleRoot>();
		roots.add(new SimpleRoot());
		final List<Map.Entry<SimpleRoot, SimpleRoot>> pairs = new ArrayList<Map.Entry<SimpleRoot, SimpleRoot>>();
		pairs.add(new AbstractMap.SimpleEntry<SimpleRoot, SimpleRoot>(new SimpleRoot(), new SimpleRoot()));
		final List<String> result = proxy.persist(roots, pairs, null).get();
		assertNull(result);
		final JsonObject persistObj = (JsonObject) MockClient.lastContent;
		assertNotNull(persistObj);
		final JsonWriter jw = new JsonWriter();
		persistObj.serialize(jw, false);
		final String json = jw.toString();
		assertTrue(json.startsWith("{\"RootName\":\"simple.SimpleRoot\",\"ToInsert\":\"[{\\\"URI\\\":\\\""));
		assertTrue(json.contains("\",\"ToUpdate\":\"[{\\\"Key\\\":{\\\"URI\\\":\\\""));
	}
}
