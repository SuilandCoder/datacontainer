package njnu.opengms.container.controller.comparison;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.component.PathConfig;
import njnu.opengms.container.enums.ResultEnum;
import njnu.opengms.container.exception.MyException;
import njnu.opengms.container.pojo.DataResource;
import njnu.opengms.container.repository.CmpTaskRepository;
import njnu.opengms.container.service.CmpTaskService;
import njnu.opengms.container.service.DataResourceServiceImp;
import njnu.opengms.container.utils.OtherUtils;
import njnu.opengms.container.utils.ProcessUtils;
import njnu.opengms.container.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 14:40 2019/10/11
 * @Modified By:
 **/
@RestController
@RequestMapping(value = "/cmpTask")
public class CmpTaskController {
    @Autowired
    PathConfig pathConfig;

    @Autowired
    CmpTaskRepository cmpTaskRepository;

    @Autowired
    CmpTaskService cmpTaskService;

    @Autowired
    DataResourceServiceImp dataResourceServiceImp;


    @RequestMapping(value = "/runCmpTask", method = RequestMethod.POST)
    JsonResult runCmpTask(@RequestBody JSONObject cmpTaskInfo) {
//        JSONObject method = JSONObject.parseObject("method");
        JSONObject cmdJson = new JSONObject();
        JSONArray params = cmpTaskInfo.getJSONArray("params");
        JSONArray inputList = cmpTaskInfo.getJSONArray("inputList");

        JSONObject inputObj = cmpTaskInfo.getJSONObject("input");
        String inputName = OtherUtils.removeExtention(inputObj.getString("name"));//防止有后缀
        JSONObject outputObj = cmpTaskInfo.getJSONObject("output");
        String outputName = OtherUtils.removeExtention(outputObj.getString("name"));//防止有后缀

        //1.找到对比方法脚本地址
        String scriptSourceId = cmpTaskInfo.getString("methodSourceId");
        String scriptPath = pathConfig.getStoreFiles() + File.separator + scriptSourceId;

        //2.找到所有输入数据
        JSONArray inputPathArray = new JSONArray();
        JSONArray inputInfoArray = new JSONArray();
//        String inputsPathStr = "--" + inputName+" ";
        // 增加参数：输入数据文件名列表：
        String fileNameList = "--inputFileNameList";
        for (int i = 0; i < inputList.size(); i++) {
            JSONObject input =  inputList.getJSONObject(i);
            String dataStoreId = input.getString("dataStoreId");
            inputPathArray.add(pathConfig.getStoreFiles() + File.separator + dataStoreId);

            // 获取文件信息
            DataResource dataResource = dataResourceServiceImp.getBySourceStoreId(dataStoreId);
            inputInfoArray.add(dataResource);
        }
        cmdJson.put(inputName,inputPathArray);
        cmdJson.put("inputInfos",inputInfoArray);

        //3.配置 params 参数
        for (int j = 0; j < params.size(); j++) {
            JSONObject param = params.getJSONObject(j);
            cmdJson.put(param.getString("name"),param.getString("value"));
        }

        //4. 输出 cmd
        String tmpFilePath = pathConfig.getTmp() + File.separator + System.currentTimeMillis();
        File dir = new File(tmpFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
//        String outputCmd = "--" + outputName + " " + tmpFilePath;
        cmdJson.put(outputName,tmpFilePath);

        //5. 获取python脚本依赖并安装
        String interpretor = cmpTaskInfo.getString("interpretor");
        if(!"python".equals(interpretor)){
            throw new MyException(ResultEnum.INTERPRETOR_NOT_SUPPORTED);
        }

        JSONArray dependencyLibs = cmpTaskInfo.getJSONArray("dependencyLibs");
        cmpTaskService.installPythonLib(dependencyLibs);

        //6. 执行 python 脚本
        // 如果是linux版本，不加 '\',json数据需用单引号括起来
        String os = System.getProperty("os.name");
        String cmd = "";
        if(os.toLowerCase().startsWith("win")){
            cmd = "python "+ scriptPath+" "+"--json "+cmdJson.toString().replaceAll("\"","\\\\\"");
        }else if(os.toLowerCase().startsWith("linux")){
            cmd = "python "+ scriptPath+" "+"--json "+"\'"+cmdJson.toString()+"\'";
        }
        System.out.println("python cmd:"+cmd);
        try {
            DataResource dataResource = cmpTaskService.runScript(interpretor, cmd, tmpFilePath);
            outputObj.put("fileName",dataResource.getFileName());
            outputObj.put("suffix",dataResource.getSuffix());
            outputObj.put("dataStoreId",dataResource.getSourceStoreId());
//            outputObj.put()
            cmpTaskInfo.put("output",outputObj);
            return ResultUtils.success(cmpTaskInfo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.FAILED_TO_EXCUTE_SCRIPT);
        }
    }



    JsonResult runCmpTask_before(@RequestBody JSONObject cmpTaskInfo) {
//        JSONObject method = JSONObject.parseObject("method");
        JSONArray params = cmpTaskInfo.getJSONArray("params");
        JSONArray inputList = cmpTaskInfo.getJSONArray("inputList");

        JSONObject inputObj = cmpTaskInfo.getJSONObject("input");
        String inputName = OtherUtils.removeExtention(inputObj.getString("name"));//防止有后缀
        JSONObject outputObj = cmpTaskInfo.getJSONObject("output");
        String outputName = OtherUtils.removeExtention(outputObj.getString("name"));//防止有后缀

        //1.找到对比方法脚本地址
        String scriptSourceId = cmpTaskInfo.getString("methodSourceId");
        String scriptPath = pathConfig.getStoreFiles() + File.separator + scriptSourceId;

        //2.找到所有输入数据
        String inputsPathStr = "--" + inputName+" ";
        // 增加参数：输入数据文件名列表：
        String fileNameList = "--inputFileNameList";
        for (int i = 0; i < inputList.size(); i++) {
            JSONObject input =  inputList.getJSONObject(i);
            String dataStoreId = input.getString("dataStoreId");
            if (i == inputList.size() - 1) {
                inputsPathStr += pathConfig.getStoreFiles() + File.separator + dataStoreId;
            } else {
                inputsPathStr += pathConfig.getStoreFiles() + File.separator + dataStoreId + ",";
            }

            // 获取文件信息
            DataResource dataResource = dataResourceServiceImp.getBySourceStoreId(dataStoreId);
            String fileName = dataResource.getFileName();
            if (i == inputList.size() - 1) {
                fileNameList += fileName;
            } else {
                fileNameList +=  ",";
            }
        }

        //3.配置 params 参数
        String paramCmd = "";
        for (int j = 0; j < params.size(); j++) {
            JSONObject param = params.getJSONObject(j);
            paramCmd = paramCmd + "--" + param.getString("name") + " " + param.getString("value") + " ";
        }

        //4. 输出 cmd
        String tmpFilePath = pathConfig.getTmp() + File.separator + System.currentTimeMillis();
        File dir = new File(tmpFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String outputCmd = "--" + outputName + " " + tmpFilePath;

        //5. 获取python脚本依赖并安装
        String interpretor = cmpTaskInfo.getString("interpretor");
        if(!"python".equals(interpretor)){
            throw new MyException(ResultEnum.INTERPRETOR_NOT_SUPPORTED);
        }

        JSONArray dependencyLibs = cmpTaskInfo.getJSONArray("dependencyLibs");
        cmpTaskService.installPythonLib(dependencyLibs);

        //6. 执行 python 脚本
        String cmd = "python "+ scriptPath+" "+inputsPathStr+" "+paramCmd+" "+outputCmd+" "+fileNameList;
        System.out.println("python cmd:"+cmd);
        try {
            DataResource dataResource = cmpTaskService.runScript(interpretor, cmd, tmpFilePath);
            outputObj.put("fileName",dataResource.getFileName());
            outputObj.put("suffix",dataResource.getSuffix());
            outputObj.put("dataStoreId",dataResource.getSourceStoreId());
//            outputObj.put()
            cmpTaskInfo.put("output",outputObj);
            return ResultUtils.success(cmpTaskInfo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.FAILED_TO_EXCUTE_SCRIPT);
        }
    }
}
