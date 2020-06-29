package njnu.opengms.container.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName GeoserverConfig
 * @Description todo
 * @Author sun_liber
 * @Date 2019/2/21
 * @Version 1.0.0
 */
@Component
@PropertySource (value = "classpath:application.properties")
@ConfigurationProperties (prefix = "geoserver")
@Data
public class GeoserverConfig {
    private String basicURL;
    private String username;
    private String password;
    private String shapefiles;
    private String geotiffes;
}
