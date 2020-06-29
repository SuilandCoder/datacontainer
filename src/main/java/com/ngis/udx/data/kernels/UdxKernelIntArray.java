package com.ngis.udx.data.kernels;

import com.ngis.udx.EType;
import com.ngis.udx.data.UdxArray;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UdxKernelIntArray
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/11
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class UdxKernelIntArray implements UdxArray<Integer> {
    final EType eType = EType.ETYPE_INT_ARRAY;
    List<Integer> value = new ArrayList<>();


    public UdxKernelIntArray(List<Integer> value) {
        this();
        this.value = value;
    }

    @Override
    public void add(Integer integer) {
        this.value.add(integer);
    }

    @Override
    public void add(int index, Integer integer) {
        this.value.add(index, integer);
    }

    @Override
    public Integer getByIndex(int index) {
        return this.value.get(index);
    }

    @Override
    public List get() {
        return this.value;
    }

    @Override
    public void set(List<Integer> list) {
        this.value = list;
    }

    @Override
    public EType returnEType() {
        return eType;
    }

}
