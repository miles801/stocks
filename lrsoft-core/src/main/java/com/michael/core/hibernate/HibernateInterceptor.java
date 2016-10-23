package com.michael.core.hibernate;

import com.michael.common.CommonDomain;
import com.michael.common.SystemPropertyConstant;
import com.michael.core.context.SecurityContext;
import com.michael.utils.string.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>用于在Hibernate操作数据库，保存实体、更新实体时做一些额外的操作</p>
 * <p>需要在配置Hibernate的时候指定属性entityInterceptor</p>
 *
 * @author Michael
 */
public class HibernateInterceptor extends EmptyInterceptor {
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        //设置额外信息
        if (entity instanceof CommonDomain) {
            CommonDomain o = (CommonDomain) entity;
            // 强制设置时间
            int createdDatetimeIndex = ArrayUtils.indexOf(propertyNames, SystemPropertyConstant.CREATED_DATETIME);
            if (createdDatetimeIndex != -1) {
                Date now = new Date();
                o.setCreatedDatetime(now);
                state[createdDatetimeIndex] = now;
            }
            // 强制设置创建人
            int userId = ArrayUtils.indexOf(propertyNames, SystemPropertyConstant.CREATOR_ID);
            if (userId != -1 && !StringUtils.isEmpty(com.michael.core.context.SecurityContext.getEmpId())) {
                state[userId] = com.michael.core.context.SecurityContext.getEmpId();
                o.setCreatorId(com.michael.core.context.SecurityContext.getEmpId());
            }

            // 强制设置创建人名称
            int userName = ArrayUtils.indexOf(propertyNames, SystemPropertyConstant.CREATOR_NAME);
            if (userName != -1 && !StringUtils.isEmpty(com.michael.core.context.SecurityContext.getEmpName())) {
                state[userName] = com.michael.core.context.SecurityContext.getEmpName();
                o.setCreatorName(com.michael.core.context.SecurityContext.getEmpName());
            }
            return true;
        }

        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        //设置额外信息
        if (entity instanceof CommonDomain) {
            CommonDomain o = (CommonDomain) entity;
            // 设置修改时间
            int modifiedDatetimeIndex = ArrayUtils.indexOf(propertyNames, SystemPropertyConstant.MODIFIED_DATETIME);
            if (modifiedDatetimeIndex != -1) {
                currentState[modifiedDatetimeIndex] = new Date();
            }
            // 设置修改人
            int modifierId = ArrayUtils.indexOf(propertyNames, SystemPropertyConstant.MODIFIER_ID);
            if (modifierId != -1) {
                currentState[modifierId] = SecurityContext.getEmpId();
            }

            int modifierName = ArrayUtils.indexOf(propertyNames, SystemPropertyConstant.MODIFIER_NAME);
            if (modifierName != -1) {
                currentState[modifierName] = com.michael.core.context.SecurityContext.getEmpName();
            }
            return super.onFlushDirty(o, id, currentState, previousState, propertyNames, types);
        }
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }
}
