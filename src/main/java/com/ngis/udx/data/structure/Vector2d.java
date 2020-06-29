package com.ngis.udx.data.structure;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName Vector2d
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/6
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
public class Vector2d {

    double x;
    double y;

    public Vector2d() {
        x = 0;
        y = 0;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
