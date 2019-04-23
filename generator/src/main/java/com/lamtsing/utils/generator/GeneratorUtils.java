package com.lamtsing.utils.generator;

import java.io.File;

/**
 * @author Lamtsing
 */
public class GeneratorUtils {

    public static String getPath(String packageName, String basePath) {
        String packagePath = packageName.replaceAll("\\.", "/");
        String path = (Thread.currentThread().getContextClassLoader().getResource("") + "../../").replaceAll("file:/", "").replaceAll("%20", " ").trim();
        if (path.indexOf(":") != 1) {
            path = File.separator + path;
        }
        return path + basePath + packagePath;
    }

    public static String firstToUpperCase(String fieldName) {
        return String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
    }

    public static String firstToLowerCase(String fieldName) {
        return String.valueOf(fieldName.charAt(0)).toLowerCase() + fieldName.substring(1);
    }

    public static String toDto(String entityName) {
        return entityName + "Dto";
    }

    public static String toEntity(String dtoName) {
        return dtoName.substring(0, dtoName.length() - 3);
    }
}
