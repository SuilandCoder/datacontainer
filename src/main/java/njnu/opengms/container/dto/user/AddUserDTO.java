package njnu.opengms.container.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName AddUserDTO
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
@Data
public class AddUserDTO {
    @NotBlank (message = "用户名不能为空")
    String username;
    @NotBlank (message = "密码不能为空")
    String password;
}
