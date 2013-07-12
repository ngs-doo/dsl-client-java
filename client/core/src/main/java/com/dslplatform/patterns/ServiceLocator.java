package com.dslplatform.patterns;

public interface ServiceLocator {
    public <T> T resolve(final Class<T> clazz);
}
