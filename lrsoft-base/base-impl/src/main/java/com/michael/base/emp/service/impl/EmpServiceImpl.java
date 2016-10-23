package com.michael.base.emp.service.impl;

import com.michael.base.common.BaseParameter;
import com.michael.base.emp.bo.EmpBo;
import com.michael.base.emp.dao.EmpDao;
import com.michael.base.emp.domain.Emp;
import com.michael.base.emp.service.EmpEvent;
import com.michael.base.emp.service.EmpService;
import com.michael.base.emp.vo.EmpVo;
import com.michael.base.parameter.service.ParameterContainer;
import com.michael.base.position.dao.PositionEmpDao;
import com.michael.base.position.domain.Position;
import com.michael.base.position.domain.PositionEmp;
import com.michael.core.SystemContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.context.SecurityContext;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.pinyin.SimplePinYin;
import com.michael.pinyin.StandardStrategy;
import com.michael.utils.md5.MD5Utils;
import com.michael.utils.string.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("empService")
public class EmpServiceImpl implements EmpService, BeanWrapCallback<Emp, EmpVo> {
    @Resource
    private EmpDao empDao;

    @Resource
    private PositionEmpDao positionEmpDao;

    @Override
    public String save(Emp emp) {
        // 设置初始化数据
        emp.setLocked(0);
        emp.setPassword(MD5Utils.encode("123"));
        if (StringUtils.isEmpty(emp.getPinyin())) {
            emp.setPinyin(new SimplePinYin().toPinYin(emp.getName(), new StandardStrategy()));
        }

        // 验证合法性：员工编号和登录用户名
        validate(emp);

        // 保存
        String id = empDao.save(emp);

        // 保存角色（岗位）
        savePosition(emp.getRoles(), id);

        return id;
    }

    /**
     * @param roles 角色ID列表，用,进行分隔
     * @param id    员工ID
     */
    private void savePosition(String roles, String id) {
        if (StringUtils.isNotEmpty(roles)) {
            String rolesIds[] = roles.split(",");
            for (String roleId : rolesIds) {
                PositionEmp pe = new PositionEmp();
                pe.setEmpId(id);
                pe.setPositionId(roleId);
                positionEmpDao.save(pe);
            }
        }
    }

    private void validate(Emp emp) {
        ValidatorUtils.validate(emp);

        // 检查用户编号
        if (StringUtils.isNotEmpty(emp.getCode())) {
            boolean hasCode = empDao.hasCode(emp.getCode(), emp.getId());
            Assert.isTrue(!hasCode, "操作失败!工号重复!");
        }

        // 检查登录用户名
        boolean hasLoginName = empDao.hasLoginName(emp.getLoginName(), emp.getId());
        Assert.isTrue(!hasLoginName, "操作失败!登录用户名已经存在!");

    }

    @Override
    public void update(Emp emp) {

        // 验证合法性
        validate(emp);
        if (StringUtils.isEmpty(emp.getPinyin())) {
            emp.setPinyin(new SimplePinYin().toPinYin(emp.getName(), new StandardStrategy()));
        }

        // 删除之前的岗位，并重新建立关系
        positionEmpDao.deleteByEmp(emp.getId());
        savePosition(emp.getRoles(), emp.getId());

        // 触发相关的事件
        EmpEvent empEvent = SystemContainer.getInstance().getBean(EmpEvent.class);
        if (empEvent != null) {
            empEvent.onUpdate(empDao.findById(emp.getId()), emp);
        }

        empDao.update(emp);
    }

