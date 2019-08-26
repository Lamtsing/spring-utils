package com.lamtsing.utils.generator.mybatis;

import com.lamtsing.utils.generator.AbstractGenerator;
import com.lamtsing.utils.generator.EntityGenerator;
import com.lamtsing.utils.generator.GeneratorUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Lamtsing
 */
@Getter
@Setter
public class MapperGenerator extends AbstractGenerator {

    private String classTemplate = "/**%n * @author Lamtsing-Generator%n */%n@Repository%npublic interface %s extends BaseMapper<%s> {%n%n";

    private EntityGenerator entityGenerator;

    @Override
    public void generator() {
        Class entity = getEntity();

        String idType = getIdType()[1];

        String packageName = getPackageName();
        if (StringUtils.isBlank(packageName)) {
            return;
        }

        String path = GeneratorUtils.getPath(packageName, getBasePath());

        String className = buildClassName(entity);
        String entityName = entity.getSimpleName();
        File file = new File(path + "/" + className + ".java");
        if (file.exists()) {
            System.err.println("Mapper: [" + className + "] generator faild! because this file exists!");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();

        // 设置包名
        appendPackage(stringBuilder);
        // 设置引入
        Set<String> imports = new HashSet<>();
        imports.add(entityGenerator.buildClassType(entity)); // 实体类
        imports.add("com.baomidou.mybatisplus.core.mapper.BaseMapper");
        imports.add(Repository.class.getTypeName());
        appendImport(stringBuilder, imports);
        // 设置类
        stringBuilder.append(String.format(classTemplate, className, entityName));
        // 根据id找实体类
        stringBuilder.append("}");

        // 执行生成
        GeneratorUtils.write(file, stringBuilder);

        System.out.println("Mapper: [" + className + "] generator success!");

    }
}
