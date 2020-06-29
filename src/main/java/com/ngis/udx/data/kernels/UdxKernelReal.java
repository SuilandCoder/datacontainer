package com.ngis.udx.data.kernels;

import com.ngis.udx.EType;
import com.ngis.udx.data.UdxSingle;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName UdxKernelReal
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/13
 * @Version 1.0.0
 */
@Data
@NoArgsConstructor
public class UdxKernelReal implements UdxSingle<Double> {
    final EType eType = EType.ETYPE_REAL;
    Double value;

    public UdxKernelReal(Double obj) {
        this.value = obj;
    }

    @Override
    public void set(Double aDouble) {
        this.value = aDouble;
    }

    @Override
    public Double get() {
        return this.value;
    }

    @Override
    public EType returnEType() {
        return eType;
    }
}
