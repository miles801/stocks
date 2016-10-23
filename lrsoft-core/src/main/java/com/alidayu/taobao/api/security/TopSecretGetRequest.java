package com.alidayu.taobao.api.security;

import com.alidayu.taobao.api.ApiRuleException;
import com.alidayu.taobao.api.BaseTaobaoRequest;
import com.alidayu.taobao.api.internal.util.RequestCheckUtils;
import com.alidayu.taobao.api.internal.util.TaobaoHashMap;

import java.util.Map;

/**
 * 
 * @author changchun
 * @since 2016年3月3日 下午1:55:04
 */
public class TopSecretGetRequest extends BaseTaobaoRequest<TopSecretGetResponse> {

    private String randomNum;// 伪随机码
    private Long secretVersion;// 秘钥版本

    public void setRandomNum(String randomNum) {
        this.randomNum = randomNum;
    }

    public void setSecretVersion(Long secretVersion) {
        this.secretVersion = secretVersion;
    }

    public String getApiMethodName() {
        return "taobao.top.secret.get";
    }

    public Map<String, String> getTextParams() {
        TaobaoHashMap params = new TaobaoHashMap();
        params.put("random_num", this.randomNum);
        if (this.secretVersion != null) {
            params.put("secret_version", this.secretVersion);
        }
        return params;
    }

    public Class<TopSecretGetResponse> getResponseClass() {
        return TopSecretGetResponse.class;
    }

    public void check() throws ApiRuleException {
        RequestCheckUtils.checkNotEmpty(randomNum, "random_num");
    }

}
