package njnu.opengms.container.dto.mappingmethod;

import lombok.Data;
import njnu.opengms.container.dto.SplitPageDTO;

/**
 * @ClassName FindMappingMethodDTO
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/19
 * @Version 1.0.0
 */
@Data
public class FindMappingMethodDTO extends SplitPageDTO {
    String name;
    String description;
}
