package com.michael.utils.gson;

import com.michael.docs.annotations.ApiField;

/**
 * @author miles
 * @datetime 2014/6/30 16:53
 */
public class ResponseData {
    /**
     * 操作成功后返回{success:true}
     */
    @ApiField(value = "是否成功", desc = "true,成功执行;false,执行报错")
    private Boolean success;
    /**
     * 操作异常后返回{error:''}
     */
    @ApiField(value = "是否错误", desc = "true,程序执行有错误;false,执行无报错")
    private Boolean error;
    /**
     * 操作失败后返回{fail:true}
     */
    private Boolean fail;
    /**
     * 其他数据
     */
    @ApiField(value = "返回的数据", desc = "成功执行后返回正常的数据,失败后返回错误信息")
    private Object data;
    /**
     * 消息描述
     */
    @ApiField(value = "请求结果的简要描述")
    private String message;
    /**
     * 状态码
     */
    @ApiField(value = "请求结果状态码", desc = "0错误 1成功")
    private String code;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Boolean getFail() {
        return fail;
    }

    public void setFail(Boolean fail) {
        this.fail = fail;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
