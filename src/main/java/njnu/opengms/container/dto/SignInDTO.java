package njnu.opengms.container.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;


/**
 * @ClassName SignInDTO
 * @Description todo
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
@Data
public class SignInDTO {
    @NotEmpty (message = "账户不能为空")
    private String username;

    @NotEmpty (message = "密码不能为空")
    @Length (min = 6, max = 15, message = "密码长度在6和15之间")
    private String password;
}
