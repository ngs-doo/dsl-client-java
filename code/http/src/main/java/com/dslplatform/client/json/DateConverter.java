package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class DateConverter {
	public static void serializeNullable(DateTime value, Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(DateTime value, Writer sw) throws IOException {
		sw.write('"');
		sw.write(value.toString());
		sw.write('"');
	}

	public static void serializeNullable(LocalDate value, Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(LocalDate value, Writer sw) throws IOException {
		sw.write('"');
		sw.write(value.toString());
		sw.write('"');
	}
}
