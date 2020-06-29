package njnu.opengms.container.controller.common;

import io.swagger.annotations.ApiOperation;
import njnu.opengms.container.annotation.SysLogs;
import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.service.common.CreateService;
import njnu.opengms.container.utils.ResultUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @InterfaceName CreateController
 * @Description 创建实体，其header中必须带有Authorization字段
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
public interface CreateController<E, AD, S extends CreateService<E, AD>> {
    @RequestMapping (value = "", method = RequestMethod.POST)
    @ApiOperation (value = "添加")
    @SysLogs ("添加Entity日志")
//    @NeedAuth ()
//    @ApiImplicitParam (paramType = "header", name = "Authorization", value = "身份认证Token", required = true)
    default JsonResult create(@RequestBody @Validated AD a) {
        return ResultUtils.success(getService().create(a));
    }

    S getService();
}
