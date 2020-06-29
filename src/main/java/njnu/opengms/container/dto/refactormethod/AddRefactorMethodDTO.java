package njnu.opengms.container.dto.refactormethod;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @ClassName AddRefactorMethodDTO
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/17
 * @Version 1.0.0
 */
@Data
public class AddRefactorMethodDTO {
    @NotBlank (message = "名字不能为空")
    String name;
    @NotBlank (message = "概要描述不能为空")
    String description;
    @NotBlank (message = "详情不能为空")
    String detailMarkDown;

    @Size (min = 1, message = "重构方法至少支持一种UdxSchema")
    List<String> supportedUdxSchemas;

    @NotBlank (message = "必须上传压缩包，获取其路径")
    String storePath;
}
