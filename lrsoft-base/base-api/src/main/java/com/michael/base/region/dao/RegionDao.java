package com.michael.base.region.dao;

import com.michael.base.region.bo.RegionBo;
import com.michael.base.region.domain.Region;

import java.util.List;

/**
 * @author miles
 * @datetime 2014-03-25
 */
public interface RegionDao {

    String save(Region region);

    void update(Region region);

    /**
     * 根据ID查询对应的名称
     *
     * @param id
     * @return
     */
    String getName(String id);

    List<Region> query(RegionBo bo);

    Long getTotal(RegionBo bo);

    /**
     * 删除指定id的数据，并返回删除的记录条数
     *
     * @param id
     * @return
     */
    int deleteById(String id);

    int nextSequenceNo(String parentId);

    /**
     * 根据城市区号查询城市名称
     *
     * @param cityCode 城市区号
     * @return 城市名称
     */
    String getNameByCode(String cityCode);

    /**
     * 根据城市id获取所属省份信息
     *
     * @param city 城市id
     */
    Region getBelongProvence(String city);

    public Region findById(String id);

    /**
     * 查询我负责的县区集合
     */
    List<Region> queryMyArea();
}
