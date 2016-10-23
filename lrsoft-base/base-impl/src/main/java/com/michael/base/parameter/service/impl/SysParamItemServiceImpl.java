package com.michael.base.parameter.service.impl;

import com.michael.base.parameter.bo.SysParamItemBo;
import com.michael.base.parameter.dao.SysParamItemDao;
import com.michael.base.parameter.dao.SysParamTypeDao;
import com.michael.base.parameter.domain.SysParamItem;
import com.michael.base.parameter.service.ParameterContainer;
import com.michael.base.parameter.service.SysParamItemService;
import com.michael.base.parameter.vo.SysParamItemVo;
import com.michael.core.beans.BeanWrapBuilder;
import com.michael.core.beans.BeanWrapCallback;
import com.michael.core.hibernate.validator.ValidatorUtils;
import com.michael.core.pager.PageVo;
import com.michael.core.pool.ThreadPool;
import com.michael.utils.string.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: miles
 * @datetime: 2014-06-20
 */
@Service("sysParamItemService")
public class SysParamItemServiceImpl implements SysParamItemService, BeanWrapCallback<SysParamItem, SysParamItemVo> {
    @Resource(name = "sysParamItemDao")
    private SysParamItemDao dao;

    @Resource(name = "sysParamTypeDao")
    private SysParamTypeDao sysParamTypeDao;

    @Override
    public String save(final SysParamItem sysParamItem) {
        validate(sysParamItem);
        String id = dao.save(sysParamItem);
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ParameterContainer.getInstance().reloadSystem(sysParamItem.getType());
            }
        });
        return id;
    }

    private void validate(SysParamItem item) {
        ValidatorUtils.validate(item);
    }


    @Override
    public void update(final SysParamItem sysParamItem) {
        validate(sysParamItem);

        dao.update(sysParamItem);
        // 更新缓存
//        noticeBroker(sysParamItem.getType());
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ParameterContainer.getInstance().reloadSystem(sysParamItem.getType());
            }
        });
    }

    @Override
    public PageVo query(SysParamItemBo bo) {
        PageVo vo = new PageVo();
        Long total = dao.getTotal(bo);
        vo.setTotal(total);
        if (total == 0) return vo;
        List<SysParamItem> sysParamItems = dao.query(bo);
        vo.setData(BeanWrapBuilder.newInstance().setCallback(this).wrapList(sysParamItems, SysParamItemVo.class));
        return vo;
    }

    @Override
    public SysParamItemVo findById(String id) {
        return BeanWrapBuilder.newInstance()
                .wrap(dao.findById(id), SysParamItemVo.class);
    }

    @Override
    public List<SysParamItemVo> queryValid(String type) {
        if (StringUtils.isEmpty(type)) {
            throw new IllegalArgumentException("根据参数类型查询有效的选项时,没有获得类型编号!");
        }
        SysParamItemBo bo = new SysParamItemBo();
        bo.setType(type);
        bo.setStatus("ACTIVE");
        List<SysParamItem> items = dao.query(bo);
        return BeanWrapBuilder.newInstance().setCallback(this).wrapList(items, SysParamItemVo.class);
    }

    @Override
    public void deleteByIds(String... ids) {
        if (ids == null || ids.length == 0) return;
        for (String id : ids) {
            if (com.michael.utils.string.StringUtils.isEmpty(id)) {
                continue;
            }
            final SysParamItem item = dao.findById(id);
            if (item == null) {
                continue;
            }


            final String type = item.getType();
            // 更新缓存
            ThreadPool.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ParameterContainer.getInstance().reloadSystem(type);
                }
            });
            dao.delete(item);

        }
    }

    @Override
    public void disable(String... ids) {
        Assert.notEmpty(ids, "禁用失败!没有获取到要禁用的参数的ID数组!");
        for (String id : ids) {
            if (com.michael.utils.string.StringUtils.isEmpty(id)) {
                continue;
            }
            SysParamItem item = dao.findById(id);
            Assert.notNull(item, "禁用失败!参数不存在!请刷新后重试!");
            item.setStatus("CANCELED");
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
            SysParamItem item = dao.findById(id);
            Assert.notNull(item, "启用失败!参数不存在!请刷新后重试!");
            item.setStatus("ACTIVE");
            // 更新缓存
            ParameterContainer.getInstance().reloadSystem(item.getType());
        }
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
    public List<SysParamItemVo> fetchCascade(String typeCode, String value) {
        List<SysParamItem> items = dao.fetchCascade(typeCode, value);
        return BeanWrapBuilder.newInstance()
                .setCallback(this)
                .wrapList(items, SysParamItemVo.class);
    }

    @Override
    public void doCallback(SysParamItem item, SysParamItemVo vo) {
        ParameterContainer container = ParameterContainer.getInstance();
        vo.setTypeName(container.getSystemTypeName(vo.getType()));
        vo.setStatusName(container.getSystemNameWithNoQuery("SYS_COMMON_STATE", vo.getStatus()));
        String cascadeType = item.getCascadeTypeCode();
        // 设置级联信息
        if (StringUtils.isNotEmpty(cascadeType)) {
            vo.setCascadeTypeName(container.getSystemTypeName(cascadeType));
            String cascadeValue = item.getCascadeItemValue();
            if (StringUtils.isNotEmpty(cascadeValue)) {
                String name = container.getSystemNameWithNoQuery(cascadeType, cascadeValue);
                if (name == null) {
                    name = dao.queryName(cascadeType, cascadeValue);
                }
                vo.setCascadeItemName(name);
            }
        }

        // 是否是级联
        boolean hasCascaded = dao.hasCascade(item.getType(), item.getValue());
        vo.setCascade(hasCascaded);
    }

    @Override
    public String findName(String type, String value) {
        Assert.hasText(type, "查询失败!类型编号不能为空!");
        Assert.hasText(value, "查询失败!参数值不能为空!");
        return dao.queryName(type, value);
    }
}
