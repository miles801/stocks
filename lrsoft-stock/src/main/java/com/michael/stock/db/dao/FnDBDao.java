package com.michael.stock.db.dao;

import com.michael.stock.db.bo.FnDBBo;
import com.michael.stock.db.domain.FnDB;

import java.util.List;

/**
 * @author Michael
 */
public interface FnDBDao {

    String save(FnDB fnDB);

    void update(FnDB fnDB);

    /**
     * 高级查询接口，不使用分页
     */
    List<FnDB> query(FnDBBo bo);

    /**
     * 高级查询接口，使用分页
     */
    List<FnDB> pageQuery(FnDBBo bo);

    /**
     * 查询总记录数
     */
    Long getTotal(FnDBBo bo);

    FnDB findById(String id);

    void deleteById(String id);

    /**
     * 根据实体对象删除
     * 必须保证该实体是存在的（一般是get或者load得到的对象）
     */
    void delete(FnDB fnDB);

}
