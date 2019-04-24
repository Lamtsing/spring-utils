package com.lamtsing.utils.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    public static void write(File file, StringBuilder stringBuilder) {
        try {
            file.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(stringBuilder.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
