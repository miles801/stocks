package com.alidayu.taobao.api.request;

import com.alidayu.taobao.api.ApiRuleException;
import com.alidayu.taobao.api.BaseTaobaoRequest;
import com.alidayu.taobao.api.internal.util.TaobaoHashMap;
import com.alidayu.taobao.api.response.HttpdnsGetResponse;

import java.util.Map;

/**
 * TOP API: taobao.httpdns.get request
 * 
 * @author top auto create
 * @since 1.0, 2016.03.24
 */
public class HttpdnsGetRequest extends BaseTaobaoRequest<HttpdnsGetResponse> {
	
	

	public String getApiMethodName() {
		return "taobao.httpdns.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		if(this.udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public Class<HttpdnsGetResponse> getResponseClass() {
		return HttpdnsGetResponse.class;
	}

	public void check() throws ApiRuleException {
	}
	

}