package njnu.opengms.container.repository;

import njnu.opengms.container.pojo.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @InterfaceName LogRepository
 * @Description todo
 * @Author sun_liber
 * @Date 2018/11/28
 * @Version 1.0.0
 */
public interface LogRepository extends MongoRepository<Log, String> {
}
