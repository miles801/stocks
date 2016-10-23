package com.michael.core.hibernate;

import com.michael.core.SystemContainer;
import com.michael.core.hibernate.filter.DynamicDataFilter;
import com.michael.core.hibernate.filter.FilterFieldType;
import com.michael.core.pager.OrderNode;
import com.michael.core.pager.Pager;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * 封装了Hibernate的getSession操作，在开启session过程中，开启了过滤器
 *
 * @author miles
 * @datetime 14-1-24 下午4:27
 */
@Component("ycrlHibernateDaoHelper")
public class HibernateDaoHelper {

    protected Logger logger = Logger.getLogger(HibernateDaoHelper.class);

    @Resource
    private SessionFactory sessionFactory;

    protected HibernateDaoHelper() {

    }


    protected Session getSession() {
        Session session = null;
        try {

            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
            Assert.isTrue(false, "获取Session失败!请检查事务配置!");
        }
        Assert.notNull(session, "获取Session失败!请检查事务配置!");
        return session;
    }

    protected Session getSession(String filerName) {
        return getSession();
    }

    /**
     * 创建一个Query对象，并设置排序和分页信息
     * 如果hql语句中有order by语句，则不设置排序；否则判断Pager对象中是否有Order对象
     *
     * @param hql
     * @return
     */
    protected Query createQuery(String hql) {
        return createQuery(hql, null);
    }

    protected Query createQuery(String hql, String filterName) {
        Assert.hasText(hql, "创建Hibernate查询对象时,HQL语句不能为空!");
        //设置排序信息
        OrderNode orderNode = Pager.getOrder();
        StringBuilder builder = new StringBuilder();
        while (orderNode != null && orderNode.hasNext()) {
            com.michael.core.pager.Order order = orderNode.next();
            builder.append(order.getName())
                    .append(" ")
                    .append(order.isReverse() ? "desc" : "asc")
                    .append(" ");

        }
        if (!hql.contains("order by") && StringUtils.isNotEmpty(builder.toString())) {
            hql += " order by " + builder.toString();
        }

        //设置分页信息
        Query query = getSession().createQuery(hql);
        if (Pager.getStart() != null) {
            query.setFirstResult(Pager.getStart());
        }
        if (Pager.getLimit() != null) {
            query.setMaxResults(Pager.getLimit());
        }
        return query;
    }

    /**
     * 创建标准查询对象，并启用数据过滤器
     *
     * @param clazz           要查询的类型
     * @param filterName      过滤器名称（编号）
     * @param fieldName       要过滤的属性的名称
     * @param filterFieldType 要过滤的属性所属的过滤类型
     */
    protected Criteria createCriteria(Class<?> clazz, String filterName, String fieldName, FilterFieldType filterFieldType) {
        Criteria criteria = getSession().createCriteria(clazz);

        // 设置过滤条件
        if (StringUtils.isNotBlank(filterName)) {
            DynamicDataFilter dynamicDataFilter = SystemContainer.getInstance().getBean(DynamicDataFilter.class);
            if (dynamicDataFilter != null) {
                dynamicDataFilter.addFilterCondition(criteria, filterName, fieldName, filterFieldType);
            }
        }
        return criteria;
    }


    /**
     * 创建一个Criteria对象，不含分页信息
     */
    protected Criteria createCriteria(Class<?> clazz) {
        return createCriteria(clazz, null, null, null);
    }

    /**
     * 创建标准查询对象，并启用数据过滤器
     *
     * @param clazz           要查询的类型
     * @param filterName      过滤器名称（编号）
     * @param fieldName       要过滤的属性的名称
     * @param filterFieldType 要过滤的属性所属的过滤类型
     * @return
     */
    protected Criteria createPagerCriteria(Class<?> clazz, String filterName, String fieldName, FilterFieldType filterFieldType) {
        Criteria criteria = getSession().createCriteria(clazz);
        //设置分页
        if (Pager.getStart() != null) {
            criteria.setFirstResult(Pager.getStart());
        }
        if (Pager.getLimit() != null) {
            criteria.setMaxResults(Pager.getLimit());
        }
        //设置排序
        OrderNode orderNode = Pager.getOrder();
        while (orderNode != null && orderNode.hasNext()) {
            com.michael.core.pager.Order order = orderNode.next();
            String propertyName = order.getName();
            criteria.addOrder(order.isReverse() ? Order.desc(propertyName) : Order.asc(propertyName));
        }

        // 设置过滤条件
        if (StringUtils.isNotBlank(filterName)) {
            DynamicDataFilter dynamicDataFilter = SystemContainer.getInstance().getBean(DynamicDataFilter.class);
            if (dynamicDataFilter != null) {
                dynamicDataFilter.addFilterCondition(criteria, filterName, fieldName, filterFieldType);
            }
        }
        return criteria;
    }


    /**
     * 创建一个Criteria对象，并设置分页信息、排序信息
     */
    protected Criteria createPagerCriteria(Class<?> clazz) {
        return createPagerCriteria(clazz, null, null, null);
    }

    /**
     * 创建一个只用于查询总记录条数的Criteria对象（没有分页与排序）
     */
    public Criteria createRowCountsCriteria(Class<?> clazz) {
        return createRowCountsCriteria(clazz, null, null, null);
    }

    /**
     * 创建查询总记录条数的标准查询对象，并启用过滤器
     *
     * @param clazz           要查询的类
     * @param filterName      过滤器名称
     * @param filterFieldName 过滤器应用的字段
     * @param filterFieldType 过滤字段的过滤类型
     */
    public Criteria createRowCountsCriteria(Class<?> clazz, String filterName, String filterFieldName, FilterFieldType filterFieldType) {
        Criteria criteria = getSession().createCriteria(clazz);
        criteria.setProjection(Projections.rowCount());
        // 设置过滤条件
        if (StringUtils.isNotBlank(filterName)) {
            DynamicDataFilter dynamicDataFilter = SystemContainer.getInstance().getBean(DynamicDataFilter.class);
            if (dynamicDataFilter != null) {
                dynamicDataFilter.addFilterCondition(criteria, filterName, filterFieldName, filterFieldType);
            }
        }
        return criteria;
    }

    public void evict(Object obj) {
        sessionFactory.getCurrentSession().evict(obj);
    }


    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
