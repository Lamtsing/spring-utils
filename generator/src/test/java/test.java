import com.lamtsing.utils.generator.Generator;

/**
 * @author Lamtsing
 */
public class test {

    public static void main(String[] args) {
        // 初始化生成器
        Generator generator = new Generator("com.lamtsing.utils.generator","User");

        // 初始化dto生成器
        generator.setDtoInit("com.lamtsing.utils.generator.dto",null);
        // 初始化mapstruct生成器
        generator.setMapstructInit("com.lamtsing.utils.generator.mapstruct", null);
        // 初始化dao生成器
        generator.setRepositoryInit("com.lamtsing.utils.generator.mapstruct", null);
        // 初始化控制层生成器
        generator.setControllerInit("com.lamtsing.utils.generator.mapstruct", null);
        // 初始化service接口生成器
        generator.setServiceInit("com.lamtsing.utils.generator.service", null);
        // 初始化service实现类生成器
        generator.setServiceImplInit("com.lamtsing.utils.generator.service", null);

        // 执行生成
        generator.generator();
    }
}
