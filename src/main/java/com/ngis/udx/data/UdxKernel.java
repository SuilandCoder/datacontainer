package com.ngis.udx.data;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ngis.udx.EType;

/**
 * @InterfaceName UdxKernel
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/13
 * @Version 1.0.0
 */
@JsonTypeInfo (use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public interface UdxKernel {
    EType returnEType();
}
