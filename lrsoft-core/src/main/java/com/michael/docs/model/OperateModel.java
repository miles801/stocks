package com.michael.docs.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口中的方法的描述
 *
 * @author Michael
 */
public class OperateModel {
    private String name;
    /**
     * 请求类型：默认来自于SpringMVC RequestMapping中的method
     */
    private String method;

    private String url;

    private String desc;

    private List<FieldModel> requestModels;

    private List<FieldModel> responseModels;

    private List<ParamModel> params;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public OperateModel addRequestModel(FieldModel model) {
        if (requestModels == null) {
            requestModels = new ArrayList<FieldModel>();
        }
        requestModels.add(model);
        return this;
    }

    public OperateModel addResponseModel(FieldModel model) {
        if (responseModels == null) {
            responseModels = new ArrayList<FieldModel>();
        }
        responseModels.add(model);
        return this;
    }

    public OperateModel addParam(ParamModel paramModel) {
        if (params == null) {
            params = new ArrayList<ParamModel>();
        }
        params.add(paramModel);
        return this;
    }

    public List<ParamModel> getParams() {
        return params;
    }

    public void setParams(List<ParamModel> params) {
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<FieldModel> getRequestModels() {
        return requestModels;
    }

    public void setRequestModels(List<FieldModel> requestModels) {
        this.requestModels = requestModels;
    }

    public List<FieldModel> getResponseModels() {
        return responseModels;
    }

    public void setResponseModels(List<FieldModel> responseModels) {
        this.responseModels = responseModels;
    }
}
