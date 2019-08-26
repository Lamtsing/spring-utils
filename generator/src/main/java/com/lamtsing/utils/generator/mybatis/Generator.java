package com.lamtsing.utils.generator.mybatis;

import com.lamtsing.utils.generator.Constant;
import com.lamtsing.utils.generator.EntityGenerator;
import com.lamtsing.utils.generator.GeneratorUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author Lamtsing
 */
public class Generator {

    private EntityGenerator entityGenerator; // 实体类
    private MapperGenerator mapperGenerator; // dao生成器
    private ServiceGenerator serviceGenerator; // service接口生成器
    private ServiceImplGenerator serviceImplGenerator; // serviceImpl生成器
    private ResourceGenerator resourceGenerator; // 控制器生成器
    private String entityPackage;
    private String entityName;
    private String basePath;

    private boolean enableRepository = false;
    private boolean enableService = false;
    private boolean enableServiceImpl = false;
    private boolean enableResource = false;

    public Generator(String entityPackage, String entityName) {
        this(entityPackage, entityName, "/src/main/java");
    }

    public Generator(String entityPackage, String entityName, String basePath) {
        this.entityGenerator = new EntityGenerator();
        this.mapperGenerator = new MapperGenerator();
        this.serviceGenerator = new ServiceGenerator();
        this.serviceImplGenerator = new ServiceImplGenerator();
        this.resourceGenerator = new ResourceGenerator();
        this.entityPackage = entityPackage;
        this.entityName = entityName;
        this.basePath = basePath;

        entityGenerator.setBasePath(basePath);
        entityGenerator.setPackageName(entityPackage);
    }

    public void setAuthor(String author){
        Constant.AUTHOR = author;
    }

    public void setMapperInit(String packageName, String basePath) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableRepository = true;
        setMapperInit(packageName,basePath,"","Mapper");

    }

    public void setServiceInit(String packageName, String basePath) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableService = true;
        setServiceInit(packageName,basePath,"I","Service");

    }

    public void setServiceImplInit(String packageName, String basePath) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableServiceImpl = true;
        setServiceImplInit(packageName,basePath,"","ServiceImpl");

    }

    public void setResourceInit(String packageName, String basePath) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableResource = true;
        setResourceInit(packageName,basePath,"","Resource");

    }

    public void setMapperInit(String packageName, String basePath, String pre, String suf) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableRepository = true;
        mapperGenerator.setPackageName(packageName);
        mapperGenerator.setBasePath(basePath);
        mapperGenerator.setPrefix(pre);
        mapperGenerator.setSuffix(suf);
    }

    public void setServiceInit(String packageName, String basePath, String pre, String suf) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableService = true;
        serviceGenerator.setPackageName(packageName);
        serviceGenerator.setBasePath(basePath);
        serviceGenerator.setPrefix(pre);
        serviceGenerator.setSuffix(suf);
    }

    public void setServiceImplInit(String packageName, String basePath, String pre, String suf) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableServiceImpl = true;
        serviceImplGenerator.setPackageName(packageName);
        serviceImplGenerator.setBasePath(basePath);
        serviceImplGenerator.setPrefix(pre);
        serviceImplGenerator.setSuffix(suf);
    }

    public void setResourceInit(String packageName, String basePath, String pre, String suf) {
        if (StringUtils.isBlank(basePath)) {
            basePath = this.basePath;
        }
        enableResource = true;
        resourceGenerator.setPackageName(packageName);
        resourceGenerator.setBasePath(basePath);
        resourceGenerator.setPrefix(pre);
        resourceGenerator.setSuffix(suf);
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("entity加载错误");
        }
        mapperGenerator.setEntity(clazz);
        serviceGenerator.setEntity(clazz);
        serviceImplGenerator.setEntity(clazz);
        resourceGenerator.setEntity(clazz);

        // 设置属性

        serviceGenerator.setEntityGenerator(entityGenerator);

        mapperGenerator.setEntityGenerator(entityGenerator);

        serviceImplGenerator.setEntityGenerator(entityGenerator);
        serviceImplGenerator.setMapperGenerator(mapperGenerator);
        serviceImplGenerator.setServiceGenerator(serviceGenerator);

        resourceGenerator.setServiceGenerator(serviceGenerator);
        resourceGenerator.setServiceImplGenerator(serviceImplGenerator);
        resourceGenerator.setEntityGenerator(entityGenerator);

        // 执行生成
        if (enableRepository)
            mapperGenerator.generator();
        if (enableService)
            serviceGenerator.generator();
        if (enableServiceImpl)
            serviceImplGenerator.generator();
        if (enableResource)
            resourceGenerator.generator();
    }
}
