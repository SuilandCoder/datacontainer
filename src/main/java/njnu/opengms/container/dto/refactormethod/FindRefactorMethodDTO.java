package njnu.opengms.container.dto.refactormethod;

import lombok.Data;
import njnu.opengms.container.dto.SplitPageDTO;

/**
 * @ClassName FindRefactorMethodDTO
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/19
 * @Version 1.0.0
 */
@Data
public class FindRefactorMethodDTO extends SplitPageDTO {
    String name;
    String description;
}
