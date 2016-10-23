package com.michael.base.resource.service;

import com.michael.base.resource.bo.ResourceBo;
import com.michael.base.resource.domain.Resource;
import com.michael.base.resource.vo.ResourceVo;
import com.michael.core.pager.PageVo;

import java.util.List;

/**
 * @author Michael
 */
public interface ResourceService {

    /**
     * 保存
     */
    String save(Resource resource);

    /**
     * 更新
     */
    void update(Resource resource);

    /**
     * 分页查询
     */
    PageVo pageQuery(ResourceBo bo);

    /**
     * 根据ID查询对象的信息
     */
    ResourceVo findById(String id);

    /**
     * 批量禁用（会同时禁用下级）
     */
    void disable(String[] ids);

    /**
     * 批量启用（会同时启用上级）
     */
    void enable(String[] ids);

    /**
     * 一次性加载所有有效的菜单资源
     */
    List<ResourceVo> validMenuResource();

    /**
     * 一次性加载所有有效的操作资源
     */
    List<ResourceVo> validElementResource();

    /**
     * 一次性加载所有有效的数据资源
     */
    List<ResourceVo> validDataResource();

    /**
     * 查询有效的跟菜单资源
     */
    List<ResourceVo> validMenuRootResource();

    /**
     * 查询有效的子资源
     *
     * @param id 资源ID
     */
    List<ResourceVo> validChildren(String id);

    /**
     * 查询所有的子资源
     *
     * @param id 资源ID
     */
    List<ResourceVo> children(String id);

    /**
     * 资源的高级查询对象，不使用分页，直接返回列表
     *
     * @param bo 高级查询对象
     */
    List<ResourceVo> query(ResourceBo bo);

    /**
     * 查询我的菜单（包含不需要授权的菜单）
     */
    List<ResourceVo> myMenu();

    /**
     * 加载个人所有的操作资源编号集合
     *
     * @return 操作资源编号的集合
     */
    List<String> queryElementResourceCode();
}