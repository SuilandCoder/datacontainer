package njnu.opengms.container.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.component.GeoserverConfig;
import njnu.opengms.container.component.PathConfig;
import njnu.opengms.container.controller.common.BaseController;
import njnu.opengms.container.dto.dataresource.AddDataResourceDTO;
import njnu.opengms.container.dto.dataresource.FindDataResourceDTO;
import njnu.opengms.container.dto.dataresource.UpdateDataResourceDTO;
import njnu.opengms.container.enums.DataResourceTypeEnum;
import njnu.opengms.container.enums.ResultEnum;
import njnu.opengms.container.exception.MyException;
import njnu.opengms.container.pojo.DataResource;
import njnu.opengms.container.pojo.FileResource;
import njnu.opengms.container.repository.FileResourceRepository;
import njnu.opengms.container.service.DataResourceServiceImp;
import njnu.opengms.container.service.GeoserverService;
import njnu.opengms.container.utils.OtherUtils;
import njnu.opengms.container.utils.ProcessUtils;
import njnu.opengms.container.utils.ResultUtils;
import njnu.opengms.container.utils.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName DataResourceController
 * @Description todo
 * @Author sun_liber
 * @Date 2019/2/13
 * @Version 1.0.0
 */
@RestController
@RequestMapping (value = "/dataResource")
public class DataResourceController implements BaseController<DataResource, DataResource, AddDataResourceDTO, FindDataResourceDTO, UpdateDataResourceDTO, String, DataResourceServiceImp> {

    @Autowired
    DataResourceServiceImp dataResourceServiceImp;

    @Autowired
    GeoserverService geoserverService;

    @Autowired
    FileResourceRepository fileResourceRepository;

    @Override
    public DataResourceServiceImp getService() {
        return this.dataResourceServiceImp;
    }

    @Autowired
    GeoserverConfig geoserverConfig;

    @Autowired
    PathConfig pathConfig;


    @ApiImplicitParam (name = "type", value = "type可以为author、mdl、dataItem、fileName，注意这里的返回是不带分页的", dataType = "string", paramType = "path", required = true)
    @RequestMapping (value = "/listByCondition", method = RequestMethod.GET)
    JsonResult listByCondition(@RequestParam ("type") String type,
                               @RequestParam ("value") String value) {
        if (type.equals("author")) {
            return ResultUtils.success(dataResourceServiceImp.getByAuthor(value));
        } else if (type.equals("mdl")) {
            return ResultUtils.success(dataResourceServiceImp.getByMdlId(value));
        } else if (type.equals("dataItem")) {
            return ResultUtils.success(dataResourceServiceImp.getByDataItemId(value));
        } else if (type.equals("fileName")) {
            return ResultUtils.success(dataResourceServiceImp.getByFileNameContains(value));
        } else {
            throw new MyException("查询条件不支持，目前支持的类型包括author，mdl，dataItem,fileName");
        }
    }


    /**
     * 将与门户的数据条目相关的所有数据资源存储打包再返回
     * 为了避免数据重名，对其进行名称添加 前缀
     * @param dataItemId
     * @return
     * @throws IOException
     */
    @RequestMapping (value = "/getResourcesRelatedDataItem/{dataItemId}", method = RequestMethod.GET)
    ResponseEntity<InputStreamResource> getResourceRelatedDataItem(@PathVariable ("dataItemId") String dataItemId) throws IOException {
        List<DataResource> list = dataResourceServiceImp.getByDataItemId(dataItemId);
        List<File> fileList = new ArrayList<>();
        List<String> renameList = new ArrayList<>();
        list.forEach(el -> {
            fileList.add(new File(pathConfig.getStoreFiles() + File.separator + el.getSourceStoreId()));
            renameList.add(new String(el.getFileName() + "." + el.getSuffix()));
        });
        File temp = File.createTempFile("zipFiles", "zip");
        zipFilesWithRename(temp, fileList, renameList);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment;filename=zipFiles.zip");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(temp.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(FileUtils.openInputStream(temp)));
    }

