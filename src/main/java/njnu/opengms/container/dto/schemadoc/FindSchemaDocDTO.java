package njnu.opengms.container.dto.schemadoc;

import lombok.Data;
import njnu.opengms.container.dto.SplitPageDTO;

/**
 * @ClassName FindSchemaDocDTO
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/19
 * @Version 1.0.0
 */
@Data
public class FindSchemaDocDTO extends SplitPageDTO {
    String name;
    String description;
}
