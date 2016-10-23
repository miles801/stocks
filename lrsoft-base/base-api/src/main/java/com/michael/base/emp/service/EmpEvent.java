package com.michael.base.emp.service;

import com.michael.base.emp.domain.Emp;

/**
 * 员工事件接口
 *
 * @author Michael
 */
public interface EmpEvent {
    /**
     * 当员工数据更新时
     *
     * @param oldEmp 老的数据
     * @param newEmp 新的数据
     */
    void onUpdate(Emp oldEmp, Emp newEmp);

    /**
     * 当员工被删除时
     *
     * @param emp 当前员工
     */
    void onDelete(Emp emp);
}
