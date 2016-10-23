package com.michael.core.context;

import com.michael.docs.model.ApiModel;

import java.util.List;

/**
 * @author Michael
 */
public class ApiContext {

    private List<ApiModel> models = null;
    private static ApiContext ourInstance = new ApiContext();

    public static ApiContext getInstance() {
        return ourInstance;
    }

    private ApiContext() {
    }

    public void setModels(List<ApiModel> models) {
        this.models = models;
    }

    public List<ApiModel> getModels() {
        return this.models;
    }
}
