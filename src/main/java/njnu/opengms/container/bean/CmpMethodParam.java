package njnu.opengms.container.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 15:34 2019/10/11
 * @Modified By:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmpMethodParam {
    String name;
    String type; // file | double | int | String | boolean
    String desc;
    boolean optional;
    String value;
}
