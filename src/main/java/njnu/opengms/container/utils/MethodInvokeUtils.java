package njnu.opengms.container.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import njnu.opengms.container.bean.ProcessResponse;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName MethodInvokeUtils
 * @Description todo
 * @Author sun_liber
 * @Date 2019/1/9
 * @Version 1.0.0
 */
public class MethodInvokeUtils {
    /**
     * @param basePath 数据映射方法存在的路径
     *
     * @return
     */
    public static ProcessResponse computeMap(String basePath, String callType, String input, String output) throws IOException {
        File configFile = new File(basePath + File.separator + "cfg.json");
        JSONObject jsonObject = JSON.parseObject(FileUtils.readFileToString(configFile, "utf-8"));
        JSONObject config = (JSONObject) jsonObject.get("config");
        String type = config.getString("type");
        String invokeEntity = config.getString("start");

        String cmd = "";
        if ("exe".equals(type)) {
            cmd = basePath + File.separator + invokeEntity;
        } else if ("jar".equals(type)) {
            cmd = "java -jar " + basePath + File.separator + invokeEntity;
        }

        if ("src2udx".equals(callType)) {
            cmd += " -r -f " + input + " -u " + output;
        } else if ("udx2src".equals(callType)) {
            cmd += " -w -u " + input + " -f " + output;
        }
        return ProcessUtils.StartProcess(basePath, cmd);
    }

    /**
     * @param basePath 数据重构方法存在的路径
     *
     * @return
     */
    public static ProcessResponse computeRefactor(String basePath, String method, List<String> fileInList, List<String> fileOutList) throws IOException {
        File configFile = new File(basePath + File.separator + "cfg.json");
        JSONObject jsonObject = JSON.parseObject(FileUtils.readFileToString(configFile, "utf-8"));
        JSONObject config = (JSONObject) jsonObject.get("config");
        String type = config.getString("type");
        String invokeEntity = config.getString("start");
        String cmd = "";
        if ("exe".equals(type)) {
            cmd = basePath + File.separator + invokeEntity + " " + method;
        } else if ("jar".equals(type)) {
            cmd = "java -jar " + basePath + File.separator + invokeEntity + " " + method;
        }
        for (String s : fileInList) {
            cmd += " " + s;
        }
        for (String s : fileOutList) {
            cmd += " " + s;
        }
        return ProcessUtils.StartProcess(basePath, cmd);
    }


}
