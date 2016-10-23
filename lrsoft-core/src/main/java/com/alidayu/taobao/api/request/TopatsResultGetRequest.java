package com.alidayu.taobao.api.request;

import com.alidayu.taobao.api.ApiRuleException;
import com.alidayu.taobao.api.BaseTaobaoRequest;
import com.alidayu.taobao.api.internal.util.RequestCheckUtils;
import com.alidayu.taobao.api.internal.util.TaobaoHashMap;
import com.alidayu.taobao.api.response.TopatsResultGetResponse;

import java.util.Map;

/**
 * TOP API: taobao.topats.result.get request
 * 
 * @author top auto create
 * @since 1.0, 2014.04.11
 */
public class TopatsResultGetRequest extends BaseTaobaoRequest<TopatsResultGetResponse> {
	
	

	/** 
	* 任务id号，创建任务时返回的task_id
	 */
	private Long taskId;

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getTaskId() {
		return this.taskId;
	}

	public String getApiMethodName() {
		return "taobao.topats.result.get";
	}

	public Map<String, String> getTextParams() {		
		TaobaoHashMap txtParams = new TaobaoHashMap();
		txtParams.put("task_id", this.taskId);
		if(this.udfParams != null) {
			txtParams.putAll(this.udfParams);
		}
		return txtParams;
	}

	public Class<TopatsResultGetResponse> getResponseClass() {
		return TopatsResultGetResponse.class;
	}

	public void check() throws ApiRuleException {
		RequestCheckUtils.checkNotEmpty(taskId, "taskId");
	}
	

}