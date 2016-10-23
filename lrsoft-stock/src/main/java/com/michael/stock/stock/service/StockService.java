package com.michael.stock.stock.service;

import com.michael.core.pager.PageVo;
import com.michael.stock.stock.bo.StockBo;
import com.michael.stock.stock.domain.Stock;
import com.michael.stock.stock.vo.StockVo;

import java.util.List;
import java.util.Map;

/**
 * @author Michael
 */
public interface StockService {

    /**
     * 保存
     */
    String save(Stock stock);

    /**
     * 更新
     */
    void update(Stock stock);

    /**
     * 分页查询
     */
    PageVo pageQuery(StockBo bo);

    /**
     * 不进行分页，常用于对外提供的查询接口
     */
    List<StockVo> query(StockBo bo);

    /**
     * 根据ID查询对象的信息
     */
    StockVo findById(String id);

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

    /**
     * 同步股票
     */
    Map<String, Object> syncStock();
}
