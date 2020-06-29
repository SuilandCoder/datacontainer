package njnu.opengms.container.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.opengms.container.component.PathConfig;
import njnu.opengms.container.dto.dataresource.AddDataResourceDTO;
import njnu.opengms.container.enums.DataResourceTypeEnum;
import njnu.opengms.container.enums.FromWhereEnum;
import njnu.opengms.container.enums.ResultEnum;
import njnu.opengms.container.exception.MyException;
import njnu.opengms.container.pojo.DataResource;
import njnu.opengms.container.pojo.FileResource;
import njnu.opengms.container.repository.DataResourceRepository;
import njnu.opengms.container.repository.FileResourceRepository;
import njnu.opengms.container.utils.OtherUtils;
import njnu.opengms.container.utils.ProcessUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 16:01 2019/10/11
 * @Modified By:
 **/
@Service
public class CmpTaskService {
    @Autowired
    PathConfig pathConfig;

    @Autowired
    FileResourceRepository fileResourceRepository;

    @Autowired
    DataResourceServiceImp dataResourceServiceImp;

    @Autowired
    DataResourceRepository dataResourceRepository;


    public void installPythonLib(JSONArray dependencyLibs) {
        String res = ProcessUtils.exeCmd_Cross_platform("pip list");
        JSONArray installedLibs = OtherUtils.getInstalledLib(res);

        //todo 目前只校验 lib 名称，不校验版本
        for (int i = 0; i < dependencyLibs.size(); i++) {
            JSONObject dependencyLib = dependencyLibs.getJSONObject(i);
            String name = dependencyLib.getString("name");
            boolean needInstall = true;
            for (int j = 0; j < installedLibs.size(); j++) {
                JSONObject installedLib = installedLibs.getJSONObject(j);
                if (name.equals(installedLib.getString("name"))) {
                    needInstall = false;
                    break;
                }
            }
            if (needInstall) {
                String installCmdInfo = ProcessUtils.exeCmd_Cross_platform("pip install " + name);
                System.out.println(installCmdInfo);
            }
        }
    }


    //运行脚本，并上传到数据资源
    public DataResource runScript(String interpretor, String cmd, String outputDir) throws IOException {
        if ("python".equals(interpretor)) {
            String cmdOutput = ProcessUtils.exeCmd_Cross_platform(cmd);
//            System.out.println(cmdOutput);
            // 将输出数据作为数据资源上传
            File dir = new File(outputDir);
            File[] files = dir.listFiles();
            if (files.length > 0) {
                // 默认只有一个输出
                File output = files[0];
                String baseName = FilenameUtils.getBaseName(output.getName());
                String extension = FilenameUtils.getExtension(output.getName());
                String sourceStoreId = innerUploadFile(outputDir,output);
                DataResource dataResource = innerPostFiletoDataSource(baseName, extension, sourceStoreId);
                return dataResource;
            } else {
                throw new MyException(ResultEnum.FAILED_TO_EXCUTE_SCRIPT);
            }
        }
        throw new MyException(ResultEnum.FAILED_TO_EXCUTE_SCRIPT);
    }

    public String innerUploadFile(String outputDir,File file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String path = pathConfig.getStoreFiles();
        // 拷贝文件
        copyFileUsingChannel(file,new File(path + File.separator + uuid));
        // 删除文件夹
        file.delete();
//        FileUtils.deleteDirectory(new File(outputDir));
//        file.renameTo(new File(path + File.separator + uuid));
        FileResource fileResource = new FileResource();
        fileResource.setSourceStoreId(uuid);
        fileResourceRepository.insert(fileResource);
        return uuid;
    }

    //todo 上传到 dataResource
    public DataResource innerPostFiletoDataSource(String baseName,String extension,String sourceStoreId){
        AddDataResourceDTO addDataResourceDTO = new AddDataResourceDTO();
        addDataResourceDTO.setSourceStoreId(sourceStoreId);
        addDataResourceDTO.setFileName(baseName);
        addDataResourceDTO.setSuffix(extension);
        addDataResourceDTO.setType(DataResourceTypeEnum.OTHER);
        addDataResourceDTO.setFromWhere(FromWhereEnum.COMPARISON);
        addDataResourceDTO.setAuthor("comparison");
        DataResource dataResource = new DataResource();
        BeanUtils.copyProperties(addDataResourceDTO, dataResource);
        dataResource.setCreateDate(new Date());
        dataResourceRepository.insert(dataResource);
        return dataResource;
    }

    public static void copyFileUsingChannel(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            if(sourceChannel != null) {
                sourceChannel.close();
            }
            if(destChannel != null) {
                destChannel.close();
            }
        }
    }
}
