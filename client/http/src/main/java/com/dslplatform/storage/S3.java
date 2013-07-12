package com.dslplatform.storage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.IOUtils;

import com.dslplatform.client.Bootstrap;
import com.dslplatform.client.ProjectSettings;
import com.dslplatform.patterns.ServiceLocator;
import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class S3 implements java.io.Serializable {

    @JsonCreator
    protected S3(
            @JacksonInject("_serviceLocator") final ServiceLocator locator,
            @JsonProperty("Bucket") final String bucket,
            @JsonProperty("Key") final String key,
            @JsonProperty("Length") final int length,
            @JsonProperty("Name") final String name,
            @JsonProperty("MimeType") final String mimeType,
            @JsonProperty("Metadata") final Map<String, String> metadata) {
        this.instanceRepository = locator.resolve(S3Repository.class);
        this.bucket = bucket;
        this.key = key;
        this.length = length;
        this.name = name;
        this.mimeType = mimeType;
        if(metadata != null) {
            for(Map.Entry<String, String> kv : metadata.entrySet())
                this.metadata.put(kv.getKey(), kv.getValue());
        }
    }

    public S3() {
        instanceRepository = null;
    }
    public S3(S3Repository repository) {
        instanceRepository = repository;
    }
    public S3(final InputStream stream) throws IOException {
        instanceRepository = null;
        upload(IOUtils.toByteArray(stream));
    }
    public S3(final InputStream stream, long length) throws IOException {
        instanceRepository = null;
        upload(stream, length);
    }
    public S3(final byte[] bytes) throws IOException {
        instanceRepository = null;
        upload(bytes);
    }

    private final S3Repository instanceRepository;
    private final static S3Repository staticRepository = Bootstrap.getLocator().resolve(S3Repository.class);
    private final static String bucketName = Bootstrap.getLocator().resolve(ProjectSettings.class).get("s3-bucket");
    private S3Repository getRepository() {
        return instanceRepository != null ? instanceRepository : staticRepository;
    }

    private String bucket;
    @JsonProperty("Bucket")
    public String getBucket() { return bucket; }

    private String key;
    @JsonProperty("Key")
    public String getKey() { return key; }

    public String getURI() { return bucket + ":" + key; }

    private long length;
    @JsonProperty("Length")
    public long getLength() { return length; }

    private String name;
    public String getName() { return name; }
    public S3 setName(final String value ) {
        name = value;
        return this;
    }

    private String mimeType;
    @JsonProperty("MimeType")
    public String getMimeType() { return mimeType; }
    public S3 setMimeType(final String value) {
        mimeType = value;
        return this;
    }

    private final HashMap<String, String> metadata = new HashMap<String, String>();
    @JsonProperty("Metadata")
    public Map<String, String> getMetadata() { return metadata; }

    private byte[] cachedContent;
    public byte[] getContent() throws IOException {
        if (cachedContent != null)
            cachedContent = getBytes();
        return cachedContent;
    }

    public InputStream getStream() throws IOException {
        if(key == null || key == "")
            return null;
        try{
            return getRepository().get(bucket, key).get();
        } catch (InterruptedException e) {
            throw new IOException(e);
        } catch (ExecutionException e) {
            throw new IOException(e);
        }
    }

    public byte[] getBytes() throws IOException {
        if (key == null || key == "")
            return null;
        final InputStream stream;
        try {
            stream = getRepository().get(bucket, key).get();
        } catch (InterruptedException e) {
            throw new IOException(e);
        } catch (ExecutionException e) {
            throw new IOException(e);
        }
        return IOUtils.toByteArray(stream);
    }

    public String upload(final ByteArrayInputStream stream) throws IOException {
        return upload(IOUtils.toByteArray(stream));
    }

    public String upload(InputStream stream, long length) throws IOException {
        return upload(bucket != null && bucket.length() > 0 ? bucket : bucketName, stream, length);
    }

    public String upload(String bucket, InputStream stream, long length) throws IOException {
        if (stream == null)
            throw new IllegalArgumentException("Stream can't be null.");
        if (key == null || key == "") {
            this.bucket = bucket;
            key = UUID.randomUUID().toString();
        }
        else if (this.bucket != bucket) {
            throw new IllegalArgumentException("Can't change bucket name");
        }
        try{
            getRepository().upload(this.bucket, this.key, stream, length, metadata).get();
        } catch (InterruptedException e) {
            throw new IOException(e);
        } catch (ExecutionException e) {
            throw new IOException(e);
        }
        this.length = length;
        cachedContent = null;
        return this.key;
    }

    public String upload(byte[] bytes) throws IOException {
        return upload(bucket != null && bucket.length() > 0 ? bucket : bucketName, bytes);
    }

    public String upload(final String bucket, final byte[] bytes) throws IOException {
        if (bytes == null)
            throw new IllegalArgumentException("Stream can't be null.");
        if (key == null || key == "") {
            this.bucket = bucket;
            key = UUID.randomUUID().toString();
        }
        else if (this.bucket != bucket) {
            throw new IllegalArgumentException("Can't change bucket name");
        }
        final ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        try{
            getRepository().upload(bucket, this.key, stream, bytes.length, metadata).get();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            throw new IOException(e);
        } catch (ExecutionException e) {
            System.out.println(e.getMessage());
            throw new IOException(e);
        }
        this.length = bytes.length;
        cachedContent = null;
        return this.key;
    }

    public void delete() throws IOException {
        if (key == null || key == "")
            throw new IllegalArgumentException("S3 object is empty.");
        cachedContent = null;
        try {
            getRepository().delete(bucket, key).get();
        } catch (InterruptedException e) {
            throw new IOException(e);
        } catch (ExecutionException e) {
            throw new IOException(e);
        }
        length = 0;
        cachedContent = null;
        key = null;
    }

    private static final long serialVersionUID = 1L;
}
