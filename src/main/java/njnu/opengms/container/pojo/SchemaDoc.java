package njnu.opengms.container.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ngis.udx.schema.UdxSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @ClassName SchemaDoc
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/15
 * @Version 1.0.0
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchemaDoc {
    @Id
    String id;
    String name;
    String description;
    String detailMarkDown;
    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createDate;
    UdxSchema udxSchema;
}
