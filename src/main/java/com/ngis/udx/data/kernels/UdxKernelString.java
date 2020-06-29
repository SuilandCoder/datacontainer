package com.ngis.udx.data.kernels;

import com.ngis.udx.EType;
import com.ngis.udx.data.UdxSingle;
import lombok.Data;

/**
 * @ClassName UdxKernelString
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/12
 * @Version 1.0.0
 */
@Data
public class UdxKernelString implements UdxSingle<String> {
    String value;
    EType eType;

    public UdxKernelString(String obj) {
        this();
        this.value = obj;
    }

    public UdxKernelString() {
        this.eType = EType.ETYPE_STRING;
    }

    @Override
    public void set(String s) {
        this.value = s;
    }

    @Override
    public String get() {
        return this.value;
    }

    @Override
    public EType returnEType() {
        return eType;
    }
}
