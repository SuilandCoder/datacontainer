package com.ngis.udx.data.kernels;

import com.ngis.udx.EType;
import com.ngis.udx.data.UdxArray;
import com.ngis.udx.data.structure.Vector3d;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UdxKernelVector3Array
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/13
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class UdxKernelVector3Array implements UdxArray<Vector3d> {

    final EType eType = EType.ETYPE_VECTOR3_ARRAY;
    List<Vector3d> value = new ArrayList<>();
    ;


    public UdxKernelVector3Array(List<Vector3d> value) {

        this.value = value;
    }

    @Override
    public void add(Vector3d vector3d) {
        this.value.add(vector3d);
    }

    @Override
    public void add(int index, Vector3d vector3d) {
        this.value.add(index, vector3d);
    }

    @Override
    public Vector3d getByIndex(int index) {
        return this.value.get(index);
    }

    @Override
    public List get() {
        return this.value;
    }

    @Override
    public void set(List<Vector3d> list) {
        this.value = list;
    }

    @Override
    public EType returnEType() {
        return eType;
    }
}
