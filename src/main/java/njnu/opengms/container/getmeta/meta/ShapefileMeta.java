package njnu.opengms.container.getmeta.meta;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ShapefileMeta
 * @Description todo
 * @Author sun_liber
 * @Date 2019/4/4
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class ShapefileMeta extends SpatialMeta {
    int featureCount;
    List<Map<String, String>> fields = new ArrayList<>();
    String geometry;
}
