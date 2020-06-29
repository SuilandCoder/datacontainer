package com.ngis.udx.data.kernels;

import com.ngis.udx.EType;
import com.ngis.udx.data.UdxArray;
import com.ngis.udx.data.structure.Vector4d;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UdxKernelVector4Array
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/13
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class UdxKernelVector4Array implements UdxArray<Vector4d> {

    final EType eType = EType.ETYPE_VECTOR4_ARRAY;
    List<Vector4d> value = new ArrayList<>();


    public UdxKernelVector4Array(List<Vector4d> value) {
        this.value = value;
    }

    @Override
    public void add(Vector4d vector4d) {
        this.value.add(vector4d);
    }

    @Override
    public void add(int index, Vector4d vector4d) {
        this.value.add(index, vector4d);
    }

    @Override
    public Vector4d getByIndex(int index) {
        return this.value.get(index);
    }

    @Override
    public List get() {
        return this.value;
    }

    @Override
    public void set(List<Vector4d> list) {
        this.value = list;
    }

    @Override
    public EType returnEType() {
        return eType;
    }
}
