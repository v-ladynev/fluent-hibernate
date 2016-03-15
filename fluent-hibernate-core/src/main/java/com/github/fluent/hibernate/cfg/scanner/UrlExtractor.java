package com.github.fluent.hibernate.cfg.scanner;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 *
 * @author V.Ladynev
 */
final class UrlExtractor {

    private final List<String> resources;

    private final Set<UrlWrapper> result = InternalUtils.CollectionUtils.newHashSet();

    private List<ClassLoader> loaders;

    private UrlExtractor(List<String> resources) {
        this.resources = InternalUtils.CollectionUtils.correctToEmpty(resources);
    }

    public static UrlExtractor createForResources(List<String> resources) {
        return new UrlExtractor(resources);
    }

    public UrlExtractor usingLoaders(List<ClassLoader> loaders) {
        this.loaders = InternalUtils.CollectionUtils.correctToEmpty(loaders);
        return this;
    }

    public Set<UrlWrapper> extract() {
        for (String resource : resources) {
            forResource(resource);
        }

        if (result.isEmpty()) {
            for (ClassLoader loader : loaders) {
                forClassLoader(loader);
            }
        }

        return result;
    }

    private void forResource(String resourceToScan) {
        for (ClassLoader loader : loaders) {
            try {
                getUrls(resourceToScan, loader);
            } catch (IOException ignore) {

            }
        }
    }

    private void getUrls(String resourceName, ClassLoader loader) throws IOException {
        Enumeration<URL> urls = loader.getResources(resourceName);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            int index = url.toExternalForm().lastIndexOf(resourceName);
            addUrl(index == -1 ? url : new URL(url.toExternalForm().substring(0, index)), loader);
        }
    }

    private void forClassLoader(ClassLoader loader) {
        if (loader == null) {
            return;
        }

        // search parent first, since it's the order ClassLoader#loadClass() uses
        forClassLoader(loader.getParent());

        if (loader instanceof URLClassLoader) {
            URL[] urls = ((URLClassLoader) loader).getURLs();
            addUrls(urls, loader);
        }
    }

    private void addUrls(URL[] urls, ClassLoader loader) {
        if (urls == null) {
            return;
        }

        for (URL url : urls) {
            addUrl(url, loader);
        }
    }

    private void addUrl(URL url, ClassLoader loader) {
        UrlWrapper urlWrapper = new UrlWrapper(url, loader);
        if (!result.contains(urlWrapper)) {
            result.add(urlWrapper);
        }
    }

}
