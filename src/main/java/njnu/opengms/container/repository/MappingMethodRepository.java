package njnu.opengms.container.repository;

import njnu.opengms.container.pojo.MappingMethod;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @InterfaceName MappingMethodRepository
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/17
 * @Version 1.0.0
 */
public interface MappingMethodRepository extends MongoRepository<MappingMethod, String> {
    List<MappingMethod> getBySupportedUdxSchema(String id);

    MappingMethod getByName(String name);
}
