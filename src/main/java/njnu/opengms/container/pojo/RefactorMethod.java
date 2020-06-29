package njnu.opengms.container.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @ClassName RefactorMethod
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/17
 * @Version 1.0.0
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefactorMethod {
    @Id
    String id;
    String name;
    String description;
    String detailMarkDown;
    Date createDate;
    List<String> supportedUdxSchemas;
    String storePath;

    /**
     * 获取重构服务的真正调用文件夹
     *
     * @return
     */
    public String getInvokePosition() {
        return storePath.substring(0, storePath.lastIndexOf("\\")) + File.separator + "invoke";
    }
}
