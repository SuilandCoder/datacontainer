package com.ngis.udx.data.kernels;

import com.ngis.udx.EType;
import com.ngis.udx.data.UdxSingle;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UdxKernelInt
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/11
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class UdxKernelInt implements UdxSingle<Integer> {
    final EType eType = EType.ETYPE_INT;
    Integer value;


    public UdxKernelInt(Integer obj) {
        this();
        this.value = obj;
    }

    @Override
    public void set(Integer integer) {
        this.value = integer;
    }

    @Override
    public Integer get() {
        return this.value;
    }

    @Override
    public EType returnEType() {
        return eType;
    }
}
