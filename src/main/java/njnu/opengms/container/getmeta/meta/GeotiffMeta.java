package njnu.opengms.container.getmeta.meta;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName GeotiffMeta
 * @Description todo
 * @Author sun_liber
 * @Date 2019/4/4
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class GeotiffMeta extends SpatialMeta {
    int[] low;
    int[] high;
    int bandCount;
    double[] pixelScales;
}
