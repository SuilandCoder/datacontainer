package com.ngis.udx.data.kernels;

import com.ngis.udx.EType;
import com.ngis.udx.data.UdxSingle;
import com.ngis.udx.data.structure.Vector3d;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UdxKernelVector3
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/13
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class UdxKernelVector3 implements UdxSingle<Vector3d> {
    final EType eType = EType.ETYPE_VECTOR3;
    Vector3d value;


    public UdxKernelVector3(Vector3d obj) {

        this.value = obj;
    }

    @Override
    public void set(Vector3d vector3d) {
        this.value = vector3d;
    }

    @Override
    public Vector3d get() {
        return this.value;
    }

    @Override
    public EType returnEType() {
        return eType;
    }
}
