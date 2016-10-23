package com.michael.base.log.service;

import com.michael.base.log.bo.LoginLogBo;
import com.michael.base.log.domain.LoginLog;
import com.michael.base.log.vo.LoginLogVo;
import com.michael.core.pager.PageVo;

import java.util.List;

/**
 * @author Michael
 */
public interface LoginLogService {

    public static final String LOGOUT_TYPE = "LOGOUT";
    public static final String LOGOUT_TYPE_NORMAL = "NORMAL";
    public static final String LOGOUT_TYPE_FORCE = "FORCE";
    public static final String LOGOUT_TYPE_TIMEOUT = "TIMEOUT";

    /**
     * 保存
     */
    String save(LoginLog loginLog);

    /**
     * 更新
     */
    void update(LoginLog loginLog);

    /**
     * 分页查询
     */
    PageVo pageQuery(LoginLogBo bo);

    /**
     * 不进行分页，常用于对外提供的查询接口
     */
    List<LoginLogVo> query(LoginLogBo bo);

    /**
     * 根据ID查询对象的信息
     */
    LoginLogVo findById(String id);

    /**
     * 强制删除
     */
    void deleteByIds(String[] ids);

    /**
     * 记录用户退出记录
     *
     * @param empId      用户ID
     * @param logoutTime 退出时间 如果为null，则默认为当前时间
     * @param type       退出方式
     */
    void logout(String empId, Long logoutTime, String type);

}
