package com.michael.base.position.service.impl;

import com.michael.base.emp.bo.EmpBo;
import com.michael.base.position.bo.PositionEmpBo;
import com.michael.base.position.dao.PositionEmpDao;
import com.michael.base.position.domain.PositionEmp;
import com.michael.base.position.service.PositionEmpService;
import com.michael.base.position.service.PositionService;
import com.michael.base.position.vo.PositionEmpVo;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.context.SecurityContext;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.utils.string.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("positionEmpService")
public class PositionEmpServiceImpl implements PositionEmpService, BeanWrapCallback<PositionEmp, PositionEmpVo> {
    @Resource
    private PositionEmpDao positionEmpDao;

    @Resource
    private PositionService positionService;

    @Override
    public String save(PositionEmp positionEmp) {
        ValidatorUtils.validate(positionEmp);
        String id = positionEmpDao.save(positionEmp);
        return id;
    }

    @Override
    public void update(PositionEmp positionEmp) {
        ValidatorUtils.validate(positionEmp);
        positionEmpDao.update(positionEmp);
    }

    @Override
    public PageVo pageQuery(PositionEmpBo bo) {
        PageVo vo = new PageVo();
        Long total = positionEmpDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<PositionEmp> positionEmpList = positionEmpDao.query(bo);
        List<PositionEmpVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(positionEmpList, PositionEmpVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public PositionEmpVo findById(String id) {
        PositionEmp positionEmp = positionEmpDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(positionEmp, PositionEmpVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            positionEmpDao.deleteById(id);
        }
    }

    @Override
    public Integer addEmp(String positionId, String[] empIds) {
        Assert.hasText(positionId, "添加员工失败!岗位不能为空!");
        Assert.notEmpty(empIds, "添加员工失败!员工ID列表不能为空!");
        int counts = 0;
        for (String empId : empIds) {
            if (StringUtils.isNotEmpty(empId)) {
                // 去掉重复添加的员工
                PositionEmp positionEmp = positionEmpDao.find(positionId, empId);
                if (positionEmp != null) {
                    continue;
                }

                // 保存关系
                positionEmp = new PositionEmp(positionId, empId);
                positionEmpDao.save(positionEmp);
                counts++;
            }
        }
        return counts;
    }

    @Override
    public Integer removeEmp(String positionId, String[] empIds) {
        Assert.hasText(positionId, "移除员工失败!岗位ID不能为空!");
        Assert.notEmpty(empIds, "移除员工失败!员工ID列表不能为空!");
        int counts = 0;
        for (String empId : empIds) {
            if (StringUtils.isNotEmpty(empId)) {
                PositionEmp orgEmp = positionEmpDao.find(positionId, empId);
                if (orgEmp != null) {
                    positionEmpDao.delete(orgEmp);
                    counts++;
                }
            }
        }
        return counts;
    }

    @Override
    public PageVo queryByPosition(String positionId, EmpBo bo) {
        return null;
    }

    @Override
    public List<String> myPosition() {
        return positionEmpDao.queryEmpPositionIds(SecurityContext.getEmpId());
    }

    @Override
    public void doCallback(PositionEmp positionEmp, PositionEmpVo vo) {
    }
}
