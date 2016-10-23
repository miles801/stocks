package com.michael.core.hibernate.filter;

import org.hibernate.Criteria;

/**
 * 动态数据过滤器
 * 用于动态创建Hibernate过滤器
 *
 * @author Michael
 */
public interface DynamicDataFilter {

    /**
     * 给标准查询对象添加对应的过滤器
     */
    void addFilterCondition(Criteria criteria, String filterName, String fieldName, FilterFieldType filterFieldType);

}