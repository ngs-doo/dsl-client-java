package com.dslplatform.client;

import com.dslplatform.patterns.AggregateRoot;
import com.dslplatform.patterns.History;
import com.dslplatform.patterns.Snapshot;
import com.dslplatform.test.simple.SimpleRoot;
import com.fasterxml.jackson.databind.JavaType;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
}
