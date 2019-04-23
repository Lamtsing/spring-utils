package com.lamtsing.utils.generator;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Lamtsing
 */
@Getter
@Setter
public abstract class AbstractGenerator implements IGenerator {

    private String packageTemplate = "package %s;%n%n";// 包名模板
    private String importTemplate = "import %s;%n"; // 导入模板
    private String entityPrefix = ""; // 前缀
    private String entitySuffix = ""; // 后缀
    private String packageName; // 包名
    private Class entity;
    private String basePath;
    private String prefix = ""; // 类名前缀
    private String suffix = ""; // 类名后缀

    public StringBuilder appendPackage(StringBuilder stringBuilder) {
        return stringBuilder.append(String.format(packageTemplate, packageName));
    }

    public StringBuilder appendImport(StringBuilder stringBuilder, Collection<String> imports) {
        for (String clazz : imports) {
            stringBuilder.append(String.format(importTemplate, clazz));
        }
        stringBuilder.append("\n");
        return stringBuilder;
    }

    @Override
    public String buildClassName(Class clazz) {
        return prefix + clazz.getSimpleName() + suffix;
    }

    @Override
    public String buildClassUrl(Class clazz) {
        return packageName + buildClassName(clazz);
    }

    public Field[] getAllField(Class clazz) {
        return getAllField(clazz, new ArrayList<>());
    }

    public Field[] getAllField(Class clazz, List<Field> list) {
        Field[] declaredFields = clazz.getDeclaredFields();
        list.addAll(Arrays.asList(declaredFields));
        while (clazz.getSuperclass() != null) {
            getAllField(clazz.getSuperclass(), list);
        }
        Field[] fields = new Field[list.size()];
        return list.toArray(fields);
    }
}
