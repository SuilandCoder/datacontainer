package njnu.opengms.container.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName PathConfig
 * @Description todo
 * @Author sun_liber
 * @Date 2019/2/27
 * @Version 1.0.0
 */
@Component
@PropertySource (value = "classpath:application.properties")
@ConfigurationProperties (prefix = "path")
@Data
public class PathConfig {
    private String base;
    private String servicesMap;
    private String servicesRefactor;
    private String storeFiles;
    private String dataProcess;
    private String shapefiles;
    private String geotiffes;
    private String getMeta;
    private String getGdal;
    private String getMiniZip;
    private String tmp;
}
