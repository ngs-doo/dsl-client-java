package com.dslplatform.client.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DateConverter {
	public static final DateTime MIN_DATE_TIME = DateTime.parse("0001-01-01T00:00:00Z");
	public static final LocalDate MIN_LOCAL_DATE = new LocalDate(1, 1, 1);
	private static final DateTimeFormatter dateTimeFormat = ISODateTimeFormat.dateTime();
	private static final DateTimeFormatter localDateFormat = ISODateTimeFormat.date();
	private static final DateTimeFormatter localDateParser = ISODateTimeFormat.localDateParser();
	private static final DateTimeZone utcZone = DateTimeZone.UTC;

	public static void serializeNullable(final DateTime value, final Writer sw) throws IOException {
		if (value == null) {
			sw.write("null");
		} else {
			serialize(value, sw);
		}
	}

	public static void serialize(final DateTime value, final Writer sw) throws IOException {
		if (sw instanceof JsonWriter) {
			serialize(value, (JsonWriter) sw);
		} else {
			sw.write('"');
			dateTimeFormat.printTo(sw, value);
			sw.write('"');
		}
	}

	public static void serialize(final DateTime value, final JsonWriter sw) throws IOException {
		final char[] buf = sw.tmp;
		buf[0] = '"';
		final int year = value.getYear();
		NumberConverter.write2(year / 100, buf, 1);
		NumberConverter.write2(year, buf, 3);
		buf[5] = '-';
		NumberConverter.write2(value.getMonthOfYear(), buf, 6);
		buf[8] = '-';
		NumberConverter.write2(value.getDayOfMonth(), buf, 9);
		buf[11] = 'T';
		NumberConverter.write2(value.getHourOfDay(), buf, 12);
		buf[14] = ':';
		NumberConverter.write2(value.getMinuteOfHour(), buf, 15);
		buf[17] = ':';
		NumberConverter.write2(value.getSecondOfMinute(), buf, 18);
		final int milis = value.getMillisOfSecond();
		if (milis != 0) {
			buf[20] = '.';
			final int hi = milis / 100;
			final int lo = milis - hi * 100;
			buf[21] = (char) (hi + 48);
			if (lo != 0) {
				NumberConverter.write2(lo, buf, 22);
				writeTimezone(buf, 24, value, sw);
			} else {
				writeTimezone(buf, 22, value, sw);
			}
		} else {
			writeTimezone(buf, 20, value, sw);
		}
	}

	private static void writeTimezone(final char[] buf, final int position, final DateTime dt, final JsonWriter sw) throws IOException {
		DateTimeZone zone = dt.getZone();
		if (utcZone.equals(zone) || zone == null) {
			buf[position] = 'Z';
			buf[position + 1] = '"';
			sw.write(buf, 0, position + 2);
		} else {
			final long ms = dt.getMillis();
			final int off = zone.getOffset(ms);
			final int hours = off / 3600000;
			final int remainder = off - hours * 3600000;
			buf[position] = off < 0 ? '-' : '+';
			NumberConverter.write2(hours, buf, position + 1);
			buf[position + 3] = ':';
			NumberConverter.write2(remainder / 60000, buf, position + 4);
			buf[position + 6] = '"';
			sw.write(buf, 0, position + 7);
		}
	}

	public static DateTime deserializeDateTime(final JsonReader reader) throws IOException {
		char[] tmp = reader.readSimpleQuote();
		int len = reader.getCurrentIndex() - reader.getTokenStart() - 1;
		if (len > 18 && len < 25 && tmp[len - 1] == 'Z' && tmp[4] == '-' && tmp[7] == '-'
				&& (tmp[10] == 'T' || tmp[10] == 't' || tmp[10] == ' ')
				&& tmp[13] == ':' && tmp[16] == ':') {
			int year = NumberConverter.read4(tmp, 0);
			int month = NumberConverter.read2(tmp, 5);
			int day = NumberConverter.read2(tmp, 8);
			int hour = NumberConverter.read2(tmp, 11);
			int min = NumberConverter.read2(tmp, 14);
			int sec = NumberConverter.read2(tmp, 17);
			if (tmp[19] == '.') {
				int milis = NumberConverter.read(tmp, 20, len - 1);
				return new DateTime(year, month, day, hour, min, sec, milis, utcZone);
			}
			return new DateTime(year, month, day, hour, min, sec, 0, utcZone);
		} else {
			return DateTime.parse(new String(tmp, 0, len));
		}
	}

	private static JsonReader.ReadObject<DateTime> DateTimeReader = new JsonReader.ReadObject<DateTime>() {
		@Override
		public DateTime read(JsonReader reader) throws IOException {
			return deserializeDateTime(reader);
		}
	};

	public static ArrayList<DateTime> deserializeDateTimeCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithGet(DateTimeReader);
	}

	public static void deserializeDateTimeCollection(final JsonReader reader, final Collection<DateTime> res) throws IOException {
		reader.deserializeCollectionWithGet(DateTimeReader, res);
	}

	public static ArrayList<DateTime> deserializeDateTimeNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithGet(DateTimeReader);
	}

	public static void deserializeDateTimeNullableCollection(final JsonReader reader, final Collection<DateTime> res) throws IOException {
		reader.deserializeNullableCollectionWithGet(DateTimeReader, res);
	}

	public static void serializeNullable(final LocalDate value, final Writer sw) throws IOException {
		if (value == null)
			sw.write("null");
		else
			serialize(value, sw);
	}

	public static void serialize(final LocalDate value, final Writer sw) throws IOException {
		if (sw instanceof JsonWriter) {
			serialize(value, (JsonWriter) sw);
		} else {
			sw.write('"');
			localDateFormat.printTo(sw, value);
			sw.write('"');
		}
	}

	public static void serialize(final LocalDate value, final JsonWriter sw) throws IOException {
		final char[] buf = sw.tmp;
		buf[0] = '"';
		final int year = value.getYear();
		NumberConverter.write2(year / 100, buf, 1);
		NumberConverter.write2(year, buf, 3);
		buf[5] = '-';
		NumberConverter.write2(value.getMonthOfYear(), buf, 6);
		buf[8] = '-';
		NumberConverter.write2(value.getDayOfMonth(), buf, 9);
		buf[11] = '"';
		sw.write(buf, 0, 12);
	}

	public static LocalDate deserializeLocalDate(final JsonReader reader) throws IOException {
		char[] tmp = reader.readSimpleQuote();
		int len = reader.getCurrentIndex() - reader.getTokenStart() - 1;
		if (len == 10 && tmp[4] == '-' && tmp[7] == '-') {
			int year = NumberConverter.read4(tmp, 0);
			int month = NumberConverter.read2(tmp, 5);
			int day = NumberConverter.read2(tmp, 8);
			return new LocalDate(year, month, day);
		} else {
			return localDateParser.parseLocalDate(new String(tmp, 0, len));
		}
	}

	private static JsonReader.ReadObject<LocalDate> LocalDateReader = new JsonReader.ReadObject<LocalDate>() {
		@Override
		public LocalDate read(final JsonReader reader) throws IOException {
			return deserializeLocalDate(reader);
		}
	};

	public static ArrayList<LocalDate> deserializeLocalDateCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithGet(LocalDateReader);
	}

	public static void deserializeLocalDateCollection(final JsonReader reader, final Collection<LocalDate> res) throws IOException {
		reader.deserializeCollectionWithGet(LocalDateReader, res);
	}

	public static ArrayList<LocalDate> deserializeLocalDateNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithGet(LocalDateReader);
	}

	public static void deserializeLocalDateNullableCollection(final JsonReader reader, final Collection<LocalDate> res) throws IOException {
		reader.deserializeNullableCollectionWithGet(LocalDateReader, res);
	}
}