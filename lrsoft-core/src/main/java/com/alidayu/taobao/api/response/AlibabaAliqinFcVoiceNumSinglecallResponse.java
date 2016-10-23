package com.alidayu.taobao.api.response;

import com.alidayu.taobao.api.TaobaoResponse;
import com.alidayu.taobao.api.domain.BizResult;
import com.alidayu.taobao.api.internal.mapping.ApiField;

/**
 * TOP API: alibaba.aliqin.fc.voice.num.singlecall response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class AlibabaAliqinFcVoiceNumSinglecallResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4444596122427383262L;

	/** 
	 * 返回值
	 */
	@ApiField("result")
	private BizResult result;


	public void setResult(BizResult result) {
		this.result = result;
	}
	public BizResult getResult( ) {
		return this.result;
	}
	


}
