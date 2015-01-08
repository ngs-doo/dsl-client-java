package com.dslplatform.client.json;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class GeomConverter {
	public static void serializeLocationNullable(final Point2D value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			serializeLocation(value, sw);
		}
	}

	private static final char[] X_PARAM = new char[] { '{', '"', 'X', '"', ':' };
	private static final char[] Y_PARAM = new char[] { ',', '"', 'Y', '"', ':' };
	private static final char[] END = new char[] { '}', '"' };

	public static void serializeLocation(final Point2D value, final JsonWriter sw) {
		sw.writeAscii(X_PARAM, 0, 5);
		NumberConverter.serialize(value.getX(), sw);
		sw.writeAscii(Y_PARAM, 0, 5);
		NumberConverter.serialize(value.getY(), sw);
		sw.writeAscii(END, 0, 2);
	}

	public static Point2D deserializeLocation(final JsonReader reader) throws IOException {
		return null;
	}

	private static JsonReader.ReadObject<Point2D> LocationReader = new JsonReader.ReadObject<Point2D>() {
		@Override
		public Point2D read(JsonReader reader) throws IOException {
			return deserializeLocation(reader);
		}
	};

	public static ArrayList<Point2D> deserializeLocationCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithGet(LocationReader);
	}

	public static void deserializeLocationCollection(final JsonReader reader, final Collection<Point2D> res) throws IOException {
		reader.deserializeCollectionWithGet(LocationReader, res);
	}

	public static ArrayList<Point2D> deserializeLocationNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithGet(LocationReader);
	}

	public static void deserializeLocationNullableCollection(final JsonReader reader, final Collection<Point2D> res) throws IOException {
		reader.deserializeNullableCollectionWithGet(LocationReader, res);
	}

	public static void serializePointNullable(final Point value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			serializePoint(value, sw);
		}
	}

	public static void serializePoint(final Point value, final JsonWriter sw) {
		sw.writeAscii(X_PARAM, 0, 5);
		NumberConverter.serialize(value.x, sw);
		sw.writeAscii(Y_PARAM, 0, 5);
		NumberConverter.serialize(value.y, sw);
		sw.writeAscii(END, 0, 2);
	}

	public static Point deserializePoint(final JsonReader reader) throws IOException {
		return null;
	}

	private static JsonReader.ReadObject<Point> PointReader = new JsonReader.ReadObject<Point>() {
		@Override
		public Point read(JsonReader reader) throws IOException {
			return deserializePoint(reader);
		}
	};

	public static ArrayList<Point> deserializePointCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithGet(PointReader);
	}

	public static void deserializePointCollection(final JsonReader reader, final Collection<Point> res) throws IOException {
		reader.deserializeCollectionWithGet(PointReader, res);
	}

	public static ArrayList<Point> deserializePointNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithGet(PointReader);
	}

	public static void deserializePointNullableCollection(final JsonReader reader, final Collection<Point> res) throws IOException {
		reader.deserializeNullableCollectionWithGet(PointReader, res);
	}

	public static void serializeRectangleNullable(final Rectangle2D value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			serializeRectangle(value, sw);
		}
	}

	public static void serializeRectangle(final Rectangle2D value, final JsonWriter sw) {
		sw.writeAscii(X_PARAM, 0, 5);
		NumberConverter.serialize(value.getX(), sw);
		sw.writeAscii(Y_PARAM, 0, 5);
		NumberConverter.serialize(value.getY(), sw);
		sw.writeAscii(",\"Width\":");
		sw.writeAscii(Double.toString(value.getWidth()));
		sw.writeAscii(",\"Height\":");
		sw.writeAscii(Double.toString(value.getHeight()));
		sw.writeByte(JsonWriter.OBJECT_END);
	}

	public static Rectangle2D deserializeRectangle(final JsonReader reader) throws IOException {
		return null;
	}

	private static JsonReader.ReadObject<Rectangle2D> RectangleReader = new JsonReader.ReadObject<Rectangle2D>() {
		@Override
		public Rectangle2D read(JsonReader reader) throws IOException {
			return deserializeRectangle(reader);
		}
	};

	public static ArrayList<Rectangle2D> deserializeRectangleCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollectionWithGet(RectangleReader);
	}

	public static void deserializeRectangleCollection(final JsonReader reader, final Collection<Rectangle2D> res) throws IOException {
		reader.deserializeCollectionWithGet(RectangleReader, res);
	}

	public static ArrayList<Rectangle2D> deserializeRectangleNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollectionWithGet(RectangleReader);
	}

	public static void deserializeRectangleNullableCollection(final JsonReader reader, final Collection<Rectangle2D> res) throws IOException {
		reader.deserializeNullableCollectionWithGet(RectangleReader, res);
	}
}
