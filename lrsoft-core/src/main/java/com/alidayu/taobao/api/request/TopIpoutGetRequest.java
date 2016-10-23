package com.alidayu.taobao.api.request;

import com.alidayu.taobao.api.ApiRuleException;
import com.alidayu.taobao.api.BaseTaobaoRequest;
import com.alidayu.taobao.api.internal.util.TaobaoHashMap;
import com.alidayu.taobao.api.response.TopIpoutGetResponse;

import java.util.Map;

/**
 * TOP API: taobao.top.ipout.get request
 * 
 * @author top auto create
 * @since 1.0, 2015.09.07
 */
public class TopIpoutGetRequest extends BaseTaobaoRequest<TopIpoutGetResponse> {
	
	

	public String getApiMethodName() {
		return "taobao.top.ipout.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		if(this.udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public Class<TopIpoutGetResponse> getResponseClass() {
		return TopIpoutGetResponse.class;
	}

	public void check() throws ApiRuleException {
	}
	

}