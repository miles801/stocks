package com.michael.core.hibernate.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author Michael
 */
public class CriteriaUtils {

    /**
     * 给一个Criteria对象添加查询条件
     * 要求给定对象中的属性名称必须和要作为查询字段的名称一致
     *
     * @param criteria 标准查询对象
     * @param bo       BO接口的实例对象
     */
    @SuppressWarnings("unchecked")
    public static void addCondition(final Criteria criteria, final BO bo) {
        if (bo == null) {
            return;
        }
        Assert.notNull(criteria);
        ReflectionUtils.doWithFields(bo.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                Condition condition = field.getAnnotation(Condition.class);
                // 如果字段上没有Condition注解，则跳过
                if (condition == null) {
                    return;
                }
                // 获取字段的值，如果值为空，则直接跳过
                field.setAccessible(true);
                Object value = ReflectionUtils.getField(field, bo);
                if (value == null || "".equals(value.toString().trim())) {
                    return;
                }

                String name = field.getName();
                // 获取ConditionModel注解
                String target = condition.target();
                if (!"".equals(target)) {
                    name = target;
                }
                MatchModel matchModel = condition.matchMode();
                if (matchModel.equals(MatchModel.EQ)) {             // =
                    criteria.add(Restrictions.eq(name, value));
                } else if (MatchModel.GE.equals(matchModel)) {      // >=
                    criteria.add(Restrictions.ge(name, value));
                } else if (MatchModel.GT.equals(matchModel)) {      // >
                    criteria.add(Restrictions.gt(name, value));
                } else if (MatchModel.LE.equals(matchModel)) {      // <=
                    criteria.add(Restrictions.le(name, value));
                } else if (MatchModel.LT.equals(matchModel)) {      // <
                    criteria.add(Restrictions.lt(name, value));
                } else if (MatchModel.IN.equals(matchModel)) {      // in()
                    Assert.isTrue(value instanceof Collection);
                    Collection collection = (Collection) value;
                    criteria.add(Restrictions.in(name, collection));
                } else if (MatchModel.NOT_IN.equals(matchModel)) {
                    Assert.isTrue(value instanceof Collection);
                    Collection collection = (Collection) value;
                    criteria.add(Restrictions.not(Restrictions.in(name, collection)));
                } else if (MatchModel.NE.equals(matchModel)) {      // <>
                    criteria.add(Restrictions.ne(name, value));
                } else if (MatchModel.LIKE.equals(matchModel)) {    // like
                    Assert.isTrue(value instanceof String, "使用Like时，字段[" + name + "]必须是String类型!");
                    // 获取匹配模型
                    LikeModel likeModel = condition.likeMode();
                    String strValue = (String) value;
                    if (LikeModel.START.equals(likeModel)) {
                        criteria.add(Restrictions.like(name, strValue, MatchMode.START));
                    } else if (LikeModel.ANYWHERE.equals(likeModel)) {
                        criteria.add(Restrictions.like(name, strValue, MatchMode.ANYWHERE));
                    } else if (LikeModel.END.equals(likeModel)) {
                        criteria.add(Restrictions.like(name, strValue, MatchMode.END));
                    }
                } else if (MatchModel.NULL.equals(matchModel)) {    // NULL
                    if (value instanceof Boolean) {
                        boolean boolValue = (Boolean) value;
                        if (boolValue) {
                            criteria.add(Restrictions.isNull(name));
                        } else {
                            criteria.add(Restrictions.isNotNull(name));
                        }
                    } else {
                        throw new RuntimeException("使用NULL作为条件的时候,属性的类型只能是Boolean!");
                    }
                } else if (MatchModel.NOT_NULL.equals(matchModel)) {
                    if (value instanceof Boolean) {
                        boolean boolValue = (Boolean) value;
                        if (boolValue) {
                            criteria.add(Restrictions.isNotNull(name));
                        } else {
                            criteria.add(Restrictions.isNull(name));
                        }
                    } else {
                        throw new RuntimeException("使用NOT_NULL作为条件的时候,属性的类型只能是Boolean!");
                    }
                } else if (MatchModel.EMPTY.equals(matchModel)) {   // EMPTY
                    if (value instanceof Boolean) {
                        boolean boolValue = (Boolean) value;
                        if (boolValue) {
                            criteria.add(Restrictions.isEmpty(name));
                        } else {
                            criteria.add(Restrictions.isNotEmpty(name));
                        }
                    } else {
                        throw new RuntimeException("使用EMPTY作为条件的时候,属性的类型只能是Boolean!");
                    }
                }
            }
        });
    }
}
