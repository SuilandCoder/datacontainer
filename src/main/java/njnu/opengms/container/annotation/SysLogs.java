package njnu.opengms.container.annotation;

import java.lang.annotation.*;

/**
 * @AnnotationName SysLogs
 * @Description 该注解可以用在@Controller层中的方法,指明该方法的调用要记录到日志数据库中
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
@Target (ElementType.METHOD)
@Retention (RetentionPolicy.RUNTIME)
@Documented
public @interface SysLogs {
    String value() default "系统默认打印";
}