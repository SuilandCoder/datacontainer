package njnu.opengms.container.service.common;

import org.springframework.data.domain.Page;

/**
 * @InterfaceName QueryService
 * @Description 查询实体Service
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
public interface QueryService<E, VO, FD, UID> {
    /**
     * 获取所有实体（分页）
     *
     * @param findDTO 过滤条件
     *
     * @return
     */
    Page<VO> list(FD findDTO);

    /**
     * 根据id 查询实体
     *
     * @param uid 实体id
     *
     * @return
     */
    E get(UID uid);

    /**
     * 根据Example 查询实体
     */
    E getByExample(E e);

    /**
     * 返回实体的总个数
     *
     * @return
     */
    long count();
}