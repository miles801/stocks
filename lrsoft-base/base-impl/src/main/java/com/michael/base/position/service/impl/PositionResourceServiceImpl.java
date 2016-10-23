package com.michael.base.position.service.impl;

import com.michael.base.position.bo.PositionResourceBo;
import com.michael.base.position.dao.PositionResourceDao;
import com.michael.base.position.domain.PositionResource;
import com.michael.base.position.service.PositionResourceService;
import com.michael.base.position.vo.PositionResourceVo;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Michael
 */
@Service("positionResourceService")
public class PositionResourceServiceImpl implements PositionResourceService, BeanWrapCallback<PositionResource, PositionResourceVo> {
    @Resource
    private PositionResourceDao positionResourceDao;

    @Override
    public String save(PositionResource positionResource) {
        ValidatorUtils.validate(positionResource);
        String id = positionResourceDao.save(positionResource);
        return id;
    }

    @Override
    public void grantMenu(String positionId, List<String> resourceIds) {
        Assert.hasText(positionId, "授权失败!岗位ID不能为空!");

        // 删除之前岗位的所有权限
        positionResourceDao.deleteByPosition(positionId, com.michael.base.resource.domain.Resource.TYPE_MENU);

        // 保存新的权限
        if (resourceIds != null) {
            for (String resourceId : resourceIds) {
                PositionResource pr = new PositionResource();
                pr.setPositionId(positionId);
                pr.setResourceId(resourceId);
                pr.setResourceType(com.michael.base.resource.domain.Resource.TYPE_MENU);
                positionResourceDao.save(pr);
            }
        }
    }

    @Override
    public void grantElement(String positionId, List<String> resourceIds) {
        Assert.hasText(positionId, "授权失败!岗位ID不能为空!");

        // 删除之前岗位的所有权限
        positionResourceDao.deleteByPosition(positionId, com.michael.base.resource.domain.Resource.TYPE_ELEMENT);

        // 保存新的权限
        if (resourceIds != null) {
            for (String resourceId : resourceIds) {
                PositionResource pr = new PositionResource();
                pr.setPositionId(positionId);
                pr.setResourceId(resourceId);
                pr.setResourceType(com.michael.base.resource.domain.Resource.TYPE_ELEMENT);
                positionResourceDao.save(pr);
            }
        }
    }

    @Override
    public void grantData(String positionId, List<String> resourceIds) {
        Assert.hasText(positionId, "授权失败!岗位ID不能为空!");

        // 删除之前岗位的所有权限
        positionResourceDao.deleteByPosition(positionId, com.michael.base.resource.domain.Resource.TYPE_DATA);

        // 保存新的权限
        if (resourceIds != null) {
            for (String resourceId : resourceIds) {
                PositionResource pr = new PositionResource();
                pr.setPositionId(positionId);
                pr.setResourceId(resourceId);
                pr.setResourceType(com.michael.base.resource.domain.Resource.TYPE_DATA);
                positionResourceDao.save(pr);
            }
        }
    }

    @Override
    public PageVo pageQuery(PositionResourceBo bo) {
        PageVo vo = new PageVo();
        Long total = positionResourceDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<PositionResource> positionResourceList = positionResourceDao.query(bo);
        List<PositionResourceVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(positionResourceList, PositionResourceVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public PositionResourceVo findById(String id) {
        PositionResource positionResource = positionResourceDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(positionResource, PositionResourceVo.class);
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            positionResourceDao.deleteById(id);
        }
    }

    @Override
    public List<String> queryByPosition(String positionId) {
        return positionResourceDao.queryByPosition(positionId);
    }

    @Override
    public void doCallback(PositionResource positionResource, PositionResourceVo vo) {
    }
}
