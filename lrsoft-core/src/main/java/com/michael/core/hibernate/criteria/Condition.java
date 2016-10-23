package com.michael.core.hibernate.criteria;

import java.lang.annotation.*;

/**
 * 条件的匹配模型
 *
 * @author Michael
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface Condition {
    /**
     * 条件的匹配模式，默认为=
     */
    MatchModel matchMode() default MatchModel.EQ;

    /**
     * 只在ConditionModel的值为LIKE时有效
     */
    LikeModel likeMode() default LikeModel.START;

    /**
     * 目标属性，用于当当前属性名称与查询时需要的字段名称不一致时进行额外指定
     */
    String target() default "";
}
