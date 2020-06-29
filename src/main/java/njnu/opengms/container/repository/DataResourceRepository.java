package njnu.opengms.container.repository;

import njnu.opengms.container.pojo.DataResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @InterfaceName DataResourceRepository
 * @Description todo
 * @Author sun_liber
 * @Date 2019/2/13
 * @Version 1.0.0
 */
public interface DataResourceRepository extends MongoRepository<DataResource, String> {

    DataResource getBySourceStoreId(String sourceStoreId);

    Page<DataResource> getByAuthor(String author, Pageable pageable);

    List<DataResource> getByAuthor(String author);

    Page<DataResource> getByDataItemId(String author, Pageable pageable);

    List<DataResource> getByDataItemId(String dataItemId);

    Page<DataResource> getByMdlId(String author, Pageable pageable);

    List<DataResource> getByMdlId(String mdlId);

    Page<DataResource> getByFileNameContains(String author, Pageable pageable);

    List<DataResource> getByFileNameContains(String fileName);
}
