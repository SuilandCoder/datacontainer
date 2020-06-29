package njnu.opengms.container.repository;

import njnu.opengms.container.pojo.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @InterfaceName UserRepository
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
public interface UserRepository extends MongoRepository<User, String> {
    User getUserByUsername(String username);
}
