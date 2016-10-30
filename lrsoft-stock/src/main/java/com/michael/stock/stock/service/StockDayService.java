package com.michael.stock.stock.service;

import com.michael.core.pager.PageVo;
import com.michael.stock.stock.bo.StockDayBo;
import com.michael.stock.stock.domain.StockDay;
import com.michael.stock.stock.vo.StockDayVo;
import java.util.List;
/**
 * @author Michael
 */
public interface StockDayService {

    /**
     * 保存
     */
    String save(StockDay stockDay);

    /**
     * 更新
     */
    void update(StockDay stockDay);

    /**
     * 分页查询
     */
    PageVo pageQuery(StockDayBo bo);

    /**
     * 不进行分页，常用于对外提供的查询接口
     */
    List<StockDayVo> query(StockDayBo bo);

    /**
     * 根据ID查询对象的信息
     */
    StockDayVo findById(String id);

    /**
     * 强制删除
     */
    void deleteByIds(String[] ids);


    /**
    * 导入数据
    * @param attachmentIds 上传的附件列表
    */
    void importData(String []attachmentIds);
}
