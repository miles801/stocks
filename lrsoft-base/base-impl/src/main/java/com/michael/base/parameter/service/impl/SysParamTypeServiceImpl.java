package com.michael.base.parameter.service.impl;

import com.michael.base.parameter.bo.SysParamTypeBo;
import com.michael.base.parameter.dao.SysParamItemDao;
import com.michael.base.parameter.dao.SysParamTypeDao;
import com.michael.base.parameter.domain.SysParamType;
import com.michael.base.parameter.service.ParameterContainer;
import com.michael.base.parameter.service.SysParamTypeService;
import com.michael.base.parameter.vo.SysParamTypeVo;
import com.michael.common.CommonStatus;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.pager.PageVo;
import com.michael.utils.string.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: miles
 * @datetime: 2014-06-20
 */
@Service("sysParamTypeService")
public class SysParamTypeServiceImpl implements SysParamTypeService, BeanWrapCallback<SysParamType, SysParamTypeVo> {
    @Resource
    private SysParamTypeDao dao;

    @Resource
    private SysParamItemDao sysParamItemDao;

    @Override
    public String save(SysParamType sysParamType) {
        sysParamType.setPath(null);
        String id = dao.save(sysParamType);
        return id;
    }

    @Override
    public void update(SysParamType sysParamType) {
        dao.update(sysParamType);

        // 更新缓存信息
        ParameterContainer.getInstance().reloadSystem(sysParamType.getCode());
    }

    @Override
    public PageVo query(SysParamTypeBo bo) {
        PageVo vo = new PageVo();
        Long total = dao.getTotal(bo);
        vo.setTotal(total);
        if (total == 0) return vo;
        List<SysParamType> sysParamTypes = dao.query(bo);
        vo.setData(BeanWrapBuilder.newInstance().setCallback(this).wrapList(sysParamTypes, SysParamTypeVo.class));
        return vo;
    }

    @Override
    public SysParamTypeVo findById(String id) {
        return BeanWrapBuilder.newInstance().wrap(dao.findById(id), SysParamTypeVo.class);
    }


    @Override
    public void deleteByIds(String... ids) {
        Assert.notEmpty(ids, "删除失败!ID列表不能为空!");
        SysParamTypeBo bo = new SysParamTypeBo();
        for (String id : ids) {
            SysParamType type = dao.findById(id);
            if (type == null) {
                continue;
            }
            // 判断是否被关联
            bo.setParentId(id);
            Long total = dao.getTotal(bo);
            Assert.isTrue(total == 0, "删除失败!存在子类型，无法直接删除!请删除了所有的子类型后再执行该操作!");

            // 删除所有的参数项
            sysParamItemDao.deleteByType(type.getCode());

            // 删除
            dao.delete(type);

            // 更新缓存
            ParameterContainer.getInstance().reloadBusiness(type.getCode());
        }
    }

    @Override
    public void disable(String... ids) {
        Assert.notEmpty(ids, "禁用失败!ID列表不能为空!");
        for (String id : ids) {
            SysParamType type = dao.findById(id);
            Assert.notNull(type, "禁用失败!参数类型不存在!请刷新后重试!");
            type.setStatus(CommonStatus.CANCELED.getValue());

            // 更新缓存
            ParameterContainer.getInstance().reloadBusiness(type.getCode());
        }
    }

    @Override
    public void enable(String... ids) {
        Assert.notEmpty(ids, "启用失败!ID列表不能为空!");
        for (String id : ids) {
            SysParamType type = dao.findById(id);
            Assert.notNull(type, "启用失败!参数类型不存在!请刷新后重试!");
            type.setStatus(CommonStatus.ACTIVE.getValue());

            // 更新缓存
            ParameterContainer.getInstance().reloadBusiness(type.getCode());
        }
    }


    @Override
    public boolean hasName(String parentId, String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("名称不能为空!");
        }
        return dao.hasName(parentId, name);
    }

    @Override
    public boolean hasCode(String code) {
        return dao.hasCode(code);
    }

    @Override
    public List<SysParamTypeVo> queryOther(String id) {
        List<SysParamType> params = dao.queryOther(id);
        return BeanWrapBuilder.newInstance().wrapList(params, SysParamTypeVo.class);
    }

    @Override
    public List<SysParamTypeVo> allForTree() {
        List<SysParamType> types = dao.query(null);
        return BeanWrapBuilder.newInstance().wrapList(types, SysParamTypeVo.class);
    }

    @Override
    public List<SysParamTypeVo> queryChildren(String id, boolean containSelf) {
        List<SysParamType> types = dao.queryChildren(id);
        if (containSelf) {
            types.add(0, dao.findById(id));
        }
        return BeanWrapBuilder.newInstance().setCallback(this).wrapList(types, SysParamTypeVo.class);
    }

    @Override
    public List<SysParamTypeVo> queryUsingTree() {
        List<SysParamType> params = dao.queryUsing();
        return BeanWrapBuilder.newInstance().wrapList(params, SysParamTypeVo.class);
    }

    @Override
    public List<SysParamTypeVo> queryValidTree() {
        SysParamTypeBo bo = new SysParamTypeBo();
        bo.setStatus(CommonStatus.ACTIVE.getValue());
        List<SysParamType> params = dao.query(bo);
        return BeanWrapBuilder.newInstance().wrapList(params, SysParamTypeVo.class);
    }

    @Override
    public void doCallback(SysParamType bean, SysParamTypeVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();
        vo.setStatusName(container.getSystemNameWithNoQuery("SYS_COMMON_STATE", vo.getStatus()));
    }

    @Override
    public String getName(String code) {
        return dao.getName(code);
    }

}
