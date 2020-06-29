package njnu.opengms.container.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName JsonResult
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel (value = "请求结果响应体")
public class JsonResult<T> {
    @ApiModelProperty (value = "响应状态回执码")
    private Integer code;

    @ApiModelProperty (value = "响应回执消息")
    private String msg;

    @ApiModelProperty (value = "数据体")
    private T data;
}
