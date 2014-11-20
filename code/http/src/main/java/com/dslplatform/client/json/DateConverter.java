package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class DateConverter {
	public static final DateTime MIN_DATE_TIME = DateTime.parse("0001-01-01T00:00:00");
	public static final LocalDate MIN_LOCAL_DATE = new LocalDate(1, 1, 1);

	public static void serializeNullable(final DateTime value, final Writer sw) throws IOException {
		if (value == null) 
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(final DateTime value, final Writer sw) throws IOException {
		sw.write('"');
		sw.write(value.toString());
		sw.write('"');
	}

	public static DateTime deserializeDateTime(final JsonReader reader) throws IOException {
		return DateTime.parse(reader.readSimpleString());
	}

	private static JsonReader.ReadObject<DateTime> DateTimeReader = new JsonReader.ReadObject<DateTime>() {
		@Override
		public DateTime read(JsonReader reader) throws IOException {
			return deserializeDateTime(reader);
		}
	};

	public static ArrayList<DateTime> deserializeDateTimeCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(DateTimeReader);
	}

	public static void deserializeDateTimeCollection(final JsonReader reader, final Collection<DateTime> res) throws IOException {
		reader.deserializeCollection(DateTimeReader, res);
	}

	public static ArrayList<DateTime> deserializeDateTimeNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(DateTimeReader);
	}

	public static void deserializeDateTimeNullableCollection(final JsonReader reader, final Collection<DateTime> res) throws IOException {
		reader.deserializeNullableCollection(DateTimeReader, res);
	}

	public static void serializeNullable(final LocalDate value, final Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(final LocalDate value, final Writer sw) throws IOException {
		sw.write('"');
		sw.write(value.toString());
		sw.write('"');
	}

	public static LocalDate deserializeLocalDate(final JsonReader reader) throws IOException {
		String value = reader.readSimpleString();
		//TODO: use year-month-date
		return LocalDate.parse(value);
	}

	private static JsonReader.ReadObject<LocalDate> LocalDateReader = new JsonReader.ReadObject<LocalDate>() {
		@Override
		public LocalDate read(final JsonReader reader) throws IOException {
			return deserializeLocalDate(reader);
		}
	};

	public static ArrayList<LocalDate> deserializeLocalDateCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(LocalDateReader);
	}

	public static void deserializeLocalDateCollection(final JsonReader reader, final Collection<LocalDate> res) throws IOException {
		reader.deserializeCollection(LocalDateReader, res);
	}

	public static ArrayList<LocalDate> deserializeLocalDateNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(LocalDateReader);
	}

	public static void deserializeLocalDateNullableCollection(final JsonReader reader, final Collection<LocalDate> res) throws IOException {
		reader.deserializeNullableCollection(LocalDateReader, res);
	}
}
