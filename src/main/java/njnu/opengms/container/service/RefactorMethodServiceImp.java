package njnu.opengms.container.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.opengms.container.bean.ProcessResponse;
import njnu.opengms.container.component.PathConfig;
import njnu.opengms.container.dto.refactormethod.AddRefactorMethodDTO;
import njnu.opengms.container.dto.refactormethod.FindRefactorMethodDTO;
import njnu.opengms.container.dto.refactormethod.UpdateRefactorMethodDTO;
import njnu.opengms.container.enums.ResultEnum;
import njnu.opengms.container.exception.MyException;
import njnu.opengms.container.pojo.RefactorMethod;
import njnu.opengms.container.repository.RefactorMethodRepository;
import njnu.opengms.container.service.common.BaseService;
import njnu.opengms.container.utils.MethodInvokeUtils;
import njnu.opengms.container.vo.RefactorMethodVO;
import org.apache.commons.io.FileUtils;
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
 * @ClassName RefactorMethodService
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/17
 * @Version 1.0.0
 */
@Service
public class RefactorMethodServiceImp implements BaseService<RefactorMethod, RefactorMethodVO, AddRefactorMethodDTO, FindRefactorMethodDTO, UpdateRefactorMethodDTO, String> {

    @Autowired
    RefactorMethodRepository refactorMethodRepository;

    @Autowired
    PathConfig pathConfig;




    @Override
    public RefactorMethod create(AddRefactorMethodDTO addDTO) {
        if (refactorMethodRepository.getByName(addDTO.getName()) != null) {
            throw new MyException(ResultEnum.EXIST_OBJECT);
        }
        RefactorMethod refactorMethod = new RefactorMethod();
        BeanUtils.copyProperties(addDTO, refactorMethod);
        refactorMethod.setCreateDate(new Date());
        return refactorMethodRepository.save(refactorMethod);
    }

    @Override
    public void delete(String id) {
        refactorMethodRepository.deleteById(id);
    }

    @Override
    public Page<RefactorMethodVO> list(FindRefactorMethodDTO findDTO) {
        RefactorMethod refactorMethod = new RefactorMethod();
        BeanUtils.copyProperties(findDTO, refactorMethod);

        // 只包含字符串的开始、结束、包含和正则匹配，以及其他字段的精确匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("description", match -> match.contains().ignoreCase())
                .withMatcher("name", match -> match.contains().ignoreCase())
                .withIncludeNullValues();

        Example<RefactorMethod> refactorMethodExample = Example.of(refactorMethod, matcher);

        PageRequest pageRequest;
        if (findDTO.getProperties() == null) {
            //不排序
            pageRequest = PageRequest.of(findDTO.getPage(), findDTO.getPageSize());
        } else {
            //排序
            Sort sort = new Sort(findDTO.getAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, findDTO.getProperties());
            pageRequest = PageRequest.of(findDTO.getPage(), findDTO.getPageSize(), sort);
        }
        Page<RefactorMethod> page = refactorMethodRepository.findAll(refactorMethodExample, pageRequest);
        List<RefactorMethodVO> listVO = new ArrayList<>();
        page.getContent().forEach(el -> {
            RefactorMethodVO vo = new RefactorMethodVO();
            BeanUtils.copyProperties(el, vo);
            listVO.add(vo);
        });
        Page<RefactorMethodVO> pageVo = new PageImpl<>(listVO, page.getPageable(), page.getTotalElements());
        return pageVo;
    }

    @Override
    public RefactorMethod get(String s) {
        return refactorMethodRepository.findById(s).orElseGet(() -> {
            throw new MyException(ResultEnum.NO_OBJECT);
        });
    }

    @Override
    public RefactorMethod getByExample(RefactorMethod refactorMethod) {
        return refactorMethodRepository.findOne(Example.of(refactorMethod)).orElse(null);
    }

    @Override
    public long count() {
        return refactorMethodRepository.count();
    }

    @Override
    public RefactorMethod update(String id, UpdateRefactorMethodDTO updateDTO) {
        RefactorMethod refactorMethod = refactorMethodRepository.findById(id).orElseGet(() -> {
            throw new MyException(ResultEnum.NO_OBJECT);
        });

            BeanUtils.copyProperties(updateDTO, refactorMethod);
        return refactorMethodRepository.save(refactorMethod);
    }

    public List<RefactorMethod> findBySchema(String id) {
        return refactorMethodRepository.getBySupportedUdxSchemas(id);
    }

    public JSONObject invoke(String id, String method, List<String> input, List<String> output) throws IOException {
        RefactorMethod refactorMethod = this.get(id);
        String invokePosition = refactorMethod.getInvokePosition();
        String basePath = pathConfig.getBase() + File.separator + invokePosition;
        List<String> inputLocal = new ArrayList<>();
        for (String s : input) {
            inputLocal.add(pathConfig.getBase() + File.separator + s);
        }
        List<String> outputLocal = new ArrayList<>();
        List<String> out = new ArrayList<>();
        for (String s : output) {
            String uuid = UUID.randomUUID().toString();
            outputLocal.add(pathConfig.getDataProcess() + File.separator + uuid + File.separator + s);
            out.add("data_process" + File.separator + uuid + File.separator + s);
        }
        ProcessResponse processResponse = MethodInvokeUtils.computeRefactor(basePath, method, inputLocal, outputLocal);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("processResponse", processResponse);
        if (processResponse.getFlag()) {
            JSONArray jsonArray = new JSONArray();
            for (String s : out) {
                jsonArray.add(s);
            }
            jsonObject.put("outputs", jsonArray);
        } else {
            jsonObject.put("outputs", null);
        }
        return jsonObject;
    }

    public String getMethods(String id) throws IOException {
        RefactorMethod refactorMethod = this.get(id);
        String invokePosition = refactorMethod.getInvokePosition();
        return FileUtils.readFileToString(new File(pathConfig.getBase() + File.separator + invokePosition + File.separator + "methods.xml"), "utf-8");
    }


}
