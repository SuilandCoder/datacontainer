package com.ngis.udx.data;

import com.ngis.udx.EType;

/**
 * @InterfaceName IFactory
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/12
 * @Version 1.0.0
 */
public interface IFactory {
    UdxArray createUdxArray(EType eType);

    UdxSingle createUdxSingle(EType eType);

    UdxArray createUdxArray(EType eType, Object object);

    UdxSingle createUdxSingle(EType eType, Object object);

    UdxKernel createContainer(EType eType);
}
