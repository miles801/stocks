package com.alidayu.qimen.api;

import com.alidayu.taobao.api.internal.mapping.ApiField;

import java.io.Serializable;


/**
 * 奇门API响应包装类。
 *
 * @author fengsheng
 * @since Jan 27, 2016
 */
public abstract class QimenResponse implements Serializable {

    private static final long serialVersionUID = 8987912149522024749L;

    @ApiField("flag")
    private String flag;

    @ApiField("code")
    private String code;

    @ApiField("message")
    private String message;

    private String body; // API响应XML串

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * 判断响应是否成功
     */
    public boolean isSuccess() {
        return "success".equals(flag);
    }

}
