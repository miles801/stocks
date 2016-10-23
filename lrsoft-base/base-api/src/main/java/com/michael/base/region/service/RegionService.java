package com.michael.base.region.service;

import com.michael.base.region.bo.RegionBo;
import com.michael.base.region.domain.Region;
import com.michael.base.region.vo.RegionVo;
import com.michael.core.pager.PageVo;

import java.util.List;

/**
 * @author miles
 * @datetime 2014-03-25
 */
public interface RegionService {

    String save(Region region);

    void update(Region region);

    PageVo query(RegionBo bo);

    RegionVo findById(String id);

    void deleteByIds(String... ids);

    List<RegionVo> tree(RegionBo bo);

    /**
     * 判断输入的区号是否正确(根据区号判断)
     *
     * @param code
     * @return 城市ID、城市名称
     */
    RegionVo queryByCode(String code);

    /**
     * 查询我所负责的所有城市的县区
     */
    List<RegionVo> queryMine();

    /**
     * 根据城市id获取所属省份信息（只返回id、名称）
     *
     * @param city 城市id
     */
    RegionVo getBelongProvence(String city);

    /**
     * 设置负责人（只能针对县区级别）
     *
     * @param id       行政区域ID
     * @param masterId 负责人ID
     */
    void setMaster(String id, String masterId);

    /**
     * 清空负责人（只能针对县区级别）
     *
     * @param id 行政区域ID
     */
    void clearMaster(String id);
}
