package com.michael.core.hibernate.filter;

/**
 * @author Michael
 */
public class DataFilter {
    /**
     * 过滤器的名称
     */
    private String filterName;
    private String column;
    private String property;
    private String columnValue;

    public DataFilter(String column, String property, String columnValue) {
        this.column = column;
        this.property = property;
        this.columnValue = columnValue;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }
}
