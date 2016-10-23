package com.alidayu.taobao.api.response;

import com.alidayu.taobao.api.TaobaoResponse;
import com.alidayu.taobao.api.domain.KfcSearchResult;
import com.alidayu.taobao.api.internal.mapping.ApiField;

/**
 * TOP API: taobao.kfc.keyword.search response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class KfcKeywordSearchResponse extends TaobaoResponse {

	private static final long serialVersionUID = 3611819672913962331L;

	/** 
	 * KFC 关键词过滤匹配结果
	 */
	@ApiField("kfc_search_result")
	private KfcSearchResult kfcSearchResult;


	public void setKfcSearchResult(KfcSearchResult kfcSearchResult) {
		this.kfcSearchResult = kfcSearchResult;
	}
	public KfcSearchResult getKfcSearchResult( ) {
		return this.kfcSearchResult;
	}
	


}
