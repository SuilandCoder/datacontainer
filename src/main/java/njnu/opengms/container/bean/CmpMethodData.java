package njnu.opengms.container.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 15:35 2019/10/11
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmpMethodData {
    String type;
    String oid;
    String instanceId;
    String cmpDataId;
    String dcSourceStoreId;
}
