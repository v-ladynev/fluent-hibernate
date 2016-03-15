package com.github.fluent.hibernate.cfg.scanner;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.List;

import com.github.fluent.hibernate.internal.util.InternalUtils.CollectionUtils;

/**
 *
 * @author V.Ladynev
 */
final class ResourceUtils {

    private static final char PACKAGE_SEPARATOR = '.';

    private static final char PATH_SEPARATOR = '/';

    private static final String CLASS_FILE_NAME_EXTENSION = ".class";

    private static final String URL_PROTOCOL_FILE = "file";

    private ResourceUtils() {

    }

    public static List<String> packagesAsResourcePath(List<String> packageNames) {
        List<String> result = CollectionUtils.newArrayListWithCapacity(CollectionUtils
                .size(packageNames));

        for (String packageName : packageNames) {
            result.add(packageAsResourcePath(packageName));
        }

        return result;
    }

    public static String packageAsResourcePath(String packageName) {
        return packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
    }

    public static String getClassNameFromPath(String classFilePath) {
        int classNameEnd = classFilePath.length() - CLASS_FILE_NAME_EXTENSION.length();
        return classFilePath.substring(0, classNameEnd).replace(PATH_SEPARATOR, PACKAGE_SEPARATOR);
    }

    public static String toDescriptor(Class<? extends Annotation> annotation) {
        return "L" + packageAsResourcePath(annotation.getName()) + ";";
    }

    public static boolean isFile(URL url) {
        return url != null && url.getProtocol().equals(URL_PROTOCOL_FILE);
    }

}
