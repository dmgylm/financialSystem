package cn.financial.model.response;

import io.swagger.annotations.ApiModelProperty;

public class LogManagerResult extends ResultUtils{

	@ApiModelProperty(value = "日志列表数据")
	private LogManagerInfo data;

	public LogManagerInfo getData() {
		return data;
	}

	public void setData(LogManagerInfo data) {
		this.data = data;
	}
	
	
	
}
