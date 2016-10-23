package com.michael.base.position.service;

import com.michael.base.position.bo.PositionResourceBo;
import com.michael.base.position.domain.PositionResource;
import com.michael.base.position.vo.PositionResourceVo;
import com.michael.core.pager.PageVo;

import java.util.List;

/**
 * @author Michael
 */
public interface PositionResourceService {

    /**
     * 保存
     */
    String save(PositionResource positionResource);

    /**
     * 给指定的岗位授权
     * 会先删除该岗位下的所有菜单资源，然后重建
     *
     * @param positionId  岗位ID
     * @param resourceIds 资源ID
     */
    void grantMenu(String positionId, List<String> resourceIds);

    /**
     * 给指定的岗位授权
     * 会先删除该岗位下的所有操作资源，然后重建
     *
     * @param positionId  岗位ID
     * @param resourceIds 资源ID
     */
    void grantElement(String positionId, List<String> resourceIds);

    /**
     * 给指定的岗位授权
     * 会先删除该岗位下的所有数据资源，然后重建
     *
     * @param positionId  岗位ID
     * @param resourceIds 资源ID
     */
    void grantData(String positionId, List<String> resourceIds);

    /**
     * 分页查询
     */
    PageVo pageQuery(PositionResourceBo bo);

    /**
     * 根据ID查询对象的信息
     */
    PositionResourceVo findById(String id);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

    /**
     * 查询指定岗位下的所有资源的ID
     *
     * @param positionId 岗位ID
     * @return 资源ID
     */
    List<String> queryByPosition(String positionId);
}
