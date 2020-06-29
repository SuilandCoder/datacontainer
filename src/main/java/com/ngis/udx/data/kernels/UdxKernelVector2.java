package com.ngis.udx.data.kernels;

import com.ngis.udx.EType;
import com.ngis.udx.data.UdxSingle;
import com.ngis.udx.data.structure.Vector2d;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UdxKernelVector2
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/13
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class UdxKernelVector2 implements UdxSingle<Vector2d> {
    final EType eType = EType.ETYPE_VECTOR2;
    Vector2d value;

    public UdxKernelVector2(Vector2d obj) {
        this.value = obj;
    }

    @Override
    public void set(Vector2d vector2d) {
        this.value = vector2d;
    }

    @Override
    public Vector2d get() {
        return this.value;
    }

    @Override
    public EType returnEType() {
        return eType;
    }
}
