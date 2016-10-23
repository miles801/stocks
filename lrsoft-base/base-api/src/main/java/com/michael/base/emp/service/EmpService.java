package com.michael.base.emp.service;

import com.michael.base.emp.bo.EmpBo;
import com.michael.base.emp.domain.Emp;
import com.michael.base.emp.vo.EmpVo;
import com.michael.core.pager.PageVo;

/**
 * @author Michael
 */
public interface EmpService {

    /**
     * 保存
     */
    String save(Emp emp);

    /**
     * 更新
     */
    void update(Emp emp);

    /**
     * 分页查询
     */
    PageVo pageQuery(EmpBo bo);

    /**
     * 根据ID查询对象的信息
     */
    EmpVo findById(String id);

    /**
     * 批量删除,实际是锁定用户
     */
    void deleteByIds(String[] ids);

    /**
     * 用户登录，如果登录成功，则返回除密码外的所有字段
     *
     * @param loginName 登录用户名
     * @param password  密码（MD5加密）
     * @return 用户信息
     */
    EmpVo login(String loginName, String password);

    /**
     * 批量启用员工
     *
     * @param ids
     */
    void start(String[] ids);

    /**
     * 更新当前用户的密码
     *
     * @param oldPwd 原始密码
     * @param newPwd 新密码
     */
    void updatePwd(String oldPwd, String newPwd);

    /**
     * 查询指定机构下符合条件的员工信息
     *
     * @param orgId 机构ID
     * @param bo    其他查询条件
     */
    PageVo queryByOrg(String orgId, EmpBo bo);

    /**
     * 查询指定岗位下的员工信息
     *
     * @param positionId 岗位ID
     * @param bo         员工过滤条件
     * @return 分页对象
     */
    PageVo queryByPosition(String positionId, EmpBo bo);
}
