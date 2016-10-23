package com.michael.base.org.dao;

import com.michael.base.org.bo.OrgBo;
import com.michael.base.org.domain.Org;

import java.util.List;
import java.util.Set;

/**
 * @author Michael
 */
public interface OrgDao {

    String save(Org org);

    void update(Org org);

    /**
     * 高级查询接口
     */
    List<Org> query(OrgBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(OrgBo bo);

    Org findById(String id);

    void deleteById(String id);

    /**
     * 指定编号是否存在
     *
     * @param code 编号
     * @param id   ID，需要排除的ID（一般用于更新时排除自身）
     * @return true存在 false不存在
     */
    Boolean hasCode(String code, String id);

    /**
     * 判断指定的名称在同级下是否存在重复的
     *
     * @param name     名称
     * @param parentId 上级ID
     * @param id       要排除的ID（一般用于更新时排除自身）
     * @return true存在，false不存在
     */
    Boolean hasName(String name, String parentId, String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Org org);


    /**
     * 查询指定机构的所有子结构（包括隔代），结果集中不包含自身
     * 利用的是path属性
     *
     * @param orgId 机构ID
     * @return 所有的子组织机构
     */
    List<Org> allChildren(String orgId);

    /**
     * 查询子机构个数（包括子子机构）
     *
     * @param orgId 机构ID
     * @return 子机构个数
     */
    Long childrenCounts(String orgId);


    /**
     * 禁用指定节点及该节点下的所有子节点
     *
     * @param orgId 节点ID
     */
    void disableAll(String orgId);

    /**
     * 启用指定节点
     *
     * @param orgIds 节点ID列表
     */
    void enableAll(Set<String> orgIds);

    /**
     * 通过全名找到对应的组织对象
     *
     * @param longName 机构全名称
     */
    Org findByLongNme(String longName);


    /**
     * 根据ID查询名称
     *
     * @param id 机构ID
     * @return 机构名称
     */
    String findNameById(String id);

    /**
     * 强制删除当前机构及对应的子机构
     *
     * @param id 机构ID
     */
    void forceDelete(String id);
}
