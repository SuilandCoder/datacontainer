package njnu.opengms.container.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import njnu.opengms.container.bean.CmpMethodData;
import njnu.opengms.container.bean.CmpMethodParam;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 16:05 2019/10/11
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class CmpTaskRecord {
    @Id
    String recordId;
    String methodOid;
    String status;
    String timeSpan;
    List<CmpMethodParam> params;
    List<CmpMethodData> inputList;
    DataResource outputDataInfo;
}
