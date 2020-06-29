package njnu.opengms.container.aspect;

import io.jsonwebtoken.Claims;
import njnu.opengms.container.enums.ResultEnum;
import njnu.opengms.container.exception.MyException;
import njnu.opengms.container.pojo.User;
import njnu.opengms.container.service.UserService;
import njnu.opengms.container.utils.JwtUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName NeedAuthAspect
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
@Aspect
@Component
@Order (2)
public class NeedAuthAspect {
    private static final String TOKEN_PREFIX = "Bearer";

    @Autowired
    UserService userServiceImp;

    @Pointcut ("@annotation(njnu.opengms.container.annotation.NeedAuth)")
    public void poin() {
    }

    @Before ("poin()")
    public void doBeforePoint() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        HttpServletResponse response = attributes.getResponse();

        String auth = JwtUtils.getTokenFromRequest(request);
        if (null == auth || !auth.startsWith(TOKEN_PREFIX)) {
            response.setStatus(401);
            throw new MyException(ResultEnum.NO_TOKEN);
        } else {
            Claims claims = JwtUtils.parseJWT(auth);
            if (claims == null) {
                response.setStatus(401);
                throw new MyException(ResultEnum.TOKEN_WRONG);
            } else {
                User user = userServiceImp.findUserByUserName((String) claims.get("username"));
                if (!user.getPassword().equals(claims.get("password"))) {
                    response.setStatus(401);
                    throw new MyException(ResultEnum.TOKEN_NOT_MATCH);
                }
            }
        }
    }
}
