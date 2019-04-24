package com.lamtsing.utils.generator;

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

    private String classTemplate = "/**%n * @author Lamtsing-Generator%n */%n@RestController%n@RequestMapping(\"%s\")%n@Slf4j%npublic class %s {%n%n";

    private ServiceGenerator serviceGenerator;
    private DtoGenerator dtoGenerator;

    public ResourceGenerator() {
        setSuffix("Resource");
    }

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
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();

        // 设置包名
        appendPackage(stringBuilder);
        // 设置引入
        Set<String> imports = new HashSet<>();
        String dto = dtoGenerator.buildClassName(entity);
        String service = serviceGenerator.buildClassName(entity);
        String dtoField = GeneratorUtils.firstToLowerCase(dto);
        String serviceField = GeneratorUtils.firstToLowerCase(service);
        imports.add(dtoGenerator.buildClassType(entity));
        imports.add(serviceGenerator.buildClassType(entity));
        imports.add(Slf4j.class.getTypeName());
        imports.add("org.springframework.web.bind.annotation.*");
        imports.add(Resource.class.getTypeName());
        appendImport(stringBuilder, imports);
        // 设置包名
        stringBuilder.append(String.format(classTemplate, GeneratorUtils.firstToLowerCase(className), className));
        // 设置属性
        stringBuilder.append("\t@Resource\n\tprivate ")
                .append(service)
                .append(" ")
                .append(serviceField)
                .append(";\n\n");
        // 保存
        stringBuilder.append("\t@PostMapping(\"/save\")\n\tpublic ")
                .append(dto)
                .append(" save(")
                .append(dto)
                .append(" ")
                .append(dtoField)
                .append(") {\n\t\treturn ")
                .append(serviceField)
                .append(".save(")
                .append(dtoField)
                .append(");\n\t}\n\n");
        // 获取一条数据
        stringBuilder.append("\t@GetMapping(\"/getOne\")\n\tpublic ")
                .append(dto)
                .append(" getOne(")
                .append(getIdType()[1])
                .append(" id) {\n\t\treturn ")
                .append(serviceField)
                .append(".getOne(id);\n\t}\n\n");
        // 删除一条数据
        stringBuilder.append("\t@DeleteMapping(\"/delete\")\n\tpublic void deleteById(")
                .append(getIdType()[1])
                .append(" id) {\n\t\t")
                .append(serviceField)
                .append(".deleteById(id);\n\t}\n\n");
        stringBuilder.append("}");

        // 执行生成
        GeneratorUtils.write(file, stringBuilder);


    }
}
