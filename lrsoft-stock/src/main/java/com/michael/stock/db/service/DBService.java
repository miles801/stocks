package com.michael.stock.db.service;

import com.michael.core.pager.PageVo;
import com.michael.stock.db.bo.DBBo;
import com.michael.stock.db.domain.DB;
import com.michael.stock.db.vo.DBVo;

import java.util.List;
import java.util.Map;

/**
 * @author Michael
 */
public interface DBService {

    String TYPE = "DB_TYPE";
    String TYPE1 = "1";
    String TYPE2 = "2";
    String TYPE3 = "3";
    String TYPE4 = "4";
    String TYPE_FN = "fn";

    /**
     * 保存
     */
    String save(DB dB);

    /**
     * 更新
     */
    void update(DB dB);

    /**
     * 分页查询
     */
    PageVo pageQuery(DBBo bo);

    /**
     * 不进行分页，常用于对外提供的查询接口
     */
    List<DBVo> query(DBBo bo);

    /**
     * 根据ID查询对象的信息
     */
    DBVo findById(String id);

    /**
     * 强制删除
     */
    void deleteByIds(String[] ids);


    /**
     * 计算
     *
     * @param type 运算方式
     */
    List<Map<String, Object>> calculate(int type);
}
