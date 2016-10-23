package com.michael.base.msg.dao.impl;

import com.michael.base.msg.bo.MessageBo;
import com.michael.base.msg.dao.MessageDao;
import com.michael.base.msg.domain.Message;
import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * @author Michael
 */
@Repository("messageDao")
public class MessageDaoImpl extends HibernateDaoHelper implements MessageDao {

    @Override
    public String save(Message message) {
        return (String) getSession().save(message);
    }

    @Override
    public void update(Message message) {
        getSession().update(message);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Message> query(MessageBo bo) {
        Criteria criteria = createPagerCriteria(Message.class);
        initCriteria(criteria, bo);
        criteria.addOrder(Order.asc("hasRead"))
                .addOrder(Order.desc("createdDatetime"));
        return criteria.list();
    }

    @Override
    public Long getTotal(MessageBo bo) {
        Criteria criteria = createRowCountsCriteria(Message.class);
        initCriteria(criteria, bo);
        return (Long) criteria.uniqueResult();
    }


    @Override
    public void deleteById(String id) {
        getSession().createQuery("delete from " + Message.class.getName() + " e where e.id=?")
                .setParameter(0, id)
                .executeUpdate();
    }

    @Override
    public void delete(Message message) {
        Assert.notNull(message, "要删除的对象不能为空!");
        getSession().delete(message);
    }

    @Override
    public Message findById(String id) {
        Assert.hasText(id, "ID不能为空!");
        return (Message) getSession().get(Message.class, id);
    }

    private void initCriteria(Criteria criteria, MessageBo bo) {
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
    }

}