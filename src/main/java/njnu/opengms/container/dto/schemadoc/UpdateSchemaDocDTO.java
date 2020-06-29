package njnu.opengms.container.dto.schemadoc;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName UpdateSchemaDocDTO
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/19
 * @Version 1.0.0
 */
@Data
public class UpdateSchemaDocDTO {
    @NotBlank (message = "描述不能为空")
    String description;
    @NotBlank (message = "详情不能为空")
    String detailMarkDown;
}
