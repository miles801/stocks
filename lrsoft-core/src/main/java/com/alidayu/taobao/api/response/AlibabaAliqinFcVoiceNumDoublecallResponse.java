package com.alidayu.taobao.api.response;

import com.alidayu.taobao.api.TaobaoResponse;
import com.alidayu.taobao.api.domain.BizResult;
import com.alidayu.taobao.api.internal.mapping.ApiField;

/**
 * TOP API: alibaba.aliqin.fc.voice.num.doublecall response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class AlibabaAliqinFcVoiceNumDoublecallResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5137928283343241921L;

	/** 
	 * 接口返回参数
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
