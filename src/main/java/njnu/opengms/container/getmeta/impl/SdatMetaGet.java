package njnu.opengms.container.getmeta.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.opengms.container.getmeta.DataStoreMetaGet;
import njnu.opengms.container.getmeta.meta.GeotiffMeta;
import njnu.opengms.container.utils.XmlUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by SongJie on 2019/4/10 21:40
 */
public class SdatMetaGet implements DataStoreMetaGet<GeotiffMeta> {
    @Override
    public GeotiffMeta getMeta(File file) throws IOException {
        GeotiffMeta geotiffMeta = new GeotiffMeta();
        String xml_str = FileUtils.readFileToString(file, "utf8");
        try {
            JSONObject xml_json = XmlUtils.xmltoJson(xml_str);
            JSONObject saga_metadata = xml_json.getJSONObject("SAGA_METADATA");
            JSONObject projection = saga_metadata.getJSONObject("PROJECTION");
            String proj = projection.getString("OGC_WKT");
            JSONObject history = saga_metadata.getJSONObject("HISTORY");
            JSONObject tool = history.getJSONObject("TOOL");
            JSONArray option = tool.getJSONArray("OPTION");
            double yMin = 0,xMin=0,yMax=0,xMax=0,cellSize = 0;
            for(int i=0;i<option.size();i++){
                //判断是否为 空间信息
                if(option.get(i) instanceof JSONObject){
                    JSONObject geoInfo = (JSONObject) option.get(i);
                    yMin = geoInfo.getDoubleValue("YMIN");
                    xMin = geoInfo.getDoubleValue("XMIN");
                    yMax = geoInfo.getDoubleValue("YMAX");
                    xMax = geoInfo.getDoubleValue("XMAX");
                    cellSize = geoInfo.getDoubleValue("CELLSIZE");
                }
            }
            int[] low = {0,0};
            double[] pixelScales={cellSize,cellSize,0};
            double[] lowerCorner = {xMin,yMin};
            double[] upperCorner = {xMax,yMax};
            int xCell = (int) ((xMax-xMin)/cellSize);
            int yCell = (int) ((yMax-yMin)/cellSize);
            int[] high = {xCell,yCell};

            geotiffMeta.setName(FilenameUtils.getBaseName(file.getName()));
            geotiffMeta.setProj(proj);
            // 不确定如何从文件中读取 bandcount 个数，默认1个。
            geotiffMeta.setBandCount(1);
            geotiffMeta.setHigh(high);
            geotiffMeta.setLow(low);
            geotiffMeta.setPixelScales(pixelScales);
            geotiffMeta.setLowerCorner(lowerCorner);
            geotiffMeta.setUpperCorner(upperCorner);

            return geotiffMeta;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return geotiffMeta;
    }
}
