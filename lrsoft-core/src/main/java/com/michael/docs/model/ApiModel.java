package com.michael.docs.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael
 */
public class ApiModel {
    /**
     * 名称
     */
    private String name;
    /**
     * 访问URL，默认情况下来自于SpringMVC的RequestMapping
     */
    private String url;
    /**
     * 描述
     */
    private String description;


    private List<OperateModel> operates;


    public ApiModel addOperate(OperateModel operateModel) {
        if (operates == null) {
            operates = new ArrayList<OperateModel>();
        }
        operates.add(operateModel);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OperateModel> getOperates() {
        return operates;
    }

    public void setOperates(List<OperateModel> operates) {
        this.operates = operates;
    }
}
