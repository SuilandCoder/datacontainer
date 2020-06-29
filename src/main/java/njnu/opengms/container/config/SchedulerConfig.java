package njnu.opengms.container.config;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName SchedulerConfig
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/15
 * @Version 1.0.0
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {


    @Value ("${web.upload-path}")
    String upload;

    @Scheduled (cron = "0 0 3 * * ?")
    public void Schedule() throws IOException {
        System.out.println("Fighting OpenMGS");
        FileUtils.cleanDirectory(new File(upload + File.separator + "data_process"));
    }
}
