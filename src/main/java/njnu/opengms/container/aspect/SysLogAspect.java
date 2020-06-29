package njnu.opengms.container.aspect;


import com.alibaba.fastjson.JSON;
import njnu.opengms.container.annotation.SysLogs;
import njnu.opengms.container.pojo.Log;
import njnu.opengms.container.repository.LogRepository;
import njnu.opengms.container.utils.JwtUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @ClassName SysLogAspect
 * @Description 拦截具有SysLogs注解的方法，并进行日志打印
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
@Aspect
@Component
@Order (1)
public class SysLogAspect {

    @Autowired
    LogRepository logRepository;

    @Pointcut ("@annotation(njnu.opengms.container.annotation.SysLogs)")
    public void log() {
    }


    @Before ("log()")
    public void doBeforePoint(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Log log = new Log();
        log.setActionName(getMethodSysLogsAnnotationValue(joinPoint));
        log.setIp(getClientIp(request));
        log.setUri(request.getRequestURI());
        String s = JSON.toJSONString(joinPoint.getArgs());
        log.setParams(s.length() > 500 ? "数据过大，不给予记录" : s);
        log.setHttpMethod(request.getMethod());
        log.setClassMethod(joinPoint.getSignature().getDeclaringTypeName()
                + "." + joinPoint.getSignature().getName() + "()");
        //判断身份是否为空
        String auth = request.getHeader("Authorization");

        if (auth != null) {
            JwtUtils.parseJWT(auth).get("username");
            log.setUserName(String.valueOf(JwtUtils.parseJWT(auth).get("username")));
            log.setUid(String.valueOf(JwtUtils.parseJWT(auth).get("uid")));
        } else {
            log.setUserName("游客");
            log.setUid("0");
        }
        log.setCreateDate(new Date());
        logRepository.insert(log);

    }

    private String getMethodSysLogsAnnotationValue(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(SysLogs.class)) {
            //获取方法上注解中表明的权限
            SysLogs sysLogs = method.getAnnotation(SysLogs.class);
            return sysLogs.value();
        }
        return "未知";
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || "".equals(ip.trim()) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0.0.0.0".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip) || "127.0.0.1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    @After ("log()")
    public void doAfterPoint() {


    }

    @AfterReturning ("log()")
    public void afterReturn() {
    }

}
