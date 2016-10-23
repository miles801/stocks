package com.michael.base.position.service;

import com.michael.base.emp.bo.EmpBo;
import com.michael.base.position.bo.PositionEmpBo;
import com.michael.base.position.domain.PositionEmp;
import com.michael.base.position.vo.PositionEmpVo;
import com.michael.core.pager.PageVo;

import java.util.List;

/**
 * @author Michael
 */
public interface PositionEmpService {

    /**
     * 保存
     */
    String save(PositionEmp positionEmp);

    /**
     * 更新
     */
    void update(PositionEmp positionEmp);

    /**
     * 分页查询
     */
    PageVo pageQuery(PositionEmpBo bo);

    /**
     * 根据ID查询对象的信息
     */
    PositionEmpVo findById(String id);

    /**
     * 批量删除
     */
    void deleteByIds(String[] ids);

    /**
     * 给指定的岗位添加员工
     *
     * @param positionId 岗位ID
     * @param empIds     员工ID列表
     * @return 真正添加的员工的数量
     */
    Integer addEmp(String positionId, String[] empIds);


    /**
     * 移除指定岗位的指定员工列表
     *
     * @param positionId 岗位ID
     * @param empIds     员工ID列表
     * @return 真正删除的员工的数量
     */
    Integer removeEmp(String positionId, String[] empIds);


    /**
     * 查询指定岗位下的员工信息
     *
     * @param positionId 岗位ID
     * @param bo         员工过滤条件
     * @return 员工的分页信息
     */
    PageVo queryByPosition(String positionId, EmpBo bo);

    /**
     * 查询个人所具有的岗位的ID
     *
     * @return 岗位ID列表
     */
    List<String> myPosition();
}
