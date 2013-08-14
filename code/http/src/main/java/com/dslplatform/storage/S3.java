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

/**
 * Data structure for working with S3 binaries.
 * Instead of storing binaries in the database, S3 can be used
 * to store bucket and key in the database which point to the
 * binary on remote server.
 */
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

    /**
     * Create new instance of S3.
     * Upload must be called before persistence to the database.
     */
    public S3() {
        instanceRepository = null;
    }
    /**
     * Create new instance of S3. Provide custom {@link S3Repository S3 repository}.
     * Upload must be called before persistence to the database.
     *
     * @param repository custom S3 repository
     */
    public S3(S3Repository repository) {
        instanceRepository = repository;
    }
    /**
     * Create new instance of S3 from provided stream.
     * Upload will be called immediately. Stream will be read to check for length.
     *
     * @param stream Input stream which will be sent to the remote server
     */
    public S3(final InputStream stream) throws IOException {
        instanceRepository = null;
        upload(IOUtils.toByteArray(stream));
    }
    /**
     * Create new instance of S3 from provided stream.
     * Upload will be called immediately.
     *
     * @param stream Input stream which will be sent to the remote server
     * @param length size of the stream
     */
    public S3(final InputStream stream, long length) throws IOException {
        instanceRepository = null;
        upload(stream, length);
    }
    /**
     * Create new instance of S3 from provided byte array.
     * Upload will be called immediately.
     *
     * @param bytes Byte array which will be sent to the remote server
     */
    public S3(final byte[] bytes) throws IOException {
        instanceRepository = null;
        upload(bytes);
    }

    private final S3Repository instanceRepository;
    @SuppressWarnings("deprecation")
    private final static S3Repository staticRepository = Bootstrap.getLocator().resolve(S3Repository.class);
    @SuppressWarnings("deprecation")
    private final static String bucketName = Bootstrap.getLocator().resolve(ProjectSettings.class).get("s3-bucket");
    private S3Repository getRepository() {
        return instanceRepository != null ? instanceRepository : staticRepository;
    }

    private String bucket;

    /**
     * Bucket under which data will be saved.
     * By default bucket is defined in the project.ini file under s3-bucket key
     *
     * @return bucket to remote server
     */
    @JsonProperty("Bucket")
    public String getBucket() { return bucket; }

    private String key;

    /**
     * Key for bucket in which the data was saved.
     *
     * @return key in bucket on the remote server
     */
    @JsonProperty("Key")
    public String getKey() { return key; }

    public String getURI() { return bucket + ":" + key; }

    private long length;

    /**
     * Byte length of data.
     *
     * @return number of bytes
     */
    @JsonProperty("Length")
    public long getLength() { return length; }

    private String name;

    /**
     * For convenience, remote data can be assigned a name.
     *
     * @return name associated with the remote data
     */
    public String getName() { return name; }

    /**
     * For convenience, remote data can be assigned a name.
     *
     * @param value name which will be associated with data
     * @return      itself
     */
    public S3 setName(final String value) {
        name = value;
        return this;
    }

    private String mimeType;

    /**
     * For convenience, remote data can be assigned a mime type.
     *
     * @return mime type associated with the remote data
     */
    @JsonProperty("MimeType")
    public String getMimeType() { return mimeType; }

    /**
     * For convenience, remote data can be assigned a mime type.
     *
     * @param value mime type which will be associated with data
     * @return      itself
     */
    public S3 setMimeType(final String value) {
        mimeType = value;
        return this;
    }

    private final HashMap<String, String> metadata = new HashMap<String, String>();

    /**
     * For convenience, various metadata can be associated with the remote data.
     * Metadata is a map of string keys and values
     *
     * @return associated metadata
     */
    @JsonProperty("Metadata")
    public Map<String, String> getMetadata() { return metadata; }

    private byte[] cachedContent;

    /**
     * Get bytes saved on the remote server.
     * Data will be cached, so subsequent request will reuse downloaded bytes.
     *
     * @return             bytes saved on the remote server
     * @throws IOException in case of communication failure
     */
    public byte[] getContent() throws IOException {
        if (cachedContent != null)
            cachedContent = getBytes();
        return cachedContent;
    }

    /**
     * Get stream saved on the remote server.
     * Data will not be cached, so subsequent request will download stream again.
     *
     * @return             stream saved on the remote server
     * @throws IOException in case of communication failure
     */
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

    /**
     * Get bytes saved on the remote server.
     * Data will not be cached, so subsequent request will download bytes again.
     *
     * @return             bytes saved on the remote server
     * @throws IOException in case of communication failure
     */
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

    /**
     * Upload provided stream to remote S3 server.
     * If key is already defined, this stream will overwrite remote stream,
     * otherwise new key will be created.
     *
     * @param stream       upload provided stream
     * @return             key under which data was saved
     * @throws IOException in case of communication error
     */
    public String upload(final ByteArrayInputStream stream) throws IOException {
        return upload(IOUtils.toByteArray(stream));
    }

    /**
     * Upload provided stream to remote S3 server.
     * If key is already defined, this stream will overwrite remote stream,
     * otherwise new key will be created.
     *
     * @param stream       upload provided stream
     * @param length       size of provided stream
     * @return             key under which data was saved
     * @throws IOException in case of communication error
     */
    public String upload(InputStream stream, long length) throws IOException {
        return upload(bucket != null && bucket.length() > 0 ? bucket : bucketName, stream, length);
    }

    /**
     * Upload provided stream to remote S3 server.
     * If key is already defined, this stream will overwrite remote stream,
     * otherwise new key will be created.
     * If key was already defined, bucket name can't be changed.
     *
     * @param bucket       bucket under data will be saved
     * @param stream       upload provided stream
     * @param length       size of provided stream
     * @return             key under which data was saved
     * @throws IOException in case of communication error
     */
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

    /**
     * Upload provided bytes to remote S3 server.
     * If key is already defined, this bytes will overwrite remote bytes,
     * otherwise new key will be created.
     *
     * @param bytes        upload provided bytes
     * @return             key under which data was saved
     * @throws IOException in case of communication error
     */
    public String upload(byte[] bytes) throws IOException {
        return upload(bucket != null && bucket.length() > 0 ? bucket : bucketName, bytes);
    }

    /**
     * Upload provided bytes to remote S3 server.
     * If key is already defined, this bytes will overwrite remote bytes,
     * otherwise new key will be created.
     * If key was already defined, bucket name can't be changed.
     *
     * @param bucket       bucket under data will be saved
     * @param bytes        upload provided bytes
     * @return             key under which data was saved
     * @throws IOException in case of communication error
     */
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

    /**
     * Remote data from the remote S3 server.
     *
     * @throws IOException in case of communication error
     */
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
