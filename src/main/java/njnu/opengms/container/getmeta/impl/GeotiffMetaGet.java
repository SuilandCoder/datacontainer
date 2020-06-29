package njnu.opengms.container.getmeta.impl;

import njnu.opengms.container.getmeta.DataStoreMetaGet;
import njnu.opengms.container.getmeta.meta.GeotiffMeta;
import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataDecoder;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.GeneralEnvelope;
import org.opengis.coverage.grid.GridEnvelope;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName GeotiffMetaGet
 * @Description todo
 * @Author sun_liber
 * @Date 2019/4/2
 * @Version 1.0.0
 */
public class GeotiffMetaGet implements DataStoreMetaGet<GeotiffMeta> {
    @Override
    public GeotiffMeta getMeta(File file) throws IOException {
        GeotiffMeta geotiffMeta = new GeotiffMeta();

//        AbstractGridFormat format = GridFormatFinder.findFormat(file);
//        Hints hints = new Hints();
//        // 目前屏蔽了除geotiff的其他数据
//        if (format instanceof GeoTiffFormat) {
//            hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
//        }else{
//            throw  new MyException("format is :"+format.getName()+" ,not GeoTiffFormat");
//        }
//        GridCoverage2DReader reader = format.getReader(file, hints);
//        GridCoverage2D coverage = reader .read(null);

        GeoTiffReader reader = new GeoTiffReader(file);
        geotiffMeta.setName(FilenameUtils.getBaseName(file.getName()));
        geotiffMeta.setBandCount(reader.getGridCoverageCount());

        GridEnvelope grid = reader.getOriginalGridRange();
        geotiffMeta.setLow(grid.getLow().getCoordinateValues());
        geotiffMeta.setHigh(grid.getHigh().getCoordinateValues());

        GeneralEnvelope envelope = reader.getOriginalEnvelope();
        geotiffMeta.setLowerCorner(envelope.getLowerCorner().getCoordinate());
        geotiffMeta.setUpperCorner(envelope.getUpperCorner().getCoordinate());
        geotiffMeta.setProj(reader.getCoordinateReferenceSystem().toString());

        GeoTiffIIOMetadataDecoder metadata = reader.getMetadata();
        geotiffMeta.setPixelScales(metadata.getModelPixelScales().getValues());

        return geotiffMeta;
    }


}
