package njnu.opengms.container.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.component.PathConfig;
import njnu.opengms.container.dto.dataresource.AddDataResourceDTO;
import njnu.opengms.container.dto.dataresource.FindDataResourceDTO;
import njnu.opengms.container.dto.dataresource.UpdateDataResourceDTO;
import njnu.opengms.container.enums.DataResourceTypeEnum;
import njnu.opengms.container.enums.ResultEnum;
import njnu.opengms.container.exception.MyException;
import njnu.opengms.container.getmeta.DataStoreMetaGet;
import njnu.opengms.container.getmeta.impl.GeotiffMetaGet;
import njnu.opengms.container.getmeta.impl.SdatMetaGet;
import njnu.opengms.container.getmeta.impl.ShapefileMetaGet;
import njnu.opengms.container.getmeta.meta.GeotiffMeta;
import njnu.opengms.container.pojo.DataResource;
import njnu.opengms.container.repository.DataResourceRepository;
import njnu.opengms.container.service.common.BaseService;
import njnu.opengms.container.utils.ResultUtils;
import njnu.opengms.container.utils.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName DataResourceService
 * @Description todo
 * @Author sun_liber
 * @Date 2019/2/13
 * @Version 1.0.0
 */
@Service
public class DataResourceServiceImp implements BaseService<DataResource, DataResource, AddDataResourceDTO, FindDataResourceDTO, UpdateDataResourceDTO, String> {

    @Autowired
    DataResourceRepository dataResourceRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    GeoserverService geoserverService;

    @Autowired
    PathConfig pathConfig;

    @Override
    public void delete(String id) throws IOException {
        DataResource dataResource = dataResourceRepository.findById(id).orElse(null);
        if (dataResource != null) {

            if(dataResource.getMeta()!=null){
                //TODO 不确定类型暂时不删除
                //     FileUtils.deleteQuietly(new File(pathConfig.getGetMeta() + File.separator + dataResource.getSourceStoreId()));
            }

            if(dataResource.isToGeoserver()){
                //TODO 不确定类型暂时不删除
                //geoserverService.delete(id, dataResource.getType());
            }
            dataResourceRepository.deleteById(id);
        }
    }

    @Override
    public DataResource update(String id, UpdateDataResourceDTO updateDTO) {
        DataResource dataResource = dataResourceRepository.findById(id).orElseGet(() -> {
            throw new MyException(ResultEnum.NO_OBJECT);
        });
        BeanUtils.copyProperties(updateDTO, dataResource);
        return dataResourceRepository.save(dataResource);
    }

    @Override
    public DataResource create(AddDataResourceDTO addDTO) {
/*        if (addDTO.getFileName().contains(".")) {
            throw new MyException("fileName 请不要带后缀");
        }*/
        DataResource dataResource = new DataResource();
        BeanUtils.copyProperties(addDTO, dataResource);
        dataResource.setCreateDate(new Date());
        return dataResourceRepository.insert(dataResource);
    }

    @Override
    public Page<DataResource> list(FindDataResourceDTO findDataResourceDTO) {
        PageRequest pageRequest;
        if (findDataResourceDTO.getProperties() != null) {
            Sort sort = new Sort(findDataResourceDTO.getAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, findDataResourceDTO.getProperties());
            pageRequest = PageRequest.of(findDataResourceDTO.getPage(), findDataResourceDTO.getPageSize(), sort);
        } else {
            pageRequest = PageRequest.of(findDataResourceDTO.getPage(), findDataResourceDTO.getPageSize());
        }

        String type = findDataResourceDTO.getType();
        String value = findDataResourceDTO.getValue();
        if (type.equals("author")) {
            return dataResourceRepository.getByAuthor(value, pageRequest);
        } else if (type.equals("mdl")) {
            return dataResourceRepository.getByMdlId(value, pageRequest);
        } else if (type.equals("dataItem")) {
            return dataResourceRepository.getByDataItemId(value, pageRequest);
        } else if (type.equals("fileName")) {
            return dataResourceRepository.getByFileNameContains(value, pageRequest);
        } else {
            return dataResourceRepository.findAll(pageRequest);
        }
    }

    @Override
    public DataResource get(String id) {
        return dataResourceRepository.findById(id).orElseGet(() -> {
            throw new MyException(ResultEnum.NO_OBJECT);
        });
    }

    @Override
    public DataResource getByExample(DataResource dataResource) {
        return dataResourceRepository.findOne(Example.of(dataResource)).orElse(null);
    }

    @Override
    public long count() {
        return dataResourceRepository.count();
    }

    public Page<DataResource> getByAuthor(String author, Integer page, Integer pageSize, boolean ascFlag) {
        Sort sort = new Sort(ascFlag ? Sort.Direction.ASC : Sort.Direction.DESC, "createDate");
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return dataResourceRepository.getByAuthor(author, pageable);
    }

    public List<DataResource> getByAuthor(String author) {
        return dataResourceRepository.getByAuthor(author);
    }

    public List<DataResource> getByDataItemId(String dataItemId) {
        return dataResourceRepository.getByDataItemId(dataItemId);
    }

    public List<DataResource> getByFileNameContains(String dataItemFileName) {
        return dataResourceRepository.getByFileNameContains(dataItemFileName);
    }

    public List<DataResource> getByMdlId(String mdlId) {
        return dataResourceRepository.getByMdlId(mdlId);
    }

    public List<DataResource> getBySourceStoreIdList(List<String> sourceStoreIdList) {
        Query query = new Query(Criteria.where("sourceStoreId").in(sourceStoreIdList));
        List<DataResource> result = mongoTemplate.find(query, DataResource.class);
        return result;
    }

