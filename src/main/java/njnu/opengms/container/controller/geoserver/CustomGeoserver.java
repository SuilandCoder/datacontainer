package njnu.opengms.container.controller.geoserver;

import io.swagger.annotations.ApiOperation;
import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.component.GeoserverConfig;
import njnu.opengms.container.component.PathConfig;
import njnu.opengms.container.enums.DataResourceTypeEnum;
import njnu.opengms.container.enums.ResultEnum;
import njnu.opengms.container.exception.MyException;
import njnu.opengms.container.service.DataResourceServiceImp;
import njnu.opengms.container.service.GeoserverService;
import njnu.opengms.container.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @ClassName CustomGeoserver
 * @Description Geoserver本身支持的数据存储分为三种，分别是矢量数据源、栅格数据源以及其他的WMS数据源
 * 代码对Geoserver提供的Rest风格接口，进行了自定义的封装
 * 完成了典型矢量数据-Shapefile和典型的栅格数据-geotiff的服务上传、调用功能
 * TODO 目前所有的业务逻辑代码是在Controller实现，可考虑生成静态工具类，以避免目前的HTTP重定向写法
 * @Author sun_liber
 * @Date 2019/2/21
 * @Version 1.0.0
 */
@RestController
@RequestMapping ("/custom_geoserver")
public class CustomGeoserver {

    @Autowired
    GeoserverConfig geoserverConfig;

    @Autowired
    PathConfig pathConfig;

    @Autowired
    DataResourceServiceImp dataResourceServiceImp;

    @Autowired
    GeoserverService geoserverService;

    @Autowired
    RestTemplate restTemplate;

    @ApiOperation (value = "查看全部图层")
    @RequestMapping (value = "/layers", method = RequestMethod.GET)
    public JsonResult listLayers() {
        return ResultUtils.success(geoserverService.getLayers());
    }

    @ApiOperation (value = "列出工作区的所有数据存储，分为DataStores和CoverageStores", notes = "注意这里工作区为datacontainer")
    @RequestMapping (value = "/datacontainer", method = RequestMethod.GET)
    JsonResult listStores() {
        return ResultUtils.success(geoserverService.getStores());
    }


    @ApiOperation (value = "Creates or modifies a single data store,如果不存在会默认创建，这里我们默认创建了名为shapefileList的datastore", notes = "Geoserver原生支持三种上传方式，分别是file、url和external，我们这里将" +
            "使用external作为默认方式，因此我们创建一个dataStore的分别为两个步骤，第一步将文件复制到external路径之下" +
            "第二步，调用该方法以实现更新" +
            "同时我们这里采取了update=overwrite会对同名文件进行覆盖")
    @RequestMapping (value = "/datacontainer/{id}", method = RequestMethod.POST)
    JsonResult create(@PathVariable ("id") String id, @RequestParam ("type") String type) throws Exception {
        String layerName;
        if (type.equals(DataResourceTypeEnum.SHAPEFILE.getType())) {
            layerName = geoserverService.createShapeFile(id);
        } else if (type.equals(DataResourceTypeEnum.GEOTIFF.getType())) {
            layerName = geoserverService.createGeotiff(id);
        } else {
            throw new MyException(ResultEnum.NOTSUPPORT_GEOSERVER_ERROR);
        }
        return ResultUtils.success("Layers:" + layerName + "发布成功");
    }

    @ApiOperation (value = "delete", notes = "delete")
    @RequestMapping (value = "/datacontainer/{id}", method = RequestMethod.DELETE)
    JsonResult delete(@PathVariable ("id") String id, @RequestParam ("type") String type) throws IOException {
        if (type.equals(DataResourceTypeEnum.SHAPEFILE.getType())) {
            geoserverService.deleteShapeFile(id);
        } else if (type.equals(DataResourceTypeEnum.GEOTIFF.getType())) {
            geoserverService.deleteGeotiff(id);
        } else {
            throw new MyException(ResultEnum.NOTSUPPORT_GEOSERVER_ERROR);
        }
        return ResultUtils.success("删除" + id + "成功");
    }


}
