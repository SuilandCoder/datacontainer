package njnu.opengms.container.service;

import com.alibaba.fastjson.JSONObject;
import njnu.opengms.container.bean.ProcessResponse;
import njnu.opengms.container.component.PathConfig;
import njnu.opengms.container.dto.mappingmethod.AddMappingMethodDTO;
import njnu.opengms.container.dto.mappingmethod.FindMappingMethodDTO;
import njnu.opengms.container.dto.mappingmethod.UpdateMappingMethodDTO;
import njnu.opengms.container.enums.ResultEnum;
import njnu.opengms.container.exception.MyException;
import njnu.opengms.container.pojo.MappingMethod;
import njnu.opengms.container.repository.MappingMethodRepository;
import njnu.opengms.container.service.common.BaseService;
import njnu.opengms.container.utils.MethodInvokeUtils;
import njnu.opengms.container.vo.MappingMethodVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName MappingMethodService
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/17
 * @Version 1.0.0
 */
@Service
public class MappingMethodServiceImp implements BaseService<MappingMethod, MappingMethodVO, AddMappingMethodDTO, FindMappingMethodDTO, UpdateMappingMethodDTO, String> {

    @Autowired
    MappingMethodRepository mappingMethodRepository;

    @Autowired
    PathConfig pathConfig;

    public List<MappingMethod> findBySchema(String id) {
        return mappingMethodRepository.getBySupportedUdxSchema(id);
    }


    @Override
    public MappingMethod create(AddMappingMethodDTO addDTO) {
        if (mappingMethodRepository.getByName(addDTO.getName()) != null) {
            throw new MyException(ResultEnum.EXIST_OBJECT);
        }
        MappingMethod mappingMethod = new MappingMethod();
        BeanUtils.copyProperties(addDTO, mappingMethod);
        mappingMethod.setCreateDate(new Date());
        return mappingMethodRepository.insert(mappingMethod);
    }

    @Override
    public void delete(String id) {
        mappingMethodRepository.deleteById(id);
    }

    @Override
    public Page<MappingMethodVO> list(FindMappingMethodDTO findDTO) {
        MappingMethod mappingMethod = new MappingMethod();
        BeanUtils.copyProperties(findDTO, mappingMethod);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("description", match -> match.contains().ignoreCase())
                .withMatcher("name", match -> match.contains().ignoreCase())
                .withIncludeNullValues();

        Example<MappingMethod> mappingMethodExample = Example.of(mappingMethod, matcher);

        PageRequest pageRequest;
        if (findDTO.getProperties() == null) {
            //不排序
            pageRequest = PageRequest.of(findDTO.getPage(), findDTO.getPageSize());
        } else {
            //排序
            Sort sort = new Sort(findDTO.getAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, findDTO.getProperties());
            pageRequest = PageRequest.of(findDTO.getPage(), findDTO.getPageSize(), sort);
        }

        Page<MappingMethod> page = mappingMethodRepository.findAll(mappingMethodExample, pageRequest);


        List<MappingMethodVO> listVO = new ArrayList<>();

        page.getContent().forEach(el -> {
            MappingMethodVO vo = new MappingMethodVO();
            BeanUtils.copyProperties(el, vo);
            listVO.add(vo);
        });
        Page<MappingMethodVO> pageVo = new PageImpl<>(listVO, page.getPageable(), page.getTotalElements());
        return pageVo;
    }

    @Override
    public MappingMethod get(String s) {
        return mappingMethodRepository.findById(s).orElseGet(() -> {
            throw new MyException(ResultEnum.NO_OBJECT);
        });
    }

    @Override
    public MappingMethod getByExample(MappingMethod mappingMethod) {
        return mappingMethodRepository.findOne(Example.of(mappingMethod)).orElse(null);
    }

    @Override
    public long count() {
        return mappingMethodRepository.count();
    }

    @Override
    public MappingMethod update(String id, UpdateMappingMethodDTO updateDTO) {
        MappingMethod mappingMethod = mappingMethodRepository.findById(id).orElseGet(() -> {
            throw new MyException(ResultEnum.NO_OBJECT);
        });

            BeanUtils.copyProperties(updateDTO, mappingMethod);
        return mappingMethodRepository.save(mappingMethod);

    }

    public JSONObject invoke(String id, String callType, String input, String output) throws IOException {
        MappingMethod mappingMethod = this.get(id);
        String invokePosition = mappingMethod.getInvokePosition();
        String uid = UUID.randomUUID().toString();
        String basePath = pathConfig.getBase() + File.separator + invokePosition;
        String inputLocal = pathConfig.getBase() + File.separator + input;
        String outputLocal = pathConfig.getDataProcess() + File.separator + uid + File.separator + output;
        ProcessResponse processResponse = MethodInvokeUtils.computeMap(basePath, callType, inputLocal, outputLocal);
        //返回cmd命令执行情况，以及输出数据路径
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("processResponse", processResponse);
        if (processResponse.getFlag()) {
            jsonObject.put("output", "data_process" + File.separator + uid + File.separator + output);
        } else {
            jsonObject.put("output", null);
        }
        return jsonObject;
    }


}
