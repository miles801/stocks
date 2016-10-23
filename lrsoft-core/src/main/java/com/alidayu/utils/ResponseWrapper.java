package com.alidayu.utils;

import com.google.gson.annotations.SerializedName;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author Michael
 */
public class ResponseWrapper {

    @SerializedName("error_response")
    private SmsResponse errorResponse;

    @SerializedName("alibaba_aliqin_fc_sms_num_send_response")
    private Map map;

    public SmsResponse getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(SmsResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * 获取正确的响应结果
     *
     * @see SmsResponse
     */
    public SmsResponse getResponse() {
        // 发送失败
        if (errorResponse != null) {
            errorResponse.setSuccess(false);
            return errorResponse;
        }
        // 可能发送成功
        if (map != null) {
            return new SmsResponse(true);
        }
        // 解析失败
        Assert.isTrue(false, "解析失败!");
        return null;
    }
}
