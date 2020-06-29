package njnu.opengms.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


/**
 * @author sun_liber
 */
@SpringBootApplication
@ServletComponentScan
public class ContainerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContainerApplication.class, args);
    }
}
