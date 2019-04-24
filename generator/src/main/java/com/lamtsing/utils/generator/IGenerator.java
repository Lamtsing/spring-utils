package com.lamtsing.utils.generator;

/**
 * @author Lamtsing
 */
public interface IGenerator {

    void generator();

    String getBasePath();

    void setBasePath(String basePath);

    String getPackageName();

    void setPackageName(String basePath);

    Class getEntity();

    void setEntity(Class clazz);

    String buildClassName(Class clazz);

    String buildClassType(Class clazz);
}
