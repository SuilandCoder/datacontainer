package njnu.opengms.container.controller;

import com.mongodb.BasicDBObject;
import com.ngis.udx.Transfer;
import com.ngis.udx.data.UdxData;
import com.ngis.udx.data.UdxNode;
import com.ngis.udx.data.kernels.UdxKernelString;
import com.ngis.udx.data.kernels.UdxKernelVector3;
import com.ngis.udx.data.structure.Vector3d;
import com.ngis.udx.schema.UdxSchema;
import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.component.AsyncTaskComponent;
import njnu.opengms.container.getmeta.DataStoreMetaGet;
import njnu.opengms.container.getmeta.impl.ShapefileMetaGet;
import njnu.opengms.container.pojo.DataResource;
import njnu.opengms.container.pojo.SchemaDoc;
import njnu.opengms.container.repository.SchemaDocRepository;
import njnu.opengms.container.service.MappingMethodServiceImp;
import njnu.opengms.container.service.SchemaDocServiceImp;
import njnu.opengms.container.utils.ResultUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName TestController
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/11
 * @Version 1.0.0
 */
@RestController
@RequestMapping (value = "/test")
@ApiIgnore
public class TestController {


    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    SchemaDocServiceImp schemaDocServiceImp;

    @Autowired
    SchemaDocRepository schemaDocRepository;

    @Autowired
    MappingMethodServiceImp mappingMethodServiceImp;


    @Autowired
    AsyncTaskComponent asyncTaskComponent;

    @RequestMapping (value = "/async", method = RequestMethod.GET)
    public JsonResult async() {
        int count = 10;
        for (int i = 0; i < count; i++) {
            asyncTaskComponent.executeAsyncTask(i);
            asyncTaskComponent.executeAsyncTaskPlus(i);
        }
        return ResultUtils.success("异步支持");
    }


    @RequestMapping (value = "/test1", method = RequestMethod.POST)
    public JsonResult test1(@RequestBody UdxData udxData) throws IOException {
        return ResultUtils.success(udxData);
    }

    @RequestMapping (value = "/test2", method = RequestMethod.POST)
    public JsonResult test2(@RequestBody UdxSchema udxSchema) throws IOException {
        return ResultUtils.success(udxSchema);
    }


    @RequestMapping (value = "/test3", method = RequestMethod.GET)
    public JsonResult test3(@RequestParam ("id") String id) {
        UdxSchema udxSchema = schemaDocServiceImp.get(id).getUdxSchema();
        UdxData udxData = Transfer.generate(udxSchema);
        return ResultUtils.success(udxData);
    }

    @RequestMapping (value = "/test4", method = RequestMethod.GET)
    public JsonResult test4(@RequestParam ("id") String id) {
        return ResultUtils.success(mappingMethodServiceImp.findBySchema(id));
    }

    @RequestMapping (value = "/test5", method = RequestMethod.GET)
    public JsonResult test5(@RequestParam ("nodeName") String nodeName) throws IOException, DocumentException {
        UdxData udxData = Transfer.loadDataFromXmlFile(new File("C:\\Users\\sun_liber\\Desktop\\d\\data\\res_temperature_tiff.xml"));
        UdxNode udxNode = udxData.getNodeByName(nodeName);
        return ResultUtils.success(udxNode);
    }


    @RequestMapping (value = "/test6", method = RequestMethod.GET)
    public JsonResult test6() throws IOException, DocumentException {
        List<SchemaDoc> list = schemaDocRepository.findAll(PageRequest.of(0, 10, Sort.Direction.DESC, "createDate")).getContent();
        return ResultUtils.success(list);
    }

    @RequestMapping (value = "/test7", method = RequestMethod.GET)
    public JsonResult test7() throws IOException, DocumentException {
        BasicDBObject dbObject = new BasicDBObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put("name", 1);
        Query query = new BasicQuery(dbObject.toJson(), fieldsObject.toJson());
        query.with(PageRequest.of(0, 10, Sort.Direction.ASC, "createDate"));
        List<SchemaDoc> list1 = mongoTemplate.find(query, SchemaDoc.class);
        return ResultUtils.success(list1);
    }

    @RequestMapping (value = "/test8", method = RequestMethod.GET)
    public JsonResult test8() throws IOException, DocumentException {
        UdxSchema pepoleSchema = Transfer.loadSchemaFromXmlFile(new File("C:\\Users\\sun_liber\\Desktop\\d\\schema\\pepole.xml"));
        UdxData pepoleData = Transfer.generate(pepoleSchema);

        UdxNode name = pepoleData.getNodeByName("名称");
        ((UdxKernelString) name.getUdxKernel()).setValue("sunlingzhi");
        UdxNode sanwei = pepoleData.getNodeByName("三围");
        ((UdxKernelVector3) sanwei.getUdxKernel()).setValue(new Vector3d(1, 2, 3));


        UdxSchema connectionSchema = Transfer.loadSchemaFromXmlFile(new File("C:\\Users\\sun_liber\\Desktop\\d\\schema\\connection.xml"));
        UdxData connectionData = Transfer.generate(connectionSchema);
        UdxNode tel = connectionData.getNodeByName("电话");
        ((UdxKernelString) tel.getUdxKernel()).setValue("18840840241");

        UdxSchema pcSchema = Transfer.loadSchemaFromXmlFile(new File("C:\\Users\\sun_liber\\Desktop\\d\\schema\\pc.xml"));
        UdxData pcData = Transfer.generate(pcSchema);

        pcData.getNodeByName("名称").setUdxKernel(name.getUdxKernel());
        pcData.getNodeByName("三围").setUdxKernel(sanwei.getUdxKernel());
        pcData.getNodeByName("电话").setUdxKernel(tel.getUdxKernel());

        UdxData p = new UdxData();
        BeanUtils.copyProperties(connectionData, p);

        return ResultUtils.success(pcData);
    }


    @RequestMapping (value = "/test9", method = RequestMethod.GET)
    public JsonResult test9() throws IOException, DocumentException {
        DataStoreMetaGet metaGet = new ShapefileMetaGet();
        metaGet.getMeta(new File("F:/sunlingzhi/datacontainer_store/geoserver_files/shapefiles/24a6b80a-e15b-49f2-bebc-d03e427a5051_QXJM.shp"));
        return ResultUtils.success();
    }

    @RequestMapping (value = "/test10", method = RequestMethod.GET)
    public JsonResult test10() throws IOException, DocumentException {
        String[] s = {"432952ae-0610-42a8-8964-27cc2618f8e1", "432952ae-0610-42a8-8964-27cc2618f8e2", "432952ae-0610-42a8-8964-27cc2618f8e3"};
        List<String> sourceStoreIdList = Arrays.asList(s);
        Criteria c = new Criteria();
        c.and("sourceStoreId").in(sourceStoreIdList);
        Query query = new Query(c);
        List<DataResource> result = mongoTemplate.find(query, DataResource.class);
        return ResultUtils.success();
    }


}
