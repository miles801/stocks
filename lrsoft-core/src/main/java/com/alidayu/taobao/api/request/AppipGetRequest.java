package com.alidayu.taobao.api.request;

import com.alidayu.taobao.api.ApiRuleException;
import com.alidayu.taobao.api.BaseTaobaoRequest;
import com.alidayu.taobao.api.internal.util.TaobaoHashMap;
import com.alidayu.taobao.api.response.AppipGetResponse;

import java.util.Map;

/**
 * TOP API: taobao.appip.get request
 * 
 * @author top auto create
 * @since 1.0, 2014.11.26
 */
public class AppipGetRequest extends BaseTaobaoRequest<AppipGetResponse> {
	
	

	public String getApiMethodName() {
		return "taobao.appip.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		if(this.udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public Class<AppipGetResponse> getResponseClass() {
		return AppipGetResponse.class;
	}

	public void check() throws ApiRuleException {
	}
	

}