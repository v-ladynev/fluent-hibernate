package com.github.fluent.hibernate.cfg.scanner;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarFile;

import sun.net.www.protocol.file.FileURLConnection;

/**
 * This class wraps URL and ClassLoader for using it with collections. The same URLs with distinct
 * class loaders are considered as equal. It doesn't use equals and hashcode of URL.
 *
 * @author V.Ladynev
 */
class UrlWrapper {

    private final URL url;

    private final String externalForm;

    private final ClassLoader loader;

    public UrlWrapper(URL url, ClassLoader loader) {
        this.url = url;
        this.loader = loader;
        externalForm = url.toExternalForm();
    }

    public URL getUrl() {
        return url;
    }

    public boolean isFile() {
        return ResourceUtils.isFile(url);
    }

    public File getFile() {
        return new File(url.getFile());
    }

    public JarFile getJarFile() throws IOException {
        URLConnection urlConnection = url.openConnection();
        if (urlConnection instanceof JarURLConnection) {
            return ((JarURLConnection) urlConnection).getJarFile();
        }

        if (urlConnection instanceof FileURLConnection) {
            return createJarFile(getFile());
        }

        return null;
    }

    private static JarFile createJarFile(File file) {
        try {
            return new JarFile(file);
        } catch (IOException ignore) {
            // Not a jar file
            return null;
        }
    }

    public ClassLoader getLoader() {
        return loader;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        return obj instanceof UrlWrapper ? externalForm.equals(((UrlWrapper) obj).externalForm)
                : false;
    }

    @Override
    public int hashCode() {
        return externalForm.hashCode();
    }

    @Override
    public String toString() {
        return url.toString();
    }

    public String getExternalForm() {
        return externalForm;
    }

}
