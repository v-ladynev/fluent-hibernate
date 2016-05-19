package com.github.fluent.hibernate.cfg.scanner;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.github.fluent.hibernate.internal.util.InternalUtils.CollectionUtils;
import com.github.fluent.hibernate.internal.util.InternalUtils.StringUtils;

/**
 *
 * @author V.Ladynev
 */
public class ClasspathScanner {

    private final Set<UrlWrapper> scanned = CollectionUtils.newHashSet();

    private final Set<String> scannedResources = new HashSet<String>();

    private List<String> resourcesToScan;

    private List<ClassLoader> loaders;

    private final IResourceAcceptor acceptor;

    private boolean scanAllPackages;

    public ClasspathScanner(IResourceAcceptor acceptor) {
        this.acceptor = acceptor;
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        this.resourcesToScan = ResourceUtils.packagesAsResourcePath(packagesToScan);
    }

    public void allPackagesToScan() {
        setPackagesToScan(Collections.<String> emptyList());
        scanAllPackages = true;
    }

    public void setLoaders(List<ClassLoader> loaders) {
        this.loaders = loaders;
    }

    public void scan() throws Exception {
        for (UrlWrapper url : getUrls()) {
            scan(url);
        }
    }

    private Set<UrlWrapper> getUrls() {
        return UrlExtractor.createForResources(resourcesToScan).usingLoaders(
                CollectionUtils.isEmpty(loaders) ? ClassLoaderUtils.defaultClassLoaders() : loaders)
                .extract();
    }

    private void scan(UrlWrapper url) throws Exception {
        // scan each url once independent of the classloader
        if (!scanned.add(url)) {
            return;
        }

        if (url.isFile()) {
            scanFile(url);
        } else {
            scanJar(url);
        }
    }

    private void scanFile(UrlWrapper url) throws Exception {
        File file = url.getFile();

        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            scanDirectory(url.getLoader(), file);
        } else {
            scanJar(url);
        }
    }

    private void scanJar(UrlWrapper url) throws Exception {
        JarFile jarFile = url.getJarFile();

        if (jarFile == null) {
            return;
        }

        try {
            for (UrlWrapper urlFromManifest : getClassPathFromManifest(jarFile, url.getLoader())) {
                scan(urlFromManifest);
            }
            scanJarFile(jarFile, url.getLoader());
        } finally {
            ResourceUtils.closeQuietly(jarFile);
        }
    }

    private void scanJarFile(JarFile file, ClassLoader loader) throws Exception {
        Enumeration<JarEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.isDirectory() || entry.getName().equals(JarFile.MANIFEST_NAME)) {
                continue;
            }

            addResource(entry.getName(), loader);
        }
    }

    private void scanDirectory(ClassLoader loader, File directory) throws Exception {
        scanDirectory(directory, loader, StringUtils.EMPTY);
    }

    private void scanDirectory(File directory, ClassLoader classloader, String packagePrefix)
            throws Exception {
        File[] files = directory.listFiles();
        if (files == null) {
            // IO error, just skip the directory
            return;
        }
        for (File f : files) {
            String name = f.getName();
            if (f.isDirectory()) {
                scanDirectory(f, classloader, packagePrefix + name + "/");
            } else {
                String resourceName = packagePrefix + name;
                if (!resourceName.equals(JarFile.MANIFEST_NAME)) {
                    addResource(resourceName, classloader);
                }
            }
        }
    }

    private static Set<UrlWrapper> getClassPathFromManifest(JarFile jarFile, ClassLoader loader)
            throws IOException {
        Manifest manifest = jarFile.getManifest();

        if (manifest == null) {
            return Collections.emptySet();
        }

        Set<UrlWrapper> result = CollectionUtils.newHashSet();
        String classpathAttribute = manifest.getMainAttributes()
                .getValue(Attributes.Name.CLASS_PATH.toString());
        if (classpathAttribute == null) {
            return result;
        }

        for (String path : StringUtils.splitBySpace(classpathAttribute)) {
            URL url;
            try {
                url = getClassPathEntry(jarFile, path);
            } catch (MalformedURLException e) {
                // Ignore bad entry
                continue;
            }
            result.add(new UrlWrapper(url, loader));
        }

        return result;
    }

    private void addResource(String resource, ClassLoader loader) throws Exception {
        if (!scannedResources.add(resource)) {
            return;
        }

        if (canAddToResult(resource)) {
            acceptor.accept(resource, loader);
        }
    }

    private boolean canAddToResult(String resource) throws IOException {
        if (scanAllPackages) {
            return true;
        }

        for (String resourceToScan : resourcesToScan) {
            if (resource.startsWith(resourceToScan)) {
                return true;
            }
        }

        return false;
    }

    private static URL getClassPathEntry(JarFile jarFile, String path)
            throws MalformedURLException {
        return new URL(new File(jarFile.getName()).toURI().toURL(), path);
    }

    public interface IResourceAcceptor {

        void accept(String resource, ClassLoader loader) throws Exception;

    }

}
