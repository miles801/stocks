package com.michael.common;

/**
 * 通用状态
 * Created by Michael on 2014/10/18.
 */
public enum CommonStatus {
    INACTIVE("INACTIVE"), ACTIVE("ACTIVE"), CANCELED("CANCELED");
    private String value;

    private CommonStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
