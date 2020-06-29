package njnu.opengms.container.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName SplitPageDTO
 * @Description todo
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
@Data
public abstract class SplitPageDTO {
    private Integer page = 0;
    private Integer pageSize = 10;
    private Boolean asc = false;
    private List<String> properties;
}
