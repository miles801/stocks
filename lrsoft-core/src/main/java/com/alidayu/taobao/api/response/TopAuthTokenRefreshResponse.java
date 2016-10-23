package com.alidayu.taobao.api.response;

import com.alidayu.taobao.api.TaobaoResponse;
import com.alidayu.taobao.api.internal.mapping.ApiField;

/**
 * TOP API: taobao.top.auth.token.refresh response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class TopAuthTokenRefreshResponse extends TaobaoResponse {

	private static final long serialVersionUID = 6684958665983442397L;

	/** 
	 * 返回的是json信息
	 */
	@ApiField("token_result")
	private String tokenResult;


	public void setTokenResult(String tokenResult) {
		this.tokenResult = tokenResult;
	}
	public String getTokenResult( ) {
		return this.tokenResult;
	}
	


}
