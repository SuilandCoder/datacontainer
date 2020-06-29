package njnu.opengms.container.dto.mappingmethod;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName AddMappingMethodDTO
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/17
 * @Version 1.0.0
 */
@Data
public class AddMappingMethodDTO {
    @NotBlank (message = "名字不能为空")
    String name;
    @NotBlank (message = "概要描述不能为空")
    String description;
    @NotBlank (message = "详情不能为空")
    String detailMarkDown;
    @NotBlank (message = "映射应该支持一种UdxSchema")
    String supportedUdxSchema;
    @NotBlank (message = "必须上传压缩包，获取其路径")
    String storePath;
}
