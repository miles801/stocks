package com.michael.stock.fn.dao;

import com.michael.stock.fn.bo.Fn4Bo;
import com.michael.stock.fn.domain.Fn4;

import java.util.List;

/**
 * @author Michael
 */
public interface Fn4Dao {

    String save(Fn4 fn4);

    void update(Fn4 fn4);

    /**
     * 高级查询接口，不使用分页
     */
    List<Fn4> query(Fn4Bo bo);

    /**
     * 高级查询接口，使用分页
     */
    List<Fn4> pageQuery(Fn4Bo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(Fn4Bo bo);

    Fn4 findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Fn4 fn4);

}
