import com.lamtsing.utils.generator.mybatis.Generator;

/**
 * @author Lamtsing
 */
public class MyBatisPlusTest {

    public static void main(String[] args) {
        // 初始化生成器
        Generator generator = new Generator("com.lamtsing.utils.generator","User");

        // 初始化dao生成器
        generator.setMapperInit("com.lamtsing.utils.generator.mapstruct", null);
        // 初始化控制层生成器
        generator.setResourceInit("com.lamtsing.utils.generator.mapstruct", null);
        // 初始化service接口生成器
        generator.setServiceInit("com.lamtsing.utils.generator.mapstruct", null);
        // 初始化service实现类生成器
        generator.setServiceImplInit("com.lamtsing.utils.generator.mapstruct", null);

        // 执行生成
        generator.generator();
    }
}
