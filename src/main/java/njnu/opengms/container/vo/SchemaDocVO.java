package njnu.opengms.container.vo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName SchemaDocVO
 * @Description schemaDocVO是前端用户展示列表信息时的Vo, 因此schemaDoc的部分字段可不对外暴露, 部分字段不用暴漏
 * @Author sun_liber
 * @Date 2018/12/19
 * @Version 1.0.0
 */
@Data
public class SchemaDocVO {
    String id;
    String name;
    String description;
    Date createDate;
}
