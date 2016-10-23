package com.michael.base.parameter.service.impl;

import com.michael.base.parameter.bo.BusinessParamItemBo;
import com.michael.base.parameter.dao.BusinessParamItemDao;
import com.michael.base.parameter.dao.BusinessParamTypeDao;
import com.michael.base.parameter.domain.BusinessParamItem;
import com.michael.base.parameter.service.BusinessParamItemService;
import com.michael.base.parameter.service.ParameterContainer;
import com.michael.base.parameter.vo.BusinessParamItemVo;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.pager.PageVo;
import com.michael.utils.string.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: miles
 * @datetime: 2014-07-02
 */
@Service("businessParamItemService")
public class BusinessParamItemServiceImpl implements BusinessParamItemService, BeanWrapCallback<BusinessParamItem, BusinessParamItemVo> {
    @Resource(name = "businessParamItemDao")
    private BusinessParamItemDao dao;

    @Resource(name = "businessParamTypeDao")
    private BusinessParamTypeDao typeDao;


    @Override
    public String save(BusinessParamItem businessParamItem) {
        String id = dao.save(businessParamItem);
        // 更新缓存
//        noticeBroker(businessParamItem.getType());
        ParameterContainer.getInstance().reloadBusiness(businessParamItem.getType());
        return id;
    }

    @Override
    public void update(BusinessParamItem sysParamItem) {
        dao.update(sysParamItem);
        // 更新缓存
//        noticeBroker(sysParamItem.getType());
        ParameterContainer.getInstance().reloadBusiness(sysParamItem.getType());
    }

    @Override
    public PageVo query(BusinessParamItemBo bo) {
        PageVo vo = new PageVo();
        Long total = dao.getTotal(bo);
        vo.setTotal(total);
        if (total == 0) return vo;
        List<BusinessParamItem> sysParamItems = dao.query(bo);
        vo.setData(BeanWrapBuilder.newInstance().setCallback(this).wrapList(sysParamItems, BusinessParamItemVo.class));
        return vo;
    }

    @Override
    public BusinessParamItemVo findById(String id) {
        return wrap(dao.findById(id));
    }

    @Override
    public List<BusinessParamItemVo> queryValid(String type) {
        if (StringUtils.isEmpty(type)) {
            throw new IllegalArgumentException("根据参数类型查询有效的选项时,没有获得类型编号!");
        }
        BusinessParamItemBo bo = new BusinessParamItemBo();
        bo.setType(type);
        bo.setStatus("ACTIVE");
        List<BusinessParamItem> items = dao.query(bo);
        return BeanWrapBuilder.newInstance().setCallback(this).wrapList(items, BusinessParamItemVo.class);
    }

