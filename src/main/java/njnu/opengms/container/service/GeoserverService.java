package njnu.opengms.container.service;

import com.alibaba.fastjson.JSONObject;
import njnu.opengms.container.component.GeoserverConfig;
import njnu.opengms.container.component.PathConfig;
import njnu.opengms.container.enums.DataResourceTypeEnum;
import njnu.opengms.container.enums.ResultEnum;
import njnu.opengms.container.exception.MyException;
import njnu.opengms.container.utils.ProcessUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @ClassName GeoServerService
 * @Description todo
 * @Author sun_liber
 * @Date 2019/4/8
 * @Version 1.0.0
 */
@Service
public class GeoserverService {

    @Autowired
    GeoserverConfig geoserverConfig;

    @Autowired
    PathConfig pathConfig;

    @Autowired
    RestTemplate restTemplate;

    public JSONObject getLayers() {
        String url = geoserverConfig.getBasicURL() + "/geoserver/rest/layers.json";
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.GET, setAuth(null), JSONObject.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        return responseEntity.getBody();
    }

    public HttpEntity setAuth(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(geoserverConfig.getUsername(), geoserverConfig.getPassword());
        if (body == null || body.equals("")) {
            return new HttpEntity<>(null, headers);
        } else {
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new HttpEntity<>(body, headers);
        }

    }

    public JSONObject getStores() {
        String url = geoserverConfig.getBasicURL() + "/geoserver/rest/workspaces.json";
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, HttpMethod.GET, setAuth(null), JSONObject.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        return responseEntity.getBody();
    }

