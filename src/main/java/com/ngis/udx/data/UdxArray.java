package com.ngis.udx.data;

import com.ngis.udx.data.structure.Vector2d;
import com.ngis.udx.data.structure.Vector3d;
import com.ngis.udx.data.structure.Vector4d;

import java.util.List;

/**
 * @InterfaceName UdxArray
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/11
 * @Version 1.0.0
 */
public interface UdxArray<T> extends UdxKernel {
    void add(T t);

    void add(int index, T t);

    T getByIndex(int index);

    List get();

    void set(List<T> list);

    default String udxArrayToString(List list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != (list.size() - 1)) {
                stringBuilder.append(list.get(i));
                Object a = list.get(i);
                if (a instanceof Vector2d || a instanceof Vector3d || a instanceof Vector4d) {
                    stringBuilder.append(';');
                } else {
                    stringBuilder.append(',');
                }
            } else {
                stringBuilder.append(list.get(i));
            }
        }
        return stringBuilder.toString();
    }
}
