package njnu.opengms.container.config;


import njnu.opengms.container.condition.WindowsCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName BeanConfig
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/14
 * @Version 1.0.0
 */
@Configuration
public class ConditionalConfig {
    //根据条件注入Bean
    @Bean
    @Conditional (WindowsCondition.class)
    public String windowsString() {
        return new String("windows条件");
    }


}