    /**
     * 根据id去指定文件夹找到对应的shapefile，发布到geoserver中
     *
     * @param id
     */
    public String createShapeFile(String id) {
        String url = geoserverConfig.getBasicURL() + "/geoserver/rest/workspaces/datacontainer/datastores/"+id+"/external.shp?update=overwrite";
        File dir = new File(pathConfig.getShapefiles());
        Collection<File> fileCollection = FileUtils.listFiles(dir, FileFilterUtils.and(new SuffixFileFilter(".shp",IOCase.INSENSITIVE), new PrefixFileFilter(id)), null);
        File real_file = fileCollection.iterator().next();
        //注意这里是PUT请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, setAuth(geoserverConfig.getShapefiles() + File.separator
                + real_file.getName()), String.class);
        if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
            //注意这里geoserver返回的HttpStatus是201
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        return real_file.getName();
    }

    public String createShapeFile(String id,String fileName,String shpDir){
        String url = geoserverConfig.getBasicURL() + "/geoserver/rest/workspaces/datacontainer/datastores/"+id+"/external.shp?update=overwrite";
        File dir = new File(shpDir);
        Collection<File> fileCollection = FileUtils.listFiles(dir, FileFilterUtils.and(new SuffixFileFilter(".shp",IOCase.INSENSITIVE), new PrefixFileFilter(fileName)), null);
        File real_file = fileCollection.iterator().next();
        try{
            long start = System.currentTimeMillis();
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, setAuth(real_file.getAbsolutePath()), String.class);
            long end = System.currentTimeMillis();
            System.out.println("发布服务耗时："+(end-start));
            if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
                throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
            }
        }catch (Exception e){
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        return real_file.getName();
    }

    public String createGeoTiff(String id,String fileName,String tifDir){
        String url = geoserverConfig.getBasicURL() + "/geoserver/rest/workspaces/datacontainer/coveragestores/" + id + "/external.geotiff";
        File dir = new File(tifDir);
        Collection<File> fileCollection = FileUtils.listFiles(dir,FileFilterUtils.and(new SuffixFileFilter(".tif",IOCase.INSENSITIVE), new PrefixFileFilter(fileName)), null);
        File real_file = fileCollection.iterator().next();
        try{
            long start = System.currentTimeMillis();
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, setAuth(real_file.getAbsolutePath()), String.class);
            long end = System.currentTimeMillis();
            System.out.println("发布服务耗时："+(end-start));
            if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
                throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
            }
        }catch (Exception e){
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        return real_file.getName();
    }

    public String createGeotiff(String id) {
        String url = geoserverConfig.getBasicURL() + "/geoserver/rest/workspaces/datacontainer/coveragestores/" + id + "/external.geotiff";
        File dir = new File(pathConfig.getGeotiffes());
        Collection<File> fileCollection = FileUtils.listFiles(dir, FileFilterUtils.and(new SuffixFileFilter(".tif",IOCase.INSENSITIVE), new PrefixFileFilter(id)), null);
        File real_file = fileCollection.iterator().next();
        try{
            long start = System.currentTimeMillis();
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, setAuth(geoserverConfig.getGeotiffes() + File.separator + real_file.getName()), String.class);
            long end = System.currentTimeMillis();
            System.out.println("发布服务耗时："+(end-start));
            if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
                throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
            }
        }catch (Exception e){
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        return real_file.getName();
    }


    //使用gdal将sdat转换为tif数据
    public void sdatToGeotiff(String id,String dir, String unzipFileDirPath) throws IOException {
        File gdalDir = new File(pathConfig.getGetGdal());
        if(!gdalDir.exists()){
            throw new MyException(ResultEnum.NO_GDAL_LIB);
        }
        File unzipFileDir = new File(unzipFileDirPath);
        if(!unzipFileDir.exists()){
            unzipFileDir.mkdirs();
        }
        //获取sdat文件的绝对路径
        Collection<File> fileCollection = FileUtils.listFiles(new File(dir),  new SuffixFileFilter(".sdat",IOCase.INSENSITIVE), null);
        File file = fileCollection.iterator().next();
        String mgrdPath = file.getAbsolutePath();
        if(mgrdPath.isEmpty()){
            throw new MyException(ResultEnum.FILE_NOT_FOUND);
        }
        //获取mgrd文件名：
        String sdatName = FilenameUtils.getBaseName(file.getName());
//        String sdatName = mgrdPath.substring(mgrdPath.lastIndexOf(File.separator),mgrdPath.lastIndexOf("."));
        //Tif 文件名
        String tifName = id+"_"+sdatName;
        String tifPath = unzipFileDirPath+File.separator+tifName+".tif";
        String[] cmd = {"cmd", "/C", ""};
        String saga2tif_cmd = pathConfig.getGetGdal()+File.separator+"gdal_translate.exe -of SAGA "+"\""+mgrdPath+"\""+" -of GTiff "+"\""+tifPath+"\"";

        ProcessUtils.exeCmd(saga2tif_cmd);
    }

    public void delete(String id, DataResourceTypeEnum type) throws IOException {
        if (type.equals(DataResourceTypeEnum.SHAPEFILE)) {
            deleteShapeFile(id);
        } else if (type.equals(DataResourceTypeEnum.GEOTIFF)) {
            deleteGeotiff(id);
        } else {
            throw new MyException(ResultEnum.NOTSUPPORT_GEOSERVER_ERROR);
        }
    }

    public void deleteShapeFile(String id) throws IOException {
        File dir = new File(pathConfig.getShapefiles());
        Collection<File> fileCollection = FileUtils.listFiles(dir, FileFilterUtils.and(new SuffixFileFilter(".shp",IOCase.INSENSITIVE), new PrefixFileFilter(id)), null);
        File real_file = fileCollection.iterator().next();
        String storeName = FilenameUtils.getBaseName(real_file.getName());
        String url = geoserverConfig.getBasicURL() + "/geoserver/rest/workspaces/datacontainer/datastores/shapefileList/featuretypes/" + storeName + "?recurse=true";
        //注意这里是PUT请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, setAuth(null), String.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        fileCollection = FileUtils.listFiles(dir, new PrefixFileFilter(id), null);
        for (File fileForDelete : fileCollection) {
            FileUtils.deleteQuietly(fileForDelete);
        }
    }

    public void deleteGeotiff(String id) throws IOException {
        String url = geoserverConfig.getBasicURL() + "/geoserver/rest/workspaces/datacontainer/coveragestores/" + id + "?recurse=true";
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, setAuth(null), String.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
        File dir = new File(pathConfig.getGeotiffes());
        Collection<File> fileCollection = FileUtils.listFiles(dir, new PrefixFileFilter(id), null);
        for (File fileForDelte : fileCollection) {
            FileUtils.deleteQuietly(fileForDelte);
        }
    }


}
