package com.alidayu.taobao.api.request;

import com.alidayu.taobao.api.ApiRuleException;
import com.alidayu.taobao.api.BaseTaobaoRequest;
import com.alidayu.taobao.api.internal.util.TaobaoHashMap;
import com.alidayu.taobao.api.response.TimeGetResponse;

import java.util.Map;

/**
 * TOP API: taobao.time.get request
 * 
 * @author top auto create
 * @since 1.0, 2016.03.28
 */
public class TimeGetRequest extends BaseTaobaoRequest<TimeGetResponse> {
	
	

	public String getApiMethodName() {
		return "taobao.time.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		if(this.udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public Class<TimeGetResponse> getResponseClass() {
		return TimeGetResponse.class;
	}

	public void check() throws ApiRuleException {
	}
	

}