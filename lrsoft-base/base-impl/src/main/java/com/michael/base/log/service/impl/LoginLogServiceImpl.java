package com.michael.base.log.service.impl;

import com.michael.base.log.bo.LoginLogBo;
import com.michael.base.log.dao.LoginLogDao;
import com.michael.base.log.domain.LoginLog;
import com.michael.base.log.service.LoginLogService;
import com.michael.base.log.vo.LoginLogVo;
import com.michael.base.parameter.service.ParameterContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.utils.collection.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Michael
 */
@Service("loginLogService")
public class LoginLogServiceImpl implements LoginLogService, BeanWrapCallback<LoginLog, LoginLogVo> {
    @Resource
    private LoginLogDao loginLogDao;

    @Override
    public String save(LoginLog loginLog) {
        validate(loginLog);
        String id = loginLogDao.save(loginLog);
        return id;
    }

    @Override
    public void update(LoginLog loginLog) {
        validate(loginLog);
        loginLogDao.update(loginLog);
    }

    private void validate(LoginLog loginLog) {
        ValidatorUtils.validate(loginLog);
    }

    @Override
    public PageVo pageQuery(LoginLogBo bo) {
        PageVo vo = new PageVo();
        Long total = loginLogDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<LoginLog> loginLogList = loginLogDao.pageQuery(bo);
        List<LoginLogVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(loginLogList, LoginLogVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public LoginLogVo findById(String id) {
        LoginLog loginLog = loginLogDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(loginLog, LoginLogVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            loginLogDao.deleteById(id);
        }
    }

    @Override
    public List<LoginLogVo> query(LoginLogBo bo) {
        List<LoginLog> loginLogList = loginLogDao.query(bo);
        List<LoginLogVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(loginLogList, LoginLogVo.class);
        return vos;
    }

    @Override
    public void logout(String empId, Long logoutTime, String type) {
        Assert.hasText(empId, "退出失败!员工ID不能为空!");
        LoginLogBo bo = new LoginLogBo();
        bo.setCreatorId(empId);
        bo.setLogout(false);
        List<LoginLog> loginLogs = loginLogDao.query(bo);
        if (CollectionUtils.isNotEmpty(loginLogs)) {
            if (logoutTime == null) {
                logoutTime = new Date().getTime();
            }
            for (LoginLog log : loginLogs) {
                log.setLogoutTime(new Date());
                log.setType(type);
                log.setDuration(logoutTime - log.getLoginTime().getTime());
            }
        }

    }

    @Override
    public void doCallback(LoginLog loginLog, LoginLogVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();

        // 退出方式
        vo.setTypeName(container.getSystemName(LOGOUT_TYPE, loginLog.getType()));
    }
}
