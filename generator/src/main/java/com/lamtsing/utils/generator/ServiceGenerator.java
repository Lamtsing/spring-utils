package com.lamtsing.utils.generator;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author Lamtsing
 */
@Getter
@Setter
public class ServiceGenerator extends AbstractGenerator {

    private String classTemplate = "/**%n * @author Lamtsing-Generator%n */%npublic interface %s {%n%n";

    private DtoGenerator dtoGenerator;

    public ServiceGenerator() {
        setSuffix("Service");
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
        String entityName = entity.getSimpleName();
        File file = new File(path + "/" + className + ".java");
        if (file.exists()) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();

        // 设置包名
        appendPackage(stringBuilder);
        // 设置类
        stringBuilder.append(String.format(classTemplate, className));
        // 构造dto名字
        String dto = dtoGenerator.buildClassName(entity);
        // 保存
        stringBuilder.append("\t")
                .append(dto)
                .append(" save(")
                .append(dto)
                .append(" ")
                .append(GeneratorUtils.firstToLowerCase(dto))
                .append(");\n\n");
        // 获取一条数据
        stringBuilder.append("\t")
                .append(dto)
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
    }
}
