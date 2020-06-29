package com.ngis.udx.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UdxSchema
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/12
 * @Version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UdxSchema {
    String name;
    String description;
    List<UdxNodeSchema> udxNodeSchemas = new ArrayList<>();
}
