package njnu.opengms.container.controller.common;


import io.swagger.annotations.ApiOperation;
import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.service.common.QueryService;
import njnu.opengms.container.utils.ResultUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @InterfaceName QueryController
 * @Description 查询实体，包括分页查询全部实体，返回实体总数，根据id返回实体
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
public interface QueryController<E, VO, FD, UID, S extends QueryService<E, VO, FD, UID>> {
    @RequestMapping (value = "", method = RequestMethod.GET)
    @ApiOperation (value = "分页获取所有列表,properties是排序字段推荐使用createDate")
    default JsonResult list(FD findDTO) {
        return ResultUtils.success(getService().list(findDTO));
    }

    S getService();

    @RequestMapping (value = "/count", method = RequestMethod.GET)
    @ApiOperation (value = "获取所有实体个数")
    default JsonResult count() {
        return ResultUtils.success(getService().count());
    }

    @RequestMapping (value = "/{id}", method = RequestMethod.GET)
    @ApiOperation (value = "根据ID获取实体")
    default JsonResult get(@PathVariable ("id") UID id) {
        return ResultUtils.success(getService().get(id));
    }
}
