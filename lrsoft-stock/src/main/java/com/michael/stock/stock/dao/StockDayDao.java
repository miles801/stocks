package com.michael.stock.stock.dao;

import com.michael.stock.stock.bo.StockDayBo;
import com.michael.stock.stock.domain.StockDay;
import com.michael.stock.stock.vo.StockDayVo;
import java.util.List;

/**
 * @author Michael
 */
public interface StockDayDao {

    String save(StockDay stockDay);

    void update(StockDay stockDay);

    /**
     * 高级查询接口，不使用分页
     */
    List<StockDay> query(StockDayBo bo);

    /**
     * 高级查询接口，使用分页
     */
    List<StockDay> pageQuery(StockDayBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(StockDayBo bo);

    StockDay findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(StockDay stockDay);

}
