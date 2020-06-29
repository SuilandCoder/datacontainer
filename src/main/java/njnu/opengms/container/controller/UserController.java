package njnu.opengms.container.controller;

import njnu.opengms.container.bean.JsonResult;
import njnu.opengms.container.dto.user.AddUserDTO;
import njnu.opengms.container.pojo.User;
import njnu.opengms.container.service.UserService;
import njnu.opengms.container.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
@RestController
@RequestMapping (value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping (value = "/login", method = RequestMethod.POST)
    public JsonResult doLogin(@RequestBody User userIn) {
        return ResultUtils.success(userService.doLogin(userIn));
    }

    @RequestMapping (value = "/register", method = RequestMethod.POST)
    public JsonResult doRegister(@RequestBody AddUserDTO addUserDTO) {
        userService.add(addUserDTO);
        return ResultUtils.success("用户创建成功");
    }

    @RequestMapping (value = "/all-permission-tag", method = RequestMethod.POST)
    public JsonResult getPermission() {
        String[] permission = {"super_admin", "admin"};
        return ResultUtils.success(permission);
    }


}
