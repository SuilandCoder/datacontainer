package com.ngis.udx.data.structure;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName Vector4d
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/6
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
public class Vector4d {
    double x;
    double y;
    double z;
    double m;

    public Vector4d() {
        x = 0;
        y = 0;
        z = 0;
        m = 0;
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z + "," + m;
    }
}
