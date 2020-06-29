package com.ngis.udx.data.kernels;

import com.ngis.udx.EType;
import com.ngis.udx.data.UdxArray;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UdxKernelRealArray
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/13
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class UdxKernelRealArray implements UdxArray<Double> {

    final EType eType = EType.ETYPE_REAL_ARRAY;
    List<Double> value = new ArrayList<>();

    public UdxKernelRealArray(List<Double> value) {
        this.value = value;
    }

    @Override
    public void add(Double aDouble) {
        this.value.add(aDouble);
    }

    @Override
    public void add(int index, Double aDouble) {
        this.value.add(index, aDouble);
    }

    @Override
    public Double getByIndex(int index) {
        return this.value.get(index);
    }

    @Override
    public List get() {
        return this.value;
    }

    @Override
    public void set(List<Double> list) {
        this.value = list;
    }

    @Override
    public EType returnEType() {
        return eType;
    }
}
