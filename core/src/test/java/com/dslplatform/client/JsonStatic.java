package com.dslplatform.client;

import com.dslplatform.patterns.History;
import com.dslplatform.patterns.Snapshot;
import com.dslplatform.test.simple.SimpleRoot;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public enum JsonStatic {
	INSTANCE;

	public final JsonSerialization jsonSerialization = new JsonSerialization(null);
}
