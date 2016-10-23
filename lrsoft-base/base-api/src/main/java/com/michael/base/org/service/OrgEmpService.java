package com.michael.base.org.service;

/**
 * @author Michael
 */
public interface OrgEmpService {


    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

    /**
     * 往一个组织机构中添加员工（同时会更新组织机构中的员工数量）
     *
     * @param orgId  组织机构ID
     * @param empIds 员工ID
     * @return 组织机构中最新的员工的数量
     */
    Integer addEmp(String orgId, String[] empIds);

    /**
     * 移除一个组织机构中的指定员工（同时会更新组织机构中的员工数量）
     *
     * @param orgId  组织机构ID
     * @param empIds 员工ID
     * @return 组织机构中最新的员工的数量
     */
    Integer removeEmp(String orgId, String[] empIds);


}
