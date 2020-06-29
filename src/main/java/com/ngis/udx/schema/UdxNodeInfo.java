package com.ngis.udx.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UdxNodeInfo
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/11
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UdxNodeInfo {
    String conceptTag;
    String unitTag;
    String dimensionTag;
    String spatialRefTag;
    String dataTemplateTag;
}
