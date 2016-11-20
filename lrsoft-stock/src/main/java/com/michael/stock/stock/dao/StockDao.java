package com.michael.stock.stock.dao;

import com.michael.stock.stock.bo.StockBo;
import com.michael.stock.stock.domain.Stock;

import java.util.List;

/**
 * @author Michael
 */
public interface StockDao {

    String save(Stock stock);

    void update(Stock stock);

    /**
     * 高级查询接口，不使用分页
     */
    List<Stock> query(StockBo bo);

    /**
     * 高级查询接口，使用分页
     */
    List<Stock> pageQuery(StockBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(StockBo bo);

    Stock findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(Stock stock);

    /**
     * 判断是否具有重复的项
     *
     * @param code 编号
     * @param id   排除自身
     * @return true：存在
     */
    boolean hasCode(String code, String id);

    /**
     * 判断是否具有重复的项
     *
     * @param name 名称
     * @param id   排除自身
     * @return true：存在
     */
    boolean hasName(String name, String id);

    /**
     * 查询指定类型最大的编号
     *
     * @param type 类型
     * @return 股票代码
     */
    String maxCode(int type);


    /**
     * 加载指定类型的股票代码
     *
     * @return 所有的股票代码
     */
    List<String> queryCode();
}
