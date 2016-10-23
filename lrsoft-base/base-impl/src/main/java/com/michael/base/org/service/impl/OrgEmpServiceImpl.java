package com.michael.base.org.service.impl;

import com.michael.base.org.dao.OrgDao;
import com.michael.base.org.dao.OrgEmpDao;
import com.michael.base.org.domain.OrgEmp;
import com.michael.base.org.service.OrgEmpService;
import com.michael.base.org.service.OrgService;
import com.michael.utils.string.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author Michael
 */
@Service("orgEmpService")
public class OrgEmpServiceImpl implements OrgEmpService {
    @Resource
    private OrgEmpDao orgEmpDao;

    @Resource
    private OrgDao orgDao;

    @Resource
    private OrgService orgService;

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            if (StringUtils.isNotEmpty(id)) {
                orgEmpDao.deleteById(id);
            }
        }
    }

    @Override
    public Integer addEmp(String orgId, String[] empIds) {
        Assert.hasText(orgId, "添加员工失败!组织机构不能为空!");
        Assert.notEmpty(empIds, "添加员工失败!员工ID列表不能为空!");
        int counts = 0;
        for (String empId : empIds) {
            if (StringUtils.isNotEmpty(empId)) {
                // 去掉重复添加的员工
                OrgEmp orgEmp = orgEmpDao.find(orgId, empId);
                if (orgEmp != null) {
                    continue;
                }

                // 保存关系
                orgEmp = new OrgEmp(orgId, empId);
                orgEmpDao.save(orgEmp);
                counts++;
            }
        }
        return counts;
    }

    @Override
    public Integer removeEmp(String orgId, String[] empIds) {
        Assert.hasText(orgId, "删除员工失败!组织机构ID不能为空!");
        Assert.notEmpty(empIds, "删除员工失败!员工ID列表不能为空!");
        int counts = 0;
        for (String empId : empIds) {
            if (StringUtils.isNotEmpty(empId)) {
                OrgEmp orgEmp = orgEmpDao.find(orgId, empId);
                if (orgEmp != null) {
                    orgEmpDao.delete(orgEmp);
                    counts++;
                }
            }
        }
        return counts;
    }

}
