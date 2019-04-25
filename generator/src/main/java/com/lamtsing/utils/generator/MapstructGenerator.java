package com.lamtsing.utils.generator;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Lamtsing
 */
@Getter
@Setter
public class MapstructGenerator extends AbstractGenerator {

    private String classTemplate = "/**%n * @author Lamtsing-Generator%n */%n@Mapper(componentModel = \"spring\", uses = {})%npublic interface %s {%n%n";

    private DtoGenerator dtoGenerator;
    private EntityGenerator entityGenerator;

    public MapstructGenerator() {
        setSuffix("Mapstruct");
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
            System.err.println("Mapstruct: [" + className + "] generator faild! because this file exists!");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();

        // 设置包名
        appendPackage(stringBuilder);
        // 设置引入
        Set<String> imports = new HashSet<>();
        imports.add(entityGenerator.buildClassType(entity));
        imports.add(dtoGenerator.buildClassType(entity));
        imports.add(List.class.getTypeName());
        imports.add(Mapper.class.getTypeName());
        appendImport(stringBuilder, imports);
        // 设置类名
        stringBuilder.append(String.format(classTemplate, className));

        String entityName = entityGenerator.buildClassName(entity);
        String dto = dtoGenerator.buildClassName(entity);
        String entityField = GeneratorUtils.firstToLowerCase(entityName);
        String dtoField = GeneratorUtils.firstToLowerCase(dto);

        // 设置toDto方法
        stringBuilder.append("\t")
                .append(dto)
                .append(" toDto(")
                .append(entityName)
                .append(" ")
                .append(entityField)
                .append(");\n\n");
        stringBuilder.append("\tList<")
                .append(dto)
                .append("> toDto(List<")
                .append(entityName)
                .append("> ")
                .append(entityField)
                .append("s);\n\n");
        // 设置toEntity方法
        stringBuilder.append("\t")
                .append(entityName)
                .append(" toEntity(")
                .append(dto)
                .append(" ")
                .append(dtoField)
                .append(");\n\n");
        stringBuilder.append("\tList<")
                .append(entityName)
                .append("> toEntity(List<")
                .append(dto)
                .append("> ")
                .append(dtoField)
                .append("s);\n\n");
        // 设置fromId方法
        String[] id = getIdType();
        stringBuilder.append("\tdefault ")
                .append(entityName)
                .append(" fromId(")
                .append(id[1])
                .append(" id){\n\t\tif (id == null){\n\t\t\treturn null;\n\t\t}\n\t\t")
                .append(entityName)
                .append(" ")
                .append(entityField)
                .append(" = new ")
                .append(entityName)
                .append("();\n\t\t")
                .append(entityField)
                .append(".set")
                .append(GeneratorUtils.firstToUpperCase(id[0]))
                .append("(id);\n\t\treturn ")
                .append(entityField)
                .append(";\n\t}\n}");

        // 执行生成
        GeneratorUtils.write(file, stringBuilder);

        System.out.println("Mapstruct: [" + className + "] generator success!");
    }
}
