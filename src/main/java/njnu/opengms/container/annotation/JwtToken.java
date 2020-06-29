package njnu.opengms.container.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @AnnotationName JwtToken
 * @Description 该注解可以用在@Controller层中方法的参数中定义，可获取"uid","username","password"
 * @Date 2018/11/14
 * @Version 1.0.0
 */
@Target (ElementType.PARAMETER)
@Retention (RetentionPolicy.RUNTIME)
public @interface JwtToken {
    String key() default "uid";
}
