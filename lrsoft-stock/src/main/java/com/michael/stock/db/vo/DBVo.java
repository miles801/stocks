package com.michael.stock.db.vo;

import com.michael.stock.db.domain.DB;

/**
 * @author Michael
 */
public class DBVo extends DB {
    // 类型--参数名称
    private String typeName;

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return this.typeName;
    }
}
