package njnu.opengms.container.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.component.PathConfig;
import njnu.opengms.container.enums.ResultEnum;
import njnu.opengms.container.exception.MyException;
import njnu.opengms.container.pojo.FileResource;
import njnu.opengms.container.repository.FileResourceRepository;
import njnu.opengms.container.utils.ResultUtils;
import njnu.opengms.container.utils.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName FileController
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/18
 * @Version 1.0.0
 */
@RestController
@RequestMapping (value = "/file")
public class FileController {
    private static final String TYPE_MAP = "map";
    private static final String TYPE_REFACTOR = "refactor";
    private static final String TYPE_DATA_PROCESS = "data_process";
    private static final String TYPE_STORE_DATARESOURCE = "store_dataResource_files";

    @Autowired
    PathConfig pathConfig;

    @Autowired
    FileResourceRepository fileResourceRepository;


    @ApiOperation (value = "上传文件", notes = "上传映射、重构服务实体、在线调用的输入文件、数据资源")
    @ApiImplicitParams ({
            @ApiImplicitParam (name = "type", value = "上传实体的类型,可以为map、refactor、data_process、store_dataResource_files", dataType = "string", paramType = "path", required = true),
            @ApiImplicitParam (name = "md5", value = "当上传类型为store_dataResource_files，可以添加由spark-md5.js提供的md5值，以此做快速上传", dataType = "string", paramType = "query", required = false)}
    )
    @RequestMapping (value = "/upload/{type}", method = RequestMethod.POST)
    JsonResult upload(@PathVariable ("type") String type, @RequestParam ("file") MultipartFile file,
                      @RequestParam (value = "md5", required = false) String md5
    ) throws IOException {
        String path;
        if (TYPE_MAP.equals(type)) {
            path = pathConfig.getServicesMap();
        } else if (TYPE_REFACTOR.equals(type)) {
            path = pathConfig.getServicesRefactor();
        } else if (TYPE_DATA_PROCESS.equals(type)) {
            path = pathConfig.getDataProcess();
        } else if (TYPE_STORE_DATARESOURCE.equals(type)) {
            //不用UUID做文件夹嵌套的
            path = pathConfig.getStoreFiles();
            String uuid = UUID.randomUUID().toString();
            String filename = file.getOriginalFilename();
            file.transferTo(new File(path + File.separator + uuid));
            if (md5 != null) {
                FileResource fileResource = new FileResource();
                fileResource.setMd5(md5);
                fileResource.setSourceStoreId(uuid);
                fileResourceRepository.insert(fileResource);
            }

//            return ResultUtils.success(uuid);
            Map data =new HashMap<>();
            data.put("source_store_id",uuid);
            data.put("file_name",filename);

            return ResultUtils.success(data);
        } else {
            throw new MyException(ResultEnum.UPLOAD_TYPE_ERROR);
        }
        path += File.separator + (UUID.randomUUID().toString());
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file.transferTo(new File(path + File.separator + file.getOriginalFilename()));
        // 针对映射和重构需要进行解压
        // 注意解压路径是同级目录下的invoke文件夹
        if (TYPE_MAP.equals(type) || TYPE_REFACTOR.equals(type)) {
            ZipUtils.unZipFiles(new File(path + File.separator + file.getOriginalFilename()), path + File.separator + "invoke");
        }
        int index = path.indexOf(pathConfig.getBase()) + pathConfig.getBase().length() + 1;
        return ResultUtils.success(path.substring(index, path.length()) + File.separator + file.getOriginalFilename());
    }

    @ApiOperation (value = "快速上传文件", notes = "上传数据资源")
    @ApiImplicitParam (name = "md5", value = "由spark-md.js在前端解析出文件的md5值", dataType = "string", paramType = "path", required = true)
    @RequestMapping (value = "/fastUpload/{md5}", method = RequestMethod.POST)
    JsonResult fastUpload(@PathVariable ("md5") String md5) {
        FileResource fileResource = fileResourceRepository.getFirstByMd5(md5);
        if (fileResource != null) {
            return ResultUtils.success(fileResource.getSourceStoreId());
        } else {
            throw new MyException("该md5:" + md5 + ",不存在对应资源，请使用普通上传");
        }
    }



    @Deprecated
    @ApiOperation (value = "根据映射和重构服务存储在后台的storePath路径，下载对应服务压缩包", notes = "根据文件的路径下载文件,这里用post方法是因为数据库存储的路径是\\,get请求拼接字符串需要对\\进行编码，所以使用post方法")
    @RequestMapping (value = "/download/service", method = RequestMethod.POST)
    ResponseEntity<InputStreamResource> download(@RequestParam ("path") String path) throws IOException {
        File file = new File(pathConfig.getBase() + File.separator + path);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment;filename=" + file.getName());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(FileUtils.openInputStream(file)));
    }


}

