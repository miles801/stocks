package com.michael.stock.db.dao;

import com.michael.stock.db.bo.DBBo;
import com.michael.stock.db.domain.DB;

import java.util.List;

/**
 * @author Michael
 */
public interface DBDao {

    String save(DB dB);

    void update(DB dB);

    /**
     * 高级查询接口，不使用分页
     */
    List<DB> query(DBBo bo);

    /**
     * 高级查询接口，使用分页
     */
    List<DB> pageQuery(DBBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(DBBo bo);

    DB findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(DB dB);

}
