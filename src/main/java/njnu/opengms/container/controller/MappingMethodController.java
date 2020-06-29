package njnu.opengms.container.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.controller.common.BaseController;
import njnu.opengms.container.dto.mappingmethod.AddMappingMethodDTO;
import njnu.opengms.container.dto.mappingmethod.FindMappingMethodDTO;
import njnu.opengms.container.dto.mappingmethod.UpdateMappingMethodDTO;
import njnu.opengms.container.pojo.MappingMethod;
import njnu.opengms.container.service.MappingMethodServiceImp;
import njnu.opengms.container.utils.ResultUtils;
import njnu.opengms.container.vo.MappingMethodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @ClassName MappingMethodController
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/17
 * @Version 1.0.0
 */
@RestController
@RequestMapping (value = "/map")
public class MappingMethodController implements BaseController<MappingMethod, MappingMethodVO, AddMappingMethodDTO, FindMappingMethodDTO, UpdateMappingMethodDTO, String, MappingMethodServiceImp> {
    @Autowired
    MappingMethodServiceImp mappingMethodServiceImp;

    @Override
    public MappingMethodServiceImp getService() {
        return this.mappingMethodServiceImp;
    }

    @ApiImplicitParams ({
            @ApiImplicitParam (name = "id", value = "数据映射对应的id"),
            @ApiImplicitParam (name = "callType", value = "可选值为src2udx,udx2src"),
            @ApiImplicitParam (name = "input", value = "上传文件的路径", example = "data_process/uid/in.txt"),
            @ApiImplicitParam (name = "output", value = "下载文件的名称", example = "out.udx"),
    })
    @RequestMapping (value = "/{id}/invoke", method = RequestMethod.GET)
    public JsonResult invoke(@PathVariable ("id") String id,
                             @RequestParam ("callType") String callType,
                             @RequestParam ("input") String input,
                             @RequestParam ("output") String output
    ) throws IOException {
        return ResultUtils.success(mappingMethodServiceImp.invoke(id, callType, input, output));
    }
}
