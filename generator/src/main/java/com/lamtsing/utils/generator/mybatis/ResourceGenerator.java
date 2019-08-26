package com.lamtsing.utils.generator.mybatis;

import com.lamtsing.utils.generator.AbstractGenerator;
import com.lamtsing.utils.generator.Constant;
import com.lamtsing.utils.generator.EntityGenerator;
import com.lamtsing.utils.generator.GeneratorUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Lamtsing
 */
@Getter
@Setter
public class ResourceGenerator extends AbstractGenerator {

    private String classTemplate = "/**%n * @author %s%n */%n@RestController%n@RequestMapping(\"%s\")%n@Slf4j%npublic class %s {%n%n";

    private ServiceGenerator serviceGenerator;
    private ServiceImplGenerator serviceImplGenerator;
    private EntityGenerator entityGenerator;

    @Override
    public void generator() {
        Class entity = getEntity();
        String packageName = getPackageName();
        if (StringUtils.isBlank(packageName)) {
            return;
        }

        String path = GeneratorUtils.getPath(packageName, getBasePath());

        String className = buildClassName(entity);
        File file = new File(path + "/" + className + ".java");
        if (file.exists()) {
            System.err.println("Resource: [" + className + "] generator faild! because this file exists!");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();

        // 设置包名
        appendPackage(stringBuilder);
        // 设置引入
        Set<String> imports = new HashSet<>();
        String entityName = entityGenerator.buildClassName(entity);
        String entityField = GeneratorUtils.firstToLowerCase(entityName);
        String service = serviceGenerator.buildClassName(entity);
        String serviceField = GeneratorUtils.firstToLowerCase(service);
        String serviceImplField = GeneratorUtils.firstToLowerCase(serviceImplGenerator.buildClassName(entity));
        imports.add(entityGenerator.buildClassType(entity));
        imports.add(serviceGenerator.buildClassType(entity));
        imports.add(Slf4j.class.getTypeName());
        imports.add("org.springframework.web.bind.annotation.*");
        imports.add(Resource.class.getTypeName());
        appendImport(stringBuilder, imports);
        // 设置包名
        stringBuilder.append(String.format(classTemplate, Constant.AUTHOR, GeneratorUtils.firstToLowerCase(className), className));
        // 设置属性
        stringBuilder.append("\t@Resource\n\tprivate ")
                .append(service)
                .append(" ")
                .append(serviceImplField)
                .append(";\n\n");
        // 保存
        stringBuilder.append("\t@PostMapping(\"/save\")\n\tpublic ")
                .append(entityName)
                .append(" insert(")
                .append(entityName)
                .append(" ")
                .append(entityField)
                .append(") {\n\t\treturn ")
                .append(serviceImplField)
                .append(".insert(")
                .append(entityField)
                .append(");\n\t}\n\n");
        // 获取一条数据
        stringBuilder.append("\t@GetMapping(\"/getOne\")\n\tpublic ")
                .append(entityName)
                .append(" selectById(")
                .append(getIdType()[1])
                .append(" id) {\n\t\treturn ")
                .append(serviceImplField)
                .append(".selectById(id);\n\t}\n\n");
        // 删除一条数据
        stringBuilder.append("\t@DeleteMapping(\"/delete\")\n\tpublic void deleteById(")
                .append(getIdType()[1])
                .append(" id) {\n\t\t")
                .append(serviceImplField)
                .append(".deleteById(id);\n\t}\n\n");
        stringBuilder.append("}");

        // 执行生成
        GeneratorUtils.write(file, stringBuilder);

        System.out.println("Resource: [" + className + "] generator success!");
    }
}
