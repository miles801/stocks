package com.michael.docs.model;

import java.util.List;

/**
 * @author Michael
 */
public class FieldModel {


    private String field;
    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    private String type;
    /**
     * 是否必填
     */
    private boolean required;

    /**
     * 关联类型
     */
    private List<FieldModel> models;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<FieldModel> getModels() {
        return models;
    }

    public void setModels(List<FieldModel> models) {
        this.models = models;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
