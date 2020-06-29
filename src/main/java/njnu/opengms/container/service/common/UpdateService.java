package njnu.opengms.container.service.common;

/**
 * @InterfaceName UpdateService
 * @Description 更新实体Service
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
public interface UpdateService<E, UD, UID> {
    /**
     * 更新实体信息
     *
     * @param id        实体id
     * @param updateDTO 实体信息
     */
    E update(UID id, UD updateDTO);
}

