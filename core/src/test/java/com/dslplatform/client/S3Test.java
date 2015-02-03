package com.dslplatform.client;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;

import com.dslplatform.patterns.ServiceLocator;
import com.dslplatform.storage.S3;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.*;
import org.slf4j.Logger;

public class S3Test {

	ServiceLocator locator;
	Logger logger;

	@Before
	public void initialize() throws IOException {
		locator = Bootstrap.init(getClass().getResourceAsStream("/projectprops/mockproject.properties"));
		logger = locator.resolve(Logger.class);
		logger.info("Initialized");
}

@Test
public void testVersioning() throws IOException {
	logger.info("Files were previously saved with external library. Everything works if 1st picture shows apple, and 2nd example.com screenshot!");

	S3 s3Object1 = new S3("test/7dc1f149-85ad-48ff-ae9f-86420b2355a0/screenshot/example", "Wlzd5cUuY3QwWBko_zvvYP7k2Fxz.Dwj").setMimeType("image/png");

	byte[] content1 = streamToByteArray(s3Object1.getStream());

	dumpToDisk(content1);

	S3 s3Object2 = new S3("test/7dc1f149-85ad-48ff-ae9f-86420b2355a0/screenshot/example", "mK6faa2b28OjMCru4aod3h.LJ2gnAPMS").setMimeType("image/png");

	byte[] content2 = streamToByteArray(s3Object2.getStream());

	dumpToDisk(content2);

	Assert.assertThat(content1, IsNot.not(IsEqual.equalTo(content2)));
}

private void dumpToDisk(byte[] content) {
	try{
		String path = "/tmp/" + java.util.UUID.randomUUID()+".png";
		logger.info("Dumping content to disk : {}", path);
		FileOutputStream fos = new FileOutputStream(new File(path));
		fos.write(content);
		fos.close();
	} catch (Exception e){

	}
}

private static byte[] streamToByteArray(final InputStream inputStream)
		throws IOException {
	final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	final byte[] buffer = new byte[4096];

	while (true) {
		final int read = inputStream.read(buffer);
		if (read == -1)
			break;
		baos.write(buffer, 0, read);
	}

	return baos.toByteArray();
}
}
