package com.michael.base.resource.service.impl;


import com.michael.base.resource.bo.ResourceBo;
import com.michael.base.resource.dao.ResourceDao;
import com.michael.base.resource.domain.Resource;
import com.michael.base.resource.service.ResourceService;
import com.michael.base.resource.vo.ResourceVo;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.context.SecurityContext;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.utils.string.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Michael
 */
@Service("resourceService")
public class ResourceServiceImpl implements ResourceService, BeanWrapCallback<Resource, ResourceVo> {
    @javax.annotation.Resource
    private ResourceDao resourceDao;

    @Override
    public String save(Resource resource) {
        ValidatorUtils.validate(resource);
        resource.setDeleted(false);
        validate(resource);
        String id = resourceDao.save(resource);
        return id;
    }

    @Override
    public void update(Resource resource) {
        ValidatorUtils.validate(resource);
        validate(resource);
        resourceDao.update(resource);
    }

    private void validate(Resource resource) {
        boolean hasCode = resourceDao.hasCode(resource.getCode(), resource.getId());
        Assert.isTrue(!hasCode, "操作失败!资源编号重复!");
        boolean hasName = resourceDao.hasName(resource.getName(), resource.getParentId(), resource.getId());
        Assert.isTrue(!hasName, "操作失败!资源名称重复!");
    }

    @Override
    public PageVo pageQuery(ResourceBo bo) {
        PageVo vo = new PageVo();
        Long total = resourceDao.getTotal(bo);
        vo.setTotal(total);
        if (total == null || total == 0) return vo;
        List<Resource> resourceList = resourceDao.query(bo);
        List<ResourceVo> vos = BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(resourceList, ResourceVo.class);
        vo.setData(vos);
        return vo;
    }


    @Override
    public ResourceVo findById(String id) {
        Resource resource = resourceDao.findById(id);
        return BeanWrapBuilder.newInstance()
                .wrap(resource, ResourceVo.class);
    }

    @Override
    public void disable(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            Resource resource = resourceDao.findById(id);
            Assert.notNull(resource, "禁用失败!资源不存在，请刷新后重试!");
            resource.setDeleted(true);

            // 禁用孩子节点
            disableChildren(resource);

        }
    }

    private void disableChildren(Resource resource) {
        List<Resource> resources = resourceDao.queryChildren(resource.getId());
        if (resources != null && !resources.isEmpty()) {
            for (Resource child : resources) {
                child.setDeleted(true);
                // 这里采用递归的方式去禁用所有的子资源
                disableChildren(child);
            }
        }
    }

    @Override
    public void enable(String[] ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            Resource resource = resourceDao.findById(id);
            Assert.notNull(resource, "启用失败!资源不存在，请刷新后重试!");
            resource.setDeleted(false);

            // 采用递归的方式去启用所有的父节点
            String parentId = resource.getParentId();
            if (StringUtils.isNotEmpty(parentId)) {
                enable(new String[]{parentId});
            }
        }
    }

    @Override
    public List<ResourceVo> validMenuResource() {
        ResourceBo bo = new ResourceBo();
        bo.setType(Resource.TYPE_MENU);
        bo.setDeleted(false);
        List<Resource> data = resourceDao.query(bo);
        return BeanWrapBuilder.newInstance()
                .addProperties(new String[]{"id", "name", "code", "parentId", "parentName", "deleted", "url"})
                .wrapList(data, ResourceVo.class);
    }

    @Override
    public List<ResourceVo> validElementResource() {
        ResourceBo bo = new ResourceBo();
        bo.setType(Resource.TYPE_ELEMENT);
        bo.setDeleted(false);
        List<Resource> data = resourceDao.query(bo);
        return BeanWrapBuilder.newInstance()
                .addProperties(new String[]{"id", "name", "code", "module", "parentId", "parentName", "deleted"})
                .wrapList(data, ResourceVo.class);
    }

    @Override
    public List<ResourceVo> validDataResource() {
        ResourceBo bo = new ResourceBo();
        bo.setType(Resource.TYPE_DATA);
        bo.setDeleted(false);
        List<Resource> data = resourceDao.query(bo);
        return BeanWrapBuilder.newInstance()
                .addProperties(new String[]{"id", "name", "code", "parentId", "parentName", "deleted"})
                .wrapList(data, ResourceVo.class);
    }

    @Override
    public List<ResourceVo> validMenuRootResource() {
        ResourceBo bo = new ResourceBo();
        bo.setType(Resource.TYPE_MENU);
        bo.setParent(true);
        bo.setDeleted(false);
        List<Resource> data = resourceDao.query(bo);
        return BeanWrapBuilder.newInstance()
                .addProperties(new String[]{"id", "name", "code", "parentId", "parentName", "deleted"})
                .wrapList(data, ResourceVo.class);
    }

    @Override
    public List<ResourceVo> validChildren(String id) {
        Assert.hasText(id, "查询失败!ID不能为空!");
        ResourceBo bo = new ResourceBo();
        bo.setParentId(id);
        bo.setDeleted(false);
        List<Resource> data = resourceDao.query(bo);
        return BeanWrapBuilder.newInstance()
                .addProperties(new String[]{"id", "name", "code", "parentId", "parentName", "deleted"})
                .wrapList(data, ResourceVo.class);
    }

    @Override
    public List<ResourceVo> children(String id) {
        Assert.hasText(id, "查询失败!ID不能为空!");
        ResourceBo bo = new ResourceBo();
        bo.setParentId(id);
        List<Resource> data = resourceDao.query(bo);
        return BeanWrapBuilder.newInstance()
                .wrapList(data, ResourceVo.class);
    }

    @Override
    public List<ResourceVo> query(ResourceBo bo) {
        List<Resource> data = resourceDao.query(bo);
        return BeanWrapBuilder.newInstance()
                .wrapList(data, ResourceVo.class);
    }

    @Override
    public List<ResourceVo> myMenu() {
        ResourceBo bo = new ResourceBo();
        bo.setDeleted(false);
        bo.setType(Resource.TYPE_MENU);
        List<Resource> data = resourceDao.permissionResource(SecurityContext.getEmpId(), bo);
        return BeanWrapBuilder.newInstance()
                .wrapList(data, ResourceVo.class);
    }

    @Override
    public List<String> queryElementResourceCode() {
        ResourceBo bo = new ResourceBo();
        bo.setDeleted(false);
        bo.setType(Resource.TYPE_ELEMENT);
        List<String> data = resourceDao.permissionResourceCode(SecurityContext.getEmpId(), bo);
        return data;
    }

    @Override
    public void doCallback(Resource resource, ResourceVo vo) {
    }
}