    @Override
    public void deleteByIds(String... ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            if (com.michael.utils.string.StringUtils.isEmpty(id)) {
                continue;
            }
            BusinessParamItem item = dao.findById(id);
            if (item == null) {
                continue;
            }

            dao.delete(item);

            // 更新缓存
            ParameterContainer.getInstance().reloadSystem(item.getType());
        }
    }

    @Override
    public void disable(String... ids) {
        Assert.notEmpty(ids, "禁用失败!没有获取到要禁用的参数的ID数组!");
        for (String id : ids) {
            if (com.michael.utils.string.StringUtils.isEmpty(id)) {
                continue;
            }
            BusinessParamItem item = dao.findById(id);
            Assert.notNull(item, "禁用失败!参数不存在!请刷新后重试!");
            item.setStatus(com.michael.common.CommonStatus.CANCELED.getValue());
            // 更新缓存
            ParameterContainer.getInstance().reloadSystem(item.getType());
        }
    }


    @Override
    public void enable(String... ids) {
        Assert.notEmpty(ids, "启用失败!没有获取到要启用的参数的ID数组!");
        for (String id : ids) {
            if (com.michael.utils.string.StringUtils.isEmpty(id)) {
                continue;
            }
            BusinessParamItem item = dao.findById(id);
            Assert.notNull(item, "启用失败!参数不存在!请刷新后重试!");
            item.setStatus(com.michael.common.CommonStatus.ACTIVE.getValue());
            // 更新缓存
            ParameterContainer.getInstance().reloadSystem(item.getType());
        }
    }


    public BusinessParamItemVo wrap(BusinessParamItem businessParamItem) {
        if (businessParamItem == null) return null;
        BusinessParamItemVo vo = new BusinessParamItemVo();
        BeanUtils.copyProperties(businessParamItem, vo);
        ParameterContainer container = ParameterContainer.getInstance();
        vo.setTypeName(container.getBusinessTypeName(vo.getType()));
        vo.setStatusName(container.getSystemName("SYS_COMMON_STATE", vo.getStatus()));

        // 获取级联参数信息
        String cascadeType = businessParamItem.getCascadeTypeCode();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(cascadeType)) {
            vo.setCascadeTypeName(container.getBusinessTypeName(cascadeType));
            String cascadeValue = businessParamItem.getCascadeItemValue();
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(cascadeValue)) {
                String name = container.getBusinessNameWithNoQuery(cascadeType, cascadeValue);
                if (name == null) {
                    name = dao.queryName(cascadeType, cascadeValue);
                }
                vo.setCascadeItemName(name);
            }
        }
        return vo;
    }

    @Override
    public boolean hasName(String typeCode, String name) {
        return dao.hasName(typeCode, name);
    }

    @Override
    public boolean hasValue(String typeCode, String value) {
        return dao.hasValue(typeCode, value);
    }

    @Override
    public List<BusinessParamItemVo> fetchCascade(String typeCode, String value) {
        List<BusinessParamItem> items = dao.fetchCascade(typeCode, value);
        Set<String> types = new HashSet<String>();
        Set<String> values = new HashSet<String>();
        for (BusinessParamItem item : items) {
            types.add(item.getType());
            values.add(item.getValue());
        }
        // 获取被级联的参数列表
        final List<String> ids = dao.hasBeenCascaded(types, values);

        return BeanWrapBuilder.newInstance().
                setCallback(new BeanWrapCallback<BusinessParamItem, BusinessParamItemVo>() {
                    @Override
                    public void doCallback(BusinessParamItem o, BusinessParamItemVo vo) {
                        // 设置是否被级联
                        if (ids != null && ids.contains(o.getId())) {
                            vo.setIsCascaded(true);
                        }
                    }
                })
                .wrapList(items, BusinessParamItemVo.class);
    }

    @Override
    public List<BusinessParamItemVo> queryCascadeItem(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        // 查询出有效的数据
        BusinessParamItemBo bo = new BusinessParamItemBo();
        bo.setType(type);
        bo.setStatus("ACTIVE");
        List<BusinessParamItem> items = dao.query(bo);
        if (items != null && !items.isEmpty()) {
            Set<String> types = new HashSet<String>(1);
            types.add(type);
            Set<String> values = new HashSet<String>(items.size());
            for (BusinessParamItem bpi : items) {
                values.add(bpi.getValue());
            }
            // 获得所有被级联的参数的id集合
            final List<String> ids = dao.hasBeenCascaded(types, values);
            if (ids == null || ids.isEmpty()) {
                return null;
            }
            // 过滤掉没有被级联的参数，并转换数据
            final BeanWrapBuilder builder = BeanWrapBuilder.newInstance();
            builder.setCallback(new BeanWrapCallback<BusinessParamItem, BusinessParamItemVo>() {
                @Override
                public void doCallback(BusinessParamItem o, BusinessParamItemVo vo) {
                    // 如果当前参数没有被级联，则跳过
                    if (!ids.contains(o.getId())) {
                        builder.skip();
                    }
                }
            });
            return builder.wrapList(items, BusinessParamItemVo.class);
        }
        return null;
    }

    @Override
    public void doCallback(BusinessParamItem item, BusinessParamItemVo vo) {

    }
}
