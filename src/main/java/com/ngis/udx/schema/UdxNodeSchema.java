package com.ngis.udx.schema;

import com.ngis.udx.EType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UdxNodeSchema
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/12
 * @Version 1.0.0
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UdxNodeSchema {
    String name;
    String description;
    EType type;
    UdxNodeInfo udxNodeInfo;
    List<UdxNodeSchema> childNodes = new ArrayList<>();

    //如果结点不是容器结点，不能添加子结点
    //Node结点可以添加任何类型，且可无限添加
    //List结点只能添加一个，再次添加会覆盖该结点
    //Map结点只能添加2个，多于的结点会覆盖最先进入的结点
    //Table结点可以添加多个，但是类型必须是Array类型

    public UdxNodeSchema addChildNode(UdxNodeSchema udxNodeSchema) {
        EType eType = this.getType();
        if (eType == EType.ETYPE_INT ||
                eType == EType.ETYPE_REAL ||
                eType == EType.ETYPE_STRING ||
                eType == EType.ETYPE_VECTOR2 ||
                eType == EType.ETYPE_VECTOR3 ||
                eType == EType.ETYPE_VECTOR4 ||
                eType == EType.ETYPE_INT_ARRAY ||
                eType == EType.ETYPE_REAL_ARRAY ||
                eType == EType.ETYPE_STRING_ARRAY ||
                eType == EType.ETYPE_VECTOR2_ARRAY ||
                eType == EType.ETYPE_VECTOR3_ARRAY ||
                eType == EType.ETYPE_VECTOR4_ARRAY
                ) {
            return null;
        } else if (eType == EType.ETYPE_LIST) {
            if (this.childNodes.size() >= 1) {
                this.childNodes.remove(0);
            }
        } else if (eType == EType.ETYPE_MAP) {
            if (this.childNodes.size() >= 2) {
                this.childNodes.remove(0);
            }
        } else if (eType == EType.ETYPE_TABLE) {
            boolean flag = false;
            EType pType = udxNodeSchema.getType();
            if (pType == EType.ETYPE_INT_ARRAY ||
                    pType == EType.ETYPE_REAL_ARRAY ||
                    pType == EType.ETYPE_STRING_ARRAY ||
                    pType == EType.ETYPE_VECTOR2_ARRAY ||
                    pType == EType.ETYPE_VECTOR3_ARRAY ||
                    pType == EType.ETYPE_VECTOR4_ARRAY) {
                flag = true;
            }

            if (flag == false) {
                return null;
            }
        }
        this.childNodes.add(udxNodeSchema);
        return udxNodeSchema;
    }

    public void removeChildNode(int index) {
        this.childNodes.remove(index);
    }

    public void removeChildNode(UdxNodeSchema udxNodeSchema) {
        this.childNodes.remove(udxNodeSchema);
    }


}
