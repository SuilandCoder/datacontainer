package njnu.opengms.container.service.common;

/**
 * @param <E>   实体
 * @param <AD>  AddDTO
 * @param <UD>  UpdateDTO
 * @param <FD>  FindDTO
 * @param <VO>  VisualizationObject
 * @param <UID> ID
 *
 * @InterfaceName BaseService
 * @Description todo
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
public interface BaseService<E, VO, AD, FD, UD, UID> extends
        CreateService<E, AD>,
        QueryService<E, VO, FD, UID>,
        DeleteService<UID>,
        UpdateService<E, UD, UID> {
}
