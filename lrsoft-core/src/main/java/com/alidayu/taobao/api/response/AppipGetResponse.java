package com.alidayu.taobao.api.response;

import com.alidayu.taobao.api.TaobaoResponse;
import com.alidayu.taobao.api.internal.mapping.ApiField;

/**
 * TOP API: taobao.appip.get response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class AppipGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 1225943441158639939L;

	/** 
	 * ISV发起请求服务器IP
	 */
	@ApiField("ip")
	private String ip;


	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIp( ) {
		return this.ip;
	}
	


}
