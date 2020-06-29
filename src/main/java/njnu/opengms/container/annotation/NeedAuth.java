package njnu.opengms.container.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @AnnotationName NeedAuth
 * @Description 该注解可以用在@Controller层中的方法,指明该方法的调用需要用户验证
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
@Target (ElementType.METHOD)//定义在method上的注解,用于AOP拦截
@Retention (RetentionPolicy.RUNTIME)
public @interface NeedAuth {
}
