package com.lamtsing.utils.generator.jpa;

import com.lamtsing.utils.generator.AbstractGenerator;
import com.lamtsing.utils.generator.EntityGenerator;
import com.lamtsing.utils.generator.GeneratorUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Lamtsing
 */
@Getter
@Setter
public class ServiceImplGenerator extends AbstractGenerator {

    private String classTemplate = "/**%n * @author Lamtsing-Generator%n */%n@Service%n@Transactional%npublic class %s implements %s {%n%n";

    private EntityGenerator entityGenerator;
    private DtoGenerator dtoGenerator;
    private MapstructGenerator mapstructGenerator;
    private RepositoryGenerator repositoryGenerator;
    private ServiceGenerator serviceGenerator;

    public ServiceImplGenerator() {
        setSuffix("ServiceImpl");
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
            System.err.println("ServiceImpl: [" + className + "] generator faild! because this file exists!");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();

        // 设置包名
        appendPackage(stringBuilder);
        // 设置引入
        String repository = repositoryGenerator.buildClassName(entity);
        String mapstruct = mapstructGenerator.buildClassName(entity);
        String dto = dtoGenerator.buildClassName(entity);
        String entitySimpleName = entity.getSimpleName();
        String entityField = GeneratorUtils.firstToLowerCase(entitySimpleName);
        String dtoField = GeneratorUtils.firstToLowerCase(dto);
        String repositoryField = GeneratorUtils.firstToLowerCase(repository);
        String mapstructField = GeneratorUtils.firstToLowerCase(mapstruct);
        Set<String> imports = new HashSet<>();
        imports.add(dtoGenerator.buildClassType(entity));
        imports.add(entityGenerator.buildClassType(entity));
        imports.add(mapstructGenerator.buildClassType(entity));
        imports.add(repositoryGenerator.buildClassType(entity));
        imports.add(serviceGenerator.buildClassType(entity));
        imports.add(Service.class.getTypeName());
        imports.add(Transactional.class.getTypeName());
        imports.add(Resource.class.getTypeName());
        appendImport(stringBuilder, imports);
        // 设置类名
        stringBuilder.append(String.format(classTemplate, className, serviceGenerator.buildClassName(entity)));
        // 设置dao
        stringBuilder.append("\t@Resource\n\tprivate ")
                .append(repository)
                .append(" ")
                .append(repositoryField)
                .append(";\n\n");
        // 设置mapstruct
        stringBuilder.append("\t@Resource\n\tprivate ")
                .append(mapstruct)
                .append(" ")
                .append(mapstructField)
                .append(";\n\n");
        // 保存
        stringBuilder.append("\t@Override\n\tpublic ")
                .append(dto)
                .append(" save(")
                .append(dto)
                .append(" ")
                .append(dtoField)
                .append(") {\n\t\t")
                .append(entitySimpleName)
                .append(" ")
                .append(entityField)
                .append(" = ")
                .append(mapstructField)
                .append(".toEntity(")
                .append(dtoField)
                .append(");\n\t\t")
                .append(repositoryField)
                .append(".save(")
                .append(entityField)
                .append(");\n\t\treturn ")
                .append(mapstructField)
                .append(".toDto(")
                .append(entityField)
                .append(");\n\t}\n\n");
        // 获取一条数据
        stringBuilder.append("\t@Override\n\tpublic ")
                .append(dto)
                .append(" getOne(")
                .append(getIdType()[1])
                .append(" id) {\n\t\t")
                .append(entity.getSimpleName())
                .append(" ")
                .append(entityField)
                .append(" = ")
                .append(repositoryField)
                .append(".findOneById(id);\n\t\treturn ")
                .append(mapstructField)
                .append(".toDto(")
                .append(entityField)
                .append(");\n\t}\n\n");
        // 删除数据
        stringBuilder.append("\t@Override\n\tpublic void delete(")
                .append(getIdType()[1])
                .append(" id) {\n\t\t")
                .append(repositoryField)
                .append(".deleteById(id);\n\t}\n\n");
        stringBuilder.append("}");

        // 执行生成
        GeneratorUtils.write(file, stringBuilder);

        System.out.println("ServiceImpl: [" + className + "] generator success!");
    }
}
