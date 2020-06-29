package njnu.opengms.container.repository;

import njnu.opengms.container.pojo.SchemaDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @InterfaceName SchemaDocRepository
 * @Description todo
 * @Author sun_liber
 * @Date 2018/12/17
 * @Version 1.0.0
 */
public interface SchemaDocRepository extends MongoRepository<SchemaDoc, String> {
    SchemaDoc getByName(String name);

    List<SchemaDoc> getByNameContainsIgnoreCase(String name);
}
