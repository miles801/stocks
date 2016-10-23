package com.michael.base.org.service;

import com.michael.base.org.bo.OrgBo;
import com.michael.base.org.domain.Org;
import com.michael.base.org.vo.OrgDTO;
import com.michael.base.org.vo.OrgVo;
import com.michael.core.pager.PageVo;

import java.util.List;

/**
 * @author Michael
 */
public interface OrgService {

    /**
     * 保存
     */
    String save(Org org);

    /**
     * 更新
     */
    void update(Org org);

    /**
     * 分页查询
     */
    PageVo pageQuery(OrgBo bo);

    /**
     * 查询直接子机构
     *
     * @param parentId 上级机构的ID（如果为空，则表示查询根）
     */
    List<OrgVo> children(String parentId);

    /**
     * 查询指定机构下的人和机构
     *
     * @param parentId 机构ID
     */
    OrgDTO childrenAndEmp(String parentId);

    /**
     * 根据ID查询对象的信息
     */
    OrgVo findById(String id);

    /**
     * 批量删除，如果具有下级，则删除失败
     */
    void deleteByIds(String[] ids);

    /**
     * 强制删除，删除的同时会删除该机构下的子机构，一般不建议使用该功能
     *
     * @param ids 机构ID集合
     */
    void forceDelete(String[] ids);

    /**
     * 禁用指定的机构以及对应所有的下级机构
     *
     * @param ids 机构ID集合
     */
    void disable(String[] ids);

    /**
     * 启用指定的组织机构以及对应的所有的上级机构
     *
     * @param ids 机构ID集合
     */
    void enable(String[] ids);

    /**
     * 根据条件查询所有机构
     *
     * @param bo 查询条件
     */
    List<OrgVo> tree(OrgBo bo);

}
