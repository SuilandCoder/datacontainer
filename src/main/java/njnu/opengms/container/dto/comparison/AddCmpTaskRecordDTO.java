package njnu.opengms.container.dto.comparison;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import njnu.opengms.container.bean.CmpMethodData;
import njnu.opengms.container.bean.CmpMethodParam;
import njnu.opengms.container.dto.dataresource.AddDataResourceDTO;
import njnu.opengms.container.pojo.DataResource;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 15:28 2019/10/11
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCmpTaskRecordDTO {
//    String recordId;
    String methodOid;
    String status;
    String timeSpan;
    List<CmpMethodParam> params;
    List<CmpMethodData> inputList;
    DataResource outputDataInfo;
}
