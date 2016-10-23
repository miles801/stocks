package com.michael.core.hibernate.validator;

import com.michael.docs.annotations.ApiField;
import com.michael.utils.string.StringUtils;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Michael
 */
public class ValidatorUtils {

    /**
     * 验证指定对象是否满足约束条件
     *
     * @param obj    要验证的对象
     * @param groups 可选的组
     * @throws IllegalArgumentException 当验证失败后将会抛出该异常
     */
    public static <T> void validate(T obj, Class<?>... groups) {
        Validator validator = ValidatorHelper.getInstance().getValidator();
        Set<ConstraintViolation<T>> errors = validator.validate(obj, groups);
        if (errors.size() > 0) {
            Map<String, String> errorMsg = new HashMap<String, String>();
            Iterator<ConstraintViolation<T>> iterator = errors.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> errorField = iterator.next();
                String fieldName = errorField.getPropertyPath().toString();
                // 如果属性使用了@APIFiled进行注解，则使用该注解，否则使用属性名称
                try {
                    Field field = obj.getClass().getField(fieldName);
                    ApiField apiField = field.getAnnotation(ApiField.class);
                    if (apiField != null && StringUtils.isNotEmpty(apiField.value())) {
                        fieldName = apiField.value();
                    }
                } catch (NoSuchFieldException ignored) {
                }
                errorMsg.put(fieldName, errorField.getMessage());
            }
            Assert.isTrue(errorMsg.size() == 0, errorMsg.toString());
        }
    }
}