    public DataResource getBySourceStoreId(String sourceStoreId) {
        Query query = new Query(Criteria.where("sourceStoreId").is(sourceStoreId));
        List<DataResource> result = mongoTemplate.find(query, DataResource.class);
        if(result.size()>0){
            return result.get(0);
        }else{
            return dataResourceRepository.getBySourceStoreId(sourceStoreId);
        }

    }


    public JSONArray getDbfInfo(String id, Integer from, Integer to) throws IOException {
        DataResource dataResource = this.get(id);
        if (dataResource.getMeta() != null && dataResource.getType().equals(DataResourceTypeEnum.SHAPEFILE)) {
            File dir = new File(pathConfig.getGetMeta() + File.separator + dataResource.getSourceStoreId());
            Collection<File> fileCollection = FileUtils.listFiles(dir, new SuffixFileFilter(".dbf",IOCase.INSENSITIVE), null);
            File real_file = fileCollection.iterator().next();
            DataStoreMetaGet metaGet = new ShapefileMetaGet();
            return ((ShapefileMetaGet) metaGet).readDBF(real_file, from, to);
        } else {
            throw new MyException("该数据的Meta未获取或者该数据不是shapefile格式");
        }
    }

    public String getMeta(DataResource dataResource) throws IOException {
        ZipUtils.unZipFiles(new File(pathConfig.getStoreFiles() + File.separator + dataResource.getSourceStoreId()),
                pathConfig.getGetMeta() + File.separator + dataResource.getSourceStoreId());
        File dir = new File(pathConfig.getGetMeta() + File.separator + dataResource.getSourceStoreId());

        Collection<File> fileCollection;
        File real_file;
        if (dataResource.getType().equals(DataResourceTypeEnum.SHAPEFILE)||dataResource.getType().equals(DataResourceTypeEnum.SHAPEFILE_LIST)) {
            fileCollection = FileUtils.listFiles(dir, new SuffixFileFilter(".shp",IOCase.INSENSITIVE), null);
            JSONArray jsonArray = new JSONArray();
            Iterator<File> iterator = fileCollection.iterator();
            while(iterator.hasNext()){
                real_file = iterator.next();
                DataStoreMetaGet metaGet = new ShapefileMetaGet();
                jsonArray.add(metaGet.getMeta(real_file));
            }
            return jsonArray.toJSONString();
        } else if(dataResource.getType().equals(DataResourceTypeEnum.GEOTIFF)||dataResource.getType().equals(DataResourceTypeEnum.GEOTIFF_LIST)) {
            //GEOTIFF
            fileCollection = FileUtils.listFiles(dir,  new SuffixFileFilter(".tif",IOCase.INSENSITIVE), null);
            JSONArray jsonArray = new JSONArray();
            Iterator<File> iterator = fileCollection.iterator();
            while(iterator.hasNext()){
                real_file = iterator.next();
                DataStoreMetaGet metaGet = new GeotiffMetaGet();
                jsonArray.add(metaGet.getMeta(real_file));
            }
            return jsonArray.toJSONString();
        }else if(dataResource.getType().equals(DataResourceTypeEnum.SDAT)||dataResource.getType().equals(DataResourceTypeEnum.SDAT_LIST)){
            fileCollection = FileUtils.listFiles(dir, new SuffixFileFilter(".mgrd",IOCase.INSENSITIVE), null);
            JSONArray jsonArray = new JSONArray();
            Iterator<File> iterator = fileCollection.iterator();
            while(iterator.hasNext()){
                real_file = iterator.next();
                DataStoreMetaGet metaGet = new SdatMetaGet();
                GeotiffMeta meta = (GeotiffMeta) metaGet.getMeta(real_file);
                jsonArray.add(meta);
            }
            return jsonArray.toJSONString();
        }else{
            throw new MyException(ResultEnum.NOTSUPPORT_GETMETA_ERROR);
        }
    }


    /***
     * 门户数据管理器关键字查询
     * @param id
     * @param searchContent
     * @return
     */
    public  List<DataResource>  managerKeywordsSearch(String id,String searchContent){
        List<DataResource> list=getByAuthor(id);

        List<DataResource> res=new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {


            if(list.get(i).getFileName().indexOf(searchContent)>-1||list.get(i).getSuffix().indexOf(searchContent)>-1){

                res.add(list.get(i));
            }

        }

        return  res;

    }


    /***
     * 门户数据管理器图片分类
     * @param id
     * @return
     */
    public  List<DataResource>  managerPics(String id){
        List<DataResource> list=getByAuthor(id);

        List<DataResource> res=new ArrayList<>();

      String[] searchContent ={"jpg","png","tif"};
        for (int j = 0; j <searchContent.length ; j++) {
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getSuffix().indexOf(searchContent[j])>-1){

                    res.add(list.get(i));
                }
            }
        }

        return  res;

    }

    /***
     * 门户数据管理器文档分类
     * @param id
     * @return
     */
    public  List<DataResource>  managerDoc(String id){
        List<DataResource> list=getByAuthor(id);

        List<DataResource> res=new ArrayList<>();

        String[] searchContent ={"doc","txt","xls"};
        for (int j = 0; j <searchContent.length ; j++) {
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getSuffix().indexOf(searchContent[j])>-1){

                    res.add(list.get(i));
                }
            }
        }

        return  res;

    }



    /***
     * 门户数据管理器其他分类
     * @param id
     * @return
     */
    public  List<DataResource>  managerOhr(String id){
        List<DataResource> list=getByAuthor(id);

        List<DataResource> res=new ArrayList<>();

        String[] searchContent ={"avi","zip","mp3","mp4","others"};
        for (int j = 0; j <searchContent.length ; j++) {
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getSuffix().indexOf(searchContent[j])>-1){

                    res.add(list.get(i));
                }
            }
        }

        return  res;

    }






}
