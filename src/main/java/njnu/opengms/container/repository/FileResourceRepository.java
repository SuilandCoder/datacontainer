package njnu.opengms.container.repository;

import njnu.opengms.container.pojo.FileResource;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @InterfaceName FileResourceRepository
 * @Description todo
 * @Author sun_liber
 * @Date 2019/4/9
 * @Version 1.0.0
 */
public interface FileResourceRepository extends MongoRepository<FileResource, String> {
    FileResource getFirstByMd5(String md5);

    FileResource getFirstBySourceStoreId(String sourceId);
}
