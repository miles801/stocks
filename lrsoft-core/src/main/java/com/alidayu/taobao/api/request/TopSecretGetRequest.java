package com.alidayu.taobao.api.request;

import com.alidayu.taobao.api.ApiRuleException;
import com.alidayu.taobao.api.BaseTaobaoRequest;
import com.alidayu.taobao.api.internal.util.RequestCheckUtils;
import com.alidayu.taobao.api.internal.util.TaobaoHashMap;
import com.alidayu.taobao.api.response.TopSecretGetResponse;

import java.util.Map;

/**
 * TOP API: taobao.top.secret.get request
 * 
 * @author top auto create
 * @since 1.0, 2016.04.06
 */
public class TopSecretGetRequest extends BaseTaobaoRequest<TopSecretGetResponse> {
	
	

	/** 
	* 伪随机数
	 */
	private String randomNum;

	/** 
	* 秘钥版本号
	 */
	private Long secretVersion;

	public void setRandomNum(String randomNum) {
		this.randomNum = randomNum;
	}

	public String getRandomNum() {
		return this.randomNum;
	}

	public void setSecretVersion(Long secretVersion) {
		this.secretVersion = secretVersion;
	}

	public Long getSecretVersion() {
		return this.secretVersion;
	}

	public String getApiMethodName() {
		return "taobao.top.secret.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("random_num", this.randomNum);
		txtParams.put("secret_version", this.secretVersion);
		if(this.udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public Class<TopSecretGetResponse> getResponseClass() {
		return TopSecretGetResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(randomNum, "randomNum");
	}
	

}