package com.michael.stock.stock.dao;

import com.michael.stock.stock.bo.StockWeekBo;
import com.michael.stock.stock.domain.StockWeek;

import java.util.List;

/**
 * @author Michael
 */
public interface StockWeekDao {

    String save(StockWeek stockWeek);

    void update(StockWeek stockWeek);

    /**
     * 高级查询接口，不使用分页
     */
    List<StockWeek> query(StockWeekBo bo);

    /**
     * 高级查询接口，使用分页
     */
    List<StockWeek> pageQuery(StockWeekBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(StockWeekBo bo);

    StockWeek findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(StockWeek stockWeek);

}
