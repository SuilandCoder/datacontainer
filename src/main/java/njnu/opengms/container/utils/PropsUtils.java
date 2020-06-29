package njnu.opengms.container.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @ClassName PropsUtils
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
public class PropsUtils {
    public static String getProps(String type) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
        Properties prop = new Properties();
        try {
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(type);
    }
}
