package njnu.opengms.container.controller.common;


import io.swagger.annotations.ApiOperation;
import njnu.opengms.container.annotation.SysLogs;
import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.service.common.DeleteService;
import njnu.opengms.container.utils.ResultUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * @InterfaceName DeleteController
 * @Description 删除实体
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
public interface DeleteController<UID, S extends DeleteService<UID>> {
    @RequestMapping (value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation (value = "删除")
    @SysLogs ("删除指定ID的Entity日志")
//    @NeedAuth ()
//    @ApiImplicitParam (paramType = "header", name = "Authorization", value = "身份认证Token", required = true)
    default JsonResult delete(@PathVariable ("id") UID id) throws IOException {
        getService().delete(id);
        return ResultUtils.success(id + "删除成功");
    }

    S getService();
}
