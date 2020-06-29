package njnu.opengms.container.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.opengms.container.enums.DataResourceTypeEnum;

import javax.json.JsonArray;

/**
 * Created by SongJie on 2019/5/6 14:44
 */
public class OtherUtils {
    public static boolean vectorOrRaster(DataResourceTypeEnum type){
        if(type==DataResourceTypeEnum.GEOTIFF||type==DataResourceTypeEnum.SHAPEFILE||type==DataResourceTypeEnum.SDAT
                ||type==DataResourceTypeEnum.GEOTIFF_LIST||type==DataResourceTypeEnum.SHAPEFILE_LIST||type==DataResourceTypeEnum.SDAT_LIST){
            return true;
        }
        return false;
    }
    
    public static String removeExtention(String fileName){
        int lastPeriodPos = fileName.lastIndexOf(".");
        if(lastPeriodPos<=0){
            return fileName;
        }else{
            return fileName.substring(0,lastPeriodPos);
        }
    }

    public static JSONArray getInstalledLib(String libs){
        String[] split = libs.split("\n");
        JSONArray installedLibs = new JSONArray();
        for(int i=2;i<split.length;i++){
            String libStr = split[i];
            JSONObject object = new JSONObject();
            String libName = libStr.substring(0,libStr.indexOf(' '));
            String libVersion = libStr.substring(libStr.indexOf(' ')).trim();
            object.put("name",libName);
            object.put("version",libVersion);
            installedLibs.add(object);
        }
        return installedLibs;
    }
}
