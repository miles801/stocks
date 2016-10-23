package com.michael.base.emp.dao;

import com.michael.base.emp.bo.EmpBo;
import com.michael.base.emp.domain.Emp;

import java.util.List;

/**
 * @author Michael
 */
public interface EmpDao {

    String save(Emp emp);

    void update(Emp emp);

    /**
     * 高级查询接口
     */
    List<Emp> query(EmpBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(EmpBo bo);

    Emp findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Emp emp);

    /**
     * 查询员工的直属机构ID
     *
     * @param id 员工ID
     * @return 直属机构ID
     */
    String findOrgId(String id);

    /**
     * 根据登录用户名查找员工
     *
     * @param loginName 登录名
     * @return 员工信息
     */
    Emp findByLoginName(String loginName);

    /**
     * 更新密码
     *
     * @param empId  员工ID
     * @param newPwd 新密码
     */
    void updatePwd(String empId, String newPwd);

    /**
     * 查询指定机构下的员工数量
     *
     * @param orgId 组织机构ID
     * @param bo    其他条件
     */
    Long getTotalByOrg(String orgId, EmpBo bo);

    /**
     * 查询指定机构下的员工列表
     *
     * @param orgId 组织机构ID
     * @param bo    其他条件
     */
    List<Emp> queryByOrg(String orgId, EmpBo bo);

    /**
     * 查询指定岗位的员工数量
     *
     * @param positionId 岗位ID
     * @param bo         其他条件
     */
    Long getTotalByPosition(String positionId, EmpBo bo);

    /**
     * 查询指定岗位下的员工列表
     *
     * @param positionId 岗位ID
     * @param bo         其他条件
     */
    List<Emp> queryByPosition(String positionId, EmpBo bo);


    /**
     * 验证编号是否存在
     *
     * @param code 编号
     * @param id   排除这个ID
     * @return true存在
     */
    boolean hasCode(String code, String id);

    /**
     * 验证登录名是否存在
     *
     * @param loginName 登录名
     * @param id        排除这个ID
     * @return true存在
     */
    boolean hasLoginName(String loginName, String id);

    /**
     * 验证考勤编号是否存在
     *
     * @param attNo 考勤编号
     * @param id    排除这个ID
     * @return true存在
     */
    boolean hasAttNo(String attNo, String id);

    /**
     * 找到员工表中最大的工号
     *
     * @return
     */

    Emp findMaxEmpCode();

    /**
     * 根据员工编号查询员工
     *
     * @param code 员工编号
     * @return 员工
     */
    Emp findByCode(String code);

    /**
     * 获取所有员工的编号以及部门id；
     */
    List<Emp> getAllCode();
}
