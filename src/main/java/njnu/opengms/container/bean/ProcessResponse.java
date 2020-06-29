package njnu.opengms.container.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ProcessResponse
 * @Description todo
 * @Author sun_liber
 * @Date 2019/1/9
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * info 是进程打印的信息
 * operator 是执行的CMD命令
 * flag 标识
 */
public class ProcessResponse {
    String info;
    String operator;
    Boolean flag;
}
