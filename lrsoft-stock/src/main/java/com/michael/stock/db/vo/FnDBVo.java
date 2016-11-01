package com.michael.stock.db.vo;

import com.michael.stock.db.domain.FnDB;

/**
 * @author Michael
 */
public class FnDBVo extends FnDB {
    // 所属数据库--参数名称
    private String typeName;

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return this.typeName;
    }
}
