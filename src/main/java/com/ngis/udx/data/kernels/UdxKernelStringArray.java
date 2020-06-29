package com.ngis.udx.data.kernels;

import com.ngis.udx.EType;
import com.ngis.udx.data.UdxArray;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UdxKernelStringArray
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/13
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class UdxKernelStringArray implements UdxArray<String> {

    final EType eType = EType.ETYPE_STRING_ARRAY;
    List<String> value = new ArrayList<>();


    public UdxKernelStringArray(List<String> value) {
        this.value = value;
    }

    @Override
    public void add(String s) {
        this.value.add(s);
    }

    @Override
    public void add(int index, String s) {
        this.value.add(index, s);
    }

    @Override
    public String getByIndex(int index) {
        return this.value.get(index);
    }

    @Override
    public List get() {
        return this.value;
    }

    @Override
    public void set(List<String> list) {
        this.value = list;
    }

    @Override
    public EType returnEType() {
        return eType;
    }
}
