package com.alidayu.taobao.api.response;

import com.alidayu.taobao.api.TaobaoResponse;
import com.alidayu.taobao.api.domain.BizResult;
import com.alidayu.taobao.api.internal.mapping.ApiField;

/**
 * TOP API: alibaba.aliqin.fc.sms.num.send response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class AlibabaAliqinFcSmsNumSendResponse extends TaobaoResponse {

	private static final long serialVersionUID = 2381246121885393281L;

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
