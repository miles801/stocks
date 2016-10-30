package com.michael.stock.stock.service;

import com.michael.core.pager.PageVo;
import com.michael.stock.stock.bo.StockWeekBo;
import com.michael.stock.stock.domain.StockWeek;
import com.michael.stock.stock.vo.StockWeekVo;

import java.util.List;

/**
 * @author Michael
 */
public interface StockWeekService {

    /**
     * 保存
     */
    String save(StockWeek stockWeek);

    /**
     * 更新
     */
    void update(StockWeek stockWeek);

    /**
     * 分页查询
     */
    PageVo pageQuery(StockWeekBo bo);

    /**
     * 不进行分页，常用于对外提供的查询接口
     */
    List<StockWeekVo> query(StockWeekBo bo);

    /**
     * 根据ID查询对象的信息
     */
    StockWeekVo findById(String id);

    /**
     * 强制删除
     */
    void deleteByIds(String[] ids);


    /**
     * 导入数据
     *
     * @param attachmentIds 上传的附件列表
     */
    void importData(String[] attachmentIds);
}
