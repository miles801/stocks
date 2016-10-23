package com.michael.utils.data;

import com.michael.core.hibernate.HibernateDaoHelper;
import com.michael.core.hibernate.criteria.BO;
import com.michael.core.hibernate.criteria.CriteriaUtils;
import org.hibernate.Criteria;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 数据操作公用类
 * Created by jayce on 2016/4/18.
 */
public class DBHelper extends HibernateDaoHelper {
    /**
     * 保存
     *
     * @param obj
     * @return
     */
    public String save(Object obj) {
        return (String) getSession().save(obj);
    }


    /**
     * 通过ID删除
     *
     * @param clazz
     * @param id
     * @param <T>   true(成功)
     */
    public <T> boolean deleteById(Class<T> clazz, String id) {
        return getSession().createQuery("delete from " + clazz.getName() + " e where e.id=?").setParameter(0, id).executeUpdate() > 0;
    }

    /**
     * 通过对象删除
     *
     * @param obj
     */
    public void deleteByObj(Object obj) {
        Assert.notNull(obj, "要删除的对象不能为空!");
        getSession().delete(obj);
    }


    /**
     * 更新
     *
     * @param obj
     */
    public void update(Object obj) {
        getSession().update(obj);
    }


    /**
     * 查询总记录数
     *
     * @param clazz 实体类
     * @param bo    实体bo类
     * @param <T>   所需实体类型
     * @return 记录数
     */
    public <T> Long getTotal(Class<T> clazz, BO bo) {
        Criteria criteria = createRowCountsCriteria(clazz);
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
        return (Long) criteria.uniqueResult();
    }

    /**
     * 查询列表
     *
     * @param clazz 实体类
     * @param bo    实体bo类
     * @param <T>   所需实体类型
     * @return <T>返回当前类型集合
     */
    public <T> List<T> queryList(Class<T> clazz, BO bo) {
        Criteria criteria = createCriteria(clazz);
        Assert.notNull(criteria, "criteria must not be null!");
        CriteriaUtils.addCondition(criteria, bo);
        return criteria.list();
    }

    /**
     * 通过ID查找当前实体类
     *
     * @param clazz
     * @param id
     * @param <T>
     * @return
     */
    public <T> T findById(Class<T> clazz, String id) {
        Assert.hasText(id, "ID不能为空!");
        return (T) getSession().get(clazz, id);
    }


}
