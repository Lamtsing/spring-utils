import com.lamtsing.utils.generator.Generator;

/**
 * @author Lamtsing
 */
public class test {

    public static void main(String[] args) {
        Generator generator = new Generator("com.lamtsing.utils.generator","User");
        generator.setDtoInit("com.lamtsing.utils.generator.dto",null);
        generator.generator();
    }
}
