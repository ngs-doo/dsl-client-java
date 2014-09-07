package com.dslplatform.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.dslplatform.storage.S3Repository;

class AmazonS3Repository implements S3Repository {
    private final String s3AccessKey;
    private final String s3SecretKey;
    private final ExecutorService executorService;

    private AmazonS3Client s3Client;

    private AmazonS3Client getS3Client() throws IOException {
        if (s3Client == null) {
            if (s3AccessKey == null || s3AccessKey.isEmpty())
                throw new IOException("S3 configuration is missing. Please add s3-user");
            if (s3SecretKey == null || s3SecretKey.isEmpty())
                throw new IOException("S3 configuration is missing. Please add s3-secret");
            s3Client = new AmazonS3Client(new BasicAWSCredentials(s3AccessKey, s3SecretKey));
        }
        return s3Client;
    }

    public AmazonS3Repository(final ProjectSettings settings, final ExecutorService executorService) {
        s3AccessKey = settings.get("s3-user");
        s3SecretKey = settings.get("s3-secret");
        this.executorService = executorService;
    }

    private void checkBucket(final String name) throws IOException {
        if (name == null || name.isEmpty())
            throw new IOException(
                    "Bucket not specified. If you wish to use default bucket name, add it as s3-bucket to dsl-project.properties");
    }

    @Override
    public Future<InputStream> get(final String bucket, final String key) {
        return executorService.submit(new Callable<InputStream>() {
            @Override
            public InputStream call() throws IOException {
                final S3Object s3 = getS3Client().getObject(new GetObjectRequest(bucket, key));
                return s3.getObjectContent();
            }
        });
    }

    @Override
    public Future<?> upload(
            final String bucket,
            final String key,
            final InputStream stream,
            final long length,
            final Map<String, String> metadata) {
        return executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws IOException {
                checkBucket(bucket);
                final ObjectMetadata om = new ObjectMetadata();
                om.setContentLength(length);
                if (metadata != null) {
                    for (final Map.Entry<String, String> kv : metadata.entrySet()) {
                        om.addUserMetadata(kv.getKey(), kv.getValue());
                    }
                }
                getS3Client().putObject(new PutObjectRequest(bucket, key, stream, om));
                return null;
            }
        });
    }

    @Override
    public Future<?> delete(final String bucket, final String key) {
        return executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws IOException {
                getS3Client().deleteObject(new DeleteObjectRequest(bucket, key));
                return null;
            }
        });
    }
}
