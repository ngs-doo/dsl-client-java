package com.dslplatform.client;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.dslplatform.patterns.ServiceLocator;

class MapServiceLocator implements ServiceLocator {
    private final Map<Class<?>, Object> components = new LinkedHashMap<Class<?>, Object>();
    private final static boolean cacheResult = true;

    MapServiceLocator() {
        components.put(ServiceLocator.class, this);
    }

    MapServiceLocator(
            final Map<Class<?>, Object> initialComponents) {
        components.put(ServiceLocator.class, this);
        components.putAll(initialComponents);
    }

    boolean contains(final Class<?> clazz) {
        return components.containsKey(clazz);
    }

    @Override
    public <T> T resolve(final Class<T> clazz) {
        final Object component = resolve(clazz, true);
        return clazz.cast(component);
    }

    private void cacheIf(final Class<?> clazz, final Object service) {
        if (cacheResult && service != null) {
            register(clazz, service);
        }
    }

    private Object resolve(final Class<?> clazz, final boolean checkErrors) {
        final Object component = components.get(clazz);

        if (component != null) {
            return component instanceof Class
                    ? tryResolve((Class<?>) component)
                    : component;
        }

        final Object instance = tryResolve(clazz);

        if (instance == null && checkErrors) {
            throw new RuntimeException(
                    "Container could not locate class of type: "
                            + clazz.getName());
        }

        cacheIf(clazz, instance);

        return instance;
    }

    private Object tryResolve(final Class<?> target) {
        for (final Constructor<?> c : target.getConstructors()) {
            final ArrayList<Object> args = new ArrayList<Object>();
            boolean success = true;
            for (final Class<?> p : c.getParameterTypes()) {
                final Object a = resolve(p, false);
                if (a == null) {
                    success = false;
                    break;
                }
                args.add(a);
            }

            if (success) {
                try {
                    final Object instance = c.newInstance(args.toArray());
                    cacheIf(target, instance);
                    return instance;
                } catch (final Exception ex) {
                    continue;
                }
            }
        }
        return null;
    }

    <T> T registerAndReturnInstance(final Class<T> target, final T service) {
        components.put(target, service);
        return service;
    }

    public <T> MapServiceLocator register(
            final Class<T> target,
            final Object service) {
        components.put(target, service);
        return this;
    }
}
