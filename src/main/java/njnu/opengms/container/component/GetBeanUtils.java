package njnu.opengms.container.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class GetBeanUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (GetBeanUtils.getApplicationContext() == null) {
            GetBeanUtils.applicationContext = applicationContext;
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
