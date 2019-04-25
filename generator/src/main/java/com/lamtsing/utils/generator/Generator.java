package com.lamtsing.utils.generator;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import java.io.File;
import java.lang.annotation.Annotation;

/**
 * @author Lamtsing
 */
public class Generator {

    private EntityGenerator entityGenerator; // 实体类
    private DtoGenerator dtoGenerator; // dto生成器
    private RepositoryGenerator repositoryGenerator; // dao生成器
    private MapstructGenerator mapstructGenerator; // mapstruct生成器
    private ServiceGenerator serviceGenerator; // service接口生成器
    private ServiceImplGenerator serviceImplGenerator; // serviceImpl生成器
    private ControllerGenerator ControllerGenerator; // 控制器生成器
    private String entityPackage;
    private String entityName;
    private String basePath;

    private boolean enableDto = false;
    private boolean enableRepository = false;
    private boolean enableMapstruct = false;
    private boolean enableService = false;
    private boolean enableServiceImpl = false;
    private boolean enableResource = false;

    public Generator(String entityPackage, String entityName) {
        this(entityPackage, entityName, "/src/main/java");
    }

    public Generator(String entityPackage, String entityName, String basePath) {
        this.entityGenerator = new EntityGenerator();
        this.dtoGenerator = new DtoGenerator();
        this.repositoryGenerator = new RepositoryGenerator();
        this.mapstructGenerator = new MapstructGenerator();
        this.serviceGenerator = new ServiceGenerator();
        this.serviceImplGenerator = new ServiceImplGenerator();
        this.ControllerGenerator = new ControllerGenerator();
        this.entityPackage = entityPackage;
        this.entityName = entityName;
        this.basePath = basePath;

        entityGenerator.setBasePath(basePath);
        entityGenerator.setPackageName(entityPackage);
    }

    public void setDtoInit(String packageName, String basePath) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableDto = true;
        dtoGenerator.setPackageName(packageName);
        dtoGenerator.setBasePath(basePath);
    }

    public void setRepositoryInit(String packageName, String basePath) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableRepository = true;
        repositoryGenerator.setPackageName(packageName);
        repositoryGenerator.setBasePath(basePath);
    }

    public void setMapstructInit(String packageName, String basePath) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableMapstruct = true;
        mapstructGenerator.setPackageName(packageName);
        mapstructGenerator.setBasePath(basePath);
    }

    public void setServiceInit(String packageName, String basePath) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableService = true;
        serviceGenerator.setPackageName(packageName);
        serviceGenerator.setBasePath(basePath);
    }

    public void setServiceImplInit(String packageName, String basePath) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableServiceImpl = true;
        serviceImplGenerator.setPackageName(packageName);
        serviceImplGenerator.setBasePath(basePath);
    }

    public void setControllerInit(String packageName, String basePath) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableResource = true;
        ControllerGenerator.setPackageName(packageName);
        ControllerGenerator.setBasePath(basePath);
    }

    public void generator() {
        String path = GeneratorUtils.getPath(entityPackage, basePath);
        File file = new File(path + "/" + entityName + ".java");
        if (!file.exists()) {
            return;
        }
        Class clazz;
        try {
            String fileName = file.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            clazz = Class.forName(entityPackage + "." + fileName);
            // 需要Entity注解
            Annotation annotation = clazz.getAnnotation(Entity.class);
            if (annotation == null) {
                return;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("entity加载错误");
        }
        dtoGenerator.setEntity(clazz);
        repositoryGenerator.setEntity(clazz);
        mapstructGenerator.setEntity(clazz);
        serviceGenerator.setEntity(clazz);
        serviceImplGenerator.setEntity(clazz);
        ControllerGenerator.setEntity(clazz);

        // 设置属性
        mapstructGenerator.setDtoGenerator(dtoGenerator);
        mapstructGenerator.setEntityGenerator(entityGenerator);

        serviceGenerator.setDtoGenerator(dtoGenerator);

        repositoryGenerator.setEntityGenerator(entityGenerator);

        serviceImplGenerator.setEntityGenerator(entityGenerator);
        serviceImplGenerator.setDtoGenerator(dtoGenerator);
        serviceImplGenerator.setMapstructGenerator(mapstructGenerator);
        serviceImplGenerator.setRepositoryGenerator(repositoryGenerator);
        serviceImplGenerator.setServiceGenerator(serviceGenerator);

        ControllerGenerator.setServiceGenerator(serviceGenerator);
        ControllerGenerator.setDtoGenerator(dtoGenerator);

        // 执行生成
        if (enableDto)
            dtoGenerator.generator();
        if (enableRepository)
            repositoryGenerator.generator();
        if (enableMapstruct)
            mapstructGenerator.generator();
        if (enableService)
            serviceGenerator.generator();
        if (enableServiceImpl)
            serviceImplGenerator.generator();
        if (enableResource)
            ControllerGenerator.generator();
    }
}
