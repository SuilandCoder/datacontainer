package njnu.opengms.container.dto.dataresource;

import lombok.Data;
import njnu.opengms.container.dto.SplitPageDTO;

/**
 * @ClassName FindDataResourceDTO
 * @Description todo
 * @Author sun_liber
 * @Date 2019/2/13
 * @Version 1.0.0
 */
@Data
public class FindDataResourceDTO extends SplitPageDTO {
    String type;
    String value;
}
