package com.alidayu.taobao.api.response;

import com.alidayu.taobao.api.TaobaoResponse;
import com.alidayu.taobao.api.domain.Task;
import com.alidayu.taobao.api.internal.mapping.ApiField;

/**
 * TOP API: taobao.topats.result.get response.
 * 
 * @author top auto create
 * @since 1.0, null
 */
public class TopatsResultGetResponse extends TaobaoResponse {

	private static final long serialVersionUID = 4352425283119599776L;

	/** 
	 * 任务结果信息
	 */
	@ApiField("task")
	private Task task;


	public void setTask(Task task) {
		this.task = task;
	}
	public Task getTask( ) {
		return this.task;
	}
	


}
