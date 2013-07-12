package com.dslplatform.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dslplatform.patterns.ServiceLocator;

public class Bootstrap {
    public static ServiceLocator init(final InputStream iniStream) throws IOException {
        try {
            //TODO really ugly. no place for it here.
            final Class<?> bootClass = Class.forName("com.dslplatform.client.BootstrapHttp");
            try {
                return staticLocator = (ServiceLocator) bootClass
                  .getMethod("init", InputStream.class)
                  .invoke(null, iniStream);
            }
            catch (final Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error occured whilst bootstrapping: " + e, e);
            }
        }
        catch (final ClassNotFoundException e) {
            throw new IOException("Missing bootstrap implementation, check if dsl-client-http.jar is in the classpath", e);
        }
    }

    public static ServiceLocator init(final String iniPath) throws IOException {
        final InputStream iniStream = new FileInputStream(iniPath);
        try {
          return init(iniStream);
        }
        finally {
          iniStream.close();
        }
    }

    private static ServiceLocator staticLocator;

    public static ServiceLocator getLocator() {
        if (staticLocator == null)
            throw new RuntimeException("Bootstrap has not been initialized, call Bootstrap.init");

        return staticLocator;
    }
}
