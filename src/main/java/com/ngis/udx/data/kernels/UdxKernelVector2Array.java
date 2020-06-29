package com.ngis.udx.data.kernels;

import com.ngis.udx.EType;
import com.ngis.udx.data.UdxArray;
import com.ngis.udx.data.structure.Vector2d;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UdxKernelVector2Array
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/13
 * @Version 1.0.0
 */
@Data
public class UdxKernelVector2Array implements UdxArray<Vector2d> {
    List<Vector2d> value = new ArrayList<>();
    EType eType = EType.ETYPE_VECTOR2_ARRAY;

    public UdxKernelVector2Array(List<Vector2d> obj) {
        this();
        this.value = obj;
    }

    public UdxKernelVector2Array() {
        this.eType = EType.ETYPE_VECTOR2_ARRAY;
    }

    @Override
    public void add(Vector2d vector2d) {
        this.value.add(vector2d);
    }

    @Override
    public void add(int index, Vector2d vector2d) {
        this.value.add(index, vector2d);
    }

    @Override
    public Vector2d getByIndex(int index) {
        return this.value.get(index);
    }

    @Override
    public List get() {
        return this.value;
    }

    @Override
    public void set(List<Vector2d> list) {
        this.value = list;
    }

    @Override
    public EType returnEType() {
        return eType;
    }


}
