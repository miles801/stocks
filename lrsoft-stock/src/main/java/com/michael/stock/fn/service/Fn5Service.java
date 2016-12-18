package com.michael.stock.fn.service;

import com.michael.core.pager.PageVo;
import com.michael.stock.fn.bo.Fn5Bo;
import com.michael.stock.fn.domain.Fn5;
import com.michael.stock.fn.vo.Fn5Vo;

import java.util.List;

/**
 * @author Michael
 */
public interface Fn5Service {

    /**
     * 保存
     */
    String save(Fn5 fn5);

    /**
     * 更新
     */
    void update(Fn5 fn5);

    /**
     * 分页查询
     */
    PageVo pageQuery(Fn5Bo bo);

    /**
     * 不进行分页，常用于对外提供的查询接口
     */
    List<Fn5Vo> query(Fn5Bo bo);

    /**
     * 根据ID查询对象的信息
     */
    Fn5Vo findById(String id);

    /**
     * 强制删除
     */
    void deleteByIds(String[] ids);

    void reset();

}
