package com.michael.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author miles
 * @datetime 2014/4/2 18:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:applicationContext-*.xml")
public class AbstractTestWrapper extends AbstractTransactionalJUnit4SpringContextTests {
    static {
    }

    @Resource
    protected BeanFactory beanFactory;


    @Resource
    protected SessionFactory sessionFactory;

    public void flush() {
        Session session = SessionFactoryUtils.getSession(sessionFactory, false);
        if (session != null) {
            session.flush();
        }
    }


}
