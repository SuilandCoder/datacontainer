package njnu.opengms.container.utils;

import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.enums.ResultEnum;

/**
 * @ClassName ResultUtils
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
public class ResultUtils {
    public static JsonResult success() {
        return success(null);
    }

    public static JsonResult success(Object obj) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setMsg(ResultEnum.SUCCESS.getMsg());
        jsonResult.setCode(ResultEnum.SUCCESS.getCode());
        jsonResult.setData(obj);
        return jsonResult;
    }

    public static JsonResult error(Integer code, String msg) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(code);
        jsonResult.setMsg(msg);
        jsonResult.setData(null);
        return jsonResult;
    }
}
