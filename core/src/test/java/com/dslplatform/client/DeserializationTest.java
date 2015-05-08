package com.dslplatform.client;

import com.dslplatform.client.json.JacksonJsonSerialization;
import com.dslplatform.client.json.JsonObject;
import com.dslplatform.client.json.JsonWriter;
import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.History;
import com.dslplatform.patterns.ServiceLocator;
import com.dslplatform.patterns.Snapshot;
import com.dslplatform.test.simple.SimpleRoot;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
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
		final TypeFactory typeFactory = new ObjectMapper().getTypeFactory();
		final JavaType st = typeFactory.constructCollectionType(
				ArrayList.class,
				typeFactory.constructParametricType(SnapshotDelegate.class, SimpleRoot.class));
		final List<SnapshotDelegate<SimpleRoot>> snapshotList = JacksonJsonSerialization.deserialize(st, jason, null);
		assertEquals(4, snapshotList.size());
		assertEquals("INSERT", snapshotList.get(0).Action);
		assertEquals("UPDATE", snapshotList.get(1).Action);
		assertEquals("UPDATE", snapshotList.get(2).Action);
		assertEquals("UPDATE", snapshotList.get(3).Action);
	}

	@Test
	public void getHistoryList() throws Exception {
		final String jason = "[{\"Snapshots\":[{\"At\":\"2014-10-27T13:37:53.249724+01:00\",\"Value\":{\"ID\":23040,\"s\":\"\",\"URI\":\"23040\",\"isOdd\":true},\"Action\":\"INSERT\"},{\"At\":\"2014-11-27T13:37:53.408438+01:00\",\"Value\":{\"ID\":23040,\"i\":5,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"},{\"At\":\"2014-11-27T13:37:53.438207+01:00\",\"Value\":{\"ID\":23040,\"i\":7,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"},{\"At\":\"2014-11-27T13:37:53.453703+01:00\",\"Value\":{\"ID\":23040,\"i\":11,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"}]},{\"Snapshots\":[{\"At\":\"2014-11-27T13:37:53.249724+01:00\",\"Value\":{\"ID\":23040,\"s\":\"\",\"URI\":\"23040\",\"isOdd\":true},\"Action\":\"INSERT\"},{\"At\":\"2014-11-27T13:37:53.408438+01:00\",\"Value\":{\"ID\":23040,\"i\":5,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"},{\"At\":\"2014-11-27T13:37:53.438207+01:00\",\"Value\":{\"ID\":23040,\"i\":7,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"},{\"At\":\"2014-11-27T13:37:53.453703+01:00\",\"Value\":{\"ID\":23040,\"i\":11,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"}]}]";
		final List<History<SimpleRoot>> historyList1 = JsonStatic.INSTANCE.jackson.deserializeHistoryList(SimpleRoot.class, jason.getBytes(), jason.length());
		final List<History<SimpleRoot>> historyList2 = JsonStatic.INSTANCE.manual.deserializeHistoryList(SimpleRoot.class, jason.getBytes(), jason.length());
		assertEquals(2, historyList1.size());
		assertEquals(2, historyList2.size());
		final List<Snapshot<SimpleRoot>> snapshots1 = historyList1.get(0).getSnapshots();
		final List<Snapshot<SimpleRoot>> snapshots2 = historyList2.get(0).getSnapshots();
		assertEquals(4, snapshots1.size());
		assertEquals(4, snapshots2.size());
		final Snapshot<SimpleRoot> simpleRootSnapshot1 = snapshots1.get(1);
		final Snapshot<SimpleRoot> simpleRootSnapshot2 = snapshots2.get(1);
		assertEquals("UPDATE", simpleRootSnapshot1.getAction());
		assertEquals("UPDATE", simpleRootSnapshot2.getAction());
		assertEquals(5, simpleRootSnapshot1.getValue().getI());
		assertEquals(simpleRootSnapshot1.getValue(), simpleRootSnapshot2.getValue());
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
			super(locator.resolve(Properties.class), JsonStatic.INSTANCE.jackson, locator.resolve(Logger.class), null, null);
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
