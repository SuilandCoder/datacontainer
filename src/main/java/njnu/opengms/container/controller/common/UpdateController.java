package njnu.opengms.container.controller.common;


import io.swagger.annotations.ApiOperation;
import njnu.opengms.container.annotation.SysLogs;
import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.service.common.UpdateService;
import njnu.opengms.container.utils.ResultUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @InterfaceName UpdateController
 * @Description 更新实体
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
public interface UpdateController<E, UD, UID, S extends UpdateService<E, UD, UID>> {
    @RequestMapping (value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation (value = "更新指定id的Entity")
    @SysLogs ("更新指定ID的Entity的信息")
//    @NeedAuth ()
//    @ApiImplicitParam (paramType = "header", name = "Authorization", value = "身份认证Token", required = true)
    default JsonResult update(@PathVariable ("id") UID id, @RequestBody UD updateDTO) {
        return ResultUtils.success(getService().update(id, updateDTO));
    }

    S getService();
}
