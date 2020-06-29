package njnu.opengms.container.repository;

import njnu.opengms.container.pojo.RefactorMethod;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @InterfaceName RefactorMethodRepository
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/17
 * @Version 1.0.0
 */
public interface RefactorMethodRepository extends MongoRepository<RefactorMethod, String> {
    List<RefactorMethod> getBySupportedUdxSchemas(String id);

    RefactorMethod getByName(String name);
}
