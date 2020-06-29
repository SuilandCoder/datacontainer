package com.ngis.udx;

/**
 * @EnumName EType
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/10
 * @Version 1.0.0
 */
public enum EType {
    ETYPE_INT(2, "DTKT_INT", "int"),
    ETYPE_REAL(4, "DTKT_REAL", "real"),
    ETYPE_STRING(8, "DTKT_STRING", "string"),
    ETYPE_VECTOR2(16, "DTKT_VECTOR_2D", "vector2d"),
    ETYPE_VECTOR3(32, "DTKT_VECTOR_3D", "vector3d"),
    ETYPE_VECTOR4(64, "DTKT_VECTOR_4D", "vector4d"),

    ETYPE_NODE(128, "DTKT_ANY", "any"),
    ETYPE_LIST(256, "DTKT_LIST", "list"),

    //大于256 小于512
    //258~320
    ETYPE_INT_ARRAY(2 | 256, "DTKT_INT | DTKT_LIST", "int_array"),
    ETYPE_REAL_ARRAY(4 | 256, "DTKT_REAL | DTKT_LIST", "real_array"),
    ETYPE_STRING_ARRAY(8 | 256, "DTKT_STRING | DTKT_LIST", "string_array"),
    ETYPE_VECTOR2_ARRAY(16 | 256, "DTKT_VECTOR2D | DTKT_LIST", "vector2d_array"),
    ETYPE_VECTOR3_ARRAY(32 | 256, "DTKT_VECTOR3D | DTKT_LIST", "vector3d_array"),
    ETYPE_VECTOR4_ARRAY(64 | 256, "DTKT_VECTOR4D | DTKT_LIST", "vector4d_array"),

    ETYPE_MAP(512, "DTKT_MAP", "map"),
    ETYPE_TABLE(1024, "DTKT_TABLE", "table");


    private int value;
    private String eSchemaType;
    private String eKernelType;

    EType(int value, String eSchemaType, String eKernelType) {
        this.value = value;
        this.eSchemaType = eSchemaType;
        this.eKernelType = eKernelType;
    }

    public static boolean checkSingle(EType eType) {
        if (eType.getValue() <= 64) {
            return true;
        }
        return false;
    }

    public int getValue() {
        return value;
    }

    public static boolean checkArray(EType eType) {
        if (eType.getValue() >= 258 && eType.getValue() <= 320) {
            return true;
        }
        return false;
    }

    public static boolean checkContainer(EType eType) {
        if (eType == EType.ETYPE_NODE || eType == EType.ETYPE_MAP || eType == EType.ETYPE_TABLE || eType == EType.ETYPE_LIST) {
            return true;
        }
        return false;
    }

    public static EType getETypeBySchema(String msg) {
        if (msg == null) {
            return null;
        }
        for (EType eType : EType.values()) {
            if (eType.getESchemaType().equals(msg)) {
                return eType;
            }
        }
        return null;
    }

    public String getESchemaType() {
        return eSchemaType;
    }

    public static EType getETypeByKernel(String msg) {
        if (msg == null) {
            return null;
        }
        for (EType eType : EType.values()) {
            if (eType.getEKernelType().equals(msg)) {
                return eType;
            }
        }
        return null;
    }

    public String getEKernelType() {
        return eKernelType;
    }
}
