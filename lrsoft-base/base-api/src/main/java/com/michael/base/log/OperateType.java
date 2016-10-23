package com.michael.base.log;

/**
 * 操作类型
 *
 * @author Michael
 */
public enum OperateType {


    ADD("add"),
    MODIFY("modify"),
    DELETE("delete"),
    OTHER("other");
    private String type;

    OperateType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
