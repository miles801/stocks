package com.alidayu.taobao.api.request;

import com.alidayu.taobao.api.ApiRuleException;
import com.alidayu.taobao.api.BaseTaobaoRequest;
import com.alidayu.taobao.api.internal.util.TaobaoHashMap;
import com.alidayu.taobao.api.response.AlibabaAliqinFcFlowQueryResponse;

import java.util.Map;

/**
 * TOP API: alibaba.aliqin.fc.flow.query request
 * 
 * @author top auto create
 * @since 1.0, 2016.03.30
 */
public class AlibabaAliqinFcFlowQueryRequest extends BaseTaobaoRequest<AlibabaAliqinFcFlowQueryResponse> {
	
	

	/** 
	* 唯一流水号
	 */
	private String outId;

	public void setOutId(String outId) {
		this.outId = outId;
	}

	public String getOutId() {
		return this.outId;
	}

	public String getApiMethodName() {
		return "alibaba.aliqin.fc.flow.query";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("out_id", this.outId);
		if(this.udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public Class<AlibabaAliqinFcFlowQueryResponse> getResponseClass() {
		return AlibabaAliqinFcFlowQueryResponse.class;
	}

	public void check() throws ApiRuleException {
	}
	

}