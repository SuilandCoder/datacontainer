package njnu.opengms.container.getmeta.meta;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SpatialMeta
 * @Description todo
 * @Author sun_liber
 * @Date 2019/4/4
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class SpatialMeta extends Meta {
    String proj;
    double[] lowerCorner;
    double[] upperCorner;
}
