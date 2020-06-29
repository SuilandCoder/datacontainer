package njnu.opengms.container.dto.refactormethod;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName UpdateRefactorMethodDTO
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/19
 * @Version 1.0.0
 */
@Data
public class UpdateRefactorMethodDTO {
    @NotBlank (message = "描述不能为空")
    String description;
    @NotBlank (message = "详情不能为空")
    String detailMarkDown;
}
