package com.michael.base.log.dao;

import com.michael.base.log.bo.LoginLogBo;
import com.michael.base.log.domain.LoginLog;

import java.util.List;

/**
 * @author Michael
 */
public interface LoginLogDao {

    String save(LoginLog loginLog);

    void update(LoginLog loginLog);

    /**
     * 高级查询接口，不使用分页
     */
    List<LoginLog> query(LoginLogBo bo);

    /**
     * 高级查询接口，使用分页
     */
    List<LoginLog> pageQuery(LoginLogBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(LoginLogBo bo);

    LoginLog findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(LoginLog loginLog);

}
