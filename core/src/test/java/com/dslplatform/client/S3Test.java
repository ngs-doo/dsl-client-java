package com.dslplatform.client;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import com.dslplatform.patterns.ServiceLocator;
import com.dslplatform.storage.S3;
import com.dslplatform.storage.S3Repository;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;

public class S3Test {

	ServiceLocator locator;
	Logger logger;

	@Before
	public void initialize() throws IOException {
		locator = Bootstrap.init(getClass().getResourceAsStream("/projectprops/mocks3project.properties"));
		logger = locator.resolve(Logger.class);
		logger.info("Initialized");
	}

	@Test
	public void testEmptyDependencyInjection() throws IOException{
		try {
			Bootstrap.init(getClass().getResourceAsStream("/projectprops/mockproject.properties"))
					.resolve(S3Repository.class);
			Assert.fail("Method should have thrown a RuntimeException");
		} catch (Throwable ex){
			Assert.assertTrue(ex.getMessage().contains("could not locate"));
		}
	}

	@Test
	public void testDependencyInjection() throws IOException{
		Assert.assertTrue(locator.resolve(S3Repository.class) instanceof AmazonS3Repository);
	}

	@Test
	public void testObjectConstruction() throws IOException{
		String bucket = "test";
		String key    = "somekey" + UUID.randomUUID();
		String mime   = "image/png";
		S3 s3 = new S3(bucket, key).setMimeType(mime);

		Assert.assertEquals(bucket, s3.getBucket());
		Assert.assertEquals(mime, s3.getMimeType());
		Assert.assertEquals(key, s3.getKey());

		Assert.assertNotNull(new S3(key).getBucket());
	}
}
