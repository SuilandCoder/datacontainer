package njnu.opengms.container.getmeta;

import java.io.File;
import java.io.IOException;

/**
 * @InterfaceName DataStoreMetaGet
 * @Description todo
 * @Author sun_liber
 * @Date 2019/4/2
 * @Version 1.0.0
 */
public interface DataStoreMetaGet<T> {
    T getMeta(File file) throws IOException;
}
