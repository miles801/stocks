package com.michael.stock.fn.service;

import com.michael.core.pager.PageVo;
import com.michael.stock.fn.bo.Fn3Bo;
import com.michael.stock.fn.domain.Fn3;
import com.michael.stock.fn.vo.Fn3Vo;

import java.util.List;

/**
 * @author Michael
 */
public interface Fn3Service {

    /**
     * 保存
     */
    String save(Fn3 fn3);

    /**
     * 更新
     */
    void update(Fn3 fn3);

    /**
     * 分页查询
     */
    PageVo pageQuery(Fn3Bo bo);

    /**
     * 不进行分页，常用于对外提供的查询接口
     */
    List<Fn3Vo> query(Fn3Bo bo);

    /**
     * 根据ID查询对象的信息
     */
    Fn3Vo findById(String id);

    /**
     * 强制删除
     */
    void deleteByIds(String[] ids);


    /**
     * 重置3元计算
     */
    void reset();
}
