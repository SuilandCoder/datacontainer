package com.ngis.udx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ngis.udx.data.*;
import com.ngis.udx.data.kernels.*;
import com.ngis.udx.data.kernels.container.UdxKernelList;
import com.ngis.udx.data.kernels.container.UdxKernelMap;
import com.ngis.udx.data.kernels.container.UdxKernelNode;
import com.ngis.udx.data.kernels.container.UdxKernelTable;
import com.ngis.udx.data.structure.Vector2d;
import com.ngis.udx.data.structure.Vector3d;
import com.ngis.udx.data.structure.Vector4d;
import com.ngis.udx.schema.UdxNodeInfo;
import com.ngis.udx.schema.UdxNodeSchema;
import com.ngis.udx.schema.UdxSchema;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName Transfer
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/14
 * @Version 1.0.0
 */
public class Transfer {

    public static UdxData generate(UdxSchema udxSchema) {
        UdxData udxData = new UdxData();
        udxData.setName(udxSchema.getName());
        udxData.setDescription(udxSchema.getDescription());

        List<UdxNodeSchema> udxNodeSchemas = udxSchema.getUdxNodeSchemas();
        List<UdxNode> udxNodes = udxData.getUdxNodes();
        for (UdxNodeSchema udxNodeSchema : udxNodeSchemas) {
            UdxNode udxNode = new UdxNode();
            udxNodes.add(udxNode);
            generateCycle(udxNodeSchema, udxNode);
        }

        return udxData;
    }


    public static void generateCycle(UdxNodeSchema udxNodeSchema, UdxNode udxNode) {
        UdxKernel udxKernel = null;
        EType type = udxNodeSchema.getType();
        String name = udxNodeSchema.getName();
        udxNode.setName(name);
        IFactory factory = new Factory();
        if (EType.checkSingle(type)) {
            udxKernel = factory.createUdxSingle(type);
        } else if (EType.checkArray(type)) {
            udxKernel = factory.createUdxArray(type);
        } else if (EType.checkContainer(type)) {
            udxKernel = factory.createContainer(type);
        }
        udxNode.setUdxKernel(udxKernel);
        if (udxNodeSchema.getChildNodes().size() != 0) {
            for (UdxNodeSchema nodeSchema : udxNodeSchema.getChildNodes()) {
                UdxNode udxNode1 = new UdxNode();
                udxNode.getChildNodes().add(udxNode1);
                generateCycle(nodeSchema, udxNode1);
            }
        }
    }


