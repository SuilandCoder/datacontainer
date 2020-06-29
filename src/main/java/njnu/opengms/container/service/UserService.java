package njnu.opengms.container.service;

import njnu.opengms.container.dto.user.AddUserDTO;
import njnu.opengms.container.enums.ResultEnum;
import njnu.opengms.container.exception.MyException;
import njnu.opengms.container.pojo.User;
import njnu.opengms.container.repository.UserRepository;
import njnu.opengms.container.utils.JwtUtils;
import njnu.opengms.container.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName UserServiceImp
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public void add(AddUserDTO addDTO) {
        if (userRepository.getUserByUsername(addDTO.getUsername()) != null) {
            throw new MyException(ResultEnum.EXIST_OBJECT);
        }
        User user = new User();
        BeanUtils.copyProperties(addDTO, user);
        user.setCreateDate(new Date());
        userRepository.insert(user);
    }


    public UserVO get(String s) {
        User user = userRepository.findById(s).orElseGet(() -> {

            throw new MyException(ResultEnum.NO_OBJECT);
        });
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    public String doLogin(User userIn) {
        User user = this.findUserByUserName(userIn.getUsername());
        if (user == null) {
            throw new MyException(ResultEnum.NO_OBJECT);
        } else {
            if (user.getPassword().equals(userIn.getPassword())) {
                String jwtToken = JwtUtils.generateToken(user.getId(), userIn.getUsername(), userIn.getPassword());
                return "Bearer" + " " + jwtToken;
            } else {
                throw new MyException(ResultEnum.USER_PASSWORD_NOT_MATCH);
            }
        }
    }

    /**
     * 根据用户名查找用户
     *
     * @param userName 用户名
     *
     * @return
     */
    public User findUserByUserName(String userName) {
        return userRepository.getUserByUsername(userName);
    }

}
