package com.michael.base.position.dao;

import com.michael.base.position.bo.PositionResourceBo;
import com.michael.base.position.domain.PositionResource;

import java.util.List;

/**
 * @author Michael
 */
public interface PositionResourceDao {

    String save(PositionResource positionResource);

    void update(PositionResource positionResource);

    /**
     * 高级查询接口
     */
    List<PositionResource> query(PositionResourceBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(PositionResourceBo bo);

    PositionResource findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(PositionResource positionResource);

    /**
     * 删除指定岗位下的所有权限
     *
     * @param positionId 岗位ID
     */
    void deleteByPosition(String positionId, String resourceType);


    /**
     * 查询指定岗位下的所有资源的ID
     *
     * @param positionId 岗位ID
     * @return 资源ID
     */
    List<String> queryByPosition(String positionId);
}