    @Override
    public PageVo pageQuery(EmpBo bo) {
        PageVo vo = new PageVo();
        Long total = empDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Emp> empList = empDao.query(bo);
        List<EmpVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(empList, EmpVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public EmpVo findById(String id) {
        Emp emp = empDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrap(emp, EmpVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            Emp emp = empDao.findById(id);
            Assert.notNull(emp, "禁用失败!员工不存在，请刷新后重试!");
            emp.setLocked(Emp.STATUS_LOCKED);
        }
    }

    @Override
    public EmpVo login(String loginName, String password) {
        Assert.hasText(loginName, "登录失败!未获取到登录名!");
        Assert.hasText(password, "登录失败!未获取到密码!");
        Emp emp = empDao.findByLoginName(loginName);
        Assert.notNull(emp, "登录失败!用户名/密码不正确!");
        Assert.isTrue(emp.getLocked().equals(Emp.STATUS_NORMAL), "登录失败!用户已经被锁定!");
        Assert.isTrue(password.equals(emp.getPassword()), "登录失败!用户名/密码不正确!");

        return BeanWrapBuilder.newInstance()
                .addProperties(new String[]{"password"})
                .exclude()
//                .setCallback(this)
                .wrap(emp, EmpVo.class);
    }

    @Override
    public void start(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            Emp emp = empDao.findById(id);
            Assert.notNull(emp, "启用失败!员工不存在，请刷新后重试!");
            emp.setLocked(Emp.STATUS_NORMAL);
        }
    }

    @Override
    public void updatePwd(String oldPwd, String newPwd) {
        Assert.hasText(oldPwd, "更新密码失败!原密码不能为空!");
        Assert.hasText(newPwd, "更新密码失败!新密码不能为空!");
        Emp emp = empDao.findById(SecurityContext.getEmpId());
        Assert.notNull(emp, "更新密码失败!当前用户不存在!");
        Assert.isTrue(emp.getPassword().equals(MD5Utils.encode(oldPwd)), "更新密码失败!原密码不正确!");
        empDao.updatePwd(SecurityContext.getEmpId(), MD5Utils.encode(newPwd));
    }

    @Override
    public PageVo queryByOrg(String orgId, EmpBo bo) {
        Assert.hasText(orgId, "查询失败!组织机构ID不能为空!");
        PageVo vo = new PageVo();
        if (bo != null) {
            bo.setOrgId(null);
        }
        Long total = empDao.getTotalByOrg(orgId, bo);
        if (total == null || total == 0) {
            return vo;
        }
        vo.setTotal(total);
        List<Emp> data = empDao.queryByOrg(orgId, bo);
        vo.setData(BeanWrapBuilder.newInstance().setCallback(this)
                .wrapList(data, EmpVo.class));
        return vo;
    }

    @Override
    public PageVo queryByPosition(String positionId, EmpBo bo) {
        Assert.hasText(positionId, "查询失败!岗位ID不能为空!");
        PageVo vo = new PageVo();
        List<String> empIds = positionEmpDao.queryEmp(positionId);
        if (empIds == null || empIds.isEmpty()) {
            vo.setTotal(0L);
            return vo;
        }
        if (bo == null) {
            bo = new EmpBo();
        }
        bo.setIds(empIds);
        Long total = empDao.getTotal(bo);
        if (total == null || total == 0) {
            vo.setTotal(0L);
            return vo;
        }
        vo.setTotal(total);
        List<Emp> data = empDao.query(bo);
        vo.setData(BeanWrapBuilder.newInstance().setCallback(this)
                .wrapList(data, EmpVo.class));
        return vo;
    }

    @Override
    public void doCallback(Emp emp, EmpVo vo) {
        Integer locked = emp.getLocked();
        if (locked == null) {

        } else if (locked == 0) {
            vo.setLockedName("正常");
        } else if (locked == 1) {
            vo.setLockedName("锁定");
        } else if (locked == 2) {
            vo.setLockedName("离职");
        } else {
            vo.setLockedName("异常");
        }

        ParameterContainer container = ParameterContainer.getInstance();
        vo.setSexName(container.getBusinessName(BaseParameter.SEX, emp.getSex()));
        vo.setDutyName(container.getBusinessName(BaseParameter.DUTY, emp.getDuty()));

        // 查询员工的角色列表（进行回显）
        List<Position> positions = positionEmpDao.queryByEmp(emp.getId());
        if (positions != null && !positions.isEmpty()) {
            StringBuilder builderNames = new StringBuilder();
            StringBuilder builderIds = new StringBuilder();

            for (Position p : positions) {
                builderIds.append(",").append(p.getId());
                builderNames.append(",").append(p.getName());
            }
            vo.setRoleIds(builderIds.substring(1));
            vo.setRoleNames(builderNames.substring(1));
        }

    }
}