    /**
     * @param zip        目标压缩文件
     * @param srcFiles   压缩文件列表
     * @param renameList 重命名列表
     *
     * @throws IOException
     */
    public void zipFilesWithRename(File zip, List<File> srcFiles, List<String> renameList) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
        byte[] buf = new byte[1024];
        try {
            for (int i = 0; i < srcFiles.size(); i++) {
                FileInputStream in = new FileInputStream(srcFiles.get(i));
                out.putNextEntry(new ZipEntry(renameList.get(i)));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.close();
    }

    /**
     * 将指定的多个数据存储打包
     * @param sourceStoreIdList 数据存储的ID列表
     * @return
     * @throws IOException
     */
    @RequestMapping (value = "/getResources", method = RequestMethod.GET)
    ResponseEntity<InputStreamResource> getResources(@RequestParam ("sourceStoreId") List<String> sourceStoreIdList
    ) throws IOException {
        List<DataResource> dataResourceList = dataResourceServiceImp.getBySourceStoreIdList(sourceStoreIdList);
        List<File> fileList = new ArrayList<>();
        List<String> renameList = new ArrayList<>();

        dataResourceList.forEach(el -> {
            fileList.add(new File(pathConfig.getStoreFiles() + File.separator + el.getSourceStoreId()));
            renameList.add(el.getFileName() + "." + el.getSuffix());
        });
        File temp = File.createTempFile("zipFiles", "zip");
        zipFilesWithRename(temp, fileList, renameList);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment;filename=zipFiles.zip");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(temp.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(FileUtils.openInputStream(temp)));
    }

    @RequestMapping (value = "/getResource", method = RequestMethod.GET)
    ResponseEntity<InputStreamResource> getResource(@RequestParam ("sourceStoreId") String sourceStoreId) throws IOException {
        DataResource dataResource = dataResourceServiceImp.getBySourceStoreId(sourceStoreId);
        File file = new File(pathConfig.getStoreFiles() + File.separator + sourceStoreId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment;filename=" + dataResource.getFileName() + "." + dataResource.getSuffix());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(FileUtils.openInputStream(file)));
    }

    /**
     * 将指定的数据存储，先全部解压缩到特定文件夹，在压缩打包
     * @param sourceStoreIdList 数据存储的ID列表
     * @return
     * @throws IOException
     */
    @RequestMapping (value = "/getResources/songjie", method = RequestMethod.GET)
    ResponseEntity<InputStreamResource> getResourcesBySongJie(@RequestParam ("sourceStoreId") List<String> sourceStoreIdList
    ) throws IOException {
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < sourceStoreIdList.size(); i++) {
            fileList.add(new File(pathConfig.getStoreFiles() + File.separator + sourceStoreIdList.get(i)));
        }
        File temp = File.createTempFile("zipFiles", "zip");
        filesToZipWithPrefix(temp, fileList);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment;filename=zipFiles.zip");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(temp.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(FileUtils.openInputStream(temp)));
    }

    /**
     * @param zip      目标压缩文件
     * @param srcFiles 压缩文件列表
     *                 将压缩文件列表中的所有数据先解压，这里的解压会在解压的文件中添加<b>前缀</b>，然后全部压缩到目标压缩文件
     *
     * @throws IOException
     */
    private void filesToZipWithPrefix(File zip, List<File> srcFiles) throws IOException {
        String uid = UUID.randomUUID().toString();
        for (File srcFile : srcFiles) {
            unZipFilesWithPrefixFilterSuffix(srcFile, pathConfig.getDataProcess() + File.separator + uid, uid, null);
        }
        File dir = new File(pathConfig.getDataProcess() + File.separator + uid);
        File[] fileArray = dir.listFiles();
        ZipUtils.zipFiles(zip, "", fileArray);
    }

    /**
     * @param zipFile 待解压缩的文件
     * @param descDir 目标文件夹
     * @param prefix  添加的前缀
     * @param suffix  忽略的后缀文件
     *
     * @throws IOException
     */
    public static void unZipFilesWithPrefixFilterSuffix(File zipFile, String descDir, String prefix, String suffix) throws IOException {
        if (!descDir.endsWith("/")) {
            descDir += "/";
        }
        File dir = new File(descDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);

        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            if (suffix != null && FilenameUtils.getExtension(zipEntryName).toLowerCase().equals(suffix)) {
                continue;
            }
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + prefix + "_" + zipEntryName).replaceAll("\\*", "/");
            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }

            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        zip.close();
    }




    @RequestMapping(value="/fastUpload_saga",method = RequestMethod.POST)
    JsonResult fastUpload_saga(@RequestParam ("sourceStoreId") String sourceStoreId,@RequestParam(value="author",required = false) String author,
                               @RequestParam(value = "tags",required = false)List<String> tags, @RequestParam (value = "md5", required = false) String md5
    ) throws IOException {
        DataResource dataResource = dataResourceServiceImp.getBySourceStoreId(sourceStoreId);
        AddDataResourceDTO addDataResourceDTO = new AddDataResourceDTO();
        if(author!=null){
            addDataResourceDTO.setAuthor(author);
        }
        if(tags!=null){
            addDataResourceDTO.setTags(tags);
        }
        addDataResourceDTO.setType(dataResource.getType());
        addDataResourceDTO.setSuffix(dataResource.getSuffix());
        addDataResourceDTO.setFileName(dataResource.getFileName());
        addDataResourceDTO.setSourceStoreId(sourceStoreId);
        DataResource dataResource_new = dataResourceServiceImp.create(addDataResourceDTO);
        UpdateDataResourceDTO updateDataResourceDTO = new UpdateDataResourceDTO(dataResource.isToGeoserver(), dataResource.getLayerName(), dataResource.getMeta());
        dataResource_new.setMeta(dataResource.getMeta());
        dataResource_new.setToGeoserver(dataResource.isToGeoserver());
        dataResource_new.setLayerName(dataResource.getLayerName());
        dataResourceServiceImp.update(dataResource_new.getId(),updateDataResourceDTO);
        return ResultUtils.success(dataResource_new);
    }

    @RequestMapping(value="/fileToDataResource",method = RequestMethod.POST)
    JsonResult fileToDataResource(@RequestParam ("sourceStoreId") String sourceStoreId,@RequestParam ("dataName") String dataName,@RequestParam(value="author",required = false) String author,
                                  @RequestParam(value = "tags",required = false)List<String> tags
    ) throws IOException {
        String path = pathConfig.getStoreFiles();
        AddDataResourceDTO dataResourceInfo = ZipUtils.getDataResourceInfo(dataName, new File(path + File.separator + sourceStoreId));
        if(author!=null){
            dataResourceInfo.setAuthor(author);
        }
        if(tags!=null){
            dataResourceInfo.setTags(tags);
        }
        dataResourceInfo.setSourceStoreId(sourceStoreId);
        DataResource dataResource = dataResourceServiceImp.create(dataResourceInfo);
        //判断是否为矢量或栅格数据，获取meta
        String metaString = "";
        if(OtherUtils.vectorOrRaster(dataResource.getType())){
            try{
                metaString = dataResourceServiceImp.getMeta(dataResource);
            }catch (Exception e){
                System.out.println(e.getMessage());
                metaString = "";
            }
        }
        if(!metaString.isEmpty()){
            UpdateDataResourceDTO updateDataResourceDTO = new UpdateDataResourceDTO();
            updateDataResourceDTO.setMeta(metaString);
            dataResource = dataResourceServiceImp.update(dataResource.getId(), updateDataResourceDTO);
        }
        return ResultUtils.success(dataResource);
    }

    @RequestMapping(value="/uploadFile_saga",method = RequestMethod.POST)
    JsonResult uploadFile_saga(@RequestParam ("file") MultipartFile file,@RequestParam(value="author",required = false) String author,
                              @RequestParam(value = "tags",required = false)List<String> tags, @RequestParam (value = "md5", required = false) String md5
                               ) throws IOException {
        //不用UUID做文件夹嵌套的
        String path = pathConfig.getStoreFiles();
        String uuid = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        file.transferTo(new File(path + File.separator + uuid));
        AddDataResourceDTO dataResourceInfo = ZipUtils.getDataResourceInfo(fileName, new File(path + File.separator + uuid));
        if(author!=null){
            dataResourceInfo.setAuthor(author);
        }
        if(tags!=null){
            dataResourceInfo.setTags(tags);
        }
        if (md5 != null) {
            FileResource fileResource = new FileResource();
            fileResource.setMd5(md5);
            fileResource.setSourceStoreId(uuid);
            fileResourceRepository.insert(fileResource);
        }
        dataResourceInfo.setSourceStoreId(uuid);
        DataResource dataResource = dataResourceServiceImp.create(dataResourceInfo);
        //判断是否为矢量或栅格数据，获取meta
        String metaString = "";
        if(OtherUtils.vectorOrRaster(dataResource.getType())){
            try{
                metaString = dataResourceServiceImp.getMeta(dataResource);
            }catch (Exception e){
                System.out.println(e.getMessage());
                metaString = "";
            }
        }
        if(!metaString.isEmpty()){
            UpdateDataResourceDTO updateDataResourceDTO = new UpdateDataResourceDTO();
            updateDataResourceDTO.setMeta(metaString);
            dataResource = dataResourceServiceImp.update(dataResource.getId(), updateDataResourceDTO);
        }
        return ResultUtils.success(dataResource);
    }



    @RequestMapping (value = "/{id}/toGeoserver", method = RequestMethod.GET)
    @ApiOperation (value = "将shapefile或者geotiff文件发布到geoserver中", notes = "")
    JsonResult toGeoserver(@PathVariable ("id") String id) throws IOException {

        DataResource dataResource = dataResourceServiceImp.get(id);
        if (dataResource.isToGeoserver()) {
            //已发布服务
            return ResultUtils.success("该数据已发布为服务");
        }
        ArrayList<String> layerNameList = new ArrayList<>();
        String filePath = pathConfig.getStoreFiles() + File.separator + dataResource.getSourceStoreId();
        long time = System.currentTimeMillis();
        if (dataResource.getType() == DataResourceTypeEnum.SHAPEFILE || dataResource.getType() == DataResourceTypeEnum.SHAPEFILE_LIST ) {
//            unZipFilesWithPrefixFilterSuffix(new File(filePath),pathConfig.getShapefiles(),id,"mshp");
            ZipUtils.unZipByApp(pathConfig.getGetMiniZip(),filePath,pathConfig.getShapefiles(),id+"_"+time,".mshp");
//            //获取文件夹下后缀为 shp 的数据;
            String dir = pathConfig.getShapefiles()+File.separator+id+"_"+time;
            Collection<File> fileCollection = FileUtils.listFiles(new File(dir), new SuffixFileFilter(".shp",IOCase.INSENSITIVE), null);
            Iterator<File> iterator = fileCollection.iterator();
            int index = 0;
            while(iterator.hasNext()){
                File file =iterator.next();
                String layerName = geoserverService.createShapeFile(id+index,FilenameUtils.getBaseName(file.getName()),dir);
                layerNameList.add(layerName);
                index++;
            }
//            layerName = geoserverService.createShapeFile(id);
        } else if (dataResource.getType() == DataResourceTypeEnum.GEOTIFF ||dataResource.getType() == DataResourceTypeEnum.GEOTIFF_LIST) {
            long start = System.currentTimeMillis();
            ZipUtils.unZipByApp(pathConfig.getGetMiniZip(),filePath,pathConfig.getGeotiffes(),id+"_"+time,"");
            long end = System.currentTimeMillis();
            System.out.println("解压缩耗时："+(end-start));
            //获取文件夹下后缀为 tif的数据;
            String dir = pathConfig.getGeotiffes()+File.separator+id+"_"+time;
            Collection<File> fileCollection = FileUtils.listFiles(new File(dir), new SuffixFileFilter(".tif",IOCase.INSENSITIVE), null);
            Iterator<File> iterator = fileCollection.iterator();
            int index = 0;
            while(iterator.hasNext()){
                File file = iterator.next();
                String layerName = geoserverService.createGeoTiff(id+index,FilenameUtils.getBaseName(file.getName()),dir);
                layerNameList.add(layerName);
                index++;
            }
        } else if (dataResource.getType() == DataResourceTypeEnum.SDAT || dataResource.getType() == DataResourceTypeEnum.SDAT_LIST) {
            //将sdat文件解压至 dataProcess 文件夹
            String dirPath = pathConfig.getDataProcess() + File.separator + id + "_" + time;
            long start = System.currentTimeMillis();
            ZipUtils.unZipByApp(pathConfig.getGetMiniZip(),filePath,pathConfig.getDataProcess(),id+"_"+time,"");
            long end = System.currentTimeMillis();
            System.out.println("解压缩耗时："+(end-start));
            //sdat转tiff需要耗时
            long start_tif = System.currentTimeMillis();
            String unzipDirPath = pathConfig.getGeotiffes()+File.separator+id+"_"+time;
            geoserverService.sdatToGeotiff(id,dirPath, unzipDirPath);
            long end_tif = System.currentTimeMillis();
            System.out.println("sdat转tiff耗时："+(end_tif-start_tif));
            //获取文件夹下后缀为 tif的数据;
            Collection<File> fileCollection = FileUtils.listFiles(new File(unzipDirPath),new SuffixFileFilter(".tif",IOCase.INSENSITIVE), null);
            Iterator<File> iterator = fileCollection.iterator();
            int index = 0;
            while(iterator.hasNext()){
                File file = iterator.next();
                String layerName = geoserverService.createGeoTiff(id+index,FilenameUtils.getBaseName(file.getName()),unzipDirPath);
                layerNameList.add(layerName);
                index++;
            }

        } else {
            throw new MyException(ResultEnum.NOTSUPPORT_GEOSERVER_ERROR);
        }
        UpdateDataResourceDTO updateDataResourceDTO = new UpdateDataResourceDTO();
        updateDataResourceDTO.setToGeoserver(true);
        updateDataResourceDTO.setLayerName(layerNameList);
        updateDataResourceDTO.setMeta(dataResource.getMeta());
        return ResultUtils.success(dataResourceServiceImp.update(id, updateDataResourceDTO));
    }

    @RequestMapping (value = "/{id}/getMeta", method = RequestMethod.GET)
    @ApiOperation (value = "获取shapefile或者geotiff文件的meta", notes = "")
    JsonResult getMeta(@PathVariable ("id") String id) throws IOException {
        DataResource dataResource = dataResourceServiceImp.get(id);
        if (dataResource.getMeta() != null) {
            return ResultUtils.success(dataResource.getMeta());
        }
        if (dataResource.getType() == DataResourceTypeEnum.SHAPEFILE || dataResource.getType() == DataResourceTypeEnum.GEOTIFF || dataResource.getType() == DataResourceTypeEnum.SDAT) {
            String metaString = dataResourceServiceImp.getMeta(dataResource);
            UpdateDataResourceDTO updateDataResourceDTO = new UpdateDataResourceDTO();
            updateDataResourceDTO.setMeta(metaString);
            updateDataResourceDTO.setLayerName(dataResource.getLayerName());
            updateDataResourceDTO.setToGeoserver(dataResource.isToGeoserver());
            dataResourceServiceImp.update(id, updateDataResourceDTO);
            return ResultUtils.success(metaString);
        } else {
            throw new MyException(ResultEnum.NOTSUPPORT_GETMETA_ERROR);
        }
    }

    @RequestMapping (value = "/{id}/getDbf", method = RequestMethod.GET)
    @ApiOperation (value = "获取shapefile的Dbf", notes = "")
    JsonResult getMeta(@RequestParam (value = "from", required = false) Integer from,
                       @RequestParam (value = "to", required = false) Integer to,
                       @PathVariable ("id") String id
    ) throws IOException {
        return ResultUtils.success(dataResourceServiceImp.getDbfInfo(id, from, to));
    }


    /***
     *
     * 门户数据管理器按关键字查询
     * @param id
     * @param searchContent
     * @return
     */
    @RequestMapping (value = "/MangerS", method = RequestMethod.GET)
    @ApiOperation (value = "用户数据集关键字搜筛选", notes = "")
    JsonResult dataManagerKeywordsSearch(@RequestParam ("id") String id,
                                   @RequestParam ("searchContent") String searchContent)  {
        return  ResultUtils.success(dataResourceServiceImp.managerKeywordsSearch(id,searchContent));
    }


    /***
     * 门户数据管理器按图片分类
     * @param id
     * @return
     */
    @RequestMapping (value = "/managerPics", method = RequestMethod.GET)
    @ApiOperation (value = "用户数据集图片分类筛选", notes = "")
    JsonResult pics(@RequestParam ("id") String id
                                         )  {
        return  ResultUtils.success(dataResourceServiceImp.managerPics(id));
    }


    /***
     * 门户数据管理器按文档分类
     * @param id
     * @return
     */
    @RequestMapping (value = "/managerDoc", method = RequestMethod.GET)
    @ApiOperation (value = "用户数据集文档分类筛选", notes = "")
    JsonResult doc(@RequestParam ("id") String id)  {
        return  ResultUtils.success(dataResourceServiceImp.managerDoc(id));
    }

    /***
     * 门户数据管理器按其他分类
     * @param id
     * @return
     */
    @RequestMapping (value = "/managerOhr", method = RequestMethod.GET)
    @ApiOperation (value = "用户数据集其他分类筛选", notes = "")
    JsonResult other(@RequestParam ("id") String id)  {
        return  ResultUtils.success(dataResourceServiceImp.managerOhr(id));
    }






}
