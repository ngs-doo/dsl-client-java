package com.dslplatform.client;

import com.dslplatform.patterns.History;
import com.dslplatform.patterns.Snapshot;
import com.dslplatform.test.simple.AnotherSimpleRoot;
import com.dslplatform.test.simple.SimpleRoot;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class DeserializationTest {

	@Test
	public void getSnapshots() throws Exception {
		final String jason = "[{\"At\":\"2014-11-27T13:37:53.249724+01:00\",\"Value\":{\"ID\":23040,\"s\":\"\",\"URI\":\"23040\",\"isOdd\":true},\"Action\":\"INSERT\"},{\"At\":\"2014-11-27T13:37:53.408438+01:00\",\"Value\":{\"ID\":23040,\"i\":5,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"},{\"At\":\"2014-11-27T13:37:53.438207+01:00\",\"Value\":{\"ID\":23040,\"i\":7,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"},{\"At\":\"2014-11-27T13:37:53.453703+01:00\",\"Value\":{\"ID\":23040,\"i\":11,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"}]";
		final JavaType ht =
				JsonSerialization.buildCollectionType(
						ArrayList.class,
						JsonSerialization.buildGenericType(Snapshot.class, SimpleRoot.class));
		//final List<History<SimpleRoot>> historyList = (new JsonSerialization(null)).deserializeHistoryList(SimpleRoot.class, jason.getBytes());
		final List<Snapshot<SimpleRoot>> snapshotList = (new JsonSerialization(null)).deserialize(ht, jason.getBytes());
		assertTrue(snapshotList.size() == 4);
		final Snapshot<SimpleRoot> simpleRootSnapshot= snapshotList.get(0);
		final String action = simpleRootSnapshot.getAction();
		assertTrue(action.equals("UPDATE"));
	}

	@Test
	public void getHistoryList() throws Exception {

		final String jason = "[{\"Snapshots\":[{\"At\":\"2014-11-27T13:37:53.249724+01:00\",\"Value\":{\"ID\":23040,\"s\":\"\",\"URI\":\"23040\",\"isOdd\":true},\"Action\":\"INSERT\"},{\"At\":\"2014-11-27T13:37:53.408438+01:00\",\"Value\":{\"ID\":23040,\"i\":5,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"},{\"At\":\"2014-11-27T13:37:53.438207+01:00\",\"Value\":{\"ID\":23040,\"i\":7,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"},{\"At\":\"2014-11-27T13:37:53.453703+01:00\",\"Value\":{\"ID\":23040,\"i\":11,\"s\":\"\",\"URI\":\"23040\"},\"Action\":\"UPDATE\"}]}]";
		final JavaType ht =
				JsonSerialization.buildCollectionType(
						ArrayList.class,
						JsonSerialization.buildGenericType(History.class, SimpleRoot.class));
		final List<History<SimpleRoot>> historyList = (new JsonSerialization(null)).deserializeHistoryList(SimpleRoot.class, jason.getBytes());
		final List<Snapshot<SimpleRoot>> snapshots = historyList.get(0).getSnapshots();
		assertTrue(snapshots.size() == 4);
		final Snapshot<SimpleRoot> simpleRootSnapshot = snapshots.get(1);
		final String action = simpleRootSnapshot.getAction();
		assertTrue(action.equals("UPDATE"));
		final int i = simpleRootSnapshot.getValue().getI();
		assertTrue(i == 5);
	}

}
