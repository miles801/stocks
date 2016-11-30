package com.michael.stock.fn.dao;

import com.michael.stock.fn.bo.Fn3Bo;
import com.michael.stock.fn.domain.Fn3;

import java.util.List;

/**
 * @author Michael
 */
public interface Fn3Dao {

    String save(Fn3 fn3);

    void update(Fn3 fn3);

    /**
     * 高级查询接口，不使用分页
     */
    List<Fn3> query(Fn3Bo bo);

    /**
     * 高级查询接口，使用分页
     */
    List<Fn3> pageQuery(Fn3Bo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(Fn3Bo bo);

    Fn3 findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Fn3 fn3);

}
