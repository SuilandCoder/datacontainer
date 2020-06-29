package njnu.opengms.container.repository;

import njnu.opengms.container.pojo.CmpTaskRecord;
import njnu.opengms.container.pojo.DataResource;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 16:02 2019/10/11
 * @Modified By:
 **/
public interface CmpTaskRepository extends MongoRepository<CmpTaskRecord, String> {
    CmpTaskRecord getByRecordId(String recordId);

}
