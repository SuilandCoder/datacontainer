package njnu.opengms.container.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @ClassName SysLogRepository
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/14
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Log {
    @Id
    String id;
    String uid;
    String actionName;
    String ip;
    String uri;
    String params;
    String httpMethod;
    String classMethod;
    String userName;
    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createDate;
}
