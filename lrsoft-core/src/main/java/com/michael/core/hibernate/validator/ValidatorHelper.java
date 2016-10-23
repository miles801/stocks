package com.michael.core.hibernate.validator;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * 用于获取一个线程安全的Validator对象
 *
 * @author Michael
 */
public class ValidatorHelper {
    private static ValidatorHelper ourInstance = new ValidatorHelper();

    public static ValidatorHelper getInstance() {
        return ourInstance;
    }

    private Validator validator;

    private ValidatorHelper() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public Validator getValidator() {
        return validator;
    }
}
