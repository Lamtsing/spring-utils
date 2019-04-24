package com.lamtsing.utils.generator;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Lamtsing
 */
public class DtoGenerator extends AbstractGenerator {

    private String classTemplate = "/**%n * @author Lamtsing-Generator%n */%n@Data%npublic class %s {%n%n";

    public DtoGenerator() {
        setSuffix("Dto");
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
        imports.add(Data.class.getTypeName());
        appendImport(stringBuilder, imports);
        // 设置类名
        stringBuilder.append(String.format(classTemplate, className));
        // 生成属性
        Field[] fields = entity.getDeclaredFields();
        for (Field field : fields) {
            stringBuilder.append("\tprivate ")
                    .append(GeneratorUtils.getGenericTypeName(field))
                    .append(" ")
                    .append(field.getName())
                    .append(";\n\n");
        }
        stringBuilder.append("}");

        // 执行生成
        GeneratorUtils.write(file, stringBuilder);
    }
}
