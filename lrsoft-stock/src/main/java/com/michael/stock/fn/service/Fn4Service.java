package com.michael.stock.fn.service;

import com.michael.core.pager.PageVo;
import com.michael.stock.fn.bo.Fn4Bo;
import com.michael.stock.fn.domain.Fn4;
import com.michael.stock.fn.vo.Fn4Vo;

import java.util.List;

/**
 * @author Michael
 */
public interface Fn4Service {

    /**
     * 保存
     */
    String save(Fn4 fn4);

    /**
     * 更新
     */
    void update(Fn4 fn4);

    /**
     * 分页查询
     */
    PageVo pageQuery(Fn4Bo bo);

    /**
     * 不进行分页，常用于对外提供的查询接口
     */
    List<Fn4Vo> query(Fn4Bo bo);

    /**
     * 根据ID查询对象的信息
     */
    Fn4Vo findById(String id);

    /**
     * 强制删除
     */
    void deleteByIds(String[] ids);


    void reset(Fn4Bo bo);

    /**
     * 获取最后一次处理信息
     * @param type
     * @return
     */
    Handle lastHandle(Integer type);

}
