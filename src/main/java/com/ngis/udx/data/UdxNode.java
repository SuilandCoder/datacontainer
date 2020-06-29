package com.ngis.udx.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UdxNode
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/12
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UdxNode {
    String name;
    UdxKernel udxKernel;
    List<UdxNode> childNodes = new ArrayList<>();
}
