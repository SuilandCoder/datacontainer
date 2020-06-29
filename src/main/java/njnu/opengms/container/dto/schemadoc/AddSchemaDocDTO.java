package njnu.opengms.container.dto.schemadoc;

import com.ngis.udx.schema.UdxSchema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName AddSchemaDocDTO
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/17
 * @Version 1.0.0
 */
@Data
public class AddSchemaDocDTO {
    @NotBlank (message = "名字不能为空")
    String name;
    @NotBlank (message = "详情不能为空")
    String detailMarkDown;
    @NotBlank (message = "简要描述不能为空")
    String description;

    UdxSchema udxSchema;
}
