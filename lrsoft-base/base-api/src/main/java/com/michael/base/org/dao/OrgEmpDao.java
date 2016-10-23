package com.michael.base.org.dao;

import com.michael.base.emp.domain.Emp;
import com.michael.base.org.bo.OrgEmpBo;
import com.michael.base.org.domain.OrgEmp;

import java.util.List;

/**
 * @author Michael
 */
public interface OrgEmpDao {

    String save(OrgEmp orgEmp);

    void update(OrgEmp orgEmp);

    /**
     * 高级查询接口
     */
    List<OrgEmp> query(OrgEmpBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(OrgEmpBo bo);

    OrgEmp findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(OrgEmp orgEmp);

    /**
     * 查询指定机构下的所有员工
     *
     * @param orgId 机构ID
     */
    List<Emp> findByOrg(String orgId);

    /**
     * 根据组织机构和员工查询对应的关联关系
     *
     * @param orgId 组织机构ID
     * @param empId 员工ID
     */
    OrgEmp find(String orgId, String empId);
}
