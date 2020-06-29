package njnu.opengms.container.getmeta.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.opengms.container.getmeta.DataStoreMetaGet;
import njnu.opengms.container.getmeta.meta.ShapefileMeta;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.PropertyType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ShapefileMetaGet
 * @Description 目前仅支持从shp文件中获取
 * @Author sun_liber
 * @Date 2019/4/2
 * @Version 1.0.0
 */
public class ShapefileMetaGet implements DataStoreMetaGet<ShapefileMeta> {
    @Override
    public ShapefileMeta getMeta(File file) throws IOException {
        ShapefileMeta shapefileMeta = new ShapefileMeta();
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        ((ShapefileDataStore) store).setCharset(Charset.forName("GBK"));

        SimpleFeatureSource featureSource = store.getFeatureSource();

        shapefileMeta.setName(featureSource.getName().getLocalPart());

        int count = featureSource.getCount(Query.ALL);
        shapefileMeta.setFeatureCount(count);

        SimpleFeatureType schema = featureSource.getSchema();

        String geometry = String.valueOf(schema.getGeometryDescriptor().getType().getName());
        shapefileMeta.setGeometry(geometry);

        List<Map<String, String>> fields = new ArrayList<>();
        for (PropertyDescriptor propertyDescriptor : schema.getDescriptors()) {
            Map<String, String> map = new HashMap<>(3);
            if (!propertyDescriptor.getName().toString().equals("the_geom")) {
                PropertyType propertyType = propertyDescriptor.getType();
                map.put("Field", propertyType.getName().toString());
                String type = propertyType.getBinding().toString();
                map.put("Type", type.substring(type.lastIndexOf(".") + 1));
                fields.add(map);
            }
        }

        shapefileMeta.setFields(fields);

        ReferencedEnvelope bounds = featureSource.getBounds();
        shapefileMeta.setLowerCorner(bounds.getLowerCorner().getCoordinate());
        shapefileMeta.setUpperCorner(bounds.getUpperCorner().getCoordinate());

        shapefileMeta.setProj(bounds.getCoordinateReferenceSystem().toString());


        return shapefileMeta;
    }

    public JSONArray readDBF(File file, Integer from, Integer to) throws IOException {
        FileChannel in = new FileInputStream(file).getChannel();
        DbaseFileReader dbfReader = new DbaseFileReader(in, false, Charset.forName("GBK"));
        DbaseFileHeader header = dbfReader.getHeader();
        int fields = header.getNumFields();

        JSONArray jsonArray = new JSONArray();
        if (from != null && to != null) {
            while (from-- != 0) {
                dbfReader.skip();
            }
            while (dbfReader.hasNext() && to-- != 0) {
                DbaseFileReader.Row row = dbfReader.readRow();
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < fields; i++) {
                    jsonObject.put(header.getFieldName(i), row.read(i));
                }
                jsonArray.add(jsonObject);
            }
            dbfReader.close();
            in.close();
            return jsonArray;
        } else {
            while (dbfReader.hasNext()) {
                DbaseFileReader.Row row = dbfReader.readRow();
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < fields; i++) {
                    jsonObject.put(header.getFieldName(i), row.read(i));
                }
                jsonArray.add(jsonObject);
            }
            dbfReader.close();
            in.close();
            return jsonArray;
        }

    }


}
