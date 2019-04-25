import com.lamtsing.utils.generator.Generator;

/**
 * @author Lamtsing
 */
public class test {

    public static void main(String[] args) {
        Generator generator = new Generator("com.lamtsing.utils.generator","User");
        generator.setDtoInit("com.lamtsing.utils.generator.dto",null);
        generator.setMapstructInit("com.lamtsing.utils.generator.mapstruct", null);
        generator.setRepositoryInit("com.lamtsing.utils.generator.mapstruct", null);
        generator.setResourceInit("com.lamtsing.utils.generator.mapstruct", null);
        generator.setServiceInit("com.lamtsing.utils.generator.service", null);
        generator.setServiceImplInit("com.lamtsing.utils.generator.service", null);
        generator.generator();
    }
}
