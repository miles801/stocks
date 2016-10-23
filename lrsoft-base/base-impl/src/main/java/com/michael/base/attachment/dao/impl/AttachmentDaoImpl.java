package com.michael.base.attachment.dao.impl;

import com.michael.base.attachment.bo.AttachmentBo;
import com.michael.base.attachment.dao.AttachmentDao;
import com.michael.base.attachment.domain.Attachment;
import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import com.michael.utils.string.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * @author miles
 * @datetime 14-2-28 下午10:53
 */
@Repository("attachmentDao")
public class AttachmentDaoImpl extends HibernateDaoHelper implements AttachmentDao {
    @Override
    public void save(Attachment attachment) {
        getSession().save(attachment);
    }

    @Override
    public Attachment findById(String id) {
        if (id == null || "".equals(id.trim())) throw new RuntimeException("无效的ID[" + id + "]");
        return (Attachment) getSession().get(Attachment.class, id);
    }

    @Override
    public void deleteById(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("id不能为空!");
        }
        getSession().delete(getSession().load(Attachment.class, id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Attachment> queryByBusiness(String businessId, String businessType, String businessClass) {
        if (StringUtils.isEmpty(businessId)) {
            throw new IllegalArgumentException("查询附件时必须指定业务ID");
        }
        Criteria criteria = createCriteria(Attachment.class);
        criteria.add(Restrictions.eq("businessId", businessId));
        if (StringUtils.isNotEmpty(businessType)) {
            criteria.add(Restrictions.eq("businessType", businessType));
        }
        if (StringUtils.isNotEmpty(businessClass)) {
            criteria.add(Restrictions.eq("businessClass", businessClass));
        }
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Attachment> queryByIds(String[] ids) {
        if (ids == null || ids.length == 0) {
            throw new IllegalArgumentException("根据附件ID列表查询对应的附件信息时,附件的ID数组不能为空!");
        }
        return createCriteria(Attachment.class)
                .add(Restrictions.in("id", ids))
                .list();
    }

    @Override
    public boolean hasFile(String businessId, String attachmentId) {
        if (StringUtils.isEmpty(businessId) || StringUtils.isEmpty(attachmentId)) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        Criteria criteria = createRowCountsCriteria(Attachment.class);
        criteria.add(Restrictions.eq("businessId", businessId))
                .add(Restrictions.eq("id", attachmentId));
        long total = (Long) criteria.uniqueResult();
        return total > 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> queryAllId(String businessId) {
        Assert.hasText(businessId, "查询某个业务对象的所有附件时,业务对象ID不能为空!");
        return createCriteria(Attachment.class)
                .setProjection(Projections.property("id"))
                .add(Restrictions.eq("businessId", businessId))
                .list();
    }

    @Override
    public List<String> queryAllId(String businessId, String businessType, String businessClass) {
        Assert.hasText(businessId, "查询某个业务对象的所有附件时,业务对象ID不能为空!");
        Criteria criteria = createCriteria(Attachment.class)
                .setProjection(Projections.property("id"))
                .add(Restrictions.eq("businessId", businessId));
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(businessType)) {
            criteria.add(Restrictions.eq("businessType", businessType));
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(businessClass)) {
            criteria.add(Restrictions.eq("businessClass", businessClass));
        }
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Attachment> batchLoad(String[] ids) {
        Assert.notEmpty(ids);
        return createCriteria(Attachment.class)
                .add(Restrictions.in("id", ids))
                .list();
    }

    @Override
    public void delete(Attachment attachment) {
        Assert.notNull(attachment);
        getSession().delete(attachment);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Attachment> pageQuery(AttachmentBo bo) {
        Criteria criteria = createPagerCriteria(Attachment.class)
                .addOrder(Order.desc("uploadTime"));
        CriteriaUtils.addCondition(criteria, bo);
        return criteria.list();
    }

    @Override
    public Long getTotal(AttachmentBo bo) {
        Criteria criteria = createRowCountsCriteria(Attachment.class);
        CriteriaUtils.addCondition(criteria, bo);
        return (Long) criteria.uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> queryHistoryTemp(long period) {
        long before = new Date().getTime() - period;
        return createCriteria(Attachment.class)
                .setProjection(Projections.id())
                .add(Restrictions.eq("status", Attachment.STATUS_TEMP))
                .add(Restrictions.le("uploadTime", before))
                .list();
    }
}
