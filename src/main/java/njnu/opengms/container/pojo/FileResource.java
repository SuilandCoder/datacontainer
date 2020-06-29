package njnu.opengms.container.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @ClassName FileResource
 * @Description todo
 * @Author sun_liber
 * @Date 2019/4/9
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class FileResource {
    @Id
    String id;
    String md5;
    String sourceStoreId;
}
