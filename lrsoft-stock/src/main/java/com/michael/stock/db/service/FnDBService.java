package com.michael.stock.db.service;

import com.michael.core.pager.PageVo;
import com.michael.stock.db.bo.FnDBBo;
import com.michael.stock.db.domain.FnDB;
import com.michael.stock.db.vo.FnDBVo;

import java.util.Date;
import java.util.List;

/**
 * @author Michael
 */
public interface FnDBService {

    /**
     * 保存
     */
    String save(FnDB fnDB);

    /**
     * 更新
     */
    void update(FnDB fnDB);

    /**
     * 分页查询
     */
    PageVo pageQuery(FnDBBo bo);

    /**
     * 不进行分页，常用于对外提供的查询接口
     */
    List<FnDBVo> query(FnDBBo bo);

    /**
     * 根据ID查询对象的信息
     */
    FnDBVo findById(String id);

    /**
     * 强制删除
     */
    void deleteByIds(String[] ids);


    void add(String type, Date dbDate);

    void delete(String type, Date dbDate);
}
