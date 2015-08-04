package com.dslplatform.client.json;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/** "Lossless" image conversion which converts everything into a 32bit PNG until a better way emerges. */
public class ImageConverter {
	static final JsonReader.ReadObject<BufferedImage> ImageReader = new JsonReader.ReadObject<BufferedImage>() {
		@Override
		public BufferedImage read(JsonReader reader) throws IOException {
			return deserializeImage(reader);
		}
	};
	static final JsonWriter.WriteObject<Image> ImageWriter = new JsonWriter.WriteObject<Image>() {
		@Override
		public void write(JsonWriter writer, Image value) {
			serialize(value, writer);
		}
	};
	static final JsonWriter.WriteObject<BufferedImage> BufferedImageWriter = new JsonWriter.WriteObject<BufferedImage>() {
		@Override
		public void write(JsonWriter writer, BufferedImage value) {
			serialize(value, writer);
		}
	};

	public static void serialize(final Image value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
			return;
		}

		final BufferedImage image;
		if (value instanceof BufferedImage) {
			image = (BufferedImage) value;
		}
		else {
			image = new BufferedImage(value.getWidth(null), value.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			final Graphics bGr = image.createGraphics();
			bGr.drawImage(value, 0, 0, null);
			bGr.dispose();
		}
		serialize(image, sw);
	}

	public static void serialize(final BufferedImage value, final JsonWriter sw) {
		if (value == null) {
			sw.writeNull();
			return;
		}

		// FIXME: Inefficient, image can be huge. Better to stream via Base64 encoder into sw
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			javax.imageio.ImageIO.write(value, "png", baos);
			BinaryConverter.serialize(baos.toByteArray(), sw);
		}
		catch (final IOException e) {
			throw new RuntimeException("Could not write PNG format (should not happen)");
		}
	}

	public static BufferedImage deserializeImage(final JsonReader reader) throws IOException {
		final byte[] content = BinaryConverter.deserialize(reader);
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
