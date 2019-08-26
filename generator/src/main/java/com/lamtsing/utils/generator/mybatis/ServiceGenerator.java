package com.lamtsing.utils.generator.mybatis;

import com.lamtsing.utils.generator.AbstractGenerator;
import com.lamtsing.utils.generator.EntityGenerator;
import com.lamtsing.utils.generator.GeneratorUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Lamtsing
 */
@Getter
@Setter
public class ServiceGenerator extends AbstractGenerator {

    private String classTemplate = "/**%n * @author Lamtsing-Generator%n */%npublic interface %s {%n%n";

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
        String entityName = entity.getSimpleName();
        File file = new File(path + "/" + className + ".java");
        if (file.exists()) {
            System.err.println("IService: [" + className + "] generator faild! because this file exists!");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();

        // 设置包名
        appendPackage(stringBuilder);
        Set<String> imports = new HashSet<>();
        imports.add(entityGenerator.buildClassType(entity));
        appendImport(stringBuilder, imports);
        // 设置类
        stringBuilder.append(String.format(classTemplate, className));
        // 保存
        stringBuilder.append("\t")
                .append(entityName)
                .append(" save(")
                .append(entityName)
                .append(" ")
                .append(GeneratorUtils.firstToLowerCase(entityName))
                .append(");\n\n");
        // 获取一条数据
        stringBuilder.append("\t")
                .append(entityName)
                .append(" getOne(")
                .append(getIdType()[1])
                .append(" id);\n\n");
        // 删除数据
        stringBuilder.append("\tvoid delete(")
                .append(getIdType()[1])
                .append(" id);\n\n");
        stringBuilder.append("}");

        // 执行生成
        GeneratorUtils.write(file, stringBuilder);

        System.out.println("IService: [" + className + "] generator success!");
    }
}
