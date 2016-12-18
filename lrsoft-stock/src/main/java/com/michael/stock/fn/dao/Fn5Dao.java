package com.michael.stock.fn.dao;

import com.michael.stock.fn.bo.Fn5Bo;
import com.michael.stock.fn.domain.Fn5;

import java.util.List;

/**
 * @author Michael
 */
public interface Fn5Dao {

    String save(Fn5 fn5);

    void update(Fn5 fn5);

    /**
     * 高级查询接口，不使用分页
     */
    List<Fn5> query(Fn5Bo bo);

    /**
     * 高级查询接口，使用分页
     */
    List<Fn5> pageQuery(Fn5Bo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(Fn5Bo bo);

    Fn5 findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Fn5 fn5);

}
