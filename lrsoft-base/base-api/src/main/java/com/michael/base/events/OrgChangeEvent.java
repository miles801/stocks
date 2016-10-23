package com.michael.base.events;

import com.michael.base.org.domain.Org;

/**
 * 当组织机构发生变化时的事件
 * 例如需要进行重置员工数量、更改path等操作
 *
 * @author Michael
 */
public interface OrgChangeEvent {

    /**
     * 新增组织机构
     */
    Object onSave(Org org);

    /**
     * 注销一个组织机构
     */
    Object onDelete(Org org);

    /**
     * 给原组织机构添加新的机构
     *
     * @param org    组织机构
     * @param parent 父级组织机构
     */
    Object onAddParent(Org org, Org parent);

    /**
     * 移除指定机构的父级机构
     *
     * @param org    组织机构
     * @param parent 父级组织机构
     */
    Object onRemoveParent(Org org, Org parent);

    /**
     * 改变指定机构的父级机构
     *
     * @param org       组织机构
     * @param oldParent 原父级组织机构
     * @param newParent 新父级组织机构
     */
    Object onChangeParent(Org org, Org oldParent, Org newParent);

    /**
     * 组织机构变更时
     *
     * @param org 最新的组织机构
     */
    Object onUpdate(Org org);
}
