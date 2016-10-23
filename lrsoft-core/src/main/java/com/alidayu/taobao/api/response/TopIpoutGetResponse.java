package com.alidayu.taobao.api.response;

import com.alidayu.taobao.api.TaobaoResponse;
import com.alidayu.taobao.api.internal.mapping.ApiField;
import com.alidayu.taobao.api.internal.mapping.ApiListField;

import java.util.List;

/**
 * TOP API: taobao.top.ipout.get response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class TopIpoutGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 5736593126856393247L;

	/** 
	 * 出口IP段列表
	 */
	@ApiListField("ip_sections")
	@ApiField("string")
	private List<String> ipSections;


	public void setIpSections(List<String> ipSections) {
		this.ipSections = ipSections;
	}
	public List<String> getIpSections( ) {
		return this.ipSections;
	}
	


}
