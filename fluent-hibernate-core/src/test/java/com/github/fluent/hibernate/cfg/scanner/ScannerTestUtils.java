package com.github.fluent.hibernate.cfg.scanner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import com.github.fluent.hibernate.cfg.scanner.jar.persistent.FirstRootEntityJar;
import com.github.fluent.hibernate.cfg.scanner.jar.persistent.NotEntityJar;
import com.github.fluent.hibernate.cfg.scanner.jar.persistent.SecondRootEntityJar;
import com.github.fluent.hibernate.cfg.scanner.jar.persistent.subpackage.FirstSubpackageEntityJar;
import com.google.common.io.Closer;
import com.google.common.io.Resources;

/**
 *
 * @author V.Ladynev
 */
public final class ScannerTestUtils {

    private static final Class<?>[] JAR_CLASSES = new Class<?>[] { FirstRootEntityJar.class,
        FirstRootEntityJar.NestedEntityJar.class, SecondRootEntityJar.class,
        FirstSubpackageEntityJar.class, NotEntityJar.class };

    private ScannerTestUtils() {

    }

    public static URLClassLoader createClassLoader(ClassLoader parent, URL... url)
            throws MalformedURLException {
        return new URLClassLoader(url, parent);
    }

    public static URL writeTestJar() throws IOException {
        File result = File.createTempFile("scanner-test", ".jar");
        writeJarFile(result, JAR_CLASSES);
        return result.toURI().toURL();
    }

    private static void writeJarFile(File jarFile, Class<?>... classes) throws IOException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        Closer closer = Closer.create();
        try {
            FileOutputStream fileOut = closer.register(new FileOutputStream(jarFile));
            JarOutputStream jarOut = closer.register(new JarOutputStream(fileOut, manifest));

            for (Class<?> clazz : classes) {
                String classResource = ResourceUtils.classAsResource(clazz);
                jarOut.putNextEntry(new ZipEntry(classResource));
                Resources.copy(ScannerTestUtils.class.getResource(ResourceUtils
                        .resourcePathFromRoot(classResource)), jarOut);
                jarOut.closeEntry();
            }

        } catch (Throwable e) {
            throw closer.rethrow(e);
        } finally {
            closer.close();
        }
    }

}
