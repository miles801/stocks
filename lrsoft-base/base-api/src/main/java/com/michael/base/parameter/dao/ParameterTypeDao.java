package com.michael.base.parameter.dao;

import java.util.List;

/**
 * @author miles
 * @datetime 2014/7/28 4:01
 */
public interface ParameterTypeDao<ENTITY, BO> {

    ENTITY findById(String id);

    String save(ENTITY entity);

    void update(ENTITY entity);

    List<ENTITY> query(BO bo);

    long getTotal(BO bo);

    void delete(ENTITY type);

    public boolean hasCode(String code);

    public boolean hasName(String parentId, String name);

    /**
     * 查询除当前id为的所有数据
     * 注意：该方法只会查询状态为有效的数据
     *
     * @param id 可为空，如果为空则查询全部
     */
    public List<ENTITY> queryOther(String id);

    public List<ENTITY> queryUsing();

    List<ENTITY> queryChildren(String id);

    /**
     * 根据编号获得对应的名称
     *
     * @param code
     * @return
     */
    String getName(String code);
}
