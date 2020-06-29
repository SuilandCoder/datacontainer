package com.ngis.udx.data;

import com.ngis.udx.EType;
import com.ngis.udx.data.kernels.*;
import com.ngis.udx.data.kernels.container.UdxKernelList;
import com.ngis.udx.data.kernels.container.UdxKernelMap;
import com.ngis.udx.data.kernels.container.UdxKernelNode;
import com.ngis.udx.data.kernels.container.UdxKernelTable;
import com.ngis.udx.data.structure.Vector2d;
import com.ngis.udx.data.structure.Vector3d;
import com.ngis.udx.data.structure.Vector4d;

import java.util.List;

/**
 * @ClassName Factory
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/12
 * @Version 1.0.0
 */
public class Factory implements IFactory {
    @Override
    public UdxArray createUdxArray(EType eType) {
        switch (eType) {
            case ETYPE_INT_ARRAY:
                return new UdxKernelIntArray();
            case ETYPE_REAL_ARRAY:
                return new UdxKernelRealArray();
            case ETYPE_STRING_ARRAY:
                return new UdxKernelStringArray();
            case ETYPE_VECTOR2_ARRAY:
                return new UdxKernelVector2Array();
            case ETYPE_VECTOR3_ARRAY:
                return new UdxKernelVector3Array();
            case ETYPE_VECTOR4_ARRAY:
                return new UdxKernelVector4Array();
            default:
                return null;
        }
    }

    @Override
    public UdxSingle createUdxSingle(EType eType) {
        switch (eType) {
            case ETYPE_INT:
                return new UdxKernelInt();
            case ETYPE_REAL:
                return new UdxKernelReal();
            case ETYPE_STRING:
                return new UdxKernelString();
            case ETYPE_VECTOR2:
                return new UdxKernelVector2();
            case ETYPE_VECTOR3:
                return new UdxKernelVector3();
            case ETYPE_VECTOR4:
                return new UdxKernelVector4();
            default:
                return null;
        }
    }

    @Override
    public UdxArray createUdxArray(EType eType, Object object) {
        switch (eType) {
            case ETYPE_INT_ARRAY:
                return new UdxKernelIntArray((List<Integer>) object);
            case ETYPE_REAL_ARRAY:
                return new UdxKernelRealArray((List<Double>) object);
            case ETYPE_STRING_ARRAY:
                return new UdxKernelStringArray((List<String>) object);
            case ETYPE_VECTOR2_ARRAY:
                return new UdxKernelVector2Array((List<Vector2d>) object);
            case ETYPE_VECTOR3_ARRAY:
                return new UdxKernelVector3Array((List<Vector3d>) object);
            case ETYPE_VECTOR4_ARRAY:
                return new UdxKernelVector4Array((List<Vector4d>) object);
            default:
                return null;
        }
    }

    @Override
    public UdxSingle createUdxSingle(EType eType, Object object) {
        switch (eType) {
            case ETYPE_INT:
                return new UdxKernelInt((Integer) object);
            case ETYPE_REAL:
                return new UdxKernelReal((Double) object);
            case ETYPE_STRING:
                return new UdxKernelString((String) object);
            case ETYPE_VECTOR2:
                return new UdxKernelVector2((Vector2d) object);
            case ETYPE_VECTOR3:
                return new UdxKernelVector3((Vector3d) object);
            case ETYPE_VECTOR4:
                return new UdxKernelVector4((Vector4d) object);
            default:
                return null;
        }
    }

    @Override
    public UdxKernel createContainer(EType eType) {
        switch (eType) {
            case ETYPE_NODE:
                return new UdxKernelNode();
            case ETYPE_LIST:
                return new UdxKernelList();
            case ETYPE_TABLE:
                return new UdxKernelTable();
            case ETYPE_MAP:
                return new UdxKernelMap();
            default:
                return null;
        }
    }


}
