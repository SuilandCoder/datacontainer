package njnu.opengms.container.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.ngis.udx.Transfer;
import com.ngis.udx.schema.UdxSchema;
import njnu.opengms.container.dto.schemadoc.AddSchemaDocDTO;
import njnu.opengms.container.dto.schemadoc.FindSchemaDocDTO;
import njnu.opengms.container.dto.schemadoc.UpdateSchemaDocDTO;
import njnu.opengms.container.enums.ResultEnum;
import njnu.opengms.container.exception.MyException;
import njnu.opengms.container.pojo.SchemaDoc;
import njnu.opengms.container.repository.MappingMethodRepository;
import njnu.opengms.container.repository.RefactorMethodRepository;
import njnu.opengms.container.repository.SchemaDocRepository;
import njnu.opengms.container.service.common.BaseService;
import njnu.opengms.container.vo.MappingMethodVO;
import njnu.opengms.container.vo.RefactorMethodVO;
import njnu.opengms.container.vo.SchemaDocVO;
import org.apache.commons.io.FilenameUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName SchemaDocService
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/17
 * @Version 1.0.0
 */
@Service
public class SchemaDocServiceImp implements BaseService<SchemaDoc, SchemaDocVO, AddSchemaDocDTO, FindSchemaDocDTO, UpdateSchemaDocDTO, String> {

    @Autowired
    SchemaDocRepository schemaDocRepository;

    private static final String SUFFIX_JSON = "json";
    private static final String SUFFIX_XML = "xml";

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MappingMethodRepository mappingMethodRepository;
    @Autowired
    RefactorMethodRepository refactorMethodRepository;

    @Override
    public SchemaDoc create(AddSchemaDocDTO addDTO) {
        if (schemaDocRepository.getByName(addDTO.getName()) != null) {
            throw new MyException(ResultEnum.EXIST_OBJECT);
        }
        SchemaDoc schemaDoc = new SchemaDoc();
        BeanUtils.copyProperties(addDTO, schemaDoc);
        schemaDoc.setCreateDate(new Date());
        return schemaDocRepository.insert(schemaDoc);
    }

    @Override
    public void delete(String id) {
        schemaDocRepository.deleteById(id);
    }

    @Override
    public Page<SchemaDocVO> list(FindSchemaDocDTO findDTO) {
        SchemaDoc schemaDoc = new SchemaDoc();
        BeanUtils.copyProperties(findDTO, schemaDoc);

        // 只包含字符串的开始、结束、包含和正则匹配，以及其他字段的精确匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("description", match -> match.contains().ignoreCase())
                .withMatcher("name", match -> match.contains().ignoreCase())
                .withIncludeNullValues();

        Example<SchemaDoc> schemaDocExample = Example.of(schemaDoc, matcher);

        PageRequest pageRequest;
        if (findDTO.getProperties() == null) {
            //不排序
            pageRequest = PageRequest.of(findDTO.getPage(), findDTO.getPageSize());
        } else {
            //排序
            Sort sort = new Sort(findDTO.getAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, findDTO.getProperties());
            pageRequest = PageRequest.of(findDTO.getPage(), findDTO.getPageSize(), sort);
        }
        Page<SchemaDoc> page = schemaDocRepository.findAll(schemaDocExample, pageRequest);
        List<SchemaDocVO> listVO = new ArrayList<>();
        page.getContent().forEach(el -> {
            SchemaDocVO vo = new SchemaDocVO();
            BeanUtils.copyProperties(el, vo);
            listVO.add(vo);
        });
        Page<SchemaDocVO> pageVo = new PageImpl<>(listVO, page.getPageable(), page.getTotalElements());
        return pageVo;
    }

    @Override
    public SchemaDoc get(String s) {
        return schemaDocRepository.findById(s).orElseGet(() -> {

            throw new MyException(ResultEnum.NO_OBJECT);
        });
    }

    @Override
    public SchemaDoc getByExample(SchemaDoc schemaDoc) {
        return schemaDocRepository.findOne(Example.of(schemaDoc)).orElse(null);
    }

    @Override
    public long count() {
        return schemaDocRepository.count();
    }

    @Override
    public SchemaDoc update(String id, UpdateSchemaDocDTO updateDTO) {
        SchemaDoc schemaDoc = schemaDocRepository.findById(id).orElseGet(() -> {
            throw new MyException(ResultEnum.NO_OBJECT);
        });
        BeanUtils.copyProperties(updateDTO, schemaDoc);
        return schemaDocRepository.save(schemaDoc);
    }

    public List<SchemaDoc> getTop10() {
        BasicDBObject dbObject = new BasicDBObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        //指定只返回name字段，注意默认会返回id字段
        fieldsObject.put("name", 1);
        Query query = new BasicQuery(dbObject.toJson(), fieldsObject.toJson());
        query.with(PageRequest.of(0, 10, Sort.Direction.ASC, "createDate"));
        return mongoTemplate.find(query, SchemaDoc.class);
    }

    public List<SchemaDoc> getByNameContainsIgnoreCase(String name) {
        return schemaDocRepository.getByNameContainsIgnoreCase(name);
    }

    public JSONObject getRelatedResource(String id) {
        List<MappingMethodVO> mappingMethodVOList = new ArrayList<>();
        mappingMethodRepository.getBySupportedUdxSchema(id).forEach(el -> {
            MappingMethodVO vo = new MappingMethodVO();
            BeanUtils.copyProperties(el, vo);
            mappingMethodVOList.add(vo);
        });

        List<RefactorMethodVO> refactorMethodVOList = new ArrayList<>();
        refactorMethodRepository.getBySupportedUdxSchemas(id).forEach(el -> {
            RefactorMethodVO vo = new RefactorMethodVO();
            BeanUtils.copyProperties(el, vo);
            refactorMethodVOList.add(vo);
        });

        JSONObject object = new JSONObject();
        object.put("map", mappingMethodVOList);
        object.put("refactor", refactorMethodVOList);

        return object;
    }

    public UdxSchema getSchemaFromFile(MultipartFile file) throws IOException, DocumentException {
        String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
        File fileNormal = File.createTempFile(UUID.randomUUID().toString(), suffix);
        file.transferTo(fileNormal);
        UdxSchema udxSchema;
        if (SUFFIX_JSON.equals(suffix)) {
            udxSchema = Transfer.loadSchemaFromJsonFile(fileNormal);
        } else if (SUFFIX_XML.equals(suffix)) {
            udxSchema = Transfer.loadSchemaFromXmlFile(fileNormal);
        } else {
            throw new MyException(ResultEnum.TRANSFER_UDX_ERROR);
        }
        return udxSchema;
    }


}
