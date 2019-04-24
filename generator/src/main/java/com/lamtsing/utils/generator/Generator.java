package com.lamtsing.utils.generator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.Entity;
import java.io.File;
import java.lang.annotation.Annotation;

/**
 * @author Lamtsing
 */
public class Generator {

    private DtoGenerator dtoGenerator; // dto生成器
    private RepositoryGenerator repositoryGenerator; // dao生成器
    private MapstructGenerator mapstructGenerator; // mapstruct生成器
    private ServiceGenerator serviceGenerator; // service接口生成器
    private ServiceImplGenerator serviceImplGenerator; // serviceImpl生成器
    private ResourceGenerator resourceGenerator; // 控制器生成器
    private String entityPackage;
    private String basePath;

    private boolean enableDto = false;
    private boolean enableRepository = false;
    private boolean enableMapstruct = false;
    private boolean enableService = false;
    private boolean enableServiceImpl = false;
    private boolean enableResource = false;

    public Generator(String entityPackage) {
        this(entityPackage, "/src/main/java");
    }

    public Generator(String entityPackage, String basePath) {
        this.dtoGenerator = new DtoGenerator();
        this.repositoryGenerator = new RepositoryGenerator();
        this.mapstructGenerator = new MapstructGenerator();
        this.serviceGenerator = new ServiceGenerator();
        this.serviceImplGenerator = new ServiceImplGenerator();
        this.resourceGenerator = new ResourceGenerator();
        this.entityPackage = entityPackage;
        this.basePath = basePath;
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

    public void setResourceInit(String packageName, String basePath) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableResource = true;
        resourceGenerator.setPackageName(packageName);
        resourceGenerator.setBasePath(basePath);
    }

    public void generator() {
        String path = GeneratorUtils.getPath(entityPackage, basePath);
        File file = new File(path);
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
            if (!ObjectUtils.isEmpty(annotation)) {
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
        resourceGenerator.setEntity(clazz);

        // 设置属性
        mapstructGenerator.setDtoGenerator(dtoGenerator);

        serviceGenerator.setDtoGenerator(dtoGenerator);

        serviceImplGenerator.setDtoGenerator(dtoGenerator);
        serviceImplGenerator.setMapstructGenerator(mapstructGenerator);
        serviceImplGenerator.setRepositoryGenerator(repositoryGenerator);
        serviceImplGenerator.setServiceGenerator(serviceGenerator);

        resourceGenerator.setServiceGenerator(serviceGenerator);
        resourceGenerator.setDtoGenerator(dtoGenerator);

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
            resourceGenerator.generator();
    }
}
