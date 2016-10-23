package com.alidayu.taobao.api.internal.cluster;

import com.alidayu.taobao.api.ApiRuleException;
import com.alidayu.taobao.api.BaseTaobaoRequest;

import java.util.HashMap;
import java.util.Map;


public class HttpdnsGetRequest extends BaseTaobaoRequest<HttpdnsGetResponse> {

    public String getApiMethodName() {
        return "taobao.httpdns.get";
    }

    public Map<String, String> getTextParams() {
        return new HashMap<String, String>();
    }

    public Class<HttpdnsGetResponse> getResponseClass() {
        return HttpdnsGetResponse.class;
    }

    public void check() throws ApiRuleException {
    }

}
