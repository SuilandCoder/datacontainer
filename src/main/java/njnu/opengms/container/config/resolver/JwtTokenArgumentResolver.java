package njnu.opengms.container.config.resolver;


import njnu.opengms.container.annotation.JwtToken;
import njnu.opengms.container.utils.JwtUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * @ClassName JwtTokenArgumentResolver
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/14
 * @Version 1.0.0
 */
public class JwtTokenArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(JwtToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String authorization = JwtUtils.getTokenFromRequest(request);
        String result = null;
        JwtToken jwtToken = null;
        if (authorization != null) {
            Annotation[] methodAnnotations = methodParameter.getParameterAnnotations();
            for (Annotation methodAnnotation : methodAnnotations) {
                if (methodAnnotation instanceof JwtToken) {
                    jwtToken = (JwtToken) methodAnnotation;
                    break;
                }
            }
            if (jwtToken != null) {
                return JwtUtils.parseJWT(authorization, jwtToken.key());
            }
        }
        return result;
    }
}
