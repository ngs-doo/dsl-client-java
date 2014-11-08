package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

public class MapConverter {
	public static void serializeNullable(Map<String, String> value, Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(Map<String, String> value, Writer sw) throws IOException {
		sw.write('{');
		int size = value.size();
		if (size > 0) {
			Iterator<Map.Entry<String, String>> iterator = value.entrySet().iterator();
			Map.Entry<String, String> kv;
			for (int i = 0; i < size; i++) {
				kv = iterator.next();
				StringConverter.serialize(kv.getKey(), sw);
				sw.write(':');
				StringConverter.serializeNullable(kv.getValue(), sw);
				sw.write(',');
			}
			kv = iterator.next();
			StringConverter.serialize(kv.getKey(), sw);
			sw.write(':');
			StringConverter.serializeNullable(kv.getValue(), sw);
		}
		sw.write('}');
	}
}
