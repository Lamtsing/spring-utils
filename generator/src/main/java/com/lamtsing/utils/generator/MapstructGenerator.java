package com.lamtsing.utils.generator;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Lamtsing
 */
@Getter
@Setter
public class MapstructGenerator extends AbstractGenerator {

    private String classTemplate = "/**%n * @author Lamtsing-Generator%n */%n@Mapper(componentModel = \"spring\", uses = {})%npublic interface %s {%n%n";

    private DtoGenerator dtoGenerator;

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
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();

        // 设置包名
        appendPackage(stringBuilder);
        // 设置引入
        Set<String> imports = new HashSet<>();
        imports.add(dtoGenerator.buildClassType(entity));
        imports.add(Mapper.class.getTypeName());
        appendImport(stringBuilder, imports);
        // 设置类名
        stringBuilder.append(String.format(classTemplate, className));
        // 设置fromId方法
        String simpleName = entity.getSimpleName();
        String firstToLowerCase = GeneratorUtils.firstToLowerCase(simpleName);
        String[] id = getIdType();
        stringBuilder.append("\tdefault ")
                .append(simpleName)
                .append(" fromId(")
                .append(id[1])
                .append(" id){\n\t\tif (id == null){\n\t\t\treturn null;\n\t\t}\n\t\t")
                .append(simpleName)
                .append(" ")
                .append(firstToLowerCase)
                .append(" = new ")
                .append(simpleName)
                .append("();\n\t\t")
                .append(firstToLowerCase)
                .append(".set")
                .append(GeneratorUtils.firstToUpperCase(id[0]))
                .append("(id);\n\t\treturn ")
                .append(firstToLowerCase)
                .append(";\n\t}\n}");

        // 执行生成
        GeneratorUtils.write(file, stringBuilder);
    }
}
