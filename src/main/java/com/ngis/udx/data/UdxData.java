package com.ngis.udx.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UdxData
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/12
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UdxData {
    String name;
    String description;
    List<UdxNode> udxNodes = new ArrayList<>();

    //这里使用递归去获取的，但是可以考虑使用lodash的_.flattenDeep(array) 回归一维数组
    //然后一次遍历就可以取值了
    public UdxNode getNodeByName(String nodeName) {
        for (UdxNode udxNode : udxNodes) {
            UdxNode node = getNode(udxNode, nodeName);
            if (node != null) {
                return node;
            }
        }
        return null;
    }

    private UdxNode getNode(UdxNode udxNode, String nodeName) {
        if (udxNode.getName().equals(nodeName)) {
            return udxNode;
        }
        List<UdxNode> udxNodes = udxNode.getChildNodes();

        if (udxNodes.size() == 0) {
            return null;
        }

        for (UdxNode node : udxNodes) {
            UdxNode n = getNode(node, nodeName);
            if (n != null) {
                return n;
            }
        }
        return null;
    }


}
