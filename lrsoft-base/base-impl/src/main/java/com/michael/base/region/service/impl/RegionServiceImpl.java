package com.michael.base.region.service.impl;

import com.michael.base.emp.dao.EmpDao;
import com.michael.base.emp.domain.Emp;
import com.michael.base.region.bo.RegionBo;
import com.michael.base.region.dao.RegionDao;
import com.michael.base.region.domain.Region;
import com.michael.base.region.domain.RegionType;
import com.michael.base.region.service.RegionService;
import com.michael.base.region.vo.RegionVo;
import com.michael.core.SystemContainer;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.exception.NullParamException;
import com.michael.core.pager.PageVo;
import com.michael.utils.uuid.UUIDGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: miles
 * @datetime: 2014-03-25
 */
@Service("regionService")
public class RegionServiceImpl implements RegionService, BeanWrapCallback<Region, RegionVo> {
    @Resource
    private RegionDao regionDao;

    @Override
    public String save(Region region) {
        region.setId(UUIDGenerator.generate());
        region.setDeleted(false);
        String id = regionDao.save(region);
        return id;
    }

    @Override
    public void update(Region region) {
        regionDao.update(region);
    }

    @Override
    public PageVo query(RegionBo bo) {
        PageVo vo = new PageVo();
        Long total = regionDao.getTotal(bo);
        if (total == null || total == 0) return vo;
        vo.setTotal(total);
        List<Region> regions = regionDao.query(bo);
        vo.setData(
                BeanWrapBuilder.newInstance()
                        .setCallback(this)
                        .wrapList(regions, RegionVo.class)
        );
        return vo;
    }

    @Override
    public RegionVo findById(String id) {
        return BeanWrapBuilder.newInstance().wrap(regionDao.findById(id), RegionVo.class);
    }


    @Override
    public void deleteByIds(String... ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            RegionBo bo = new RegionBo();
            bo.setParentId(id);
            Long total = regionDao.getTotal(bo);
            Assert.isTrue(total == null || total == 0, "删除失败!行政区域下包含子的行政区域，不允许删除!");
            regionDao.deleteById(id);
        }
    }

    @Override
    public List<RegionVo> tree(RegionBo bo) {
        //查询数据
        List<Region> regions = regionDao.query(bo);
        return BeanWrapBuilder.newInstance()
//                .setCallback(this)
                .wrapList(regions, RegionVo.class);
    }

    @Override
    public RegionVo queryByCode(String code) {
        RegionVo vo = new RegionVo();
        if (StringUtils.isBlank(code)) {
            throw new NullParamException("区号不能为空!");
        }
        RegionBo regionBo = new RegionBo();
        regionBo.setCode(code);
        List<Region> regions = regionDao.query(regionBo);
        if (regions != null && regions.size() > 0) {
            Region region = regions.get(0);
            vo.setId(region.getId());
            vo.setName(region.getName());
        }
        return vo;
    }

    @Override
    public List<RegionVo> queryMine() {
        List<Region> regions = regionDao.queryMyArea();
        final Map<String, String> map = new HashMap<String, String>();
        return BeanWrapBuilder.newInstance()
                .setCallback(new BeanWrapCallback<Region, RegionVo>() {
                    @Override
                    public void doCallback(Region region, RegionVo vo) {
                        // 设置上级名称
                        String parentId = region.getParentId();
                        if (com.michael.utils.string.StringUtils.isEmpty(region.getParentName())) {
                            // 从缓存的数据中取出名称
                            String pn = map.get(parentId);
                            // 如果名称还未进行缓存，则重新加载
                            if (com.michael.utils.string.StringUtils.isEmpty(pn)) {
                                Region r = regionDao.findById(parentId);
                                if (r != null) {
                                    vo.setParentName(r.getName());
                                    map.put(parentId, r.getName());
                                }
                            } else {
                                vo.setParentName(pn);
                            }
                        }
                    }
                })
                .wrapList(regions, RegionVo.class);
    }

    @Override
    public void setMaster(String id, String masterId) {
        Assert.hasText(id, "操作失败!行政区域ID不能为空!");
        Assert.hasText(masterId, "操作失败!负责人ID不能为空!");
        Region region = regionDao.findById(id);
        Assert.notNull(region, "操作失败!行政区域已经不存在!请刷新后重试!");
        Assert.isTrue(RegionType.DISTRICT == region.getType(), "操作失败!只允许对县区的负责人进行设置!");
        Emp emp = SystemContainer.getInstance().getBean(EmpDao.class).findById(masterId);
        Assert.notNull(emp, "操作失败!员工已经不存在，请刷新后重试!");
        region.setMasterId(masterId);
        region.setMasterName(emp.getName());
    }

    @Override
    public void clearMaster(String id) {
        Assert.hasText(id, "操作失败!行政区域ID不能为空!");
        Region region = regionDao.findById(id);
        Assert.notNull(region, "操作失败!行政区域已经不存在!请刷新后重试!");
        Assert.isTrue(RegionType.DISTRICT == region.getType(), "操作失败!只允许对县区的负责人进行设置!");
        region.setMasterId(null);
        region.setMasterName(null);
    }

    @Override
    public void doCallback(Region region, RegionVo vo) {
    }

    @Override
    public RegionVo getBelongProvence(String city) {
        Region region = regionDao.getBelongProvence(city);
        return BeanWrapBuilder.newInstance()
                .addProperties(new String[]{"id", "name"})
                .wrap(region, RegionVo.class);
    }
}
