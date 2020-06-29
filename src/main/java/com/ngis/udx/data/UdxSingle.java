package com.ngis.udx.data;

/**
 * @InterfaceName UdxSingle
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/11
 * @Version 1.0.0
 */
public interface UdxSingle<T> extends UdxKernel {
    void set(T t);

    T get();


    default String udxSingleToString(T t) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(t);
        return stringBuilder.toString();
    }
}