    public static UdxSchema loadSchemaFromJsonFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        UdxSchema udxSchema = mapper.readValue(file, UdxSchema.class);
        return udxSchema;
    }

    public static void exportSchemaToJsonFile(File file, UdxSchema udxSchema) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.writeValue(file, udxSchema);
    }

    public static UdxSchema loadSchemaFromXmlFile(File file) throws IOException, DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(file);
        Element eleRoot = doc.getRootElement();
        UdxSchema udxSchema = new UdxSchema();
        udxSchema.setName(eleRoot.attributeValue("name"));
        udxSchema.setDescription(eleRoot.attributeValue("description"));
        Element eleBody = eleRoot.elements().get(0);
        for (int i = 0; i < eleBody.elements().size(); i++) {
            parseRootSchema(udxSchema.getUdxNodeSchemas(), eleBody.elements().get(i));
        }


        Element semanticEle = eleRoot.elements().get(1);
        if (semanticEle != null) {
            for (int i = 0; i < semanticEle.elements().size(); i++) {
                Element childEle = semanticEle.elements().get(i);
                String ele_name = childEle.getName();
                int temp_count = childEle.elements().size();
                if (("Concepts").equals(ele_name)) {
                    for (int j = 0; j < temp_count; j++) {
                        Element infoEle = childEle.elements().get(j);
                        String nodeName = infoEle.attribute("nodeName").getText();
                        String conceptId = infoEle.attribute("conceptId").getText();
                        UdxNodeSchema dataNode = getNodeWithName(nodeName, udxSchema);
                        dataNode.getUdxNodeInfo().setConceptTag(conceptId);
                    }
                } else if (("SpatialRefs").equals(ele_name)) {

                    for (int j = 0; j < temp_count; j++) {
                        Element infoEle = childEle.elements().get(j);
                        String nodeName = infoEle.attribute("nodeName").getText();
                        String SpatialRefsId = infoEle.attribute("SpatialRefsId").getText();
                        UdxNodeSchema dataNode = getNodeWithName(nodeName, udxSchema);
                        dataNode.getUdxNodeInfo().setSpatialRefTag(SpatialRefsId);
                    }
                } else if (("Units").equals(ele_name)) {

                    for (int j = 0; j < temp_count; j++) {
                        Element infoEle = childEle.elements().get(j);
                        String nodeName = infoEle.attribute("nodeName").getText();
                        String UnitsId = infoEle.attribute("UnitsId").getText();
                        UdxNodeSchema dataNode = getNodeWithName(nodeName, udxSchema);
                        dataNode.getUdxNodeInfo().setUnitTag(UnitsId);
                    }
                } else if (("DataTemplates").equals(ele_name)) {
                    for (int j = 0; j < temp_count; j++) {
                        Element infoEle = childEle.elements().get(j);
                        String nodeName = infoEle.attribute("nodeName").getText();
                        String DataTemplatesId = infoEle.attribute("DataTemplatesId").getText();
                        UdxNodeSchema dataNode = getNodeWithName(nodeName, udxSchema);
                        dataNode.getUdxNodeInfo().setDataTemplateTag(DataTemplatesId);
                    }
                }
            }
        }
        return udxSchema;
    }

    public static void exportSchemaToXmlFile(File file, UdxSchema udxSchema) throws IOException {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("UdxDeclaration")
                .addAttribute("name", udxSchema.getName()).
                        addAttribute("description", udxSchema.getDescription());


        Element ele = root.addElement("UdxNodeSchema");

        Element semanticNode = root.addElement("SemanticAttachment");

        semanticNode.addElement("Concepts");
        semanticNode.addElement("SpatialRefs");
        semanticNode.addElement("Units");
        semanticNode.addElement("DataTemplates");

        for (int i = 0; i < udxSchema.getUdxNodeSchemas().size(); i++) {
            UdxNodeSchema node = udxSchema.getUdxNodeSchemas().get(i);
            exportXDO(node, ele, root);
        }


        //注意这里不要使用FileWriter()
        // FileWriter不会处理编码，所以即使你使用format.setEncoding("utf-8");他仍然不会使用utf-8编码，
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
        writer.write(doc);
        writer.close();


    }


    public static UdxData loadDataFromJsonFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        UdxData udxData = mapper.readValue(file, UdxData.class);
        return udxData;
    }

    public static void exportDataToJsonFile(File file, UdxData udxData) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.writeValue(file, udxData);
    }

    public static UdxData loadDataFromXmlFile(File file) throws IOException, DocumentException {
        try {
            UdxData udxData = new UdxData();
            SAXReader reader = new SAXReader();
            Document doc = reader.read(file);

            Element rootEle = doc.getRootElement();

            List childList = rootEle.elements();
            for (int i = 0; i < childList.size(); i++) {
                parseRootData(udxData.getUdxNodes(), (Element) childList.get(i));
            }
            return udxData;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void exportDataToXmlFile(File file, UdxData udxData) throws IOException {
        Document doc = DocumentHelper.createDocument();
        Element element = doc.addElement("dataset");
        int count = udxData.getUdxNodes().size();
        for (int i = 0; i < count; i++) {
            UdxNode tempNode = udxData.getUdxNodes().get(i);
            exportXDO(tempNode, element);
        }

        //注意这里不要使用FileWriter()
        // FileWriter不会处理编码，所以即使你使用format.setEncoding("utf-8");他仍然不会使用utf-8编码，
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
        writer.write(doc);
        writer.close();
    }

    private static void parseRootData(List<UdxNode> childNodes, Element element) {
        UdxNode node = new UdxNode();
        childNodes.add(node);
        parse(node, element);
    }

    private static void parse(UdxNode udxNode, Element el) {
        String name = el.attributeValue("name");
        String typeStr = el.attributeValue("kernelType");
        udxNode.setName(name);

        if (("int").equals(typeStr)) {
            int val = Integer.parseInt(el.attributeValue("value"));
            UdxKernel udxKernel = new UdxKernelInt(val);
            udxNode.setUdxKernel(udxKernel);
        } else if (("real").equals(typeStr)) {
            double val = Double.parseDouble(el.attributeValue("value"));
            UdxKernel udxKernel = new UdxKernelReal(val);
            udxNode.setUdxKernel(udxKernel);
        } else if (("string").equals(typeStr)) {
            String val = el.attributeValue("value");
            UdxKernel udxKernel = new UdxKernelString(val);
            udxNode.setUdxKernel(udxKernel);
        } else if (("vector2d").equals(typeStr)) {
            String[] val = el.attributeValue("value").split(",");
            double x, y;
            x = Double.parseDouble(val[0]);
            y = Double.parseDouble(val[1]);
            UdxKernel udxKernel = new UdxKernelVector2(new Vector2d(x, y));
            udxNode.setUdxKernel(udxKernel);
        } else if (("vector3d").equals(typeStr)) {
            String[] val = el.attributeValue("value").split(",");
            double x, y, z;
            x = Double.parseDouble(val[0]);
            y = Double.parseDouble(val[1]);
            z = Double.parseDouble(val[2]);
            UdxKernel udxKernel = new UdxKernelVector3(new Vector3d(x, y, z));
            udxNode.setUdxKernel(udxKernel);
        } else if (("vector4d").equals(typeStr)) {
            String[] val = el.attributeValue("value").split(",");
            double x, y, z, w;
            x = Double.parseDouble(val[0]);
            y = Double.parseDouble(val[1]);
            z = Double.parseDouble(val[2]);
            w = Double.parseDouble(val[3]);
            UdxKernel udxKernel = new UdxKernelVector4(new Vector4d(x, y, z, w));
            udxNode.setUdxKernel(udxKernel);
        } else if (("int_array").equals(typeStr)) {
            String[] ret = el.attributeValue("value").trim().split(",");
            UdxKernel udxKernel = new UdxKernelIntArray();
            udxNode.setUdxKernel(udxKernel);
            for (int i = 0; i < ret.length; i++) {
                Integer integer = Integer.parseInt(ret[i].trim());
                ((UdxKernelIntArray) udxKernel).add(integer);
            }
        } else if (("real_array").equals(typeStr)) {
            String[] ret = el.attributeValue("value").trim().split(",");
            UdxKernel udxKernel = new UdxKernelRealArray();
            udxNode.setUdxKernel(udxKernel);
            for (int i = 0; i < ret.length; i++) {
                Double aDouble = Double.parseDouble(ret[i].trim());
                ((UdxKernelRealArray) udxKernel).add(aDouble);
            }
        } else if (("string_array").equals(typeStr)) {
            String[] ret = el.attributeValue("value").trim().split(",");
            UdxKernel udxKernel = new UdxKernelStringArray();
            udxNode.setUdxKernel(udxKernel);
            for (int i = 0; i < ret.length; i++) {
                String s = ret[i].trim();
                ((UdxKernelStringArray) udxKernel).add(s);
            }
        } else if (("vector2d_array").equals(typeStr)) {
            String[] ret = el.attributeValue("value").trim().split(";");
            UdxKernel udxKernel = new UdxKernelVector2Array();
            udxNode.setUdxKernel(udxKernel);
            for (int i = 0; i < ret.length; i++) {
                double x, y;
                String[] ret1 = ret[i].trim().split(",");
                x = Double.parseDouble(ret1[0].trim());
                y = Double.parseDouble(ret1[1].trim());
                ((UdxKernelVector2Array) udxKernel).add(new Vector2d(x, y));
            }
        } else if (("vector3d_array").equals(typeStr)) {
            String[] ret = el.attributeValue("value").trim().split(";");
            UdxKernel udxKernel = new UdxKernelVector3Array();
            udxNode.setUdxKernel(udxKernel);
            for (int i = 0; i < ret.length; i++) {
                double x, y, z;
                String[] ret1 = ret[i].trim().split(",");
                x = Double.parseDouble(ret1[0].trim());
                y = Double.parseDouble(ret1[1].trim());
                z = Double.parseDouble(ret1[2].trim());
                ((UdxKernelVector3Array) udxKernel).add(new Vector3d(x, y, z));
            }
        } else if (("vector4d_array").equals(typeStr)) {
            String[] ret = el.attributeValue("value").trim().split(";");
            UdxKernel udxKernel = new UdxKernelVector2Array();
            udxNode.setUdxKernel(udxKernel);
            for (int i = 0; i < ret.length; i++) {
                double x, y, z, w;
                String[] ret1 = ret[i].trim().split(",");
                x = Double.parseDouble(ret1[0].trim());
                y = Double.parseDouble(ret1[1].trim());
                z = Double.parseDouble(ret1[2].trim());
                w = Double.parseDouble(ret1[3].trim());
                ((UdxKernelVector4Array) udxKernel).add(new Vector4d(x, y, z, w));
            }
        } else if (("any").equals(typeStr) || ("list").equals(typeStr) || ("map").equals(typeStr) || ("table").equals(typeStr)) {
            List childList = el.elements();
            UdxKernel udxKernel = null;
            if (("any").equals(typeStr)) {
                udxKernel = new UdxKernelNode();
            } else if (("list").equals(typeStr)) {
                udxKernel = new UdxKernelList();
            } else if (("map").equals(typeStr)) {
                udxKernel = new UdxKernelMap();
            } else if (("table").equals(typeStr)) {
                udxKernel = new UdxKernelTable();
            }

            udxNode.setUdxKernel(udxKernel);
            for (int i = 0; i < childList.size(); i++) {
                parseRootData(udxNode.getChildNodes(), (Element) childList.get(i));
            }
        } else {
            ;
        }
    }

    private static void exportXDO(UdxNodeSchema node, Element ele, Element root) {
        String nodeName = node.getName();
        Element childEle = ele.addElement("UdxNodeSchema");
        childEle.addAttribute("name", node.getName());
        childEle.addAttribute("type", node.getType().getESchemaType());
        childEle.addAttribute("description", node.getDescription());


        Element semanticNode = root.element("SemanticAttachment");
        Element conceptsEle = semanticNode.element("Concepts");
        Element spatialRefsEle = semanticNode.element("SpatialRefs");
        Element unitsEle = semanticNode.element("Units");
        Element dataTemplatesEle = semanticNode.element("DataTemplates");


        if (node.getUdxNodeInfo() != null) {
            String conceptTag = node.getUdxNodeInfo().getConceptTag();
            String spatialRefTag = node.getUdxNodeInfo().getSpatialRefTag();
            String unitTag = node.getUdxNodeInfo().getUnitTag();
            String dataTemplateTag = node.getUdxNodeInfo().getDataTemplateTag();


            if (conceptTag != null) {
                Element s_node = conceptsEle.addElement("Concept");
                s_node.addAttribute("nodeName", nodeName);
                s_node.addAttribute("conceptId", conceptTag);
            }
            if (spatialRefTag != null) {
                Element s_node = spatialRefsEle.addElement("SpatialRef");
                s_node.addAttribute("nodeName", nodeName);
                s_node.addAttribute("spatialRefId", spatialRefTag);
            }
            if (unitTag != null) {
                Element s_node = unitsEle.addElement("Unit");
                s_node.addAttribute("nodeName", nodeName);
                s_node.addAttribute("unitId", unitTag);
            }
            if (dataTemplateTag != null) {
                Element s_node = dataTemplatesEle.addElement("DataTemplate");
                s_node.addAttribute("nodeName", nodeName);
                s_node.addAttribute("dataTemplateId", dataTemplateTag);
            }
        }


        EType eType = node.getType();
        if (eType == EType.ETYPE_NODE ||
                eType == EType.ETYPE_LIST ||
                eType == EType.ETYPE_MAP ||
                eType == EType.ETYPE_TABLE) {

            for (int i = 0; i < node.getChildNodes().size(); i++) {
                exportXDO(node.getChildNodes().get(i), childEle, root);
            }
        }


    }

    private static void exportXDO(UdxNode node, Element element) {
        Element childEle = element.addElement("XDO");
        String name = node.getName();
        UdxKernel udxKernel = node.getUdxKernel();
        childEle.addAttribute("name", name);
        childEle.addAttribute("kernelType", udxKernel.returnEType().getEKernelType());

        if (udxKernel instanceof UdxSingle) {
            childEle.addAttribute("value", ((UdxSingle) udxKernel).udxSingleToString(((UdxSingle) udxKernel).get()));
        } else if (udxKernel instanceof UdxArray) {
            childEle.addAttribute("value", ((UdxArray) udxKernel).udxArrayToString(((UdxArray) udxKernel).get()));
        }
        for (int i = 0; i < node.getChildNodes().size(); i++) {
            exportXDO(node.getChildNodes().get(i), childEle);
        }

    }

    private static void parseRootSchema(List<UdxNodeSchema> udxNodeSchemas, Element element) {
        UdxNodeSchema udxNodeSchema = new UdxNodeSchema();
        udxNodeSchemas.add(udxNodeSchema);
        parse(udxNodeSchema, element);
    }

    private static void parse(UdxNodeSchema udxNodeSchema, Element element) {
        String name = element.attribute("name").getText();
        String description = element.attribute("description").getText();
        String typeStr = element.attribute("type").getText();

        udxNodeSchema.setName(name);
        udxNodeSchema.setDescription(description);
        udxNodeSchema.setUdxNodeInfo(new UdxNodeInfo());

        if (("external").equals(typeStr)) {
            EType eType = EType.ETYPE_NODE;
            udxNodeSchema.setType(eType);
            String externalId = element.attribute("externalId").getText();
            udxNodeSchema.getUdxNodeInfo().setDataTemplateTag(externalId);
        } else {
            EType eType = EType.getETypeBySchema(typeStr);
            udxNodeSchema.setType(eType);

            if (eType == EType.ETYPE_NODE ||
                    eType == EType.ETYPE_LIST ||
                    eType == EType.ETYPE_MAP ||
                    eType == EType.ETYPE_TABLE) {
                int count = element.elements().size();
                for (int i = 0; i < count; i++) {
                    parseRootSchema(udxNodeSchema.getChildNodes(), element.elements().get(i));
                }
            }
        }

    }

    private static UdxNodeSchema getNodeWithName(String nodeName, UdxSchema udxSchema) {
        for (int i = 0; i < udxSchema.getUdxNodeSchemas().size(); i++) {
            UdxNodeSchema nodeSchema = udxSchema.getUdxNodeSchemas().get(i);
            if (nodeSchema.getName().equals(nodeName)) {
                return nodeSchema;
            }
        }
        return null;
    }

}
