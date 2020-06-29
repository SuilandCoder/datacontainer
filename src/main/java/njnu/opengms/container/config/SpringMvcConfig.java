package njnu.opengms.container.config;


import njnu.opengms.container.config.converter.MyConverter;
import njnu.opengms.container.config.interceptor.MyInterceptor;
import njnu.opengms.container.config.resolver.JwtTokenArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * @ClassName SpringMVCConfig
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/14
 * @Version 1.0.0
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //URL路径可以取后缀
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/store/**").addResourceLocations("file:E:/store/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(false)
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(3600);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //在这里可以写路径跳转到view
        //但是我们考虑前后端分离所以暂时不使用
//        registry.addViewController("/index").setViewName("/index");
//        registry.addViewController("/sse").setViewName("/sse");
    }

    /**
     * @param resolvers
     *
     * @Description 添加参数resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JwtTokenArgumentResolver());
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MyConverter());
    }
}
