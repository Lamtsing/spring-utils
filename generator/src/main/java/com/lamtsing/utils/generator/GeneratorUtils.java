package com.lamtsing.utils.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author Lamtsing
 */
public class GeneratorUtils {

    public static String getPath(String packageName, String basePath) {
        String packagePath = "/" + packageName.replaceAll("\\.", "/");
        String path = (Thread.currentThread().getContextClassLoader().getResource("") + "../..").replaceAll("file:/", "").replaceAll("%20", " ").trim();
        return path + basePath + packagePath;
    }

    public static String firstToUpperCase(String fieldName) {
        return String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
    }

    public static String firstToLowerCase(String fieldName) {
        return String.valueOf(fieldName.charAt(0)).toLowerCase() + fieldName.substring(1);
    }

    public static String getGenericTypeName(Field field) {
        String typeName = field.getGenericType().getTypeName();
        return typeName.substring(typeName.lastIndexOf(".") + 1);
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
