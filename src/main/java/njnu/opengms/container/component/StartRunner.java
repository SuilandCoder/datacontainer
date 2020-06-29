package njnu.opengms.container.component;

import org.springframework.boot.CommandLineRunner;

/**
 * @ClassName StartRunner
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
public class StartRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        for (String arg : args) {
            System.out.println(arg);
        }
    }
}
