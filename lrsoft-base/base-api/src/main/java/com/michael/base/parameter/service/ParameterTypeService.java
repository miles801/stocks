package com.michael.base.parameter.service;

import com.michael.core.pager.PageVo;

import java.util.List;

/**
 * Created on 2014/7/28 3:56
 *
 * @author miles
 */
public interface ParameterTypeService<ENTITY, VO, BO> {

    String save(ENTITY entity);

    void update(ENTITY entity);

    PageVo query(BO bo);

    VO findById(String id);

    /**
     * 真的删除！慎重
     *
     * @param ids ID列表
     */
    void deleteByIds(String... ids);

    /**
     * 禁用
     *
     * @param ids ID列表
     */
    void disable(String... ids);

    /**
     * 启用
     *
     * @param ids ID列表
     */
    void enable(String... ids);

    /**
     * 检测指定的编号是否已经存在,如果存在返回true，不存在返回false
     */
    boolean hasCode(String code);

    /**
     * 检测指定的名称在指定节点下是否存在，如果存在返回true，不存在返回false
     *
     * @param parentId 上级节点的id
     * @param name     名称
     */
    boolean hasName(String parentId, String name);

    /**
     * 查询非当前节点及子节点的所有数据（选择上级菜单时用）
     * 仅仅查询状态为有效的数据
     *
     * @param id （可选）当前节点的id
     * @return 属性集合
     */
    List<VO> queryOther(String id);

    /**
     * 查询所有的系统参数类型，并组装成树
     * （包含未启用和已注销状态的数据）
     *
     * @return 树形
     */
    List<VO> allForTree();

    /**
     * 查询指定节点的所有子节点（包含自身）
     *
     * @param id          当前节点的id
     * @param containSelf 是否包含自身（true表示包含）
     */

    List<VO> queryChildren(String id, boolean containSelf);


    /**
     * 查询所有状态为有效的参数类型，并组装成树
     *
     * @return 树形集合
     */
    List<VO> queryValidTree();

    /**
     * 查询所有状态为已注销、启用状态的参数类型，并组装成树
     *
     * @return 树形集合
     */
    List<VO> queryUsingTree();

    /**
     * 根据类型编号查询类型的名称（为了性能考虑，这里可以考虑使用二级缓存）
     *
     * @param code 类型的编号
     */
    String getName(String code);
}
