package com.michael.core.exception;


public class ExceptionUtils {

    private ExceptionUtils() {
        //no instance
    }


    /**
     * 如果目标为空则抛出异常
     *
     * @param target
     * @param errorMessage
     */
    public static void throwIfNull(Object target, String errorMessage) {
        if (target == null) {
            throw new NullParamException(errorMessage);
        }
    }


    /**
     * 如果目标为空则抛出异常
     *
     * @param target
     * @param errorMessage
     */
    public static void throwIfEmpty(String target, String errorMessage) {
        if (target == null || "".equals(target.toString().trim().equals(""))) {
            throw new NullParamException(errorMessage);
        }
    }

}
