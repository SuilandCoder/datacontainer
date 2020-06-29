package com.ngis.udx.data.structure;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName Vector3d
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/6
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
public class Vector3d {
    double x;
    double y;
    double z;

    public Vector3d() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z;
    }
}
