package com.dslplatform.client.json;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class GeomConverter {

	static JsonReader.ReadObject<Point2D> LocationReader = new JsonReader.ReadObject<Point2D>() {
		@Override
		public Point2D read(JsonReader reader) throws IOException {
			return deserializeLocation(reader);
		}
	};
	static JsonWriter.WriteObject<Point2D> LocationWriter = new JsonWriter.WriteObject<Point2D>() {
		@Override
		public void write(JsonWriter writer, Point2D value) {
			serializeLocationNullable(value, writer);
		}
	};
	static JsonWriter.WriteObject<Point2D.Double> LocationWriterDouble = new JsonWriter.WriteObject<Point2D.Double>() {
		@Override
		public void write(JsonWriter writer, Point2D.Double value) {
			serializeLocationNullable(value, writer);
		}
	};
	static JsonWriter.WriteObject<Point2D.Float> LocationWriterFloat = new JsonWriter.WriteObject<Point2D.Float>() {
		@Override
		public void write(JsonWriter writer, Point2D.Float value) {
			serializeLocationNullable(value, writer);
		}
	};
	static JsonReader.ReadObject<Point> PointReader = new JsonReader.ReadObject<Point>() {
		@Override
		public Point read(JsonReader reader) throws IOException {
			return deserializePoint(reader);
		}
	};
	static JsonWriter.WriteObject<Point> PointWriter = new JsonWriter.WriteObject<Point>() {
		@Override
		public void write(JsonWriter writer, Point value) {
			serializePointNullable(value, writer);
		}
	};
	static JsonReader.ReadObject<Rectangle2D> RectangleReader = new JsonReader.ReadObject<Rectangle2D>() {
		@Override
		public Rectangle2D read(JsonReader reader) throws IOException {
			return deserializeRectangle(reader);
		}
	};
	static JsonWriter.WriteObject<Rectangle2D> RectangleWriter = new JsonWriter.WriteObject<Rectangle2D>() {
		@Override
		public void write(JsonWriter writer, Rectangle2D value) {
			serializeRectangleNullable(value, writer);
		}
	};
	static JsonWriter.WriteObject<Rectangle2D.Double> RectangleWriterDouble = new JsonWriter.WriteObject<Rectangle2D.Double>() {
		@Override
		public void write(JsonWriter writer, Rectangle2D.Double value) {
			serializeRectangleNullable(value, writer);
		}
	};
	static JsonWriter.WriteObject<Rectangle2D.Float> RectangleWriterFloat = new JsonWriter.WriteObject<Rectangle2D.Float>() {
		@Override
		public void write(JsonWriter writer, Rectangle2D.Float value) {
			serializeRectangleNullable(value, writer);
		}
	};
	static JsonReader.ReadObject<BufferedImage> ImageReader = new JsonReader.ReadObject<BufferedImage>() {
		@Override
		public BufferedImage read(JsonReader reader) throws IOException {
			return deserializeImage(reader);
		}
	};
	static JsonWriter.WriteObject<Image> ImageWriter = new JsonWriter.WriteObject<Image>() {
		@Override
		public void write(JsonWriter writer, Image value) {
			serialize(value, writer);
		}
	};
	static JsonWriter.WriteObject<BufferedImage> BufferedImageWriter = new JsonWriter.WriteObject<BufferedImage>() {
		@Override
		public void write(JsonWriter writer, BufferedImage value) {
			serialize(value, writer);
		}
	};

	public static void serializeLocationNullable(final Point2D value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			serializeLocation(value, sw);
		}
	}

	public static void serializeLocation(final Point2D value, final JsonWriter sw) {
		sw.writeAscii("{\"X\":");
		NumberConverter.serialize(value.getX(), sw);
		sw.writeAscii(",\"Y\":");
		NumberConverter.serialize(value.getY(), sw);
		sw.writeByte(JsonWriter.OBJECT_END);
	}

	public static Point2D deserializeLocation(final JsonReader reader) throws IOException {
		if (reader.last() != '{') throw new IOException("Expecting '{' at position " + reader.positionInStream() + ". Found " + (char)reader.last());
		byte nextToken = reader.getNextToken();
		if (nextToken == '}') return new Point2D.Double();
		double x = 0;
		double y = 0;
		String name = StringConverter.deserialize(reader);
		nextToken = reader.getNextToken();
		if (nextToken != ':') throw new IOException("Expecting ':' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
		reader.getNextToken();
		double value = NumberConverter.deserializeDouble(reader);
		if ("X".equalsIgnoreCase(name)) {
			x = value;
		} else if ("Y".equalsIgnoreCase(name)) {
			y = value;
		}
		while ((nextToken = reader.getNextToken()) == ',') {
			reader.getNextToken();
			name = StringConverter.deserialize(reader);
			nextToken = reader.getNextToken();
			if (nextToken != ':') throw new IOException("Expecting ':' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
			reader.getNextToken();
			value = NumberConverter.deserializeDouble(reader);
			if ("X".equalsIgnoreCase(name)) {
				x = value;
			} else if ("Y".equalsIgnoreCase(name)) {
				y = value;
			}
		}
		if (nextToken != '}') throw new IOException("Expecting '}' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
		return new Point2D.Double(x, y);
	}

	public static ArrayList<Point2D> deserializeLocationCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(LocationReader);
	}

	public static void deserializeLocationCollection(final JsonReader reader, final Collection<Point2D> res) throws IOException {
		reader.deserializeCollection(LocationReader, res);
	}

	public static ArrayList<Point2D> deserializeLocationNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(LocationReader);
	}

	public static void deserializeLocationNullableCollection(final JsonReader reader, final Collection<Point2D> res) throws IOException {
		reader.deserializeNullableCollection(LocationReader, res);
	}

	public static void serializePointNullable(final Point value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			serializePoint(value, sw);
		}
	}

	public static void serializePoint(final Point value, final JsonWriter sw) {
		sw.writeAscii("{\"X\":");
		NumberConverter.serialize(value.x, sw);
		sw.writeAscii(",\"Y\":");
		NumberConverter.serialize(value.y, sw);
		sw.writeByte(JsonWriter.OBJECT_END);
	}

	public static Point deserializePoint(final JsonReader reader) throws IOException {
		if (reader.last() != '{') throw new IOException("Expecting '{' at position " + reader.positionInStream() + ". Found " + (char)reader.last());
		byte nextToken = reader.getNextToken();
		if (nextToken == '}') return new Point();
		int x = 0;
		int y = 0;
		String name = StringConverter.deserialize(reader);
		nextToken = reader.getNextToken();
		if (nextToken != ':') throw new IOException("Expecting ':' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
		reader.getNextToken();
		int value = NumberConverter.deserializeInt(reader);
		if ("X".equalsIgnoreCase(name)) {
			x = value;
		} else if ("Y".equalsIgnoreCase(name)) {
			y = value;
		}
		while ((nextToken = reader.getNextToken()) == ',') {
			reader.getNextToken();
			name = StringConverter.deserialize(reader);
			nextToken = reader.getNextToken();
			if (nextToken != ':') throw new IOException("Expecting ':' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
			reader.getNextToken();
			value = NumberConverter.deserializeInt(reader);
			if ("X".equalsIgnoreCase(name)) {
				x = value;
			} else if ("Y".equalsIgnoreCase(name)) {
				y = value;
			}
		}
		if (nextToken != '}') throw new IOException("Expecting '}' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
		return new Point(x, y);

	}

	public static ArrayList<Point> deserializePointCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(PointReader);
	}

	public static void deserializePointCollection(final JsonReader reader, final Collection<Point> res) throws IOException {
		reader.deserializeCollection(PointReader, res);
	}

	public static ArrayList<Point> deserializePointNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(PointReader);
	}

	public static void deserializePointNullableCollection(final JsonReader reader, final Collection<Point> res) throws IOException {
		reader.deserializeNullableCollection(PointReader, res);
	}

	public static void serializeRectangleNullable(final Rectangle2D value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else {
			serializeRectangle(value, sw);
		}
	}

	public static void serializeRectangle(final Rectangle2D value, final JsonWriter sw) {
		sw.writeAscii("{\"X\":");
		NumberConverter.serialize(value.getX(), sw);
		sw.writeAscii(",\"Y\":");
		NumberConverter.serialize(value.getY(), sw);
		sw.writeAscii(",\"Width\":");
		NumberConverter.serialize(value.getWidth(), sw);
		sw.writeAscii(",\"Height\":");
		NumberConverter.serialize(value.getHeight(), sw);
		sw.writeByte(JsonWriter.OBJECT_END);
	}

	public static Rectangle2D deserializeRectangle(final JsonReader reader) throws IOException {
		if (reader.last() != '{') throw new IOException("Expecting '{' at position " + reader.positionInStream() + ". Found " + (char)reader.last());
		byte nextToken = reader.getNextToken();
		if (nextToken == '}') return new Rectangle2D.Double();
		double x = 0;
		double y = 0;
		double width = 0;
		double height = 0;
		String name = StringConverter.deserialize(reader);
		nextToken = reader.getNextToken();
		if (nextToken != ':') throw new IOException("Expecting ':' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
		reader.getNextToken();
		double value = NumberConverter.deserializeDouble(reader);
		if ("X".equalsIgnoreCase(name)) {
			x = value;
		} else if ("Y".equalsIgnoreCase(name)) {
			y = value;
		} else if ("Width".equalsIgnoreCase(name)) {
			width = value;
		} else if ("Height".equalsIgnoreCase(name)) {
			height = value;
		}
		while ((nextToken = reader.getNextToken()) == ',') {
			reader.getNextToken();
			name = StringConverter.deserialize(reader);
			nextToken = reader.getNextToken();
			if (nextToken != ':') throw new IOException("Expecting ':' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
			reader.getNextToken();
			value = NumberConverter.deserializeDouble(reader);
			if ("X".equalsIgnoreCase(name)) {
				x = value;
			} else if ("Y".equalsIgnoreCase(name)) {
				y = value;
			} else if ("Width".equalsIgnoreCase(name)) {
				width = value;
			} else if ("Height".equalsIgnoreCase(name)) {
				height = value;
			}
		}
		if (nextToken != '}') throw new IOException("Expecting '}' at position " + reader.positionInStream() + ". Found " + (char)nextToken);
		return new Rectangle2D.Double(x, y, width, height);
	}

	public static ArrayList<Rectangle2D> deserializeRectangleCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(RectangleReader);
	}

	public static void deserializeRectangleCollection(final JsonReader reader, final Collection<Rectangle2D> res) throws IOException {
		reader.deserializeCollection(RectangleReader, res);
	}

	public static ArrayList<Rectangle2D> deserializeRectangleNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(RectangleReader);
	}

	public static void deserializeRectangleNullableCollection(final JsonReader reader, final Collection<Rectangle2D> res) throws IOException {
		reader.deserializeNullableCollection(RectangleReader, res);
	}

	public static void serialize(final Image value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
		} else if (value instanceof BufferedImage) {
			final WritableRaster raster = ((BufferedImage)value).getRaster();
			final DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
			BinaryConverter.serialize(data.getData(), sw);
		} else {
			final BufferedImage image = new BufferedImage(value.getWidth(null), value.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			final Graphics2D bGr = image.createGraphics();
			bGr.drawImage(value, 0, 0, null);
			bGr.dispose();
			final WritableRaster raster = image.getRaster();
			final DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
			BinaryConverter.serialize(data.getData(), sw);
		}
	}

	public static BufferedImage deserializeImage(final JsonReader reader) throws IOException {
		final byte[] content = com.dslplatform.client.json.BinaryConverter.deserialize(reader);
		return javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(content));
	}

	public static ArrayList<BufferedImage> deserializeImageCollection(final JsonReader reader) throws IOException {
		return reader.deserializeCollection(ImageReader);
	}

	public static void deserializeImageCollection(final JsonReader reader, final Collection<BufferedImage> res) throws IOException {
		reader.deserializeCollection(ImageReader, res);
	}

	public static ArrayList<BufferedImage> deserializeImageNullableCollection(final JsonReader reader) throws IOException {
		return reader.deserializeNullableCollection(ImageReader);
	}

	public static void deserializeImageNullableCollection(final JsonReader reader, final Collection<BufferedImage> res) throws IOException {
		reader.deserializeNullableCollection(ImageReader, res);
	}

}
