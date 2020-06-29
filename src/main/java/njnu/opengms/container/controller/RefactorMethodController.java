package njnu.opengms.container.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.controller.common.BaseController;
import njnu.opengms.container.dto.refactormethod.AddRefactorMethodDTO;
import njnu.opengms.container.dto.refactormethod.FindRefactorMethodDTO;
import njnu.opengms.container.dto.refactormethod.UpdateRefactorMethodDTO;
import njnu.opengms.container.pojo.RefactorMethod;
import njnu.opengms.container.service.RefactorMethodServiceImp;
import njnu.opengms.container.utils.ResultUtils;
import njnu.opengms.container.vo.RefactorMethodVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName RefactorMethodController
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/17
 * @Version 1.0.0
 */
@RestController
@RequestMapping (value = "/refactor")
public class RefactorMethodController implements BaseController<RefactorMethod, RefactorMethodVO, AddRefactorMethodDTO, FindRefactorMethodDTO, UpdateRefactorMethodDTO, String, RefactorMethodServiceImp> {
    @Autowired
    RefactorMethodServiceImp refactorMethodServiceImp;

    @Override
    public RefactorMethodServiceImp getService() {
        return this.refactorMethodServiceImp;
    }


    @ApiImplicitParams ({
            @ApiImplicitParam (name = "id", value = "数据重构对应的id"),
            @ApiImplicitParam (name = "method", value = "数据重构中调用的方法"),
            @ApiImplicitParam (name = "input", value = "上传文件的路径数组"),
            @ApiImplicitParam (name = "output", value = "下载文件的名称数组"),
    })
    @RequestMapping (value = "/{id}/invoke", method = RequestMethod.GET)
    public JsonResult invoke(@PathVariable ("id") String id,
                             @RequestParam ("method") String method,
                             @RequestParam ("input") List<String> input,
                             @RequestParam ("output") List<String> output
    ) throws IOException {
        return ResultUtils.success(refactorMethodServiceImp.invoke(id, method, input, output));
    }

    @ApiOperation (value = "获取重构库中包含的方法", notes = "注意这里返回的是xml字符串在前端可以考虑xml2json或者直接用dom解析")
    @RequestMapping (value = "/{id}/getMethod", method = RequestMethod.GET)
    public JsonResult getMethods(@PathVariable ("id") String id) throws IOException {
        return ResultUtils.success(refactorMethodServiceImp.getMethods(id));
    }
}
