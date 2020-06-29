package com.ngis.udx.data.kernels;

import com.ngis.udx.EType;
import com.ngis.udx.data.UdxSingle;
import com.ngis.udx.data.structure.Vector4d;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UdxKernelVector4
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/13
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class UdxKernelVector4 implements UdxSingle<Vector4d> {
    final EType eType = EType.ETYPE_VECTOR4;
    Vector4d value;

    public UdxKernelVector4(Vector4d obj) {

        this.value = obj;
    }


    @Override
    public void set(Vector4d vector4d) {
        this.value = vector4d;
    }

    @Override
    public Vector4d get() {
        return this.value;
    }

    @Override
    public EType returnEType() {
        return eType;
    }
}
