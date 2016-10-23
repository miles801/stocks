package com.michael.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * 自动设置ID（如果ID为空则设置，不为空则返回原来的）
 * 采用snowflake算法生成一个自增的id
 * 当从实体类中获取到ID为空时，则生成一个，否则返回原来的ID
 *
 * @author Michael
 */
public class SnowflakeIDStrategy implements IdentifierGenerator {
    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        Serializable id = sessionImplementor.getEntityPersister(o.getClass().getName(), o).getIdentifier(o, sessionImplementor);
        if (id != null) {
            return id;
        }
        return SnowflakeID.getInstance().nextId() + "";
    }
}
