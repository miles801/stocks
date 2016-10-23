package com.michael.base.resource.dao;

import com.michael.base.resource.bo.ResourceBo;
import com.michael.base.resource.domain.Resource;

import java.util.List;

/**
 * @author Michael
 */
public interface ResourceDao {

    String save(Resource resource);

    void update(Resource resource);

    /**
     * 高级查询接口
     */
    List<Resource> query(ResourceBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(ResourceBo bo);

    Resource findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Resource resource);

    /**
     * 判断指定的编号是否存在（指定的ID除外）
     *
     * @param code 编号
     * @param id   ID
     * @return true存在，false不存在
     */
    boolean hasCode(String code, String id);

    /**
     * 判断指定层级下，指定名称的资源是否存在（指定的ID除外）
     *
     * @param name     名称
     * @param parentId 上级ID
     * @param id       ID
     * @return true存在，false不存在
     */
    boolean hasName(String name, String parentId, String id);

    /**
     * 查询指定资源下的直接子资源
     *
     * @param parentId 上级资源ID
     * @return 直接子资源
     */
    List<Resource> queryChildren(String parentId);

    /**
     * 查询指定员工的资源列表
     *
     * @param empId 员工ID
     * @param bo    资源的高级条件
     */
    List<Resource> permissionResource(String empId, ResourceBo bo);

    /**
     * 查询所有个人具有的资源的编号集合
     *
     * @param empId 员工ID
     * @param bo    其他条件
     * @return 编号集合
     */
    List<String> permissionResourceCode(String empId, ResourceBo bo);
}
